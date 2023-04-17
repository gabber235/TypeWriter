// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'tree_view.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#custom-getters-and-methods');

/// @nodoc
mixin _$TreeNode<T> {
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function(List<TreeNode<T>> children) root,
    required TResult Function(String name, List<TreeNode<T>> children) inner,
    required TResult Function(T value) leaf,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function(List<TreeNode<T>> children)? root,
    TResult? Function(String name, List<TreeNode<T>> children)? inner,
    TResult? Function(T value)? leaf,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function(List<TreeNode<T>> children)? root,
    TResult Function(String name, List<TreeNode<T>> children)? inner,
    TResult Function(T value)? leaf,
    required TResult orElse(),
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(RootTreeNode<T> value) root,
    required TResult Function(InnerTreeNode<T> value) inner,
    required TResult Function(LeafTreeNode<T> value) leaf,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(RootTreeNode<T> value)? root,
    TResult? Function(InnerTreeNode<T> value)? inner,
    TResult? Function(LeafTreeNode<T> value)? leaf,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(RootTreeNode<T> value)? root,
    TResult Function(InnerTreeNode<T> value)? inner,
    TResult Function(LeafTreeNode<T> value)? leaf,
    required TResult orElse(),
  }) =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $TreeNodeCopyWith<T, $Res> {
  factory $TreeNodeCopyWith(
          TreeNode<T> value, $Res Function(TreeNode<T>) then) =
      _$TreeNodeCopyWithImpl<T, $Res, TreeNode<T>>;
}

/// @nodoc
class _$TreeNodeCopyWithImpl<T, $Res, $Val extends TreeNode<T>>
    implements $TreeNodeCopyWith<T, $Res> {
  _$TreeNodeCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;
}

/// @nodoc
abstract class _$$RootTreeNodeCopyWith<T, $Res> {
  factory _$$RootTreeNodeCopyWith(
          _$RootTreeNode<T> value, $Res Function(_$RootTreeNode<T>) then) =
      __$$RootTreeNodeCopyWithImpl<T, $Res>;
  @useResult
  $Res call({List<TreeNode<T>> children});
}

/// @nodoc
class __$$RootTreeNodeCopyWithImpl<T, $Res>
    extends _$TreeNodeCopyWithImpl<T, $Res, _$RootTreeNode<T>>
    implements _$$RootTreeNodeCopyWith<T, $Res> {
  __$$RootTreeNodeCopyWithImpl(
      _$RootTreeNode<T> _value, $Res Function(_$RootTreeNode<T>) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? children = null,
  }) {
    return _then(_$RootTreeNode<T>(
      children: null == children
          ? _value._children
          : children // ignore: cast_nullable_to_non_nullable
              as List<TreeNode<T>>,
    ));
  }
}

/// @nodoc

class _$RootTreeNode<T> implements RootTreeNode<T> {
  const _$RootTreeNode({required final List<TreeNode<T>> children})
      : _children = children;

  final List<TreeNode<T>> _children;
  @override
  List<TreeNode<T>> get children {
    if (_children is EqualUnmodifiableListView) return _children;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_children);
  }

  @override
  String toString() {
    return 'TreeNode<$T>.root(children: $children)';
  }

  @override
  bool operator ==(dynamic other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$RootTreeNode<T> &&
            const DeepCollectionEquality().equals(other._children, _children));
  }

  @override
  int get hashCode =>
      Object.hash(runtimeType, const DeepCollectionEquality().hash(_children));

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$RootTreeNodeCopyWith<T, _$RootTreeNode<T>> get copyWith =>
      __$$RootTreeNodeCopyWithImpl<T, _$RootTreeNode<T>>(this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function(List<TreeNode<T>> children) root,
    required TResult Function(String name, List<TreeNode<T>> children) inner,
    required TResult Function(T value) leaf,
  }) {
    return root(children);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function(List<TreeNode<T>> children)? root,
    TResult? Function(String name, List<TreeNode<T>> children)? inner,
    TResult? Function(T value)? leaf,
  }) {
    return root?.call(children);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function(List<TreeNode<T>> children)? root,
    TResult Function(String name, List<TreeNode<T>> children)? inner,
    TResult Function(T value)? leaf,
    required TResult orElse(),
  }) {
    if (root != null) {
      return root(children);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(RootTreeNode<T> value) root,
    required TResult Function(InnerTreeNode<T> value) inner,
    required TResult Function(LeafTreeNode<T> value) leaf,
  }) {
    return root(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(RootTreeNode<T> value)? root,
    TResult? Function(InnerTreeNode<T> value)? inner,
    TResult? Function(LeafTreeNode<T> value)? leaf,
  }) {
    return root?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(RootTreeNode<T> value)? root,
    TResult Function(InnerTreeNode<T> value)? inner,
    TResult Function(LeafTreeNode<T> value)? leaf,
    required TResult orElse(),
  }) {
    if (root != null) {
      return root(this);
    }
    return orElse();
  }
}

abstract class RootTreeNode<T> implements TreeNode<T> {
  const factory RootTreeNode({required final List<TreeNode<T>> children}) =
      _$RootTreeNode<T>;

  List<TreeNode<T>> get children;
  @JsonKey(ignore: true)
  _$$RootTreeNodeCopyWith<T, _$RootTreeNode<T>> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$InnerTreeNodeCopyWith<T, $Res> {
  factory _$$InnerTreeNodeCopyWith(
          _$InnerTreeNode<T> value, $Res Function(_$InnerTreeNode<T>) then) =
      __$$InnerTreeNodeCopyWithImpl<T, $Res>;
  @useResult
  $Res call({String name, List<TreeNode<T>> children});
}

/// @nodoc
class __$$InnerTreeNodeCopyWithImpl<T, $Res>
    extends _$TreeNodeCopyWithImpl<T, $Res, _$InnerTreeNode<T>>
    implements _$$InnerTreeNodeCopyWith<T, $Res> {
  __$$InnerTreeNodeCopyWithImpl(
      _$InnerTreeNode<T> _value, $Res Function(_$InnerTreeNode<T>) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? name = null,
    Object? children = null,
  }) {
    return _then(_$InnerTreeNode<T>(
      name: null == name
          ? _value.name
          : name // ignore: cast_nullable_to_non_nullable
              as String,
      children: null == children
          ? _value._children
          : children // ignore: cast_nullable_to_non_nullable
              as List<TreeNode<T>>,
    ));
  }
}

/// @nodoc

class _$InnerTreeNode<T> implements InnerTreeNode<T> {
  const _$InnerTreeNode(
      {required this.name, required final List<TreeNode<T>> children})
      : _children = children;

  @override
  final String name;
  final List<TreeNode<T>> _children;
  @override
  List<TreeNode<T>> get children {
    if (_children is EqualUnmodifiableListView) return _children;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_children);
  }

  @override
  String toString() {
    return 'TreeNode<$T>.inner(name: $name, children: $children)';
  }

  @override
  bool operator ==(dynamic other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$InnerTreeNode<T> &&
            (identical(other.name, name) || other.name == name) &&
            const DeepCollectionEquality().equals(other._children, _children));
  }

  @override
  int get hashCode => Object.hash(
      runtimeType, name, const DeepCollectionEquality().hash(_children));

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$InnerTreeNodeCopyWith<T, _$InnerTreeNode<T>> get copyWith =>
      __$$InnerTreeNodeCopyWithImpl<T, _$InnerTreeNode<T>>(this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function(List<TreeNode<T>> children) root,
    required TResult Function(String name, List<TreeNode<T>> children) inner,
    required TResult Function(T value) leaf,
  }) {
    return inner(name, children);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function(List<TreeNode<T>> children)? root,
    TResult? Function(String name, List<TreeNode<T>> children)? inner,
    TResult? Function(T value)? leaf,
  }) {
    return inner?.call(name, children);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function(List<TreeNode<T>> children)? root,
    TResult Function(String name, List<TreeNode<T>> children)? inner,
    TResult Function(T value)? leaf,
    required TResult orElse(),
  }) {
    if (inner != null) {
      return inner(name, children);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(RootTreeNode<T> value) root,
    required TResult Function(InnerTreeNode<T> value) inner,
    required TResult Function(LeafTreeNode<T> value) leaf,
  }) {
    return inner(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(RootTreeNode<T> value)? root,
    TResult? Function(InnerTreeNode<T> value)? inner,
    TResult? Function(LeafTreeNode<T> value)? leaf,
  }) {
    return inner?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(RootTreeNode<T> value)? root,
    TResult Function(InnerTreeNode<T> value)? inner,
    TResult Function(LeafTreeNode<T> value)? leaf,
    required TResult orElse(),
  }) {
    if (inner != null) {
      return inner(this);
    }
    return orElse();
  }
}

abstract class InnerTreeNode<T> implements TreeNode<T> {
  const factory InnerTreeNode(
      {required final String name,
      required final List<TreeNode<T>> children}) = _$InnerTreeNode<T>;

  String get name;
  List<TreeNode<T>> get children;
  @JsonKey(ignore: true)
  _$$InnerTreeNodeCopyWith<T, _$InnerTreeNode<T>> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$LeafTreeNodeCopyWith<T, $Res> {
  factory _$$LeafTreeNodeCopyWith(
          _$LeafTreeNode<T> value, $Res Function(_$LeafTreeNode<T>) then) =
      __$$LeafTreeNodeCopyWithImpl<T, $Res>;
  @useResult
  $Res call({T value});
}

/// @nodoc
class __$$LeafTreeNodeCopyWithImpl<T, $Res>
    extends _$TreeNodeCopyWithImpl<T, $Res, _$LeafTreeNode<T>>
    implements _$$LeafTreeNodeCopyWith<T, $Res> {
  __$$LeafTreeNodeCopyWithImpl(
      _$LeafTreeNode<T> _value, $Res Function(_$LeafTreeNode<T>) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? value = freezed,
  }) {
    return _then(_$LeafTreeNode<T>(
      value: freezed == value
          ? _value.value
          : value // ignore: cast_nullable_to_non_nullable
              as T,
    ));
  }
}

/// @nodoc

class _$LeafTreeNode<T> implements LeafTreeNode<T> {
  const _$LeafTreeNode({required this.value});

  @override
  final T value;

  @override
  String toString() {
    return 'TreeNode<$T>.leaf(value: $value)';
  }

  @override
  bool operator ==(dynamic other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$LeafTreeNode<T> &&
            const DeepCollectionEquality().equals(other.value, value));
  }

  @override
  int get hashCode =>
      Object.hash(runtimeType, const DeepCollectionEquality().hash(value));

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$LeafTreeNodeCopyWith<T, _$LeafTreeNode<T>> get copyWith =>
      __$$LeafTreeNodeCopyWithImpl<T, _$LeafTreeNode<T>>(this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function(List<TreeNode<T>> children) root,
    required TResult Function(String name, List<TreeNode<T>> children) inner,
    required TResult Function(T value) leaf,
  }) {
    return leaf(value);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function(List<TreeNode<T>> children)? root,
    TResult? Function(String name, List<TreeNode<T>> children)? inner,
    TResult? Function(T value)? leaf,
  }) {
    return leaf?.call(value);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function(List<TreeNode<T>> children)? root,
    TResult Function(String name, List<TreeNode<T>> children)? inner,
    TResult Function(T value)? leaf,
    required TResult orElse(),
  }) {
    if (leaf != null) {
      return leaf(value);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(RootTreeNode<T> value) root,
    required TResult Function(InnerTreeNode<T> value) inner,
    required TResult Function(LeafTreeNode<T> value) leaf,
  }) {
    return leaf(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(RootTreeNode<T> value)? root,
    TResult? Function(InnerTreeNode<T> value)? inner,
    TResult? Function(LeafTreeNode<T> value)? leaf,
  }) {
    return leaf?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(RootTreeNode<T> value)? root,
    TResult Function(InnerTreeNode<T> value)? inner,
    TResult Function(LeafTreeNode<T> value)? leaf,
    required TResult orElse(),
  }) {
    if (leaf != null) {
      return leaf(this);
    }
    return orElse();
  }
}

abstract class LeafTreeNode<T> implements TreeNode<T> {
  const factory LeafTreeNode({required final T value}) = _$LeafTreeNode<T>;

  T get value;
  @JsonKey(ignore: true)
  _$$LeafTreeNodeCopyWith<T, _$LeafTreeNode<T>> get copyWith =>
      throw _privateConstructorUsedError;
}
