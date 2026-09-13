package com.typewritermc.types.ksp

import com.google.devtools.ksp.symbol.KSType
import com.typewritermc.types.TypeGraph

/**
 * Converts this compile time type into the portable graph consumed by generated prototypes and discovery catalogs.
 *
 * Use [identityPolicy] when the public Typewriter identity is not the Kotlin qualified name. Unsupported types are
 * returned as diagnostics so a processor can report source context without accepting a partial graph.
 */
fun KSType.toTypewriterGraph(identityPolicy: KspTypeIdentityPolicy = QualifiedKotlinTypeIdentityPolicy): KspTypeConversionResult =
    KspTypeGraphConverter(identityPolicy).convert(this)

/** Returns a successful graph and throws one diagnostic exception on failure. */
fun KspTypeConversionResult.getOrThrow(): TypeGraph =
    when (this) {
        is KspTypeConversionResult.Success -> graph
        is KspTypeConversionResult.Failure -> throw KspTypeConversionException(diagnostics)
    }

/** Returns a successful graph, or null when conversion failed. */
fun KspTypeConversionResult.getOrNull(): TypeGraph? =
    when (this) {
        is KspTypeConversionResult.Success -> graph
        is KspTypeConversionResult.Failure -> null
    }

/**
 * Signals that a compile time type could not become a complete portable Typewriter graph.
 *
 * [diagnostics] retains every reported traversal failure. This is intended for processor boundaries that choose
 * exception based failure instead of handling [KspTypeConversionResult] directly.
 */
class KspTypeConversionException(
    val diagnostics: List<KspTypeDiagnostic>,
) : IllegalArgumentException(diagnostics.joinToString(separator = "\n"))
