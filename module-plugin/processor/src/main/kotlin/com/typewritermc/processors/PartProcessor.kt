package com.typewritermc.processors

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated

interface PartProcessor {
    fun process(resolver: Resolver): List<KSAnnotated>
}
