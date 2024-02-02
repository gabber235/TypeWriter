use colored::Colorize;
use convert_case::{Case, Casing};
use indicatif::{ParallelProgressIterator, ProgressIterator, ProgressStyle};
use itertools::Itertools;
use rayon::iter::{IntoParallelRefIterator, ParallelIterator};
use std::{
    fs::{self, read_dir, File},
    io::{Read, Write},
    path::{Path, PathBuf},
};
use thiserror::Error;
use walkdir::WalkDir;

use crate::{
    adapter_file_parser::{parse_adapter_class, AdapterClass, AdapterParseError},
    entry_file_parser::{parse_entry_class, EntryClass, EntryParseError},
    markdown_formatter::{format_adapter_markdown, format_entry_markdown},
};

#[derive(Debug)]
pub struct AdapterParsed {
    pub adapter: String,
    file_path: String,
    file_name: String,
    pub adapter_data: AdapterClass,
}

#[derive(Debug)]
struct AdapterFormatted {
    adapter: String,
    file_path: String,
    file_name: String,
    adapter_data: AdapterClass,
    markdown: String,
}

#[derive(Debug)]
struct EntryRef {
    adapter: String,
    category: String,
    file_path: String,
    file_name: String,
}

#[derive(Debug)]
pub struct EntryParsed {
    pub adapter: String,
    pub category: String,
    file_path: String,
    file_name: String,
    pub entry_data: EntryClass,
}

#[derive(Debug)]
struct EntryFormatted {
    adapter: String,
    category: String,
    file_path: String,
    file_name: String,
    entry_data: EntryClass,
    markdown: String,
}

pub fn orchestrate_documentation_creation() -> Result<(), OrchestrationError> {
    let adapters = find_adapters()?;

    println!("Found {} adapters", adapters.len());

    let references = find_references()?;

    let parsed = parse_entries(references)?;

    println!("Found {} entries", parsed.len());

    let formatted_adapters = format_adapters(adapters, &parsed)?;

    let formatted_entries = format_entries(parsed)?;

    write_files(formatted_adapters, formatted_entries)?;

    Ok(())
}

fn write_files(
    adapters: Vec<AdapterFormatted>,
    entries: Vec<EntryFormatted>,
) -> Result<(), OrchestrationError> {
    for adapter in adapters {
        let adapter_entries = entries
            .iter()
            .filter(|entry| entry.adapter == adapter.adapter)
            .collect::<Vec<&EntryFormatted>>();
        write_adapter_files(&adapter, adapter_entries)?;
    }

    return Ok(());
}

fn write_adapter_files(
    adapter: &AdapterFormatted,
    entries: Vec<&EntryFormatted>,
) -> Result<(), OrchestrationError> {
    let base_path = Path::new("../documentation/docs/adapters").join(&adapter.adapter);

    // If the directory already exists, then we ignore it
    if base_path.exists() {
        println!(
            "Directory {} already exists, skipping {}",
            base_path.to_str().unwrap().yellow(),
            adapter.adapter.yellow(),
        );
        return Ok(());
    }

    fs::create_dir_all(&base_path)?;

    let mut adapter_file = File::create(base_path.join(format! {"{}.mdx", adapter.adapter}))?;
    adapter_file.write_all(adapter.markdown.as_bytes())?;

    let mut categories_file = File::create(base_path.join("_category_.yml"))?;
    categories_file
        .write_all(format!("label: {}", adapter.adapter.to_case(Case::Title)).as_bytes())?;

    let entries_dir = base_path.join("entries");
    fs::create_dir_all(&entries_dir)?;

    let mut entries_category_file = File::create(entries_dir.join("_category_.yml"))?;
    entries_category_file.write_all(b"label: Entries")?;

    let categories = entries.iter().map(|entry| entry.category.clone()).unique();

    for category in categories {
        let category_dir = entries_dir.join(&category);
        fs::create_dir_all(&category_dir)?;

        let mut category_file = File::create(category_dir.join("_category_.yml"))?;
        category_file.write_all(format!("label: {}s", category.to_case(Case::Title)).as_bytes())?;
    }

    for entry in entries {
        write_entry_file(&entries_dir, entry)?;
    }

    return Ok(());
}

fn write_entry_file(dir: &Path, entry: &EntryFormatted) -> Result<(), OrchestrationError> {
    let mut entry_file =
        File::create(dir.join(format! {"{}/{}.mdx", entry.category, entry.entry_data.name}))?;
    entry_file.write_all(entry.markdown.as_bytes())?;

    return Ok(());
}

fn format_entries(entries: Vec<EntryParsed>) -> Result<Vec<EntryFormatted>, OrchestrationError> {
    let progress_style = ProgressStyle::with_template(
        "Formatting Entries: [{elapsed_precise}] {bar:40.cyan/blue} {pos:>7}/{len:7} {msg}",
    )
    .unwrap();
    let formatted: Vec<Result<EntryFormatted, OrchestrationError>> = entries
        .par_iter()
        .progress_with_style(progress_style)
        .map(|entry| format_entry(entry))
        .collect();

    let mut formatted_entries = vec![];
    for entry in formatted {
        match entry {
            Err(error) => match error {
                OrchestrationError::FormatError(error, file) => {
                    println!(
                        "Error while formatting file {}: {}",
                        file.yellow(),
                        error.to_string().red()
                    );
                }
                _ => {
                    return Err(error);
                }
            },
            Ok(entry) => {
                formatted_entries.push(entry);
            }
        }
    }

    return Ok(formatted_entries);
}

fn format_entry(entry: &EntryParsed) -> Result<EntryFormatted, OrchestrationError> {
    let markdown = format_entry_markdown(&entry.entry_data)
        .map_err(|error| OrchestrationError::FormatError(error, entry.file_path.clone()))?;

    return Ok(EntryFormatted {
        adapter: entry.adapter.clone(),
        category: entry.category.clone(),
        file_path: entry.file_path.clone(),
        file_name: entry.file_name.clone(),
        entry_data: entry.entry_data.clone(),
        markdown,
    });
}

fn parse_entries(documents: Vec<EntryRef>) -> Result<Vec<EntryParsed>, OrchestrationError> {
    let progress_style = ProgressStyle::with_template(
        "Parsing Documents: [{elapsed_precise}] {bar:40.cyan/blue} {pos:>7}/{len:7} {msg}",
    )
    .unwrap();
    let parsed: Vec<Result<Vec<EntryParsed>, OrchestrationError>> = documents
        .par_iter()
        .progress_with_style(progress_style)
        .map(|document| parse_entry(document))
        .collect();

    let mut parsed_documents = vec![];
    for document in parsed {
        match document {
            Err(error) => match error {
                OrchestrationError::NoEntriesFoundError(file) => {
                    println!("No entries found in {}", file.yellow());
                }
                OrchestrationError::EntryParseError(error, file) => {
                    println!(
                        "Error while parsing file {}: {}",
                        file.yellow(),
                        error.to_string().red()
                    );
                }
                _ => {
                    return Err(error);
                }
            },
            Ok(mut entries) => {
                parsed_documents.append(&mut entries);
            }
        }
    }

    Ok(parsed_documents)
}

fn parse_entry(document: &EntryRef) -> Result<Vec<EntryParsed>, OrchestrationError> {
    let file = get_file(&document.file_path)?;
    let entry_data = match parse_entry_class(&file) {
        Ok(entry_data) => entry_data,
        Err(error) => {
            return Err(OrchestrationError::EntryParseError(
                error,
                format!("Error while parsing file {}", document.file_path),
            ))
        }
    };

    if entry_data.is_empty() {
        return Err(OrchestrationError::NoEntriesFoundError(format!(
            "{} » {} » {}",
            document.adapter, document.category, document.file_name
        )));
    }

    entry_data
        .iter()
        .map(|entry| {
            Ok(EntryParsed {
                adapter: document.adapter.clone(),
                category: document.category.clone(),
                file_path: document.file_path.clone(),
                file_name: document.file_name.clone(),
                entry_data: entry.clone(),
            })
        })
        .collect()
}

fn format_adapters(
    adapters: Vec<AdapterParsed>,
    entries: &Vec<EntryParsed>,
) -> Result<Vec<AdapterFormatted>, OrchestrationError> {
    let progress_style = ProgressStyle::with_template(
        "Formatting Adapters: [{elapsed_precise}] {bar:40.cyan/blue} {pos:>7}/{len:7} {msg}",
    )
    .unwrap();

    adapters
        .par_iter()
        .progress_with_style(progress_style)
        .map(|adapter| format_adapter(adapter, entries))
        .collect()
}

fn format_adapter(
    adapter: &AdapterParsed,
    entries: &Vec<EntryParsed>,
) -> Result<AdapterFormatted, OrchestrationError> {
    let adapter_entries: Vec<&EntryParsed> = entries
        .iter()
        .filter(|entry| entry.adapter == adapter.adapter)
        .collect();
    let markdown = format_adapter_markdown(adapter, adapter_entries)
        .map_err(|error| OrchestrationError::FormatError(error, adapter.file_path.clone()))?;

    Ok(AdapterFormatted {
        adapter: adapter.adapter.clone(),
        file_path: adapter.file_path.clone(),
        file_name: adapter.file_name.clone(),
        adapter_data: adapter.adapter_data.clone(),
        markdown,
    })
}

fn find_adapters() -> Result<Vec<AdapterParsed>, OrchestrationError> {
    let progress_style = ProgressStyle::with_template(
        "Finding Adapters: [{elapsed_precise}] {bar:40.cyan/blue} {pos:>7}/{len:7} {msg}",
    )
    .unwrap();

    let adapters = find_adapters_names()?;

    adapters
        .iter()
        .progress_with_style(progress_style)
        .map(|adapter| find_adapter_data(adapter))
        .collect()
}

fn find_adapter_data(adapter: &str) -> Result<AdapterParsed, OrchestrationError> {
    let adapter_src = find_adapter_src(adapter)?;

    let kotlin_files = read_dir(adapter_src.clone())?
        .filter_map(|entry| entry.map(|entry| entry.path()).ok())
        .filter(|path| path.is_file())
        .filter(|path| path.extension().and_then(|ext| ext.to_str()) == Some("kt"))
        .collect::<Vec<PathBuf>>();

    let adapters_data = kotlin_files
        .iter()
        .map(|file| parse_adapter_data(file.clone()))
        .collect::<Vec<Result<(PathBuf, AdapterClass), OrchestrationError>>>();

    let mut adapter_data = None;

    for data in adapters_data {
        match data {
            Ok(info) => {
                adapter_data = Some(info);
            }
            Err(OrchestrationError::AdapterParseError(AdapterParseError::NotAdapterClass, _)) => {
                continue;
            }
            Err(error) => {
                return Err(error);
            }
        }
    }

    let Some((adapter_file, adapter_data)) = adapter_data else {
        return Err(OrchestrationError::AdapterParseError(
            AdapterParseError::NotAdapterClass,
            format!("No adapter class found in {}", adapter_src),
        ));
    };

    let file_name = adapter_file
        .file_name()
        .ok_or(OrchestrationError::AdapterParseError(
            AdapterParseError::CouldNotReadFile,
            "File not found".to_string(),
        ))?
        .to_str()
        .ok_or(OrchestrationError::AdapterParseError(
            AdapterParseError::CouldNotReadFile,
            "File not found".to_string(),
        ))?
        .to_string();

    let file_path = adapter_file
        .to_str()
        .ok_or(OrchestrationError::AdapterParseError(
            AdapterParseError::CouldNotReadFile,
            "File not found".to_string(),
        ))?
        .to_string();

    Ok(AdapterParsed {
        adapter: adapter.to_string(),
        file_name,
        file_path,
        adapter_data,
    })
}

fn parse_adapter_data(file: PathBuf) -> Result<(PathBuf, AdapterClass), OrchestrationError> {
    let path = file.to_str().ok_or(OrchestrationError::AdapterParseError(
        AdapterParseError::CouldNotReadFile,
        "File not found".to_string(),
    ))?;
    let contents = get_file(&path)?;
    let adapter_data = parse_adapter_class(&contents).map_err(|error| {
        OrchestrationError::AdapterParseError(
            error,
            format!("Error while parsing file {}", file.to_str().unwrap()),
        )
    })?;
    Ok((file, adapter_data))
}

fn find_references() -> Result<Vec<EntryRef>, OrchestrationError> {
    let progress_style = ProgressStyle::with_template(
        "Finding References: [{elapsed_precise}] {bar:40.cyan/blue} {pos:>7}/{len:7} {msg}",
    )
    .unwrap();
    let adapters = find_adapters_names()?;

    let references: Vec<Result<Vec<EntryRef>, OrchestrationError>> = adapters
        .par_iter()
        .progress_with_style(progress_style)
        .map(|adapter| find_adapter_references(adapter))
        .collect();

    let mut documents = vec![];
    for reference in references {
        documents.append(&mut reference?);
    }
    return Ok(documents);
}

fn find_adapter_references(adapter: &str) -> Result<Vec<EntryRef>, OrchestrationError> {
    let adpater_src = find_adapter_entries_dir(adapter)?;
    let categories = find_categories(&adpater_src)?;

    let references: Vec<Result<Vec<EntryRef>, OrchestrationError>> = categories
        .iter()
        .map(|category| find_category_references(adapter, category, &adpater_src))
        .collect();

    let mut documents = vec![];
    for reference in references {
        documents.append(&mut reference?);
    }
    return Ok(documents);
}

fn find_category_references(
    adapter: &str,
    category: &str,
    adpater_src: &str,
) -> Result<Vec<EntryRef>, OrchestrationError> {
    let category_src = format!("{}/{}", adpater_src, category);

    let references = WalkDir::new(category_src)
        .into_iter()
        .filter_map(|e| e.ok())
        .filter(|e| e.file_type().is_file())
        .filter(|e| e.path().extension().is_some())
        .filter(|e| e.path().extension().unwrap() == "kt")
        .map(|e| {
            let file_path = e.path().to_str().unwrap();
            let file_name = e.file_name().to_str().unwrap();
            EntryRef {
                adapter: adapter.to_string(),
                category: category.to_string(),
                file_path: file_path.to_string(),
                file_name: file_name.to_string(),
            }
        })
        .collect::<Vec<EntryRef>>();

    Ok(references)
}

fn find_adapters_names() -> Result<Vec<String>, OrchestrationError> {
    let adpaters_dir = fs::read_dir("../adapters")?;

    // Get all the adapters
    let adapters = adpaters_dir
        .map(|res| res.map(|e| e.path()))
        .filter(|path| path.is_ok())
        .map(|path| path.unwrap())
        .filter(|path| path.is_dir())
        .map(|path| path.file_name().unwrap().to_str().unwrap().to_string())
        .collect::<Vec<String>>();

    return Ok(adapters);
}

fn find_categories(adapter_src: &str) -> Result<Vec<String>, OrchestrationError> {
    let categories_dir = fs::read_dir(adapter_src)?;

    // Get all the adapters
    let categories = categories_dir
        .map(|res| res.map(|e| e.path()))
        .filter(|path| path.is_ok())
        .map(|path| path.unwrap())
        .filter(|path| path.is_dir())
        .map(|path| path.file_name().unwrap().to_str().unwrap().to_string())
        .collect::<Vec<String>>();

    return Ok(categories);
}

fn find_adapter_entries_dir(adapter: &str) -> Result<String, OrchestrationError> {
    let base = find_adapter_src(adapter)?;

    // The entries should be in {base}/entries
    let entries_dir = format!("{}/entries", base);

    if !Path::new(&entries_dir).exists() {
        return Err(OrchestrationError::FindEntryFolderError);
    }

    Ok(entries_dir)
}

fn find_adapter_src(adapter: &str) -> Result<String, OrchestrationError> {
    let base = format!("../adapters/{}/src/main/kotlin", adapter);

    // go down one level until we find a folder a .kt file
    let mut path = base;
    loop {
        // Get all files and folders in the current directory excluding hidden files
        let files = fs::read_dir(&path)?
            .map(|res| res.map(|e| e.path()))
            .filter(|path| path.is_ok())
            .map(|path| path.unwrap())
            .filter(|path| {
                !path
                    .file_name()
                    .unwrap_or_default()
                    .to_str()
                    .unwrap_or_default()
                    .starts_with(".")
            })
            .collect::<Vec<PathBuf>>();

        let kotlin_files = files
            .iter()
            .filter(|path| path.extension().unwrap_or_default() == "kt")
            .collect::<Vec<&PathBuf>>();

        let count = kotlin_files.len();

        if count > 0 {
            break;
        }

        let direcories = files
            .iter()
            .filter(|path| path.is_dir())
            .collect::<Vec<&PathBuf>>();

        let count = direcories.len();

        if count != 1 {
            return Err(OrchestrationError::FindAdapterSrcError);
        }

        let dir = direcories.first().unwrap();

        path = format!("{}/{}", path, dir.file_name().unwrap().to_str().unwrap());
    }

    Ok(path)
}

pub fn get_file(path: &str) -> Result<String, OrchestrationError> {
    let mut file = File::open(path)
        .map_err(|_| OrchestrationError::ReadFileError(format!("Could not read file: {}", path)))?;
    let mut contents = String::new();
    file.read_to_string(&mut contents)?;
    Ok(contents)
}

pub fn parse_and_format_entry_file(file: &str) -> Result<String, OrchestrationError> {
    let contents = get_file(file)?;
    let entry_data = match parse_entry_class(&contents) {
        Ok(entry_data) => entry_data,
        Err(error) => {
            return Err(OrchestrationError::EntryParseError(
                error,
                format!("Error while parsing file {}", file),
            ))
        }
    };

    if entry_data.len() == 0 {
        return Err(OrchestrationError::NoEntriesFoundError(file.to_string()));
    }

    let entry_data = entry_data.first().unwrap();

    format_entry_markdown(&entry_data)
        .map_err(|error| OrchestrationError::FormatError(error, file.to_string()))
}

#[derive(Debug, Error)]
pub enum OrchestrationError {
    #[error("Failed to parse entry: {0}, {1}")]
    EntryParseError(EntryParseError, String),

    #[error("Failed to parse adapter: {0}, {1}")]
    AdapterParseError(AdapterParseError, String),

    #[error("Failed to read from filesystem: {0}")]
    ReadDirError(#[from] std::io::Error),

    #[error("Failed to walk directory: {0}")]
    WalkDirError(#[from] walkdir::Error),

    #[error("Failed to find adapter src")]
    FindAdapterSrcError,

    #[error("Failed to find entry folder")]
    FindEntryFolderError,

    #[error("Failed to read from filesystem: {0}")]
    ReadFileError(String),

    #[error("Failed no entries found in: {0}")]
    NoEntriesFoundError(String),

    #[error("Failed to format: {0}, {1}")]
    FormatError(crate::markdown_formatter::FormatError, String),
}
