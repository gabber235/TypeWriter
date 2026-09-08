import "package:flutter/material.dart";
import "package:typewriter_panel/typewriter_panel.dart";
import "package:typewriter_testkit/typewriter_testkit.dart";
import "package:widgetbook_annotation/widgetbook_annotation.dart" as widgetbook;

class EditorStories extends StatelessWidget {
  const EditorStories({required this.child, super.key});
  final Widget child;

  @override
  Widget build(BuildContext context) => FakeApp(
    child: Center(
      child: Section(
        child: Padding(
          padding: const EdgeInsets.all(16),
          child: ConstrainedBox(
            constraints: const BoxConstraints(maxWidth: 500),
            child: child,
          ),
        ),
      ),
    ),
  );
}

class EditorStory extends StatefulWidget {
  const EditorStory({required this.rootType, this.initialValue, super.key});
  final RecordType rootType;
  final RecordValue? initialValue;
  @override
  State<EditorStory> createState() => _EditorStoryState();
}

class _EditorStoryState extends State<EditorStory> {
  late final LocalEditor owner = LocalEditor(
    rootType: widget.rootType,
    typeCatalog: const TypeCatalog([]),
    value:
        widget.initialValue ??
        widget.rootType.createInitialValue().valueOrNull!,
  );
  @override
  Widget build(BuildContext context) => EditorStories(
    child: SingleChildScrollView(
      child: ComposedEditor(model: PresentationModel.editor(owner: owner)),
    ),
  );
  @override
  void dispose() {
    owner.dispose();
    super.dispose();
  }
}

Widget _valueStory(EditorValue value) => EditorStories(
  child: ComposedEditor(
    model: PresentationModel(
      catalog: const TypeCatalog([]),
      inputs: {
        const BindingId(0): PresentationInput.value(
          type: const StringType(),
          value: value,
        ),
      },
      root: const StringType().generateDefaultPresentation(),
    ),
  ),
);

@widgetbook.UseCase(name: "Loading", type: TypedEditor)
Widget loadingEditorUseCase(BuildContext context) =>
    _valueStory(const EditorValue.loading());

@widgetbook.UseCase(name: "Mixed", type: TypedEditor)
Widget conflictValueEditorUseCase(BuildContext context) =>
    _valueStory(const EditorValue.mixed());

@widgetbook.UseCase(name: "Invalid", type: TypedEditor)
Widget invalidValueEditorUseCase(BuildContext context) => _valueStory(
  const EditorValue.invalid([
    TypeDiagnostic(
      code: TypeDiagnosticCode.invalidValue,
      message: "The editor value is missing",
    ),
  ]),
);

@widgetbook.UseCase(name: "Ready", type: TypedEditor)
Widget readyValueEditorUseCase(BuildContext context) =>
    _valueStory(const EditorValue.ready(StringValue("Typed value")));
