import "package:flutter/material.dart";
import "package:freezed_annotation/freezed_annotation.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter_panel/typewriter_panel.dart";

part "scene.freezed.dart";

@freezed
abstract class Cue with _$Cue {
  @Assert("id != \"\"", "ID must not be empty.")
  @Assert("startFrame >= 0", "Start frame must not be negative.")
  @Assert("endFrame >= startFrame", "End frame must not precede start frame.")
  const factory Cue.segment({
    required String id,
    required int startFrame,
    required int endFrame,
    required ElementDefinition elementDefinition,
    required RecordValue data,
    required List<ElementLink> inwardLinks,
    required List<ElementLink> outwardLinks,
    @Default(0) int authoringSequence,
  }) = Segment;

  @Assert("id != \"\"", "ID must not be empty.")
  @Assert("frame >= 0", "Frame must not be negative.")
  const factory Cue.keyframe({
    required String id,
    required int frame,
    required ElementDefinition elementDefinition,
    required RecordValue data,
    required List<ElementLink> inwardLinks,
    @Default(0) int authoringSequence,
  }) = Keyframe;
}

class CueIdentifier extends SelectableIdentifier {
  const CueIdentifier({required this.pageId, required this.id});

  final String pageId;

  @override
  final String id;

  @override
  Object get resourceId => recordId("element:$id");

  @override
  AsyncValue<Selectable<CueIdentifier>> create(Ref ref) {
    final organizationId = ref.watch(organizationIdProvider);
    final realmId = ref.watch(realmIdProvider);
    if (organizationId == null || realmId == null) {
      return AsyncValue.error(
        ApiException.badRequest("No realm selected"),
        StackTrace.current,
      );
    }
    final state = ref.watch(authoringSessionProvider(organizationId, realmId));
    final repository = ref
        .watch(resourceRepositoriesProvider)
        .authoring(organizationId, realmId);
    final asyncElements = ref.watch(
      pageElementsProvider(organizationId, realmId, pageId),
    );
    if (asyncElements.mapUnready<Selectable<CueIdentifier>>()
        case final value?) {
      return value;
    }
    final elements = asyncElements.requireValue;

    Cue? cue;
    for (final element in elements) {
      if (element case PageElementCue(
        cue: final candidate,
      ) when candidate.id == id) {
        cue = candidate;
        break;
      }
    }

    if (cue == null) {
      throw SelectableNotFoundException(this);
    }

    final resolvedCue = cue;
    final catalogState = ref.watch(
      realmEditorCatalogForTypeProvider(resolvedCue.elementDefinition.rootType),
    );
    return catalogState.resolveElement(
      resolvedCue.elementDefinition,
      (catalog, presentations) => CueSelection(
        target: authoringElementTarget(
          repository: repository,
          state: state,
          identity: this,
          pageId: pageId,
          label: resolvedCue.elementDefinition.name,
          document: EditorDocument(
            rootType: NamedType(resolvedCue.elementDefinition.rootType),
            typeCatalog: catalog,
            confirmedValue: resolvedCue.data,
            revision: resolvedCue.authoringSequence,
          ),
        ),
        id: this,
        cue: resolvedCue,
        typeCatalog: catalog,
        presentations: presentations,
      ),
    );
  }

  @override
  int get hashCode => Object.hash(pageId, id);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other is CueIdentifier && other.pageId == pageId && other.id == id);
  }

  @override
  String toString() => "CueIdentifier($pageId, $id)";
}

class CueSelection extends EditableSelectable<CueIdentifier> {
  const CueSelection({
    required this.target,
    required this.id,
    required this.cue,
    required this.typeCatalog,
    required this.presentations,
  });

  final EditorTarget target;

  @override
  final CueIdentifier id;

  final Cue cue;

  @override
  final TypeCatalog typeCatalog;
  @override
  final List<PresentationDefinition> presentations;

  @override
  String get name => cue.elementDefinition.name;

  @override
  EditorDocument get document => target.document;

  @override
  DataPath get presentationPath => elementValuePath;

  @override
  ResolvedTypeRef get rootType => cue.elementDefinition.rootType;

  @override
  List<SelectionCapability> get capabilities => const [];

  @override
  Widget? buildInspectorHeader() {
    return CueHeader(id: id.id, name: name, color: cue.elementDefinition.color);
  }

  @override
  EditableResource get resource => target.resource;
  @override
  EditorSnapshot get snapshot => target.snapshot;

  @override
  String toString() => "CueSelection($id)";
}

class CueHeader extends StatelessWidget {
  const CueHeader({
    required this.id,
    required this.name,
    required this.color,
    super.key,
  });

  final String id;
  final String name;
  final Color color;

  @override
  Widget build(BuildContext context) {
    final textTheme = Theme.of(context).textTheme;

    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: [
        Expanded(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(name, style: textTheme.headlineSmall),
              Text(id, style: textTheme.bodyMedium),
            ],
          ),
        ),
        CircleAvatar(backgroundColor: color, radius: 12),
      ],
    );
  }
}
