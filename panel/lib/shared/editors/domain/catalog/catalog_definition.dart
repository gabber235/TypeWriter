import "package:freezed_annotation/freezed_annotation.dart";
import "package:typewriter_panel/typewriter_panel.dart";

part "catalog_definition.freezed.dart";

@freezed
abstract class PresentationDefinition with _$PresentationDefinition {
  const factory PresentationDefinition({
    required PresentationId id,
    required List<PresentationInputParameter> inputs,
    required PresentationNode root, BindingId? primaryInput,
  }) = _PresentationDefinition;

  const PresentationDefinition._();

  factory PresentationDefinition.single({
    required PresentationId id,
    required TypeExpression target,
    required PresentationNode root,
  }) => PresentationDefinition(
    id: id,
    inputs: [
      PresentationInputParameter(
        id: const BindingId(0),
        name: "value",
        type: target,
      ),
    ],
    primaryInput: const BindingId(0),
    root: root,
  );

  TypeExpression? get target =>
      inputs.where((input) => input.id == primaryInput).firstOrNull?.type;
}

enum PresentationInputAccess { read, edit }

@freezed
abstract class PresentationInputParameter with _$PresentationInputParameter {
  const factory PresentationInputParameter({
    required BindingId id,
    required String name,
    required TypeExpression type,
    @Default(PresentationInputAccess.read) PresentationInputAccess access,
  }) = _PresentationInputParameter;
}

@freezed
sealed class CapabilityDefinition with _$CapabilityDefinition {
  const factory CapabilityDefinition.search({
    required CapabilityId id,
    required ResolvedTypeRef requestType,
    required ResolvedTypeRef resultType,
  }) = SearchCapabilityDefinition;

  const factory CapabilityDefinition.computation({
    required CapabilityId id,
    required ResolvedTypeRef requestType,
    required ResolvedTypeRef resultType,
  }) = ComputationCapabilityDefinition;

  const factory CapabilityDefinition.command({
    required CapabilityId id,
    required ResolvedTypeRef requestType,
  }) = CommandCapabilityDefinition;
}

@freezed
abstract class TypedValueEnvelope with _$TypedValueEnvelope {
  const factory TypedValueEnvelope({
    required ResolvedTypeRef rootType,
    required DataValue rootValue,
  }) = _TypedValueEnvelope;
}
