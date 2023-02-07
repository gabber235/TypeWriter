// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'materials.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$_MinecraftMaterial _$$_MinecraftMaterialFromJson(Map<String, dynamic> json) =>
    _$_MinecraftMaterial(
      name: json['name'] as String,
      properties: (json['properties'] as List<dynamic>)
          .map((e) => $enumDecode(_$MaterialPropertyEnumMap, e))
          .toList(),
      icon: json['icon'] as String,
    );

Map<String, dynamic> _$$_MinecraftMaterialToJson(
        _$_MinecraftMaterial instance) =>
    <String, dynamic>{
      'name': instance.name,
      'properties': instance.properties
          .map((e) => _$MaterialPropertyEnumMap[e]!)
          .toList(),
      'icon': instance.icon,
    };

const _$MaterialPropertyEnumMap = {
  MaterialProperty.item: 'item',
  MaterialProperty.block: 'block',
  MaterialProperty.solid: 'solid',
  MaterialProperty.transparent: 'transparent',
  MaterialProperty.intractable: 'intractable',
  MaterialProperty.occluding: 'occluding',
  MaterialProperty.record: 'record',
  MaterialProperty.tool: 'tool',
  MaterialProperty.weapon: 'weapon',
  MaterialProperty.armor: 'armor',
  MaterialProperty.flammable: 'flammable',
  MaterialProperty.burnable: 'burnable',
  MaterialProperty.edible: 'edible',
  MaterialProperty.fuel: 'fuel',
  MaterialProperty.ore: 'ore',
};
