import "dart:convert";

import "package:flutter/material.dart" hide FilledButton;
import "package:flutter_hooks/flutter_hooks.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:http/http.dart" as http;
import "package:typewriter/models/adapter.dart";
import "package:typewriter/utils/extensions.dart";
import "package:typewriter/utils/icons.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/widgets/components/app/header_button.dart";
import "package:typewriter/widgets/components/general/admonition.dart";
import "package:typewriter/widgets/components/general/filled_button.dart";
import "package:typewriter/widgets/components/general/formatted_text_field.dart";
import "package:typewriter/widgets/components/general/iconify.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/editors/string.dart";
import "package:typewriter/widgets/inspector/header.dart";
import "package:typewriter/widgets/inspector/inspector.dart";
import "package:typewriter/widgets/inspector/section_title.dart";

class SkinEditorFilter extends EditorFilter {
  @override
  Widget build(String path, FieldInfo info) =>
      SkinEditor(path: path, field: info as CustomField);

  @override
  bool canEdit(FieldInfo info) => info is CustomField && info.editor == "skin";
}

class SkinEditor extends HookConsumerWidget {
  const SkinEditor({
    required this.path,
    required this.field,
    super.key,
  });

  final String path;
  final CustomField field;

  String? _getSkinUrl(String textureData) {
    if (textureData.isEmpty) return null;
    // Decode base64 string
    final bytes = base64Decode(textureData);
    // Convert to a string and read the json
    final json = jsonDecode(utf8.decode(bytes));

    // Read the textures.SKIN.url field
    final url = json["textures"]["SKIN"]["url"];
    if (url is! String) {
      return null;
    }

    final id = url.replacePrefix("http://textures.minecraft.net/texture/", "");
    return "https://nmsr.nickac.dev/fullbody/$id";
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final texture = ref.watch(fieldValueProvider("$path.texture", ""));
    final url = useMemoized(() => _getSkinUrl(texture), [texture]);
    return FieldHeader(
      path: path,
      field: field,
      canExpand: true,
      actions: [
        HeaderButton(
          tooltip: "Fetch From UUID",
          icon: TWIcons.accountTag,
          color: Colors.green,
          onTap: () => showDialog(
            context: context,
            builder: (context) => _FetchFromMineSkinDialogue(
              path: path,
              url: "https://api.mineskin.org/generate/user",
              bodyKey: "uuid",
              icon: TWIcons.accountTag,
            ),
          ),
        ),
        HeaderButton(
          tooltip: "Fetch From URL",
          icon: TWIcons.url,
          color: Colors.orange,
          onTap: () => showDialog(
            context: context,
            builder: (context) => _FetchFromMineSkinDialogue(
              path: path,
              url: "https://api.mineskin.org/generate/url",
              bodyKey: "url",
              icon: TWIcons.url,
            ),
          ),
        ),
      ],
      child: Row(
        children: [
          if (url != null) ...[
            Image.network(
              url,
              width: 100,
              loadingBuilder: (context, child, loadingProgress) {
                if (loadingProgress == null) {
                  return child;
                }
                return const Center(
                  child: CircularProgressIndicator(),
                );
              },
            ),
            const SizedBox(width: 12),
          ],
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              mainAxisSize: MainAxisSize.min,
              children: [
                const SizedBox(height: 12),
                const SectionTitle(title: "Texture"),
                const SizedBox(height: 8),
                StringEditor(
                  path: "$path.texture",
                  field: const PrimitiveField(type: PrimitiveFieldType.string),
                ),
                const SizedBox(height: 8),
                const SectionTitle(title: "Signature"),
                const SizedBox(height: 8),
                StringEditor(
                  path: "$path.signature",
                  field: const PrimitiveField(type: PrimitiveFieldType.string),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}

class _FetchFromMineSkinDialogue extends HookConsumerWidget {
  const _FetchFromMineSkinDialogue({
    required this.path,
    required this.url,
    required this.bodyKey,
    required this.icon,
  });

  final String path;
  final String url;
  final String bodyKey;
  final String icon;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final controller = useTextEditingController();
    final focus = useFocusNode();
    final error = useState<String?>(null);

    return AlertDialog(
      title: const Text("Fetch Skin"),
      content: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          if (error.value != null) ...[
            Admonition.danger(
              child: Text(error.value!),
            ),
            const SizedBox(height: 8),
          ],
          FormattedTextField(
            focus: focus,
            controller: controller,
            icon: icon,
            hintText: "Enter the $bodyKey to fetch the skin",
          ),
        ],
      ),
      actions: [
        TextButton(
          onPressed: () => Navigator.pop(context),
          child: const Text("Cancel"),
        ),
        FilledButton.icon(
          icon: const Iconify(TWIcons.download),
          onPressed: () async {
            final navigator = Navigator.of(context);
            final result = await _fetchSkin(ref.passing, controller.text);
            if (result == null) {
              navigator.pop();
              return;
            }
            focus.requestFocus();
            error.value = result;
          },
          label: const Text("Fetch"),
        ),
      ],
    );
  }

  Future<String?> _fetchSkin(PassingRef ref, String data) async {
    final body = {
      "visibility": "1",
      bodyKey: data,
    };
    // Make a post request to the MineSkin API
    // and update the texture and signature fields
    final response = await http.post(
      Uri.parse(url),
      body: body,
    );

    if (response.statusCode != 200) {
      // Parse the repondse json for the error field and return that
      final data = jsonDecode(response.body);
      if (data is Map<String, dynamic> && data.containsKey("error")) {
        return data["error"];
      }
      return "An unknown error occurred";
    }

    final result = jsonDecode(response.body);
    if (result is! Map<String, dynamic>) {
      return "An unknown error occurred";
    }

    if (!result.containsKey("data")) {
      return "Could not find the skin data in the response";
    }
    final resultData = result["data"];

    if (resultData is! Map<String, dynamic>) {
      return "Result data is not a map";
    }

    if (!resultData.containsKey("texture")) {
      return "Could not find the texture in the response";
    }
    final textureData = resultData["texture"];

    if (textureData is! Map<String, dynamic>) {
      return "Texture is not a map";
    }

    if (!textureData.containsKey("value")) {
      return "Could not find the texture value in the response";
    }
    final texture = textureData["value"];

    if (texture is! String) {
      return "Texture value is not a string";
    }

    if (!textureData.containsKey("signature")) {
      return "Could not find the signature in the response";
    }

    final signature = textureData["signature"];

    if (signature is! String) {
      return "Signature is not a string";
    }

    final definition = ref.read(inspectingEntryDefinitionProvider);
    if (definition == null) {
      return "Currently not inspecting an entry";
    }
    await definition.updateField(ref, path, {
      "texture": texture,
      "signature": signature,
    });

    return null;
  }
}
