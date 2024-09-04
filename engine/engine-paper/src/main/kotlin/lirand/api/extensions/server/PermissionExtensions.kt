package lirand.api.extensions.server

import org.bukkit.permissions.Permissible

fun Permissible.hasPermissionOrStar(permission: String): Boolean =
	hasPermission(permission) || hasPermission(permission.replaceAfterLast('.', "*"))