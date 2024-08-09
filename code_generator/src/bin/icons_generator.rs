use code_generator::*;
use convert_case::{Case, Casing};
use indoc::writedoc;
use itertools::Itertools;
use lazy_static::lazy_static;
use serde::Deserialize;
use std::collections::HashMap;
use std::fmt::Write;
use std::path::Path;
use tokio::fs::File;
use tokio::io::AsyncWriteExt;

#[tokio::main]
async fn main() -> Result<()> {
    let collections = fetch_collections().await?;

    let kotlin = format_for_kotlin(&collections).await?;

    println!("Writing to 'Icons.kt'...");
    // Write to 'Icons.kt'
    let path = Path::new("Icons.kt");
    let mut file = File::create(path).await?;
    file.write_all(kotlin.as_bytes()).await?;

    let full_path = std::fs::canonicalize(path)?;
    println!("Written to: {}", full_path.display());
    Ok(())
}

async fn format_for_kotlin(collections: &HashMap<String, IconCollection>) -> Result<String> {
    let mut result = String::new();

    writedoc!(
        result,
        "
              package me.gabber235.typewriter.utils

              /**
               * There are multiple collections of icons available. 
               * Each has its own enum class.
               * To find the icon you want, use https://icon-sets.iconify.design/
               */
              class Icons private constructor() {{
              "
    )?;

    for (collection, icon_collection) in collections.iter().sorted_by_key(|(k, _)| k.to_string()) {
        let enum_class = create_icon_collection_enum(collection, icon_collection).await?;
        writeln!(result, "{}", enum_class)?;
    }

    writedoc!(
        result,
        "
        }}
        "
    )?;

    Ok(result)
}

lazy_static! {
    static ref NAME_ADJUSTMENTS: HashMap<String, String> = collection! {
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
    };
}

async fn fetch_collections() -> Result<HashMap<String, IconCollection>> {
    let url = "https://api.iconify.design/collections";
    let response = reqwest::get(url).await?;
    let text = response.text().await?;
    Ok(serde_json::from_str(&text)?)
}

#[derive(Debug, Deserialize)]
struct IconCollection {
    name: String,
    total: u32,
    version: Option<String>,
    author: Author,
}

#[derive(Debug, Deserialize)]
struct Author {
    name: String,
    url: Option<String>,
}

async fn create_icon_collection_enum(
    collection: &str,
    icon_collection: &IconCollection,
) -> Result<String> {
    let icons = fetch_icons(collection).await?;
    let mut result = String::new();

    let class_name = collection.to_case(Case::Pascal);

    writedoc!(
        result,
        "
        /**
         * Icon collection for the [{name}] {version} icon set ({total} icons).
         * Made by [{author_name} ({author_url})].
         * 
         *
         * To find the icon you want, use https://icon-sets.iconify.design/
         */
        enum class {class}(val id: String) {{
        ",
        name = icon_collection.name,
        class = class_name,
        version = icon_collection.version.as_deref().unwrap_or(""),
        total = icon_collection.total,
        author_name = icon_collection.author.name,
        author_url = icon_collection.author.url.as_deref().unwrap_or(""),
    )?;

    for icon in icons.iter().sorted() {
        let id = icon.to_case(Case::UpperSnake);
        let id = NAME_ADJUSTMENTS.get(&id).unwrap_or(&id);

        writedoc!(
            result,
            "
            {id}(\"{icon}\"),
            ",
            icon = icon,
            id = id
        )?;
    }

    writedoc!(
        result,
        "
        }}
        "
    )?;

    Ok(result)
}

async fn fetch_icons(collection: &str) -> Result<Vec<String>> {
    let url = format!(
        "https://api.iconify.design/collection?prefix={}",
        collection
    );
    println!("{}", url);
    let response = reqwest::get(&url).await?;

    if !response.status().is_success() {
        return Err(Error::InvalidJson(response.text().await?));
    }

    let text = response.text().await?;
    let json: Collection = serde_json::from_str(&text)?;

    let icons = match json {
        Collection::Uncategorized { uncategorized } => uncategorized,
        Collection::Categories { categories } => categories
            .values()
            .flatten()
            .map(|s| s.to_string())
            .collect(),
    };
    Ok(icons)
}

#[derive(Debug, Deserialize)]
#[serde(untagged)]
enum Collection {
    Uncategorized {
        uncategorized: Vec<String>,
    },
    Categories {
        categories: HashMap<String, Vec<String>>,
    },
}

#[derive(thiserror::Error, Debug)]
enum Error {
    #[error("Failed to fetch icon json: {0}")]
    Fetch(#[from] reqwest::Error),

    #[error("Failed to parse icon json: {0}")]
    Parse(#[from] serde_json::Error),

    #[error("Failed to write file: {0}")]
    Write(#[from] std::io::Error),

    #[error("Failed to format: {0}")]
    Format(#[from] std::fmt::Error),

    #[error("Invalid JSON: {0}")]
    InvalidJson(String),
}

type Result<T> = std::result::Result<T, Error>;
