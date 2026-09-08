package com.typewritermc.presentation

import com.typewritermc.capability.CapabilityId
import com.typewritermc.capability.RealmCapabilityDescriptor
import com.typewritermc.capability.RealmCommandCapabilityRef
import com.typewritermc.capability.RealmSearchCapabilityRef
import com.typewritermc.types.NominalTypeKind
import com.typewritermc.types.ResolvedTypeRef
import com.typewritermc.types.TypeCatalog
import com.typewritermc.types.TypeDefinition
import com.typewritermc.types.TypeId
import com.typewritermc.types.TypePrototype
import com.typewritermc.types.TypePrototypeRegistry
import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import skirout.editor.v1.action.EditorAction
import skirout.editor.v1.action.RealmEditorAction
import skirout.editor.v1.path.DataPathSegment
import skirout.editor.v1.presentation.ChildrenElement
import skirout.editor.v1.presentation.PresentationElement
import skirout.editor.v1.presentation.PresentationHeader
import skirout.editor.v1.presentation.PresentationHeaderTitle
import skirout.editor.v1.presentation.PresentationNode
import skirout.editor.v1.presentation.SearchProvider
import kotlin.reflect.KClass

private data class Sample(
    val message: String,
)

private data class SearchItem(
    val key: String,
    val label: String,
)

private data class SearchSample(
    val selected: SearchItem,
)

val PresentationCatalogAssemblerTest by testSuite {
    test("select defaults retain their input expression through catalog compilation") {
        val prototypes = TypePrototypeRegistry(emptyList())
        val specification =
            context(PresentationBuildContext(prototypes)) {
                presentation("selection") {
                    val preferred = input<String>("preferred")
                    val selected = editableInput<String>("selected")
                    selectInput(
                        selected.value(),
                        listOf(selectOption("paper", "Paper", "paper")),
                        defaultValue = preferred.value(),
                    )
                }
            }
        val catalog =
            PresentationCatalogAssembler.assemble(
                listOf(provider("test", false, specification = specification)),
                prototypes,
                TypeCatalog(emptyList()),
            )
        catalog.diagnostics shouldBe emptyList()
        val node =
            (
                catalog.definitions
                    .single()
                    .root.element as PresentationElement.ChildrenWrapper
            ).value.children.single()
        val select = (node.element as PresentationElement.SelectInputWrapper).value
        select.control.binding.bindingId.value shouldBe 1L
        val option = select.options.single()
        option.optionId shouldBe "paper"
        (option.label.expression as skirout.editor.v1.expression.Expression.LiteralWrapper).value shouldBe
            skirout.editor.v1.type_catalog.TypedValue
                .StringWrapper("Paper")
        (option.value.expression as skirout.editor.v1.expression.Expression.LiteralWrapper).value shouldBe
            skirout.editor.v1.type_catalog.TypedValue
                .StringWrapper("paper")
        val defaultBinding = (select.defaultValue!!.expression as skirout.editor.v1.expression.Expression.BindingWrapper).value
        defaultBinding.bindingId.value shouldBe 0L
        val restored =
            skirout.editor.v1.presentation.SelectControl.serializer.fromBytes(
                skirout.editor.v1.presentation.SelectControl.serializer
                    .toBytes(select)
                    .toByteArray(),
            )
        restored.defaultValue shouldBe select.defaultValue
    }

    test("typed literal choices compile numeric and boolean values") {
        val prototypes = TypePrototypeRegistry(emptyList())
        val specification =
            context(PresentationBuildContext(prototypes)) {
                presentation("literal_choices") {
                    val count = editableInput<Int>("count")
                    val enabled = editableInput<Boolean>("enabled")
                    selectInput(count.value(), listOf(selectOption("two", "Two", 2)))
                    selectInput(enabled.value(), listOf(selectOption("yes", "Yes", true)))
                }
            }
        val catalog =
            PresentationCatalogAssembler.assemble(
                listOf(provider("test", false, specification = specification)),
                prototypes,
                TypeCatalog(emptyList()),
            )
        catalog.diagnostics shouldBe emptyList()
        val nodes =
            (
                catalog.definitions
                    .single()
                    .root.element as PresentationElement.ChildrenWrapper
            ).value.children
        val values =
            nodes.map { node ->
                val select = (node.element as PresentationElement.SelectInputWrapper).value
                (
                    select.options
                        .single()
                        .value.expression as skirout.editor.v1.expression.Expression.LiteralWrapper
                ).value
            }
        values shouldBe
            listOf(
                skirout.editor.v1.type_catalog.TypedValue
                    .SignedSixtyFourWrapper(2L),
                skirout.editor.v1.type_catalog.TypedValue
                    .BooleanWrapper(true),
            )
    }

    test("composed inputs compile explicit scalar bindings and invocations") {
        val prototypes = TypePrototypeRegistry(emptyList())
        val context = PresentationBuildContext(prototypes)
        val child =
            context(context) {
                presentation("child") {
                    val text = editableInput<String>("text")
                    textInput(text.value())
                    commitControls(text.value())
                }
            }
        val parent =
            context(context) {
                presentation("parent") {
                    val status = input<String>("status")
                    val name = editableInput<String>("name")
                    text(status.value())
                    include(com.typewritermc.types.PresentationId("test", "child"), child.input<String>("text") receives name.value())
                }
            }
        val catalog =
            PresentationCatalogAssembler.assemble(
                listOf(provider("test", false, specification = child), provider("test", false, specification = parent)),
                prototypes,
                TypeCatalog(emptyList()),
            )
        catalog.diagnostics shouldBe emptyList()
        val compiled = catalog.definitions.single { it.presentationId.name == "parent" }
        compiled.inputs.map { it.name } shouldBe listOf("status", "name")
        compiled.primaryInput shouldBe null
        val children = (compiled.root.element as PresentationElement.ChildrenWrapper).value.children
        val invocation = (children[1].element as PresentationElement.InvocationWrapper).value
        invocation.arguments
            .single()
            .binding.bindingId.value shouldBe 1L
        val control =
            (
                (
                    catalog.definitions
                        .single { it.presentationId.name == "child" }
                        .root.element as PresentationElement.ChildrenWrapper
                ).value.children
                    .first()
                    .element as PresentationElement.TextInputWrapper
            ).value.control
        control.binding.path.segments shouldBe emptyList()
        val childRoot = catalog.definitions.single { it.presentationId.name == "child" }.root
        val commit =
            (
                (childRoot.element as PresentationElement.ChildrenWrapper)
                    .value.children
                    .last()
                    .element
                    as PresentationElement.CommitControlsWrapper
            ).value
        commit.binding.bindingId.value shouldBe 0L
        commit.binding.path.segments shouldBe emptyList()
    }

    test("catalog rejects commit controls authored against a read input") {
        val prototypes = TypePrototypeRegistry(emptyList())
        val valid = context(PresentationBuildContext(prototypes)) { presentation("invalid") {} }
        val invalidProvider =
            object : PresentationProvider by provider("test", false, specification = valid) {
                override fun specification(context: PresentationBuildContext): PresentationSpec<*> =
                    context(context) {
                        presentation("invalid") {
                            val observation = input<String>("observation")
                            commitControls(observation.value())
                        }
                    }
            }
        val catalog =
            PresentationCatalogAssembler.assemble(
                listOf(invalidProvider),
                prototypes,
                TypeCatalog(emptyList()),
            )
        catalog.definitions shouldBe emptyList()
        catalog.diagnostics.map { it.code } shouldBe listOf("invalid_presentation")
    }

    test("an edit input cannot receive a read input") {
        val prototypes = TypePrototypeRegistry(emptyList())
        val context = PresentationBuildContext(prototypes)
        val child = context(context) { presentation("child") { editableInput<String>("text") } }
        val parent =
            context(context) {
                presentation("parent") {
                    val observed = input<String>("status")
                    include(com.typewritermc.types.PresentationId("test", "child"), child.input<String>("text") receives observed.value())
                }
            }
        val catalog =
            PresentationCatalogAssembler.assemble(
                listOf(provider("test", false, specification = child), provider("test", false, specification = parent)),
                prototypes,
                TypeCatalog(emptyList()),
            )
        catalog.definitions.map { it.presentationId.name } shouldBe listOf("child")
        catalog.diagnostics.map { it.code } shouldBe listOf("invalid_invocation")
    }

    test("typed property references use generated serialized field names") {
        val prototype = prototype(Sample::class, "sample", mapOf("message" to "wire_message"))
        val prototypes = TypePrototypeRegistry(listOf(prototype))
        val specification =
            context(PresentationBuildContext(prototypes)) {
                presentation<Sample>("editor") {
                    textInput(Sample::message)
                }
            }
        val catalog =
            PresentationCatalogAssembler.assemble(
                providers =
                    listOf(
                        provider(
                            namespace = "example:artifact",
                            default = true,
                            specification = specification,
                        ),
                    ),
                prototypes = prototypes,
                types = TypeCatalog(listOf(prototype.definition)),
            )

        catalog.types.definitions
            .single()
            .defaultPresentationId
            ?.name shouldBe "editor"
        val root =
            catalog.definitions
                .single()
                .root.element as PresentationElement.ChildrenWrapper
        val input =
            root.value.children
                .single()
                .element as PresentationElement.TextInputWrapper
        val field =
            input.value.control.binding.path.segments
                .single() as DataPathSegment.FieldWrapper
        field.value.fieldName shouldBe "wire_message"
        catalog.diagnostics shouldBe emptyList()
    }

    test("embedded wire trees assert duplicate descendant node ids") {
        val prototype = prototype(Sample::class, "sample")
        val prototypes = TypePrototypeRegistry(listOf(prototype))
        val duplicate = PresentationNode.partial(nodeId = "duplicate")
        val wireRoot =
            PresentationNode.partial(
                nodeId = "wire-root",
                header =
                    PresentationHeader.partial(
                        title = PresentationHeaderTitle.PresentationWrapper(duplicate),
                    ),
                element =
                    PresentationElement.ChildrenWrapper(
                        ChildrenElement.partial(children = listOf(duplicate)),
                    ),
            )
        val specification =
            context(PresentationBuildContext(prototypes)) {
                presentation<Sample>("editor") { wire(wireRoot) }
            }

        val catalog =
            PresentationCatalogAssembler.assemble(
                providers = listOf(provider("example", true, specification = specification)),
                prototypes = prototypes,
                types = TypeCatalog(listOf(prototype.definition)),
            )

        catalog.definitions shouldBe emptyList()
        catalog.diagnostics.map(PresentationDiagnostic::code) shouldBe listOf("invalid_presentation")
        catalog.diagnostics.single().message shouldBe "Presentation node ids must be unique: [duplicate]."
    }

    test("a tied priority is diagnosed before the next unique candidate is selected") {
        val prototype = prototype(Sample::class, "sample")
        val prototypes = TypePrototypeRegistry(listOf(prototype))
        val context = PresentationBuildContext(prototypes)
        val providers =
            context(context) {
                listOf(
                    provider("first", true, 10, presentation<Sample>("first") { textInput(Sample::message) }),
                    provider("second", true, 10, presentation<Sample>("second") { textInput(Sample::message) }),
                    provider("fallback", true, 5, presentation<Sample>("fallback") { textInput(Sample::message) }),
                )
            }

        val catalog =
            PresentationCatalogAssembler.assemble(
                providers,
                prototypes,
                TypeCatalog(listOf(prototype.definition)),
            )

        catalog.types.definitions
            .single()
            .defaultPresentationId
            ?.namespace shouldBe "fallback"
        catalog.diagnostics.map(PresentationDiagnostic::code) shouldContain "priority_tie"
    }

    test("typed Realm controls emit capability dependencies and generated field paths") {
        val sample = prototype(SearchSample::class, "search_sample", mapOf("selected" to "wire_selected"))
        val item = prototype(SearchItem::class, "search_item", mapOf("key" to "wire_key", "label" to "wire_label"))
        val prototypes = TypePrototypeRegistry(listOf(sample, item))
        val searchId = CapabilityId("search-items")
        val commandId = CapabilityId("refresh-items")
        val specification =
            context(PresentationBuildContext(prototypes)) {
                presentation<SearchSample>("editor") {
                    realmSearchInput(
                        property = SearchSample::selected,
                        capability = RealmSearchCapabilityRef(searchId, SearchSample::class, SearchItem::class),
                        resultKey = SearchItem::key,
                        resultLabel = SearchItem::label,
                    )
                    commandButton(
                        label = "Refresh",
                        capability = RealmCommandCapabilityRef(commandId, SearchSample::class),
                    )
                }
            }
        val catalog =
            PresentationCatalogAssembler.assemble(
                providers = listOf(provider("example", true, specification = specification)),
                prototypes = prototypes,
                types = TypeCatalog(listOf(sample.definition, item.definition)),
                capabilities =
                    listOf(
                        RealmCapabilityDescriptor.Search(searchId, sample.type, item.type),
                        RealmCapabilityDescriptor.Command(commandId, sample.type),
                    ),
            )

        val definition = catalog.definitions.single()
        definition.dependencies.capabilities
            .map { it.value }
            .toSet() shouldBe setOf(searchId.value, commandId.value)
        val root = definition.root.element as PresentationElement.ChildrenWrapper
        val search = root.value.children[0].element as PresentationElement.SearchInputWrapper
        val provider = search.value.provider as SearchProvider.RealmCallbackWrapper
        val keyPath = provider.value.result.key.expression as skirout.editor.v1.expression.Expression.BindingWrapper
        val keyField =
            keyPath.value.path.segments
                .single() as DataPathSegment.FieldWrapper
        keyField.value.fieldName shouldBe "wire_key"
        val selectedField =
            search.value.control.binding.path.segments
                .single() as DataPathSegment.FieldWrapper
        selectedField.value.fieldName shouldBe "wire_selected"
        val button = root.value.children[1].element as PresentationElement.ButtonWrapper
        val action = button.value.action as EditorAction.RealmWrapper
        val command = action.value as RealmEditorAction.CommandWrapper
        command.value.capabilityId.value shouldBe commandId.value
        catalog.diagnostics shouldBe emptyList()
    }
}

private fun <T : Any> prototype(
    type: KClass<T>,
    name: String,
    fieldNames: Map<String, String> = mapOf("message" to "message"),
): TypePrototype<T> {
    val reference = ResolvedTypeRef(TypeId.Qualified("test", name), revision = 1)
    return object : TypePrototype<T> {
        override val runtimeType = type
        override val type = reference
        override val definition = TypeDefinition(reference, NominalTypeKind.CONCRETE)
        override val serializedFieldNames = fieldNames
    }
}

private fun provider(
    namespace: String,
    default: Boolean,
    priority: Int = 0,
    specification: PresentationSpec<*>,
): PresentationProvider =
    object : PresentationProvider {
        override val namespace = namespace
        override val sourcePart = "common"
        override val declarationName = specification.name
        override val default = default
        override val priority = priority

        override fun specification(context: PresentationBuildContext) = specification
    }
