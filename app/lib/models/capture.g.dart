// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'capture.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$CaptureRequestImpl _$$CaptureRequestImplFromJson(Map<String, dynamic> json) =>
    _$CaptureRequestImpl(
      capturerClassPath: json['capturerClassPath'] as String,
      entryId: json['entryId'] as String,
      fieldPath: json['fieldPath'] as String,
      fieldValue: json['fieldValue'],
      cinematic: json['cinematic'] as String?,
      cinematicRange:
          const IntRangeConverter().fromJson(json['cinematicRange']),
    );

Map<String, dynamic> _$$CaptureRequestImplToJson(
        _$CaptureRequestImpl instance) =>
    <String, dynamic>{
      'capturerClassPath': instance.capturerClassPath,
      'entryId': instance.entryId,
      'fieldPath': instance.fieldPath,
      'fieldValue': instance.fieldValue,
      'cinematic': instance.cinematic,
      'cinematicRange':
          const IntRangeConverter().toJson(instance.cinematicRange),
    };
