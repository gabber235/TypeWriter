// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'capture.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$_CaptureRequest _$$_CaptureRequestFromJson(Map<String, dynamic> json) =>
    _$_CaptureRequest(
      capturerClassPath: json['capturerClassPath'] as String,
      entryId: json['entryId'] as String,
      fieldPath: json['fieldPath'] as String,
      fieldValue: json['fieldValue'],
      cinematic: json['cinematic'] as String?,
      cinematicRange:
          const IntRangeConverter().fromJson(json['cinematicRange']),
    );

Map<String, dynamic> _$$_CaptureRequestToJson(_$_CaptureRequest instance) =>
    <String, dynamic>{
      'capturerClassPath': instance.capturerClassPath,
      'entryId': instance.entryId,
      'fieldPath': instance.fieldPath,
      'fieldValue': instance.fieldValue,
      'cinematic': instance.cinematic,
      'cinematicRange':
          const IntRangeConverter().toJson(instance.cinematicRange),
    };
