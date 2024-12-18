package com.typewritermc.example

import com.typewritermc.core.entries.Entry
import com.typewritermc.core.entries.Query

fun queryExample() {
    //<code-block:query_multiple>
    val entries = Query.find<MyEntry>()
    //</code-block:query_multiple>
}

fun queryExampleWithFilter() {
    //<code-block:query_multiple_with_filter>
    val entries = Query.findWhere<MyEntry> {
        it.someField == "some value"
    }
    //</code-block:query_multiple_with_filter>
}

fun queryExampleWithoutFilter() {
    //<code-block:query_by_id>
    val id = "some_id"
    val entry = Query.findById<MyEntry>(id)
    //</code-block:query_by_id>
}

fun queryExampleFromPage() {
    //<code-block:query_from_page>
    val pageId = "some_page_id"
    val entries = Query.findWhereFromPage<MyEntry>(pageId) {
        it.someField == "some value"
    }
    //</code-block:query_from_page>
}

class MyEntry(
    override val id: String = "",
    override val name: String = "",
    val someField: String = "",
) : Entry