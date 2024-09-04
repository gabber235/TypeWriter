package com.typewritermc.core.books.pages

import com.google.gson.annotations.SerializedName

enum class PageType(val id: String) {
    @SerializedName("sequence")
    SEQUENCE("sequence"),

    @SerializedName("static")
    STATIC("static"),

    @SerializedName("cinematic")
    CINEMATIC("cinematic"),

    @SerializedName("manifest")
    MANIFEST("manifest"),
    ;

    companion object {
        fun fromId(id: String) = entries.firstOrNull { it.id == id }
    }
}
