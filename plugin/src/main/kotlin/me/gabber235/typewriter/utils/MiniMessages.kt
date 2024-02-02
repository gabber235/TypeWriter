package me.gabber235.typewriter.utils

import me.gabber235.typewriter.entry.dialogue.confirmationKey
import net.kyori.adventure.text.*
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.Tag
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

private val mm = MiniMessage.builder()
    .tags(
        TagResolver.builder()
            .resolver(StandardTags.defaults())
            .tag("confirmation_key") { _, _ -> Tag.preProcessParsed(confirmationKey.keybind) }
            .build()
    )
    .build()

fun Component.asMini() = mm.serialize(this)

fun String.asMini() = mm.deserialize(this)

fun String.asMiniWithResolvers(vararg resolvers: TagResolver) = mm.deserialize(this, *resolvers)

fun CommandSender.sendMini(message: String) = sendMessage(message.asMini())

fun CommandSender.sendMiniWithResolvers(message: String, vararg resolvers: TagResolver) =
    sendMessage(message.asMiniWithResolvers(*resolvers))

fun CommandSender.msg(message: String) = sendMini("<red><bold>Typewriter »<reset><white> $message")

fun Component.plainText(): String = ChatColor.stripColor(PlainTextComponentSerializer.plainText().serialize(this)) ?: ""

fun Component.legacy(): String = LegacyComponentSerializer.legacy('§').serialize(this)

fun String.stripped(): String =
    this.asMini().plainText().replace("§", "")

fun String.legacy(): String =
    this.asMini().legacy()


fun String.asPartialFormattedMini(
    percentage: Double,
    minLines: Int = 3,
    maxLineLength: Int = 40,
    padding: String = "    ",
): Component {
    return replace("\n", "\n<reset><white>")
        .limitLineLength(maxLineLength)
        .minimalLines(minLines)
        .asMini()
        .splitPercentage(percentage)
        .addPaddingBeforeLines(padding)
        .minimalLines(minLines)
        .color(NamedTextColor.WHITE)
}

fun Component.minimalLines(minLines: Int = 3): Component {
    val message = this.plainText()
    val lineCount = message.count { it == '\n' } + 1
    val missingLines = (minLines - lineCount).coerceAtLeast(0)
    val missingLinesString = "\n".repeat(missingLines)
    return this.append(Component.text(missingLinesString))
}

fun Component.addPaddingBeforeLines(padding: String = "    "): Component {
    val paddingComponent = Component.text(padding)
    val prePadded = paddingComponent.append(this)
    return prePadded.replaceText(
        TextReplacementConfig.builder().match("\\n").replacement(Component.text("\n$padding")).build()
    )
}

fun Component.splitPercentage(percentage: Double): Component {
    val components = iterable(ComponentIteratorType.DEPTH_FIRST)

    val message = components.first().plainText()
    val totalLength = message.length
    val subLength = (totalLength * percentage.coerceIn(.0, 1.0)).toInt().coerceIn(0, totalLength)

    val substringComponents = mutableListOf<Component>(Component.empty())

    var textRemaining = subLength
    val componentIterator = components.iterator()
    while (componentIterator.hasNext() && textRemaining > 0) {
        val component = componentIterator.next()
        // If the component is not a text component, it can't be split.
        if (component !is TextComponent) {
            substringComponents.add(component)
            continue
        }

        val text = component.content()
        val size = text.length

        // If the text is longer than the remaining text, this is the last component.
        if (size > textRemaining) {
            val newText = text.substring(0, textRemaining)
            val newComponent = component.content(newText).noChildren()
            substringComponents.add(newComponent)
            break
        }

        substringComponents.add(component.noChildren())
        textRemaining -= size
    }

    return Component.join(JoinConfiguration.noSeparators(), substringComponents)
}

fun Component.noChildren() = this.children(mutableListOf())

/**
 * Adds missing lines to a string.
 */
private fun String.minimalLines(minLines: Int = 3): String {
    val lineCount = count { it == '\n' } + 1
    val missingLines = (minLines - lineCount).coerceAtLeast(0)
    val missingLinesString = "\n".repeat(missingLines)
    return "$this$missingLinesString"
}

/**
 * Splits a string into multiple lines with a maximum length.
 */
fun String.limitLineLength(maxLength: Int = 40): String {
    if (this.stripped().length <= maxLength) return this

    val words = this.split(" ")
    val lines = mutableListOf<String>()
    var currentLine = ""

    for (word in words) {
        val potentialLine = "$currentLine$word".stripped()

        if (potentialLine.length > maxLength) {
            lines.add(currentLine)
            currentLine = ""
        }

        currentLine += "$word "
    }

    lines.add(currentLine)
    return lines.joinToString("\n")
}