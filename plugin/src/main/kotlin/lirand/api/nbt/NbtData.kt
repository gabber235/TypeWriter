package lirand.api.nbt

inline fun nbtData(crossinline builder: NbtData.() -> Unit) = NbtData().apply(builder)


class NbtData internal constructor(nbtTagCompound: Any?) {
    internal val nbtTagCompound: Any = nbtTagCompound ?: nbtCompoundConstructor.newInstance()

    @Suppress("UNCHECKED_CAST")
    private val map: MutableMap<String, Any> = nbtCompoundMapField.get(this.nbtTagCompound) as MutableMap<String, Any>

    constructor() : this(null)
    constructor(nbtString: String) : this(mojangParseMethod.invoke(null, nbtString))


    /**
     * Gives access to change [NbtData] values of certain generic types.
     */
    val tag = NbtDataAccessor(this)

    /**
     * Returns a [Set] of all keys in this nbt.
     */
    val keys: Set<String> get() = map.keys


    /**
     * Gets the value
     * at the given [key]. The returned [dataType]
     * must be specified.
     * The returned value is null, if it
     * was not possible to find any value at
     * the specified location, or if the type
     * is not the one which was specified.
     */
    operator fun <T : Any> get(key: String, dataType: NbtDataType<T>): T? {
        val value = map[key] ?: return null

        return dataType.decode(value)
    }

    /**
     * Gets the nbt value (NBTBase subtype)
     * at the given [key].
     * The returned value is null, if it
     * was not possible to find any value at
     * the specified location.
     */
    fun getNbtTag(key: String): Any? {
        return map[key]
    }

    /**
     * Gets the value
     * at the given [key]. The returned [dataType]
     * must be specified.
     * If it was not possible to find any value at
     * the specified location, or if the type
     * is not the one which was specified,
     * the result of calling [defaultValue] was put into specified location.
     */
    inline fun <T : Any> getOrSet(key: String, dataType: NbtDataType<T>, defaultValue: () -> T): T {
        return get(key, dataType) ?: defaultValue().also {
            set(key, dataType, it)
        }
    }

    /**
     * Gets the value
     * at the given [key]. The returned [dataType]
     * must be specified.
     * The returned value is the result of calling [defaultValue],
     * if it was not possible to find any value at
     * the specified location, or if the type
     * is not the one which was specified.
     */
    inline fun <T : Any> getOrDefault(key: String, dataType: NbtDataType<T>, defaultValue: () -> T): T {
        return get(key, dataType) ?: defaultValue()
    }

    /**
     * Sets some [value]
     * at the position of the given [key].
     * The [dataType] of the given [value]
     * must be specified.
     */
    operator fun <T : Any> set(key: String, dataType: NbtDataType<T>, value: T) {
        map[key] = dataType.encode(value)
    }

    /**
     * Sets some NBT [value] (NBTBase subtype)
     * at the position of the given [key].
     */
    fun setNbtTag(key: String, value: Any) {
        map[key] = value
    }


    /**
     * Puts all values from [nbtData]
     * to this [NbtData].
     */
    fun putAll(nbtData: NbtData) {
        map.putAll(nbtData.map.toMap())
    }

    /**
     * Removes the
     * given [key] from the NBTTagCompound.
     * Its value will be lost.
     */
    fun remove(key: String) {
        map.remove(key)
    }

    /**
     * @see remove
     */
    operator fun minusAssign(key: String) = remove(key)

    /**
     * Clears this [NbtData].
     */
    fun clear() {
        map.clear()
    }


    /**
     * Gets type id of the value under the provided [key].
     */
    fun getTypeId(key: String): Int? {
        if (key !in this) return null

        return (nbtBaseGetTypeIdMethod.invoke(map[key]) as Byte).toInt()
    }


    /**
     * Returns `true` if the nbt contains the specified [key].
     */
    operator fun contains(key: String): Boolean {
        return map.containsKey(key)
    }

    /**
     * Returns `true` if the nbt contains the specified [key] of [type].
     */
    fun containsKeyOfType(key: String, type: NbtDataType<*>): Boolean {
        return getTypeId(key) == type.typeId
    }

    /**
     * Returns `true` if the nbt contains the specified [key] of [typeId].
     */
    fun containsKeyOfType(key: String, typeId: Int): Boolean {
        return getTypeId(key) == typeId
    }


    override fun toString(): String {
        return nbtTagCompound.toString()
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NbtData

        if (nbtTagCompound != other.nbtTagCompound) return false

        return true
    }

    override fun hashCode(): Int {
        return nbtTagCompound.hashCode()
    }
}


internal val nmsPackage = run {
    "net.minecraft.nbt"
}

internal val nbtCompoundClass = Class.forName("$nmsPackage.NBTTagCompound")

private val mojangParseMethod = Class.forName("$nmsPackage.MojangsonParser").methods
    .find {
        it.returnType == nbtCompoundClass && it.parameterTypes.let {
            it.size == 1 && it[0] == String::class.java
        }
    }!!
internal val nbtCompoundConstructor = nbtCompoundClass.getConstructor()

private val nbtCompoundMapField = nbtCompoundClass.declaredFields
    .find { it.type == MutableMap::class.java }!!
    .apply { isAccessible = true }