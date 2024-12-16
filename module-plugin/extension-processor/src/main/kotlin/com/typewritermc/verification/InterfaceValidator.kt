package com.typewritermc.verification

import com.typewritermc.core.extension.annotations.*

class EntryOnlyOnEntryValidatorProvider : InterfaceValidatorProvider<Entry>(
    Entry::class,
    com.typewritermc.core.entries.Entry::class.qualifiedName!!
)

class TagsOnlyOnEntryValidatorProvider : InterfaceValidatorProvider<Tags>(
    Tags::class,
    com.typewritermc.core.entries.Entry::class.qualifiedName!!
)

class VariableDataOnlyOnEntryValidatorProvider : InterfaceValidatorProvider<VariableData>(
    VariableData::class,
    com.typewritermc.core.entries.Entry::class.qualifiedName!!
)

class GenericConstraintOnlyOnEntryValidatorProvider : InterfaceValidatorProvider<GenericConstraint>(
    GenericConstraint::class,
    com.typewritermc.core.entries.Entry::class.qualifiedName!!
)

class ContextKeysOnlyOnEntryValidatorProvider : InterfaceValidatorProvider<ContextKeys>(
    ContextKeys::class,
    com.typewritermc.core.entries.Entry::class.qualifiedName!!
)

class KeyTypeOnlyOnEntryContextKeysValidatorProvider : InterfaceValidatorProvider<KeyType>(
    KeyType::class,
    com.typewritermc.core.interaction.EntryContextKey::class.qualifiedName!!
)
