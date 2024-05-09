package lirand.api.dsl.command.types.extensions

import com.mojang.brigadier.Message
import lirand.api.dsl.command.types.EnumType
import lirand.api.dsl.command.types.exceptions.ChatCommandExceptionType
import net.kyori.adventure.text.Component
import net.md_5.bungee.api.chat.TranslatableComponent
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.Player


val ParticleType = EnumType.build<Particle>(
	allowedConstants = { enumValues<Particle>().associateWith { null } },
	notFoundExceptionType = ChatCommandExceptionType {
		Component.translatable("argument.id.unknown", Component.text(it[0].toString().lowercase()))
	}
)

fun ParticleType(
	allowedConstants: (sender: Player?) -> Map<Particle, Message?> = ParticleType.allowedConstants,
	notFoundExceptionType: ChatCommandExceptionType = ParticleType.notFoundExceptionType
) = EnumType.build<Particle>(allowedConstants, notFoundExceptionType)



val MaterialType = EnumType.build<Material>(
	allowedConstants = { enumValues<Material>().filter { !it.isLegacy }.associateWith { null } },
	notFoundExceptionType = ChatCommandExceptionType {
		Component.translatable("argument.id.unknown", Component.text(it[0].toString()))
	}
)

fun MaterialType(
	allowedConstants: (sender: Player?) -> Map<Material, Message?> = MaterialType.allowedConstants,
	notFoundExceptionType: ChatCommandExceptionType = MaterialType.notFoundExceptionType
) = EnumType.build<Material>(allowedConstants, notFoundExceptionType)