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
abstract class _$$RootTreeNodeImplCopyWith<T, $Res> {
  factory _$$RootTreeNodeImplCopyWith(_$RootTreeNodeImpl<T> value,
          $Res Function(_$RootTreeNodeImpl<T>) then) =
      __$$RootTreeNodeImplCopyWithImpl<T, $Res>;
  @useResult
  $Res call({List<TreeNode<T>> children});
}

/// @nodoc
class __$$RootTreeNodeImplCopyWithImpl<T, $Res>
    extends _$TreeNodeCopyWithImpl<T, $Res, _$RootTreeNodeImpl<T>>
    implements _$$RootTreeNodeImplCopyWith<T, $Res> {
  __$$RootTreeNodeImplCopyWithImpl(
      _$RootTreeNodeImpl<T> _value, $Res Function(_$RootTreeNodeImpl<T>) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? children = null,
  }) {
    return _then(_$RootTreeNodeImpl<T>(
      children: null == children
          ? _value._children
          : children // ignore: cast_nullable_to_non_nullable
              as List<TreeNode<T>>,
    ));
  }
}

/// @nodoc

class _$RootTreeNodeImpl<T> implements RootTreeNode<T> {
  const _$RootTreeNodeImpl({required final List<TreeNode<T>> children})
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
            other is _$RootTreeNodeImpl<T> &&
            const DeepCollectionEquality().equals(other._children, _children));
  }

  @override
  int get hashCode =>
      Object.hash(runtimeType, const DeepCollectionEquality().hash(_children));

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$RootTreeNodeImplCopyWith<T, _$RootTreeNodeImpl<T>> get copyWith =>
      __$$RootTreeNodeImplCopyWithImpl<T, _$RootTreeNodeImpl<T>>(
          this, _$identity);

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
      _$RootTreeNodeImpl<T>;

  List<TreeNode<T>> get children;
  @JsonKey(ignore: true)
  _$$RootTreeNodeImplCopyWith<T, _$RootTreeNodeImpl<T>> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$InnerTreeNodeImplCopyWith<T, $Res> {
  factory _$$InnerTreeNodeImplCopyWith(_$InnerTreeNodeImpl<T> value,
          $Res Function(_$InnerTreeNodeImpl<T>) then) =
      __$$InnerTreeNodeImplCopyWithImpl<T, $Res>;
  @useResult
  $Res call({String name, List<TreeNode<T>> children});
}

/// @nodoc
class __$$InnerTreeNodeImplCopyWithImpl<T, $Res>
    extends _$TreeNodeCopyWithImpl<T, $Res, _$InnerTreeNodeImpl<T>>
    implements _$$InnerTreeNodeImplCopyWith<T, $Res> {
  __$$InnerTreeNodeImplCopyWithImpl(_$InnerTreeNodeImpl<T> _value,
      $Res Function(_$InnerTreeNodeImpl<T>) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? name = null,
    Object? children = null,
  }) {
    return _then(_$InnerTreeNodeImpl<T>(
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

class _$InnerTreeNodeImpl<T> implements InnerTreeNode<T> {
  const _$InnerTreeNodeImpl(
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
            other is _$InnerTreeNodeImpl<T> &&
            (identical(other.name, name) || other.name == name) &&
            const DeepCollectionEquality().equals(other._children, _children));
  }

  @override
  int get hashCode => Object.hash(
      runtimeType, name, const DeepCollectionEquality().hash(_children));

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$InnerTreeNodeImplCopyWith<T, _$InnerTreeNodeImpl<T>> get copyWith =>
      __$$InnerTreeNodeImplCopyWithImpl<T, _$InnerTreeNodeImpl<T>>(
          this, _$identity);

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
      required final List<TreeNode<T>> children}) = _$InnerTreeNodeImpl<T>;

  String get name;
  List<TreeNode<T>> get children;
  @JsonKey(ignore: true)
  _$$InnerTreeNodeImplCopyWith<T, _$InnerTreeNodeImpl<T>> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$LeafTreeNodeImplCopyWith<T, $Res> {
  factory _$$LeafTreeNodeImplCopyWith(_$LeafTreeNodeImpl<T> value,
          $Res Function(_$LeafTreeNodeImpl<T>) then) =
      __$$LeafTreeNodeImplCopyWithImpl<T, $Res>;
  @useResult
  $Res call({T value});
}

/// @nodoc
class __$$LeafTreeNodeImplCopyWithImpl<T, $Res>
    extends _$TreeNodeCopyWithImpl<T, $Res, _$LeafTreeNodeImpl<T>>
    implements _$$LeafTreeNodeImplCopyWith<T, $Res> {
  __$$LeafTreeNodeImplCopyWithImpl(
      _$LeafTreeNodeImpl<T> _value, $Res Function(_$LeafTreeNodeImpl<T>) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? value = freezed,
  }) {
    return _then(_$LeafTreeNodeImpl<T>(
      value: freezed == value
          ? _value.value
          : value // ignore: cast_nullable_to_non_nullable
              as T,
    ));
  }
}

/// @nodoc

class _$LeafTreeNodeImpl<T> implements LeafTreeNode<T> {
  const _$LeafTreeNodeImpl({required this.value});

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
            other is _$LeafTreeNodeImpl<T> &&
            const DeepCollectionEquality().equals(other.value, value));
  }

  @override
  int get hashCode =>
      Object.hash(runtimeType, const DeepCollectionEquality().hash(value));

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$LeafTreeNodeImplCopyWith<T, _$LeafTreeNodeImpl<T>> get copyWith =>
      __$$LeafTreeNodeImplCopyWithImpl<T, _$LeafTreeNodeImpl<T>>(
          this, _$identity);

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
  const factory LeafTreeNode({required final T value}) = _$LeafTreeNodeImpl<T>;

  T get value;
  @JsonKey(ignore: true)
  _$$LeafTreeNodeImplCopyWith<T, _$LeafTreeNodeImpl<T>> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
mixin _$TreeModification<T> {
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function(TreeNode<T> node) add,
    required TResult Function(String path, TreeNode<T> node) update,
    required TResult Function(String path) remove,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function(TreeNode<T> node)? add,
    TResult? Function(String path, TreeNode<T> node)? update,
    TResult? Function(String path)? remove,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function(TreeNode<T> node)? add,
    TResult Function(String path, TreeNode<T> node)? update,
    TResult Function(String path)? remove,
    required TResult orElse(),
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_TreeAdd<T> value) add,
    required TResult Function(_TreeUpdate<T> value) update,
    required TResult Function(_TreeRemove<T> value) remove,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_TreeAdd<T> value)? add,
    TResult? Function(_TreeUpdate<T> value)? update,
    TResult? Function(_TreeRemove<T> value)? remove,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_TreeAdd<T> value)? add,
    TResult Function(_TreeUpdate<T> value)? update,
    TResult Function(_TreeRemove<T> value)? remove,
    required TResult orElse(),
  }) =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$TreeModificationCopyWith<T, $Res> {
  factory _$TreeModificationCopyWith(_TreeModification<T> value,
          $Res Function(_TreeModification<T>) then) =
      __$TreeModificationCopyWithImpl<T, $Res, _TreeModification<T>>;
}

/// @nodoc
class __$TreeModificationCopyWithImpl<T, $Res,
        $Val extends _TreeModification<T>>
    implements _$TreeModificationCopyWith<T, $Res> {
  __$TreeModificationCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;
}

/// @nodoc
abstract class _$$TreeAddImplCopyWith<T, $Res> {
  factory _$$TreeAddImplCopyWith(
          _$TreeAddImpl<T> value, $Res Function(_$TreeAddImpl<T>) then) =
      __$$TreeAddImplCopyWithImpl<T, $Res>;
  @useResult
  $Res call({TreeNode<T> node});

  $TreeNodeCopyWith<T, $Res> get node;
}

/// @nodoc
class __$$TreeAddImplCopyWithImpl<T, $Res>
    extends __$TreeModificationCopyWithImpl<T, $Res, _$TreeAddImpl<T>>
    implements _$$TreeAddImplCopyWith<T, $Res> {
  __$$TreeAddImplCopyWithImpl(
      _$TreeAddImpl<T> _value, $Res Function(_$TreeAddImpl<T>) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? node = null,
  }) {
    return _then(_$TreeAddImpl<T>(
      node: null == node
          ? _value.node
          : node // ignore: cast_nullable_to_non_nullable
              as TreeNode<T>,
    ));
  }

  @override
  @pragma('vm:prefer-inline')
  $TreeNodeCopyWith<T, $Res> get node {
    return $TreeNodeCopyWith<T, $Res>(_value.node, (value) {
      return _then(_value.copyWith(node: value));
    });
  }
}

/// @nodoc

class _$TreeAddImpl<T> implements _TreeAdd<T> {
  const _$TreeAddImpl({required this.node});

  @override
  final TreeNode<T> node;

  @override
  String toString() {
    return '_TreeModification<$T>.add(node: $node)';
  }

  @override
  bool operator ==(dynamic other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$TreeAddImpl<T> &&
            (identical(other.node, node) || other.node == node));
  }

  @override
  int get hashCode => Object.hash(runtimeType, node);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$TreeAddImplCopyWith<T, _$TreeAddImpl<T>> get copyWith =>
      __$$TreeAddImplCopyWithImpl<T, _$TreeAddImpl<T>>(this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function(TreeNode<T> node) add,
    required TResult Function(String path, TreeNode<T> node) update,
    required TResult Function(String path) remove,
  }) {
    return add(node);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function(TreeNode<T> node)? add,
    TResult? Function(String path, TreeNode<T> node)? update,
    TResult? Function(String path)? remove,
  }) {
    return add?.call(node);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function(TreeNode<T> node)? add,
    TResult Function(String path, TreeNode<T> node)? update,
    TResult Function(String path)? remove,
    required TResult orElse(),
  }) {
    if (add != null) {
      return add(node);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_TreeAdd<T> value) add,
    required TResult Function(_TreeUpdate<T> value) update,
    required TResult Function(_TreeRemove<T> value) remove,
  }) {
    return add(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_TreeAdd<T> value)? add,
    TResult? Function(_TreeUpdate<T> value)? update,
    TResult? Function(_TreeRemove<T> value)? remove,
  }) {
    return add?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_TreeAdd<T> value)? add,
    TResult Function(_TreeUpdate<T> value)? update,
    TResult Function(_TreeRemove<T> value)? remove,
    required TResult orElse(),
  }) {
    if (add != null) {
      return add(this);
    }
    return orElse();
  }
}

abstract class _TreeAdd<T> implements _TreeModification<T> {
  const factory _TreeAdd({required final TreeNode<T> node}) = _$TreeAddImpl<T>;

  TreeNode<T> get node;
  @JsonKey(ignore: true)
  _$$TreeAddImplCopyWith<T, _$TreeAddImpl<T>> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$TreeUpdateImplCopyWith<T, $Res> {
  factory _$$TreeUpdateImplCopyWith(
          _$TreeUpdateImpl<T> value, $Res Function(_$TreeUpdateImpl<T>) then) =
      __$$TreeUpdateImplCopyWithImpl<T, $Res>;
  @useResult
  $Res call({String path, TreeNode<T> node});

  $TreeNodeCopyWith<T, $Res> get node;
}

/// @nodoc
class __$$TreeUpdateImplCopyWithImpl<T, $Res>
    extends __$TreeModificationCopyWithImpl<T, $Res, _$TreeUpdateImpl<T>>
    implements _$$TreeUpdateImplCopyWith<T, $Res> {
  __$$TreeUpdateImplCopyWithImpl(
      _$TreeUpdateImpl<T> _value, $Res Function(_$TreeUpdateImpl<T>) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? path = null,
    Object? node = null,
  }) {
    return _then(_$TreeUpdateImpl<T>(
      path: null == path
          ? _value.path
          : path // ignore: cast_nullable_to_non_nullable
              as String,
      node: null == node
          ? _value.node
          : node // ignore: cast_nullable_to_non_nullable
              as TreeNode<T>,
    ));
  }

  @override
  @pragma('vm:prefer-inline')
  $TreeNodeCopyWith<T, $Res> get node {
    return $TreeNodeCopyWith<T, $Res>(_value.node, (value) {
      return _then(_value.copyWith(node: value));
    });
  }
}

/// @nodoc

class _$TreeUpdateImpl<T> implements _TreeUpdate<T> {
  const _$TreeUpdateImpl({required this.path, required this.node});

  @override
  final String path;
  @override
  final TreeNode<T> node;

  @override
  String toString() {
    return '_TreeModification<$T>.update(path: $path, node: $node)';
  }

  @override
  bool operator ==(dynamic other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$TreeUpdateImpl<T> &&
            (identical(other.path, path) || other.path == path) &&
            (identical(other.node, node) || other.node == node));
  }

  @override
  int get hashCode => Object.hash(runtimeType, path, node);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$TreeUpdateImplCopyWith<T, _$TreeUpdateImpl<T>> get copyWith =>
      __$$TreeUpdateImplCopyWithImpl<T, _$TreeUpdateImpl<T>>(this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function(TreeNode<T> node) add,
    required TResult Function(String path, TreeNode<T> node) update,
    required TResult Function(String path) remove,
  }) {
    return update(path, node);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function(TreeNode<T> node)? add,
    TResult? Function(String path, TreeNode<T> node)? update,
    TResult? Function(String path)? remove,
  }) {
    return update?.call(path, node);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function(TreeNode<T> node)? add,
    TResult Function(String path, TreeNode<T> node)? update,
    TResult Function(String path)? remove,
    required TResult orElse(),
  }) {
    if (update != null) {
      return update(path, node);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_TreeAdd<T> value) add,
    required TResult Function(_TreeUpdate<T> value) update,
    required TResult Function(_TreeRemove<T> value) remove,
  }) {
    return update(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_TreeAdd<T> value)? add,
    TResult? Function(_TreeUpdate<T> value)? update,
    TResult? Function(_TreeRemove<T> value)? remove,
  }) {
    return update?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_TreeAdd<T> value)? add,
    TResult Function(_TreeUpdate<T> value)? update,
    TResult Function(_TreeRemove<T> value)? remove,
    required TResult orElse(),
  }) {
    if (update != null) {
      return update(this);
    }
    return orElse();
  }
}

abstract class _TreeUpdate<T> implements _TreeModification<T> {
  const factory _TreeUpdate(
      {required final String path,
      required final TreeNode<T> node}) = _$TreeUpdateImpl<T>;

  String get path;
  TreeNode<T> get node;
  @JsonKey(ignore: true)
  _$$TreeUpdateImplCopyWith<T, _$TreeUpdateImpl<T>> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$TreeRemoveImplCopyWith<T, $Res> {
  factory _$$TreeRemoveImplCopyWith(
          _$TreeRemoveImpl<T> value, $Res Function(_$TreeRemoveImpl<T>) then) =
      __$$TreeRemoveImplCopyWithImpl<T, $Res>;
  @useResult
  $Res call({String path});
}

/// @nodoc
class __$$TreeRemoveImplCopyWithImpl<T, $Res>
    extends __$TreeModificationCopyWithImpl<T, $Res, _$TreeRemoveImpl<T>>
    implements _$$TreeRemoveImplCopyWith<T, $Res> {
  __$$TreeRemoveImplCopyWithImpl(
      _$TreeRemoveImpl<T> _value, $Res Function(_$TreeRemoveImpl<T>) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? path = null,
  }) {
    return _then(_$TreeRemoveImpl<T>(
      path: null == path
          ? _value.path
          : path // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc

class _$TreeRemoveImpl<T> implements _TreeRemove<T> {
  const _$TreeRemoveImpl({required this.path});

  @override
  final String path;

  @override
  String toString() {
    return '_TreeModification<$T>.remove(path: $path)';
  }

  @override
  bool operator ==(dynamic other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$TreeRemoveImpl<T> &&
            (identical(other.path, path) || other.path == path));
  }

  @override
  int get hashCode => Object.hash(runtimeType, path);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$TreeRemoveImplCopyWith<T, _$TreeRemoveImpl<T>> get copyWith =>
      __$$TreeRemoveImplCopyWithImpl<T, _$TreeRemoveImpl<T>>(this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function(TreeNode<T> node) add,
    required TResult Function(String path, TreeNode<T> node) update,
    required TResult Function(String path) remove,
  }) {
    return remove(path);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function(TreeNode<T> node)? add,
    TResult? Function(String path, TreeNode<T> node)? update,
    TResult? Function(String path)? remove,
  }) {
    return remove?.call(path);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function(TreeNode<T> node)? add,
    TResult Function(String path, TreeNode<T> node)? update,
    TResult Function(String path)? remove,
    required TResult orElse(),
  }) {
    if (remove != null) {
      return remove(path);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_TreeAdd<T> value) add,
    required TResult Function(_TreeUpdate<T> value) update,
    required TResult Function(_TreeRemove<T> value) remove,
  }) {
    return remove(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_TreeAdd<T> value)? add,
    TResult? Function(_TreeUpdate<T> value)? update,
    TResult? Function(_TreeRemove<T> value)? remove,
  }) {
    return remove?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_TreeAdd<T> value)? add,
    TResult Function(_TreeUpdate<T> value)? update,
    TResult Function(_TreeRemove<T> value)? remove,
    required TResult orElse(),
  }) {
    if (remove != null) {
      return remove(this);
    }
    return orElse();
  }
}

abstract class _TreeRemove<T> implements _TreeModification<T> {
  const factory _TreeRemove({required final String path}) = _$TreeRemoveImpl<T>;

  String get path;
  @JsonKey(ignore: true)
  _$$TreeRemoveImplCopyWith<T, _$TreeRemoveImpl<T>> get copyWith =>
      throw _privateConstructorUsedError;
}
