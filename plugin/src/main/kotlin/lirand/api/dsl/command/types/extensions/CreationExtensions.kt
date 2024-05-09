package lirand.api.dsl.command.types.extensions

import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.arguments.DoubleArgumentType
import com.mojang.brigadier.arguments.FloatArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.LongArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import lirand.api.dsl.command.types.WordType


val WordType: WordType<String> = WordType { reader -> reader.readUnquoted() }

val QuotableStringType: StringArgumentType = StringArgumentType.string()

val GreedyStringType: StringArgumentType = StringArgumentType.greedyString()



val IntType: IntegerArgumentType = IntegerArgumentType.integer()

fun IntType(
	min: Int = Int.MIN_VALUE,
	max: Int = Int.MAX_VALUE
): IntegerArgumentType = IntegerArgumentType.integer(min, max)


val LongType: LongArgumentType = LongArgumentType.longArg()

fun LongType(
	min: Long = Long.MIN_VALUE,
	max: Long = Long.MAX_VALUE
): LongArgumentType = LongArgumentType.longArg(min, max)



val FloatType: FloatArgumentType = FloatArgumentType.floatArg()

fun FloatType(
	min: Float = Float.MIN_VALUE,
	max: Float = Float.MAX_VALUE
): FloatArgumentType = FloatArgumentType.floatArg(min, max)


val DoubleType: DoubleArgumentType = DoubleArgumentType.doubleArg()

fun DoubleType(
	min: Double = Double.MIN_VALUE,
	max: Double = Double.MIN_VALUE
): DoubleArgumentType = DoubleArgumentType.doubleArg(min, max)



val BooleanType: BoolArgumentType = BoolArgumentType.bool()