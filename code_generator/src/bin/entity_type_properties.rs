/// This script generates parts for the EntityTypeProperty.kt file.
/// It uses the data provided by https://joakimthorsen.github.io/MCPropertyEncyclopedia/entities.html?selection=eye_height,height,id,width
/// To generate the different parts of the data.
use serde::{Deserialize, Serialize};
use std::collections::HashMap;
use std::fs::File;
use std::io::Write;

#[derive(Debug, Deserialize, Serialize)]
struct Entity {
    entity: String,
    eye_height: EntityDimension,
    height: EntityDimension,
    id: String,
    width: EntityDimension,
}

#[derive(Debug, Deserialize, Serialize)]
#[serde(untagged)]
enum EntityDimension {
    Simple(f64),
    Complex(HashMap<String, f64>),
}

fn parse_json(json_str: &str) -> Result<Vec<Entity>, serde_json::Error> {
    serde_json::from_str(json_str)
}

fn format_value(value: f64) -> String {
    if value == value.trunc() {
        format!("{:.1}", value)
    } else {
        format!("{}", value)
    }
}

fn format_property(property: &str, value: &str) -> String {
    match property {
        "pose" => format!("pose = EntityPose.{}", value.to_uppercase()),
        "puff_state" => format!("puffState = PufferFishMeta.State.entries[{}]", value),
        "is_baby" => format!("isBaby = {}", value),
        _ => format!("{} = {}", property, value),
    }
}

fn generate_kotlin_code<F>(entities: &[Entity], property_accessor: F) -> Vec<String>
where
    F: Fn(&Entity) -> &EntityDimension,
{
    entities
        .iter()
        .flat_map(|entity| generate_entity_code(entity, &property_accessor))
        .collect()
}

fn generate_entity_code<F>(entity: &Entity, property_accessor: F) -> Vec<String>
where
    F: Fn(&Entity) -> &EntityDimension,
{
    let entity_type = format!("EntityTypes.{}", entity.id.to_uppercase());

    match property_accessor(entity) {
        EntityDimension::Simple(value) => {
            vec![format!(
                "EntityDataMatcher({}) -> {}",
                entity_type,
                format_value(*value)
            )]
        }
        EntityDimension::Complex(map) => map
            .iter()
            .flat_map(|(key, value)| {
                key.split("<br>")
                    .map(|condition_set| {
                        let conditions = parse_conditions(condition_set);
                        let matcher = format!(
                            "EntityDataMatcher({}, {})",
                            entity_type,
                            conditions.join(", ")
                        );
                        format!("{} -> {}", matcher, format_value(*value))
                    })
                    .collect::<Vec<String>>()
            })
            .collect(),
    }
}

fn parse_conditions(condition_set: &str) -> Vec<String> {
    condition_set
        .split(", ")
        .filter_map(|condition| {
            let parts: Vec<&str> = condition.split(": ").collect();
            if parts.len() == 2 {
                Some(format_property(parts[0], parts[1]))
            } else {
                None
            }
        })
        .collect()
}

fn write_kotlin_file(file_name: &str, content: &[String]) -> std::io::Result<()> {
    let mut file = File::create(file_name)?;
    writeln!(
        file,
        "//<editor-fold desc=\"Entity {} by properties\">",
        file_name.split('.').next().unwrap()
    )?;
    for line in content {
        writeln!(file, "        {}", line)?;
    }
    writeln!(file, "//</editor-fold>")?;
    Ok(())
}

fn generate_property(
    entities: &[Entity],
    property_name: &str,
    accessor: impl Fn(&Entity) -> &EntityDimension,
) -> String {
    let kotlin_code = generate_kotlin_code(&entities, accessor);
    let file_name = format!("Entity{}Property.kt", property_name);
    write_kotlin_file(&file_name, &kotlin_code).unwrap();
    println!("{} generated successfully!", file_name);
    file_name
}

fn main() -> Result<(), Box<dyn std::error::Error>> {
    let json_str = std::fs::read_to_string("entitylist.json")?;
    let entities = parse_json(&json_str)?;

    generate_property(&entities, "EyeHeight", |e: &Entity| &e.eye_height);
    generate_property(&entities, "Height", |e: &Entity| &e.height);
    generate_property(&entities, "Width", |e: &Entity| &e.width);

    Ok(())
}
