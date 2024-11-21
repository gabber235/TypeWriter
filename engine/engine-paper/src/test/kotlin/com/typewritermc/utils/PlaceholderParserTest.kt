package com.typewritermc.utils

import com.typewritermc.engine.paper.entry.literal
import com.typewritermc.engine.paper.entry.placeholderParser
import com.typewritermc.engine.paper.entry.string
import com.typewritermc.engine.paper.entry.supply
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class PlaceholderParserTest : FunSpec({
    test("Parser should parse with no arguments") {
        val parser = placeholderParser {
            supply {
                "Hey"
            }
        }

        parser.parse(null, emptyList()) shouldBe "Hey"
    }

    test("Parser with no argument should fail when given an argument") {
        val parser = placeholderParser {
            supply {
                "Hey"
            }
        }

        parser.parse(null, listOf("Hello")) shouldBe null
    }

    context("Literals") {
        test("Parser with single literal should parse when given the literal, otherwise it should fail") {
            val parser = placeholderParser {
                literal("hello") {
                    supply {
                        "Hey"
                    }
                }
            }

            parser.parse(null, listOf("hello")) shouldBe "Hey"
            parser.parse(null, listOf("world")) shouldBe null
            parser.parse(null, listOf("hello", "world")) shouldBe null
            parser.parse(null, emptyList()) shouldBe null
        }
        test("Parser with multiple literals should only parse when all literals are supplied") {
            val parser = placeholderParser {
                literal("hello") {
                    supply {
                        "Hey"
                    }
                }
                literal("world") {
                    supply {
                        "World"
                    }
                }
            }

            parser.parse(null, listOf("hello")) shouldBe "Hey"
            parser.parse(null, listOf("world")) shouldBe "World"
            parser.parse(null, listOf("something")) shouldBe null
            parser.parse(null, listOf("hello", "world")) shouldBe null
            parser.parse(null, emptyList()) shouldBe null
        }
        test("Parser with multiple nested literals should only parse the correct literals") {
            val parser = placeholderParser {
                literal("hello") {
                    literal("world") {
                        supply {
                            "Hey"
                        }
                    }
                }
            }

            parser.parse(null, listOf("hello", "world")) shouldBe "Hey"
            parser.parse(null, listOf("hello", "something")) shouldBe null
            parser.parse(null, listOf("something", "world")) shouldBe null
            parser.parse(null, emptyList()) shouldBe null
        }
        test("Parser with multiple executors should only supply the correct executor") {
            val parser = placeholderParser {
                literal("hello") {
                    supply {
                        "Hey"
                    }
                }
                supply {
                    "World"
                }
            }

            parser.parse(null, listOf("hello")) shouldBe "Hey"
            parser.parse(null, listOf("world")) shouldBe null
            parser.parse(null, emptyList()) shouldBe "World"
        }

        test("Parser with overlapping literals will only parse where all arguments match") {
            val parser = placeholderParser {
                literal("hello") {
                    literal("world") {
                        supply {
                            "Hey"
                        }
                    }
                    supply {
                        "World"
                    }
                }
                literal("hello") {
                    literal("sun") {
                        supply {
                            "Sun"
                        }
                    }
                    supply {
                        "Impossible"
                    }
                }
            }

            parser.parse(null, listOf("hello", "world")) shouldBe "Hey"
            parser.parse(null, listOf("hello", "sun")) shouldBe "Sun"
            parser.parse(null, listOf("hello", "something")) shouldBe null
            parser.parse(null, listOf("hello")) shouldBe "World"
            parser.parse(null, emptyList()) shouldBe null
        }
    }

    context("String Argument") {
        test("Parser with string argument should parse when given the argument, otherwise it should fail") {
            val parser = placeholderParser {
                string("name") { string ->
                    supply {
                        "Hey ${string()}"
                    }
                }
            }

            parser.parse(null, listOf("bob")) shouldBe "Hey bob"
            parser.parse(null, listOf("alice")) shouldBe "Hey alice"
            parser.parse(null, emptyList()) shouldBe null
        }

        test("Parser with multiple nested string arguments should only parse when all arguments are supplied") {
            val parser = placeholderParser {
                string("name") { string ->
                    string("action") { action ->
                        supply {
                            "Hey ${string()}, ${action()}"
                        }
                    }
                }
            }

            parser.parse(null, listOf("bob", "jump")) shouldBe "Hey bob, jump"
            parser.parse(null, listOf("bob", "run")) shouldBe "Hey bob, run"
            parser.parse(null, listOf("alice", "jump")) shouldBe "Hey alice, jump"
            parser.parse(null, listOf("alice", "run")) shouldBe "Hey alice, run"
            parser.parse(null, listOf("bob")) shouldBe null
            parser.parse(null, emptyList()) shouldBe null
        }

        test("Parser with literal and string argument should only run the literal") {
            val parser = placeholderParser {
                literal("hello") {
                    supply {
                        "Hey"
                    }
                }

                string("name") { string ->
                    supply {
                        "Hey ${string()}"
                    }
                }
            }

            parser.parse(null, listOf("hello")) shouldBe "Hey"
            parser.parse(null, listOf("bob")) shouldBe "Hey bob"
            parser.parse(null, emptyList()) shouldBe null
        }

        test("Parser with where multiple paths are possible should only use the first") {
            val parser = placeholderParser {
                string("name") { string ->
                    supply {
                        "Hey ${string()}"
                    }
                }

                string("name") { string ->
                    supply {
                        "Whats up ${string()}"
                    }
                }
            }

            parser.parse(null, listOf("bob")) shouldBe "Hey bob"
            parser.parse(null, emptyList()) shouldBe null
        }
    }
})