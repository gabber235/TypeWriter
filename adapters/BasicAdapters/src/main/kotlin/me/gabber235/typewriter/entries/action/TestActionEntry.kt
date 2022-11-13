package me.gabber235.typewriter.entries.action

import me.gabber235.typewriter.adapters.Entry

enum class TestEnum {
	A,
	B,
	C
}

@Entry("test", "This is a test action")
data class TestActionEntry(
	override val id: String = "",
	override val name: String = "",
	val text: String = "",
	val number: Int = 0,
	val decimal: Double = 0.0,
	val bool: Boolean = false,
	val enum: TestEnum = TestEnum.A,
	val list: List<String> = emptyList(),

	) :
	me.gabber235.typewriter.entry.Entry
