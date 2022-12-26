use crate::Error::{InvalidFile, InvalidJson};
use code_generator::*;
use std::cmp::Ordering;

use recase::ReCase;
use regex::Regex;

use image::codecs::webp::WebPDecoder;
use image::ImageFormat::WebP;
use serde::Deserialize;
use serde_json::Value;
use std::collections::HashMap;
use std::fmt::{write, Display, Formatter};
use std::path::Path;
use std::str::FromStr;
use strum_macros::EnumString;
use tokio::fs::File;
use tokio::io::{AsyncReadExt, AsyncWriteExt};

#[tokio::main]
async fn main() -> Result<()> {
    let mut json = String::new();
    let mut file = File::open("materials.json").await?;
    file.read_to_string(&mut json).await?;

    let materials = parse_materials(&json)?;

    let dart = generator_for_dart(&materials);

    let mut file = File::create("materials.dart").await?;
    file.write_all(dart.as_bytes()).await?;

    check_files_skipped(&materials);

    Ok(())
}

#[derive(Debug)]
struct Material {
    id: String,
    name: String,
    icon: String,
    properties: Vec<Property>,
}

#[derive(Debug, Deserialize, EnumString)]
enum Property {
    Item,
    Block,
    Solid,
    Transparent,
    Flammable,
    Burnable,
    Edible,
    Fuel,
    Interactable,
    Occluding,
    Record,
    Tool,
    Weapon,
    Armor,
    Ore,
}

impl Display for Property {
    fn fmt(&self, f: &mut Formatter<'_>) -> std::fmt::Result {
        let name = match self {
            Property::Item => "item",
            Property::Block => "block",
            Property::Solid => "solid",
            Property::Transparent => "transparent",
            Property::Flammable => "flammable",
            Property::Burnable => "burnable",
            Property::Edible => "edible",
            Property::Fuel => "fuel",
            Property::Interactable => "intractable",
            Property::Occluding => "occluding",
            Property::Record => "record",
            Property::Tool => "tool",
            Property::Weapon => "weapon",
            Property::Armor => "armor",
            Property::Ore => "ore",
        };

        write!(f, "MaterialProperty.{}", name)
    }
}

fn parse_materials(json: &str) -> Result<Vec<Material>> {
    let mut materials = Vec::new();

    let json: Value = serde_json::from_str(json)?;
    let array = json
        .as_array()
        .ok_or_else(|| err!(json, "Main structure is not a json object"))?;

    for value in array {
        let id = value
            .get("id")
            .ok_or_else(|| err!(json, "Missing id"))?
            .as_str()
            .ok_or_else(|| err!(json, "Id is not a string"))?
            .to_string();

        let name = value
            .get("name")
            .ok_or_else(|| err!(json, "Missing name"))?
            .as_str()
            .ok_or_else(|| err!(json, "Name is not a string"))?
            .to_string();

        let properties = value
            .get("properties")
            .ok_or_else(|| err!(json, "Missing properties"))?
            .as_array()
            .ok_or_else(|| err!(json, "Properties is not an array"))?
            .iter()
            .map(|value| {
                let property = value
                    .as_str()
                    .ok_or_else(|| err!(json, "Property is not a string"))?;
                let property = property.to_string();
                let property = ReCase::new(property).pascal_case();
                let property = serde_json::from_str(&format!("\"{}\"", property))?;
                Ok(property)
            })
            .collect::<Result<Vec<Property>>>()?;

        let icon = fetch_icon(&id)?;

        materials.push(Material {
            id,
            name: format_name(name),
            icon,
            properties,
        });
    }

    Ok(materials)
}

fn format_name(name: String) -> String {
    ReCase::new(name.to_lowercase()).title_case()
}

fn fetch_icon(name: &str) -> Result<String> {
    let file_png = format!("items_1.19.2/{}.png", name);
    let file_webp = format!("items_1.19.2/{}.webp", name);

    let png_exists = Path::new(&file_png).exists();
    let webp_exists = Path::new(&file_webp).exists();

    if png_exists && webp_exists {
        return Err(err!(
            file,
            format!("Both png and webp files exist for {}", name)
        ));
    }

    if png_exists {
        return Ok(format!("assets/materials/{}.png", name));
    }

    if webp_exists {
        return Ok(format!("assets/materials/{}.webp", name));
    }

    Err(err!(file, format!("No icon found for {}", name)))
}

fn generator_for_dart(materials: &Vec<Material>) -> String {
    let mut code = String::new();

    for material in materials {
        code.push_str(&format!(
            "\"{}\": MinecraftMaterial(name: \"{}\", properties: [{}], icon: \"{}\",),\n",
            material.id,
            material.name,
            material
                .properties
                .iter()
                .map(Property::to_string)
                .collect::<Vec<String>>()
                .join(", "),
            material.icon
        ));
    }

    code
}

fn check_files_skipped(materials: &[Material]) {
    let files = std::fs::read_dir("items_1.19.2").unwrap();
    let mut files: Vec<String> = files
        .map(|file| file.unwrap().file_name().into_string().unwrap())
        .collect();

    for material in materials {
        let png = format!("{}.png", material.id);
        let webp = format!("{}.webp", material.id);

        files.retain(|file| file != &png && file != &webp);
    }

    if !files.is_empty() {
        println!("Files skipped: {:?}", files);
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

    #[error("Failed to load image")]
    Image(#[from] image::ImageError),

    #[error("Invalid JSON: {0}")]
    InvalidJson(String),

    #[error("Invalid File: {0}")]
    InvalidFile(String),
}

type Result<T> = std::result::Result<T, Error>;
