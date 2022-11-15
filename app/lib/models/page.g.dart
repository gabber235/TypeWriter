// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'page.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$_Page _$$_PageFromJson(Map<String, dynamic> json) => _$_Page(
      name: json['name'] as String,
      entries: (json['entries'] as List<dynamic>?)
              ?.map((e) => Entry.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
    );

Map<String, dynamic> _$$_PageToJson(_$_Page instance) => <String, dynamic>{
      'name': instance.name,
      'entries': instance.entries,
    };

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

// ignore_for_file: avoid_private_typedef_functions, non_constant_identifier_names, subtype_of_sealed_class, invalid_use_of_internal_member, unused_element, constant_identifier_names, unnecessary_raw_strings, library_private_types_in_public_api

/// Copied from Dart SDK
class _SystemHash {
  _SystemHash._();

  static int combine(int hash, int value) {
    // ignore: parameter_assignments
    hash = 0x1fffffff & (hash + value);
    // ignore: parameter_assignments
    hash = 0x1fffffff & (hash + ((0x0007ffff & hash) << 10));
    return hash ^ (hash >> 6);
  }

  static int finish(int hash) {
    // ignore: parameter_assignments
    hash = 0x1fffffff & (hash + ((0x03ffffff & hash) << 3));
    // ignore: parameter_assignments
    hash = hash ^ (hash >> 11);
    return 0x1fffffff & (hash + ((0x00003fff & hash) << 15));
  }
}

String $pagesHash() => r'ea54e231325c749b3affb60c0d7c761cce4f78c6';

/// See also [pages].
final pagesProvider = AutoDisposeProvider<List<Page>>(
  pages,
  name: r'pagesProvider',
  debugGetCreateSourceHash:
      const bool.fromEnvironment('dart.vm.product') ? null : $pagesHash,
);
typedef PagesRef = AutoDisposeProviderRef<List<Page>>;
