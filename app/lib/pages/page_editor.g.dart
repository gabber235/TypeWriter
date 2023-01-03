// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'page_editor.dart';

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

String _$currentPageIdHash() => r'129e6ddb01a02d157dec946020e529c65e901b85';

/// See also [currentPageId].
final currentPageIdProvider = Provider<String?>(
  currentPageId,
  name: r'currentPageIdProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : _$currentPageIdHash,
);
typedef CurrentPageIdRef = ProviderRef<String?>;
String _$currentPageLabelHash() => r'3a0685c6033a322a1538a72ce9f77abd6add0ab9';

/// See also [currentPageLabel].
final currentPageLabelProvider = AutoDisposeProvider<String>(
  currentPageLabel,
  name: r'currentPageLabelProvider',
  debugGetCreateSourceHash: const bool.fromEnvironment('dart.vm.product')
      ? null
      : _$currentPageLabelHash,
);
typedef CurrentPageLabelRef = AutoDisposeProviderRef<String>;
String _$currentPageHash() => r'019404b1e80234d7a6d9ed18030acc589ce2d700';

/// See also [currentPage].
final currentPageProvider = AutoDisposeProvider<Page?>(
  currentPage,
  name: r'currentPageProvider',
  debugGetCreateSourceHash:
      const bool.fromEnvironment('dart.vm.product') ? null : _$currentPageHash,
);
typedef CurrentPageRef = AutoDisposeProviderRef<Page?>;
