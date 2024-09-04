package com.typewritermc.core.entries

import com.typewritermc.core.books.pages.PageType

data class Page(
    val id: String = "",
    val name: String = "",
    val entries: List<Entry> = emptyList(),
    val type: PageType = PageType.SEQUENCE,
    val priority: Int = 0
)

