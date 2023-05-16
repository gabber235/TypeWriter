package me.gabber235.typewriter.extensions.protocollib

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.WrappedDataValue
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Registry
import me.gabber235.typewriter.extensions.protocollib.BaseEntityFields.STATUS


interface EntityMetaDataField {
    fun create(): WrappedDataValue
}

private interface EntityFields {
    val defaultGenerator: () -> EntityMetaDataField
}

private enum class BaseEntityFields(override val defaultGenerator: () -> EntityMetaDataField) : EntityFields {
    STATUS({ EntityStatusField(listOf()) });
}

class EntityMetaDataComponent(private val entityId: Int) {
    private val fields = mutableMapOf<EntityFields, EntityMetaDataField>()

    private inline fun <reified F : EntityMetaDataField> modifyField(field: EntityFields, modifier: (F) -> F) {
        val oldField = fields[field] as? F ?: field.defaultGenerator() as F
        val newField = modifier(oldField)
        fields[field] = newField
    }

    fun addStatus(status: EntityStatus) = modifyField<EntityStatusField>(STATUS) { field ->
        EntityStatusField(field.statuses + status)
    }

    fun hasStatus(status: EntityStatus) = (fields[STATUS] as? EntityStatusField)?.statuses?.contains(status) ?: false


    fun removeStatus(status: EntityStatus) = modifyField<EntityStatusField>(STATUS) { field ->
        EntityStatusField(field.statuses - status)
    }

    fun clearStatuses() = modifyField<EntityStatusField>(STATUS) { _ ->
        EntityStatusField(listOf())
    }

    var boatType: BoatType
        get() = (fields[BoatEntityFields.TYPE] as? BoatTypeField)?.type ?: BoatType.OAK
        set(value) = modifyField<BoatTypeField>(BoatEntityFields.TYPE) { _ ->
            BoatTypeField(value)
        }

    fun createEntityMetaDataPacket(): PacketContainer {
        val packet = PacketContainer(PacketType.Play.Server.ENTITY_METADATA)

        packet.integers.write(0, entityId)
        packet.dataValueCollectionModifier.write(0, fields.map { it.value.create() })

        return packet
    }
}

class EntityStatusField(val statuses: List<EntityStatus>) : EntityMetaDataField {
    override fun create(): WrappedDataValue {
        val status = statuses.fold(0) { acc, status -> acc or status.mask }
        return WrappedDataValue(0, Registry.get(java.lang.Byte::class.java), status.toByte())
    }
}

enum class EntityStatus(val mask: Int) {
    ON_FIRE(0x01),
    CROUCHING(0x02),
    SPRINTING(0x08),
    SWIMMING(0x10),
    INVISIBLE(0x20),
    GLOWING(0x40),
    ELYTRA_FLYING(0x80);
}

/// ------------------- Boat Data ------------------- ///

enum class BoatEntityFields(override val defaultGenerator: () -> EntityMetaDataField) : EntityFields {
    TYPE({ BoatTypeField(BoatType.OAK) });
}

enum class BoatType {
    OAK,
    SPRUCE,
    BIRCH,
    JUNGLE,
    ACACIA,
    DARK_OAK,
    ;
}

class BoatTypeField(val type: BoatType) : EntityMetaDataField {
    override fun create(): WrappedDataValue {
        return WrappedDataValue(11, Registry.get(java.lang.Integer::class.java), type.ordinal)
    }
}