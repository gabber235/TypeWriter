package com.typewritermc.example.entries

import com.typewritermc.engine.paper.entry.*

//<code-block:simple_placeholder_entry>
class SimpleExamplePlaceholderEntry(
    override val id: String,
    override val name: String,
) : PlaceholderEntry {
    override fun parser(): PlaceholderParser = placeholderParser {
        supply { player ->
            "Hello, ${player?.name ?: "World"}!"
        }
    }
}
//</code-block:simple_placeholder_entry>

class LiteralExamplePlaceholderEntry(
    override val id: String,
    override val name: String,
) : PlaceholderEntry {
//<code-block:literal_placeholder_entry>
    override fun parser(): PlaceholderParser = placeholderParser {
        literal("greet") {
            literal("enthusiastic") {
                supply { player ->
                    "HEY HOW IS YOUR DAY, ${player?.name ?: "World"}!"
                }
            }
            supply { player ->
                "Hello, ${player?.name ?: "World"}"
            }
        }
        supply {
            "Standard text"
        }
    }
//</code-block:literal_placeholder_entry>
}

class StringExamplePlaceholderEntry(
    override val id: String,
    override val name: String,
) : PlaceholderEntry {
//<code-block:string_placeholder_entry>
    override fun parser(): PlaceholderParser = placeholderParser {
        string("name") { name ->
            supply {
                "Hello, ${name()}!"
            }
        }
    }
//</code-block:string_placeholder_entry>
}