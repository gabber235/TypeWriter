use thiserror::Error;
use tree_sitter::{Parser, Query, QueryCursor};

pub fn print_tree(code: &str) {
    let mut parser = Parser::new();
    parser.set_language(tree_sitter_kotlin::language()).unwrap();

    let tree = parser.parse(&code, None).unwrap();

    println!("{}", tree.root_node().to_sexp());
}

pub(crate) fn query(code: &str, query: &str) -> Result<QueryResult, QueryError> {
    let mut parser = Parser::new();
    parser.set_language(tree_sitter_kotlin::language())?;

    let query = Query::new(tree_sitter_kotlin::language(), query)?;

    let parsed = parser.parse(&code, None).ok_or(QueryError::ParseError)?;

    let mut cursor = QueryCursor::new();
    let matches = cursor.matches(&query, parsed.root_node(), code.as_bytes());
    let captuers: Result<Vec<QueryCapture>, QueryError> = matches
        .into_iter()
        .flat_map(|mat| {
            mat.captures.into_iter().map(|cap| {
                cap.node
                    .utf8_text(code.as_bytes())
                    .map(|text| QueryCapture::new(text.to_string()))
                    .map_err(|error| QueryError::Utf8Error(error))
            })
        })
        .collect();

    let captuers = captuers?;

    Ok(QueryResult::new(captuers))
}

#[derive(Debug)]
pub struct QueryResult {
    pub captures: Vec<QueryCapture>,
}

impl QueryResult {
    fn new(captures: Vec<QueryCapture>) -> QueryResult {
        QueryResult { captures }
    }
}

impl ToString for QueryResult {
    fn to_string(&self) -> String {
        let mut result = String::new();
        for cap in &self.captures {
            result.push_str(&cap.to_string());
        }
        result
    }
}

#[derive(Debug)]
pub struct QueryCapture {
    pub code: String,
}

impl QueryCapture {
    fn new(code: String) -> QueryCapture {
        QueryCapture { code }
    }
}

impl ToString for QueryCapture {
    fn to_string(&self) -> String {
        format!("{}", self.code)
    }
}

#[derive(Debug, Error)]
pub enum QueryError {
    #[error("Error parsing code")]
    ParseError,

    #[error("Error parsing query")]
    QueryError(#[from] tree_sitter::QueryError),

    #[error("Given language is not supported")]
    LanguageError(#[from] tree_sitter::LanguageError),

    #[error("Error parsing query")]
    Utf8Error(#[from] std::str::Utf8Error),
}
