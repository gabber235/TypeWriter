package me.gabber235.typewriter.utils

import me.gabber235.typewriter.Typewriter.Companion.adventure
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.command.CommandSender

private val mm = MiniMessage.miniMessage()

fun String.asMini() = mm.deserialize(this)

fun CommandSender.sendMessage(message: Component) = adventure.sender(this).sendMessage(message)
fun CommandSender.sendMini(message: String) = adventure.sender(this).sendMessage(message.asMini())

fun CommandSender.msg(message: String) = sendMini("<red><bold>Typewriter »<reset><white> $message")

fun Component.plainText(): String = PlainTextComponentSerializer.plainText().serialize(this)

fun Component.legacy(): String = LegacyComponentSerializer.legacy('§').serialize(this)

fun String.striped(): String =
	this.asMini().plainText().replace("§", "")

fun String.legacy(): String =
	this.asMini().legacy()
