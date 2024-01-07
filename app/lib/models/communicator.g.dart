// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'communicator.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$ResponseImpl _$$ResponseImplFromJson(Map<String, dynamic> json) =>
    _$ResponseImpl(
      success: json['success'] as bool,
      message: json['message'] as String,
    );

Map<String, dynamic> _$$ResponseImplToJson(_$ResponseImpl instance) =>
    <String, dynamic>{
      'success': instance.success,
      'message': instance.message,
    };

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

String _$communicatorHash() => r'9085b5b631ddf5d09d571a34e6d1de239259e08f';

/// See also [communicator].
@ProviderFor(communicator)
final communicatorProvider = Provider<Communicator>.internal(
  communicator,
  name: r'communicatorProvider',
  debugGetCreateSourceHash:
      const bool.fromEnvironment('dart.vm.product') ? null : _$communicatorHash,
  dependencies: null,
  allTransitiveDependencies: null,
);

typedef CommunicatorRef = ProviderRef<Communicator>;
// ignore_for_file: type=lint
// ignore_for_file: subtype_of_sealed_class, invalid_use_of_internal_member, invalid_use_of_visible_for_testing_member
