use documentation_orchestrator::{
    orchestrate_documentation_creation, parse_and_format_entry_file, OrchestrationError,
};

mod adapter_file_parser;
mod documentation_orchestrator;
mod entry_file_parser;
mod markdown_formatter;
mod treesitter;

fn main() -> Result<(), OrchestrationError> {
    // return parse_and_format_entry_file("../adapters/BasicAdapter/src/main/kotlin/me/gabber235/typewriter/entries/action/CinematicEntry.kt").map(|formatted| {
    //     println!("{}", "-".repeat(80));
    //     println!("{}", formatted);
    //     println!("{}", "-".repeat(80));
    // });
    return orchestrate_documentation_creation();
}
