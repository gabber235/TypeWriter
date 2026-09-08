package com.typewritermc.presentation

import com.typewritermc.capability.RealmCapabilityDescriptor
import com.typewritermc.types.PresentationId
import com.typewritermc.types.ResolvedTypeRef
import com.typewritermc.types.TypeCatalog
import com.typewritermc.types.TypeExpression
import com.typewritermc.types.TypePrototypeRegistry
import com.typewritermc.types.skir.SkirTypeCodec
import com.typewritermc.types.skir.getOrThrow
import skirout.editor.v1.action.EditorAction
import skirout.editor.v1.action.RealmEditorAction
import skirout.editor.v1.binding.BindingId
import skirout.editor.v1.binding.BindingRef
import skirout.editor.v1.expression.Expression
import skirout.editor.v1.expression.TypedExpression
import skirout.editor.v1.path.DataPath
import skirout.editor.v1.path.DataPathSegment
import skirout.editor.v1.path.FieldPathSegment
import skirout.editor.v1.presentation.AxisChildrenLayout
import skirout.editor.v1.presentation.BoundControl
import skirout.editor.v1.presentation.ButtonElement
import skirout.editor.v1.presentation.ChildrenElement
import skirout.editor.v1.presentation.ChildrenLayout
import skirout.editor.v1.presentation.CommitControlsElement
import skirout.editor.v1.presentation.ConcreteTypePresentation
import skirout.editor.v1.presentation.CrossAxisAlignment
import skirout.editor.v1.presentation.MainAxisAlignment
import skirout.editor.v1.presentation.PolymorphicControl
import skirout.editor.v1.presentation.PresentationDefinition
import skirout.editor.v1.presentation.PresentationElement
import skirout.editor.v1.presentation.PresentationHeader
import skirout.editor.v1.presentation.PresentationHeaderTitle
import skirout.editor.v1.presentation.PresentationNode
import skirout.editor.v1.presentation.PresentationProperties
import skirout.editor.v1.presentation.SearchControl
import skirout.editor.v1.presentation.SearchProvider
import skirout.editor.v1.presentation.SearchResultMapping
import skirout.editor.v1.presentation.SearchSelectionMode
import skirout.editor.v1.presentation.SectionLayout
import skirout.editor.v1.presentation.TextContent
import skirout.editor.v1.presentation.TextControl
import skirout.editor.v1.type_catalog.CapabilityId
import skirout.editor.v1.type_catalog.IntegerWidth
import skirout.editor.v1.type_catalog.NumericConstraints
import skirout.editor.v1.type_catalog.StringConstraints
import skirout.editor.v1.type_catalog.TypedValue
import skirout.editor.v1.type_catalog.PresentationId as SkirPresentationId
import skirout.editor.v1.type_catalog.TypeExpression as SkirTypeExpression

/**
 * Generated bridge from an annotated declaration to deployment presentation assembly.
 *
 * Provenance identifies invalid declarations. [specification] runs authored builder code with deployment field
 * metadata and may fail; the assembler turns such failures into diagnostics.
 */
interface PresentationProvider {
    val namespace: String
    val sourcePart: String
    val declarationName: String
    val default: Boolean
    val priority: Int

    fun specification(context: PresentationBuildContext): PresentationSpec<*>
}

data class PresentationDiagnostic(
    val code: String,
    val message: String,
    val namespace: String? = null,
    val sourcePart: String? = null,
    val presentationName: String? = null,
)

/**
 * Returns compiled protocol presentations, updated type associations, and rejected declaration diagnostics.
 *
 * Use the returned type catalog to observe default and named presentation choices; the input catalog is not
 * mutated.
 */
data class PresentationCatalog(
    val types: TypeCatalog,
    val definitions: List<PresentationDefinition>,
    val diagnostics: List<PresentationDiagnostic>,
)

/**
 * Compiles authored presentations and associates valid choices with their target types.
 *
 * Malformed trees, missing capabilities, and duplicate identities produce diagnostics. Default and named selection
 * prefer the highest unique priority, skipping tied priority groups. Compiled definitions and provider processing
 * use stable ordering.
 */
object PresentationCatalogAssembler {
    fun assemble(
        providers: Collection<PresentationProvider>,
        prototypes: TypePrototypeRegistry,
        types: TypeCatalog,
        capabilities: Collection<RealmCapabilityDescriptor> = emptyList(),
    ): PresentationCatalog {
        val diagnostics = mutableListOf<PresentationDiagnostic>()
        val context = PresentationBuildContext(prototypes)
        val compiled =
            providers
                .sortedWith(
                    compareBy<PresentationProvider>(
                        { it.namespace },
                        { it.sourcePart },
                        { it.declarationName },
                    ),
                ).mapNotNull { provider -> compile(provider, context, prototypes, diagnostics) }
        val knownCapabilities = capabilities.mapTo(mutableSetOf()) { it.id.value }
        val valid =
            compiled.filter { candidate ->
                val missing =
                    candidate.definition.dependencies.capabilities
                        .map { it.value }
                        .filterNot(knownCapabilities::contains)
                if (missing.isEmpty()) {
                    true
                } else {
                    diagnostics +=
                        candidate.diagnostic(
                            "missing_capability",
                            "Presentation references unavailable capabilities: ${missing.sorted().joinToString()}.",
                        )
                    false
                }
            }
        val unique =
            valid.groupBy { it.id }.flatMap { (id, candidates) ->
                if (candidates.size == 1) {
                    candidates
                } else {
                    candidates.forEach { candidate ->
                        diagnostics += candidate.diagnostic("duplicate_id", "Presentation id $id is declared more than once.")
                    }
                    emptyList()
                }
            }
        val available = unique.toMutableList()
        do {
            val known = available.associateBy { it.id }
            val rejected =
                available.filter { candidate ->
                    runCatching {
                        candidate.invocations.forEach { invocation ->
                            val target = requireNotNull(known[invocation.id]) { "Invoked presentation ${invocation.id} is unavailable." }
                            require(
                                invocation.arguments.size == target.inputs.size,
                            ) { "Presentation input count does not match ${invocation.id}." }
                            require(
                                invocation.arguments
                                    .map { it.input }
                                    .toSet()
                                    .size == invocation.arguments.size,
                            ) { "Presentation arguments must be unique." }
                            invocation.arguments.forEach { argument ->
                                require(
                                    target.inputs.any {
                                        it.index == argument.input.index && it.name == argument.input.name &&
                                            it.type == argument.input.type
                                    },
                                ) { "Argument belongs to a different presentation declaration." }
                                require(
                                    argument.input.type == argument.value.type,
                                ) { "Presentation argument type does not match ${argument.input.name}." }
                                require(
                                    !argument.input.editable || argument.value.input.editable,
                                ) { "Presentation input ${argument.input.name} requires editing." }
                            }
                        }
                    }.exceptionOrNull()?.let { failure ->
                        diagnostics += candidate.diagnostic("invalid_invocation", failure.message.orEmpty())
                        true
                    } ?: false
                }
            available.removeAll(rejected.toSet())
        } while (rejected.isNotEmpty())
        val byTarget = available.groupBy(CompiledPresentation::target)
        val updatedTypes =
            types.definitions.map { definition ->
                val candidates = byTarget[definition.id].orEmpty()
                val default = select(candidates.filter(CompiledPresentation::default), "default", diagnostics)
                val named =
                    candidates
                        .groupBy { it.specificationName }
                        .mapNotNull { (name, values) -> select(values, "named presentation $name", diagnostics)?.let { name to it.id } }
                        .toMap()
                definition.copy(defaultPresentationId = default?.id, namedPresentations = named)
            }
        return PresentationCatalog(
            types = TypeCatalog(updatedTypes),
            definitions =
                available
                    .map(
                        CompiledPresentation::definition,
                    ).sortedBy { "${it.presentationId.namespace}/${it.presentationId.name}" },
            diagnostics = diagnostics,
        )
    }

    private fun compile(
        provider: PresentationProvider,
        context: PresentationBuildContext,
        prototypes: TypePrototypeRegistry,
        diagnostics: MutableList<PresentationDiagnostic>,
    ): CompiledPresentation? =
        runCatching {
            val specification = provider.specification(context)
            val target =
                specification.inputs
                    .singleOrNull()
                    ?.let { context.type(it.type) as? TypeExpression.Named }
                    ?.reference
            require(!provider.default || target != null) { "Default presentations require one nominal input." }
            val compiler = NodeCompiler(prototypes, specification.inputs)
            val root = compiler.compile(specification.root, "root", emptyList())
            assertUniqueNodeIds(root)
            val id = PresentationId(provider.namespace, specification.name)
            val dependencies = collectPresentationDependencies(root, specification.inputs.map { context.type(it.type) })
            CompiledPresentation(
                id = id,
                target = target,
                inputs = specification.inputs,
                invocations = compiler.invocations,
                specificationName = specification.name,
                default = provider.default,
                priority = provider.priority,
                provider = provider,
                definition =
                    PresentationDefinition(
                        presentationId = SkirPresentationId(namespace = id.namespace, name = id.name),
                        inputs =
                            specification.inputs.map { input ->
                                skirout.editor.v1.presentation.PresentationInput(
                                    bindingId = BindingId(value = input.index),
                                    name = input.name,
                                    valueType = SkirTypeCodec.encode(context.type(input.type)).getOrThrow(),
                                    access =
                                        if (input.editable) {
                                            skirout.editor.v1.presentation.PresentationInputAccess.EDIT
                                        } else {
                                            skirout.editor.v1.presentation.PresentationInputAccess.READ
                                        },
                                )
                            },
                        primaryInput = specification.inputs.singleOrNull()?.let { BindingId(value = it.index) },
                        root = root,
                        dependencies = dependencies.toWire(),
                    ),
            )
        }.getOrElse { failure ->
            diagnostics +=
                PresentationDiagnostic(
                    code = "invalid_presentation",
                    message = failure.message ?: "Presentation compilation failed.",
                    namespace = provider.namespace,
                    sourcePart = provider.sourcePart,
                    presentationName = provider.declarationName,
                )
            null
        }

    private fun select(
        candidates: List<CompiledPresentation>,
        association: String,
        diagnostics: MutableList<PresentationDiagnostic>,
    ): CompiledPresentation? {
        candidates.groupBy(CompiledPresentation::priority).toSortedMap(compareByDescending { it }).forEach { (priority, values) ->
            if (values.size == 1) return values.single()
            values.forEach { candidate ->
                diagnostics += candidate.diagnostic("priority_tie", "Priority $priority is tied for $association.")
            }
        }
        return null
    }
}

private data class CompiledPresentation(
    val id: PresentationId,
    val target: ResolvedTypeRef?,
    val inputs: List<PresentationInputRef<*>>,
    val invocations: List<AuthoredPresentationNode.Invocation>,
    val specificationName: String,
    val default: Boolean,
    val priority: Int,
    val provider: PresentationProvider,
    val definition: PresentationDefinition,
) {
    fun diagnostic(
        code: String,
        message: String,
    ) = PresentationDiagnostic(code, message, provider.namespace, provider.sourcePart, specificationName)
}

private class NodeCompiler(
    private val prototypes: TypePrototypeRegistry,
    private val inputs: List<PresentationInputRef<*>>,
) {
    val invocations = mutableListOf<AuthoredPresentationNode.Invocation>()
    private var nextBindingId = inputs.size.toLong()

    private fun inputId(input: PresentationInputRef<*>): Long {
        require(inputs.any { it === input }) { "Presentation references an input from a different declaration." }
        return input.index
    }

    private fun reference(value: PresentationValue<*>): BindingRef =
        BindingRef(
            bindingId = BindingId(value = inputId(value.input)),
            path = DataPath(segments = value.fields.map(::fieldPathSegment)),
        )

    fun compile(
        node: AuthoredPresentationNode,
        path: String,
        bindingPath: List<String>,
        bindingId: Long = 0L,
    ): PresentationNode =
        when (node) {
            is AuthoredPresentationNode.CommitControls -> {
                require(node.value.input.editable) { "Commit controls require an editable presentation input." }
                presentationNode(
                    path,
                    PresentationElement.CommitControlsWrapper(CommitControlsElement(binding = reference(node.value))),
                )
            }

            is AuthoredPresentationNode.Column -> {
                column(node, path, bindingPath, bindingId)
            }

            is AuthoredPresentationNode.Section -> {
                section(node, path, bindingPath, bindingId)
            }

            is AuthoredPresentationNode.TextInput -> {
                textInput(node, path, bindingPath, bindingId)
            }

            is AuthoredPresentationNode.NumericInput -> {
                numericInput(node, path, bindingPath, bindingId)
            }

            is AuthoredPresentationNode.CommandButton -> {
                commandButton(node, path, bindingPath, bindingId)
            }

            is AuthoredPresentationNode.RealmSearchInput -> {
                realmSearchInput(node, path, bindingPath, bindingId)
            }

            is AuthoredPresentationNode.PolymorphicInput -> {
                polymorphicInput(node, path, bindingPath, bindingId)
            }

            is AuthoredPresentationNode.Wire -> {
                node.node
            }

            is AuthoredPresentationNode.Text -> {
                presentationNode(
                    path,
                    PresentationElement.TextWrapper(
                        TextContent.partial(value = bindingExpression(node.value.type, inputId(node.value.input), node.value.fields)),
                    ),
                )
            }

            is AuthoredPresentationNode.DefaultEditor -> {
                presentationNode(
                    path,
                    PresentationElement.DefaultPresentationWrapper(
                        skirout.editor.v1.presentation.DefaultPresentationElement(
                            binding = reference(node.value),
                            presentationId = null,
                        ),
                    ),
                )
            }

            is AuthoredPresentationNode.Invocation -> {
                presentationNode(
                    path,
                    PresentationElement.InvocationWrapper(
                        skirout.editor.v1.presentation.PresentationInvocation(
                            presentationId = SkirPresentationId(namespace = node.id.namespace, name = node.id.name),
                            arguments =
                                node.also(invocations::add).arguments.map { argument ->
                                    skirout.editor.v1.presentation.PresentationArgument(
                                        input = BindingId(value = argument.input.index),
                                        binding = reference(argument.value),
                                    )
                                },
                        ),
                    ),
                )
            }
        }

    private fun column(
        node: AuthoredPresentationNode.Column,
        path: String,
        bindingPath: List<String>,
        bindingId: Long = 0L,
    ): PresentationNode =
        presentationNode(
            path,
            PresentationElement.ChildrenWrapper(
                ChildrenElement(
                    children = node.children.mapIndexed { index, child -> compile(child, "$path.$index", bindingPath, bindingId) },
                    layout =
                        ChildrenLayout.ColumnWrapper(
                            AxisChildrenLayout(
                                spacing = 8.0,
                                mainAxisAlignment = MainAxisAlignment.START,
                                crossAxisAlignment = CrossAxisAlignment.STRETCH,
                            ),
                        ),
                ),
            ),
        )

    private fun section(
        node: AuthoredPresentationNode.Section,
        path: String,
        bindingPath: List<String>,
        bindingId: Long = 0L,
    ): PresentationNode =
        presentationNode(
            node.key,
            PresentationElement.SectionWrapper(
                SectionLayout(child = compile(node.child, "$path.content", bindingPath, bindingId), border = null),
            ),
            PresentationHeader(
                binding = null,
                title = node.title?.let { PresentationHeaderTitle.TextWrapper(stringExpression(it)) },
                description = null,
                initiallyExpanded = node.initiallyExpanded,
                items = emptyList(),
                headerPadding = null,
                contentPadding = null,
            ),
        )

    private fun textInput(
        node: AuthoredPresentationNode.TextInput,
        path: String,
        bindingPath: List<String>,
        bindingId: Long = 0L,
    ): PresentationNode {
        val field = field(node.field)
        val fields = ((if (node.field.input == null) bindingPath else node.field.prefix) + field).filter(String::isNotEmpty)
        val control = boundControl(fields, node.label, node.field.input?.let(::inputId) ?: bindingId)
        return presentationNode(
            "field:${fields.joinToString(".")}:$path",
            PresentationElement.TextInputWrapper(
                TextControl(control = control, multiline = node.multiline, placeholder = null, inputFormatters = emptyList()),
            ),
        )
    }

    private fun numericInput(
        node: AuthoredPresentationNode.NumericInput,
        path: String,
        bindingPath: List<String>,
        bindingId: Long = 0L,
    ): PresentationNode {
        val field = field(node.field)
        val fields = ((if (node.field.input == null) bindingPath else node.field.prefix) + field).filter(String::isNotEmpty)
        return presentationNode(
            "field:${fields.joinToString(".")}:$path",
            PresentationElement.NumericInputWrapper(boundControl(fields, node.label, node.field.input?.let(::inputId) ?: bindingId)),
        )
    }

    private fun commandButton(
        node: AuthoredPresentationNode.CommandButton,
        path: String,
        bindingPath: List<String>,
        bindingId: Long = 0L,
    ): PresentationNode {
        val payload =
            node.payload?.let { bindingExpression(it.type, inputId(it.input), it.fields) }
                ?: bindingExpression(node.capability.requestType, bindingId, bindingPath)
        val action =
            EditorAction.RealmWrapper(
                RealmEditorAction.createCommand(
                    capabilityId = CapabilityId(value = node.capability.id.value),
                    payload = payload,
                ),
            )
        return presentationNode(
            "command:${node.capability.id.value}:$path",
            PresentationElement.ButtonWrapper(ButtonElement(label = stringExpression(node.label), action = action)),
        )
    }

    private fun realmSearchInput(
        node: AuthoredPresentationNode.RealmSearchInput,
        path: String,
        bindingPath: List<String>,
        bindingId: Long = 0L,
    ): PresentationNode {
        val field = field(node.field)
        val fields = ((if (node.field.input == null) bindingPath else node.field.prefix) + field).filter(String::isNotEmpty)
        val queryBindingId = allocateBindingId()
        val summaryBindingId = allocateBindingId()
        val resultBindingId = allocateBindingId()
        val resultValue = bindingExpression(node.capability.resultType, resultBindingId, emptyList())
        val resultKey = stringBindingExpression(resultBindingId, listOf(field(node.resultKey)))
        val resultLabel = stringBindingExpression(resultBindingId, listOf(field(node.resultLabel)))
        val resultPresentation =
            presentationNode(
                "search-result:${node.capability.id.value}:$path",
                PresentationElement.TextWrapper(TextContent.partial(value = resultLabel)),
            )
        val result =
            SearchResultMapping(
                bindingId = BindingId(value = resultBindingId),
                key = resultKey,
                selectedValue = resultValue,
                presentation = resultPresentation,
                label = resultLabel,
            )
        val provider =
            SearchProvider.createRealmCallback(
                capabilityId = CapabilityId(value = node.capability.id.value),
                payload =
                    node.payload?.let { bindingExpression(it.type, inputId(it.input), it.fields) }
                        ?: bindingExpression(node.capability.requestType, bindingId, bindingPath),
                result = result,
                selectors = emptyList(),
            )
        return presentationNode(
            "field:${fields.joinToString(".")}:$path",
            PresentationElement.SearchInputWrapper(
                SearchControl(
                    control = boundControl(fields, node.label, node.field.input?.let(::inputId) ?: bindingId),
                    selectionMode = SearchSelectionMode.SINGLE,
                    queryBindingId = BindingId(value = queryBindingId),
                    summaryBindingId = BindingId(value = summaryBindingId),
                    maximumExtent = integerExpression(320),
                    provider = provider,
                    summary = null,
                    placeholder = null,
                    customValue = null,
                    initialQuery = null,
                ),
            ),
        )
    }

    private fun polymorphicInput(
        node: AuthoredPresentationNode.PolymorphicInput,
        path: String,
        bindingPath: List<String>,
        bindingId: Long = 0L,
    ): PresentationNode {
        val field = field(node.field)
        val fields = ((if (node.field.input == null) bindingPath else node.field.prefix) + field).filter(String::isNotEmpty)
        val types =
            node.types.mapIndexed { index, type ->
                ConcreteTypePresentation(
                    concreteType = SkirTypeCodec.encode(prototypes.require(type.type).type).getOrThrow(),
                    label = stringExpression(type.label),
                    presentation = compile(type.root, "$path.type.$index", fields, node.field.input?.let(::inputId) ?: bindingId),
                )
            }
        return presentationNode(
            "field:${fields.joinToString(".")}",
            PresentationElement.PolymorphicInputWrapper(
                PolymorphicControl(
                    control = boundControl(fields, null, node.field.input?.let(::inputId) ?: bindingId),
                    concreteTypes = types,
                ),
            ),
        )
    }

    private fun field(reference: FieldReference): String = reference.serializedName

    private fun allocateBindingId(): Long = nextBindingId++

    private fun bindingExpression(
        type: kotlin.reflect.KClass<*>,
        bindingId: Long,
        fields: List<String>,
    ): TypedExpression =
        bindingExpression(
            SkirTypeCodec.encode(PresentationBuildContext(prototypes).type(type)).getOrThrow(),
            bindingId,
            fields,
        )

    private fun stringBindingExpression(
        bindingId: Long,
        fields: List<String>,
    ): TypedExpression = bindingExpression(SkirTypeExpression.StringWrapper(StringConstraints.partial()), bindingId, fields)

    private fun bindingExpression(
        resultType: SkirTypeExpression,
        bindingId: Long,
        fields: List<String>,
    ): TypedExpression =
        TypedExpression(
            resultType = resultType,
            expression =
                Expression.BindingWrapper(
                    BindingRef(
                        path = DataPath(segments = fields.map(::fieldPathSegment)),
                        bindingId = BindingId(value = bindingId),
                    ),
                ),
        )

    private fun boundControl(
        fields: List<String>,
        label: String?,
        bindingId: Long,
    ): BoundControl =
        BoundControl(
            binding =
                BindingRef(
                    path =
                        DataPath(
                            segments = fields.map { DataPathSegment.FieldWrapper(FieldPathSegment(fieldName = it)) },
                        ),
                    bindingId = BindingId(value = bindingId),
                ),
            label = label?.let(::stringExpression),
            description = null,
            prefix = null,
            semanticLabel = null,
        )

    private fun presentationNode(
        id: String,
        element: PresentationElement,
        header: PresentationHeader? = null,
    ): PresentationNode =
        PresentationNode(
            nodeId = id,
            properties = PresentationProperties(enabledIf = null, readOnly = false),
            element = element,
            header = header,
        )
}

private fun fieldPathSegment(field: String): DataPathSegment = DataPathSegment.FieldWrapper(FieldPathSegment(fieldName = field))

private fun stringExpression(value: String): TypedExpression =
    TypedExpression(
        resultType = SkirTypeExpression.StringWrapper(StringConstraints.partial()),
        expression = Expression.LiteralWrapper(TypedValue.StringWrapper(value)),
    )

private fun integerExpression(value: Int): TypedExpression =
    TypedExpression(
        resultType =
            SkirTypeExpression.createSignedInteger(
                width = IntegerWidth.THIRTY_TWO_BITS,
                constraints = NumericConstraints.partial(),
            ),
        expression = Expression.LiteralWrapper(TypedValue.SignedThirtyTwoWrapper(value)),
    )
