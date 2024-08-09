package lirand.api.extensions.server

import org.bukkit.permissions.Permissible

fun Permissible.anyPermission(vararg permissions: String): Boolean = permissions.any { hasPermission(it) }

fun Permissible.allPermissions(vararg permissions: String): Boolean = permissions.all { hasPermission(it) }

fun Permissible.hasPermissionOrStar(permission: String): Boolean =
	hasPermission(permission) || hasPermission(permission.replaceAfterLast('.', "*"))