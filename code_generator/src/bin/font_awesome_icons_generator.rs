use crate::Error::InvalidJson;
use lazy_static::lazy_static;
use recase::ReCase;
use serde_json::Value;
use std::collections::HashMap;
use tokio::fs::File;
use tokio::io::AsyncWriteExt;
use code_generator::*;

#[tokio::main]
async fn main() -> Result<()> {
    let branch = "6.x";
    let url = format!(
        "https://raw.githubusercontent.com/FortAwesome/Font-Awesome/{branch}/metadata/icons.json"
    );

    let json = fetch_icon_json(url).await?;
    let metadata = IconMetadata::from_json(&json)?;

    let dart = format_for_dart(&metadata);
    let kotlin = format_for_kotlin(&metadata);

    // Write to 'icons.dart'
    let mut file = File::create("../../icons.dart").await?;
    file.write_all(dart.as_bytes()).await?;

    // Write to 'Icons.kt'
    let mut file = File::create("../../Icons.kt").await?;
    file.write_all(kotlin.as_bytes()).await?;

    Ok(())
}

fn format_for_dart(metadata: &[IconMetadata]) -> String {
    let mut result = String::new();

    result.push_str("import \"package:flutter/material.dart\";\nimport \"package:font_awesome_flutter/font_awesome_flutter.dart\";\n\n");
    result.push_str("const Map<String, IconData> icons = {\n");

    for icon in metadata {
        for style in &icon.styles {
            // Let the IconData class be `IconData{style}` where style is with the first letter capitalized
            let icon_data = format!(
                "IconData{}{}",
                style[0..1].to_uppercase(),
                style[1..].to_lowercase()
            );
            result.push_str(&format!(
                "  \"{}\": {}(0x{}),\n",
                ReCase::new(icon.normalized_name(style)).camel_case(),
                icon_data,
                icon.unicode
            ));
        }
    }

    result.push_str("};\n");

    result
}

fn format_for_kotlin(metadata: &[IconMetadata]) -> String {
    let mut result = String::new();

    result.push_str("package me.gabber235.typewriter.utils\n\n");

    result.push_str("enum class Icons(val id: String) {\n");

    for icon in metadata {
        for style in &icon.styles {
            // Add documentation. Given the label and the url.
            result.push_str(&format!(
                "  /**\n  * {}{} icon\n  * https://fontawesome.com/icons/{}?style={}\n  */\n",
                ReCase::new(style.to_owned()).sentence_case(),
                icon.label,
                icon.name,
                style
            ));

            result.push_str(&format!(
                "  {}(\"{}\"),\n\n",
                ReCase::new(icon.normalized_name(style)).upper_snake_case(),
                ReCase::new(icon.normalized_name(style)).camel_case()
            ));
        }
    }

    result.push_str("}\n");

    result
}

#[derive(Debug)]
struct IconMetadata {
    name: String,
    label: String,
    unicode: String,
    search: Vec<String>,
    styles: Vec<String>,
    aliases: Vec<String>,
}

lazy_static! {
    static ref NAME_ADJUSTMENTS: HashMap<String, String> = collection! {
        "500px".to_string() => "fiveHundredPx".to_string(),
        "360-degrees".to_string() => "threeHundredSixtyDegrees".to_string(),
        "1".to_string() => "one".to_string(),
        "2".to_string() => "two".to_string(),
        "3".to_string() => "three".to_string(),
        "4".to_string() => "four".to_string(),
        "5".to_string() => "five".to_string(),
        "6".to_string() => "six".to_string(),
        "7".to_string() => "seven".to_string(),
        "8".to_string() => "eight".to_string(),
        "9".to_string() => "nine".to_string(),
        "0".to_string() => "zero".to_string(),
        "42-group".to_string() => "fortyTwoGroup".to_string(),
        "00".to_string() => "zeroZero".to_string(),
        "100".to_string() => "hundred".to_string(),
    };
}

impl IconMetadata {
    fn normalized_name(&self, style: &String) -> String {
        let name = NAME_ADJUSTMENTS.get(&self.name).unwrap_or(&self.name);

        if self.styles.len() > 1 && style != "regular" {
            format!("{}_{}", style, name)
        } else {
            name.to_string()
        }
    }

    fn from_json(json: &str) -> Result<Vec<IconMetadata>> {
        let mut icons = Vec::new();
        let v: Value = serde_json::from_str(json)?;
        for (name, value) in v
            .as_object()
            .ok_or_else(|| err!(json, "Main structure is not a json object"))?
        {
            let metadata = IconMetadata::from_json_value(name, value)?;
            icons.push(metadata);
        }
        Ok(icons)
    }

    fn from_json_value(name: &String, value: &Value) -> Result<IconMetadata> {
        let label = value
            .get("label")
            .ok_or_else(|| err!(json, "Missing label"))?
            .as_str()
            .ok_or_else(|| err!(json, "Label is not a string"))?
            .to_string();
        let unicode = value
            .get("unicode")
            .ok_or_else(|| err!(json, "Missing unicode"))?
            .as_str()
            .ok_or_else(|| err!(json, "Unicode is not a string"))?
            .to_string();
        let search = value
            .get("search")
            .ok_or_else(|| err!(json, "Missing search"))?
            .as_object()
            .ok_or_else(|| err!(json, "Search is not an object"))?
            .get("terms")
            .ok_or_else(|| err!(json, "Missing search terms"))?
            .as_array()
            .ok_or_else(|| err!(json, "Search terms is not an array"))?
            .iter()
            .map(|v| {
                v.as_str()
                    .ok_or_else(|| err!(json, "Search item is not a string"))
            })
            .collect::<Result<Vec<&str>>>()?
            .iter()
            .map(|s| s.to_string())
            .collect();
        let styles = value
            .get("styles")
            .ok_or_else(|| err!(json, "Missing styles"))?
            .as_array()
            .ok_or_else(|| err!(json, "Styles is not an array"))?
            .iter()
            .map(|v| {
                v.as_str()
                    .ok_or_else(|| err!(json, "Styles item is not a string"))
            })
            .collect::<Result<Vec<&str>>>()?
            .iter()
            .map(|s| s.to_string())
            .collect();
        let aliases: Vec<String> = value
            .get("aliases")
            .map(|v| {
                v.as_array()
                    .map(|a| a.iter().filter_map(|v| v.as_str()).collect::<Vec<&str>>())
                    .unwrap_or_default()
                    .iter()
                    .map(|s| s.to_string())
                    .collect()
            })
            .unwrap_or_default();
        Ok(IconMetadata {
            name: name.to_string(),
            label,
            unicode,
            search,
            styles,
            aliases,
        })
    }
}

#[derive(thiserror::Error, Debug)]
enum Error {
    #[error("Failed to fetch icon json")]
    Fetch(#[from] reqwest::Error),

    #[error("Failed to parse icon json")]
    Parse(#[from] serde_json::Error),

    #[error("Failed to write file")]
    Write(#[from] std::io::Error),

    #[error("Invalid JSON: {0}")]
    InvalidJson(String),
}

type Result<T> = std::result::Result<T, Error>;
