use std::str::FromStr;

use serde::Serialize;
use thiserror::Error;

use crate::{
    entry_file_parser::{parse_field_annotations, FieldAnnotation},
    treesitter::{query, QueryResult},
};

#[derive(Debug, Serialize, Clone)]
pub struct AdapterClass {
    pub name: String,
    pub description: String,
    pub comment: Option<String>,
    pub annotations: Vec<FieldAnnotation>,
}

impl AdapterClass {
    pub fn has_annotation(&self, annotation: &str) -> bool {
        self.annotations.iter().any(|a| a.name == annotation)
    }
}

pub fn parse_adapter_class(code: &str) -> Result<AdapterClass, AdapterParseError> {
    let classes_code: QueryResult = query(&code, "(source_file (object_declaration) @class)")?;

    if classes_code.captures.len() == 0 {
        return Err(AdapterParseError::NotAdapterClass);
    }

    let classes_result: Vec<Result<AdapterClass, AdapterParseError>> = classes_code
        .captures
        .iter()
        .map(|cap| cap.code.parse())
        .collect();

    let mut classes: Vec<AdapterClass> = Vec::new();

    for class in classes_result {
        match class {
            Ok(class) => classes.push(class),
            Err(AdapterParseError::NotAdapterClass) => continue,
            Err(err) => return Err(err),
        }
    }

    if classes.len() == 0 {
        return Err(AdapterParseError::NotAdapterClass);
    }

    if classes.len() > 1 {
        return Err(AdapterParseError::TooManyClasses);
    }

    Ok(classes.pop().unwrap())
}

impl FromStr for AdapterClass {
    type Err = AdapterParseError;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let annotation = get_annotation(s)?;
        let (name, description) = parse_annotation(&annotation)?;

        Ok(AdapterClass {
            name,
            description,
            comment: parse_comment(s).ok(),
            annotations: parse_field_annotations(s)?,
        })
    }
}

fn get_annotation(code: &str) -> Result<String, AdapterParseError> {
    let annotation_code: QueryResult = query(&code, "(modifiers (annotation) @annotation)")?;

    let Some(annotation) = annotation_code
        .captures
        .iter()
        .find(|cap| cap.code.starts_with("@Adapter"))
    else {
        return Err(AdapterParseError::NotAdapterClass);
    };

    return Ok(annotation.code.clone());
}

fn parse_annotation(code: &str) -> Result<(String, String), AdapterParseError> {
    let annotation_code: QueryResult = query(&code, "(string_literal) @string")?;

    if annotation_code.captures.len() < 2 {
        return Err(AdapterParseError::NotAdapterClass);
    }

    let name = annotation_code.captures[0]
        .code
        .clone()
        .trim_start_matches('"')
        .trim_end_matches('"')
        .to_string();
    let description = annotation_code.captures[1]
        .code
        .clone()
        .trim_start_matches('"')
        .trim_end_matches('"')
        .to_string();

    Ok((name, description))
}

fn parse_comment(code: &str) -> Result<String, AdapterParseError> {
    let comment_code: QueryResult =
        query(&code, "(object_declaration (multiline_comment) @comment)")?;

    if comment_code.captures.len() != 1 {
        return Err(AdapterParseError::FieldNotFound("comment".to_string()));
    }

    let raw_comment = comment_code.captures[0].code.clone();

    // Remove the comment characters (/**, */, *)
    let comment: String = raw_comment
        .lines()
        .map(|line| line.trim())
        .map(|line| line.trim_start_matches("/*"))
        .map(|line| line.trim_end_matches("*/"))
        .map(|line| line.trim_start_matches("*"))
        .map(|line| line.trim())
        .collect::<Vec<&str>>()
        .join("\n")
        .trim_start_matches("\n")
        .trim_end_matches("\n")
        .to_string();

    Ok(comment)
}

#[derive(Debug, Error)]
pub enum AdapterParseError {
    #[error("The provided code is not a valid adapter class")]
    NotAdapterClass,

    #[error("The provided code contains too many adapter classes")]
    TooManyClasses,

    #[error("The provided code is not a valid Kotlin file")]
    CouldNotReadFile,

    #[error("Error parsing query")]
    QueryError(#[from] crate::treesitter::QueryError),

    #[error("The {0} field was not found")]
    FieldNotFound(String),

    #[error("Error parsing field annotations")]
    FieldAnnotationError(#[from] crate::entry_file_parser::EntryParseError),
}
