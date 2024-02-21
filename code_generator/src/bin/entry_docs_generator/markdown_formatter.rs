use indoc::formatdoc;
use itertools::Itertools;
use std::string::FromUtf8Error;

use crate::{
    documentation_orchestrator::{AdapterParsed, EntryParsed},
    entry_file_parser::{EntryClass, EntryField, FieldAnnotation},
};
use convert_case::{Case, Casing};
use string_builder::{Builder, ToBytes};
use thiserror::Error;

pub fn format_entry_markdown(entry: &EntryClass) -> Result<String, FormatError> {
    let mut builder = Builder::default();

    append_headers(&mut builder, entry);
    builder.empty_line();

    append_info(&mut builder, entry);
    builder.empty_line();

    append_fields(&mut builder, entry);

    return builder
        .string()
        .map_err(|error| FormatError::FailedToFormatEntry(error));
}

fn append_headers(builder: &mut Builder, entry: &EntryClass) {
    builder.append_line("import * as fields from '@site/src/components/EntryField';");
    builder.append_line("import Admonition from '@theme/Admonition';");
    builder.append_line("import Link from '@docusaurus/Link';");
}

fn append_deprecation_warning(builder: &mut Builder, entry: &EntryClass) {
    let Some(message) = entry.annotations.iter().find(|a| a.name == "Deprecated") else {
        return;
    };
    builder.append_line(formatdoc! {"
            import DeprecationWarning from '@site/src/components/DeprecationWarning';

            <DeprecationWarning message='{}' />
            ", message.arguments[0]});
}

fn append_info(builder: &mut Builder, entry: &EntryClass) {
    builder.append_line(format!(
        "# {}",
        entry
            .class_name
            .to_case(Case::Title)
            .trim_end_matches("Entry")
    ));

    append_deprecation_warning(builder, entry);

    builder.empty_line();
    if let Some(description) = &entry.comment {
        builder.append_line(description.to_string());
    } else {
        builder.append_line(entry.description.to_string());
    }
    builder.empty_line();
}

fn append_fields(builder: &mut Builder, entry: &EntryClass) {
    builder.append_line("## Fields");
    builder.empty_line();

    entry
        .fields
        .iter()
        .filter(|field| !field.should_ignore_field())
        .for_each(|field| {
            append_field(builder, field, entry);
        });
}

fn append_field(builder: &mut Builder, field: &EntryField, entry: &EntryClass) {
    if format_standard_field(builder, field, entry) {
        return;
    }

    builder.append_line(format!(
        "<fields.EntryField name='{}' {}>",
        field_name(field).to_case(Case::Title),
        field_modifiers(field)
    ));
    match (&field.help, &field.comment) {
        (_, Some(comment)) => builder.append_line(format!("     {}", comment)),
        (Some(help), _) => builder.append_line(format!("     {}", help)),
        (_, _) => {}
    }
    builder.append_line("</fields.EntryField>");
}

fn format_standard_field(builder: &mut Builder, field: &EntryField, entry: &EntryClass) -> bool {
    match (field_name(&field).as_str(), field.field_type.as_str()) {
        ("criteria", "List<Criteria>") => builder.append_line("<fields.CriteriaField />"),
        ("modifiers", "List<Modifier>") => builder.append_line("<fields.ModifiersField />"),
        ("triggers", "List<String>") => builder.append_line("<fields.TriggersField />"),
        ("speaker", "String") => builder.append_line("<fields.SpeakerField />"),
        ("comment", "String") if entry.is_fact() => builder.append_line("<fields.CommentField />"),
        (_, _) => return false,
    }
    return true;
}

fn field_name(field: &EntryField) -> String {
    if let Some(name) = field
        .annotations
        .iter()
        .find(|annotation| annotation.name == "SerializedName")
    {
        if name.arguments.len() != 1 {
            panic!(
                "Expected 1 argument for SerializedName annotation for field: {:?}",
                field
            );
        }
        return name.arguments[0].clone();
    }
    return field.name.clone();
}

fn field_modifiers(field: &EntryField) -> String {
    let mut modifiers = Vec::new();

    if field.field_type.starts_with("Optional<") {
        modifiers.push("optional");
    } else {
        modifiers.push("required");
    }

    if field.field_type.starts_with("List<") {
        modifiers.push("multiple");
    }

    if field.field_type.starts_with("Map<") {
        modifiers.push("map");
    }

    if field.field_type == "Duration" {
        modifiers.push("duration");
    }

    for annotation in &field.annotations {
        if let Some(modifier) = match_annotation_to_modifier(annotation) {
            modifiers.push(modifier);
        }
    }

    return modifiers.join(" ");
}

fn match_annotation_to_modifier(annotation: &FieldAnnotation) -> Option<&str> {
    match annotation.name.as_str() {
        "Deprecated" => Some("deprecated"),
        "Colored" => Some("colored"),
        "Regex" => Some("regex"),
        "MultiLine" => Some("multiline"),
        "Placeholder" => Some("placeholders"),
        "Sound" => Some("sound"),
        "WithRotation" => Some("rotation"),
        "EntryIdentifier" => Some("reference"),
        "Segments" => Some("segment"),
        "Help" => None,
        "InnerMin" => None,
        "SerializedName" => None,
        _ => {
            println!("Unknown annotation: {}", annotation);
            None
        }
    }
}

pub fn format_adapter_markdown(
    adapter: &AdapterParsed,
    entries: Vec<&EntryParsed>,
) -> Result<String, FormatError> {
    let mut builder = Builder::default();

    format_adapter_description(&mut builder, adapter);
    builder.empty_line();
    format_adapter_entries(&mut builder, adapter, entries)?;

    return builder
        .string()
        .map_err(|error| FormatError::FailedToFormatAdapter(error));
}

fn format_adapter_description(builder: &mut Builder, adapter: &AdapterParsed) {
    if let Some(message) = adapter
        .adapter_data
        .annotations
        .iter()
        .find(|a| a.name == "Deprecated")
    {
        builder.append_line(formatdoc! {"
            import DeprecationWarning from '@site/src/components/DeprecationWarning';

            <DeprecationWarning adapter message='{}' />
            ", message.arguments[0]});
    }

    builder.append_line(format!("# {}", adapter.adapter.to_case(Case::Title)));

    builder.empty_line();
    if let Some(comment) = &adapter.adapter_data.comment {
        builder.append_line(comment.to_string());
    } else {
        builder.append_line(adapter.adapter_data.description.to_string());
    }

    if adapter.adapter_data.has_annotation("Untested") {
        builder.empty_line();
        builder.append_line(":::caution Untested");
        builder.append_line("This adapter is untested. It may not work as expected. Please report any issues you find.");
        builder.append_line(":::");
    }
}

fn format_adapter_entries(
    builder: &mut Builder,
    adapter: &AdapterParsed,
    mut entries: Vec<&EntryParsed>,
) -> Result<(), FormatError> {
    builder.append_line("## Entries");
    builder.empty_line();

    entries.sort_by(|a, b| a.entry_data.class_name.cmp(&b.entry_data.class_name));

    let categories = entries
        .iter()
        .map(|entry| entry.category.clone())
        .unique()
        .sorted();

    for category in categories {
        builder.append_line(format!("### {}s", category.to_case(Case::Title)));
        builder.empty_line();

        builder.append_line("| Name | Description |");
        builder.append_line("| ---- | ----------- |");
        for entry in entries.iter().filter(|entry| entry.category == category) {
            format_adapter_entry(builder, adapter, entry)?;
        }
        builder.empty_line();
    }

    return Ok(());
}

fn format_adapter_entry(
    builder: &mut Builder,
    adapter: &AdapterParsed,
    entry: &EntryParsed,
) -> Result<(), FormatError> {
    let name = entry
        .entry_data
        .class_name
        .trim_end_matches("Entry")
        .to_case(Case::Title);
    let link = format!(
        "{}/entries/{}/{}",
        adapter.adapter, entry.category, entry.entry_data.name
    );
    let description = entry.entry_data.description.clone();
    builder.append_line(format!(
        "| [{}]({}) | {} |",
        name.to_case(Case::Title),
        link,
        description
    ));

    return Ok(());
}

trait AppendLine {
    fn append_line<T: ToBytes>(&mut self, line: T);
    fn empty_line(&mut self) {
        self.append_line("");
    }
}

impl AppendLine for Builder {
    fn append_line<T: ToBytes>(&mut self, line: T) {
        self.append(line);
        self.append("\n");
    }
}

#[derive(Debug, Error)]
pub enum FormatError {
    #[error("Failed to format entry: {0}")]
    FailedToFormatEntry(#[from] FromUtf8Error),

    #[error("Failed to format adapter: {0}")]
    FailedToFormatAdapter(FromUtf8Error),
}
