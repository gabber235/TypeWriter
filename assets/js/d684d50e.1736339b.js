"use strict";(self.webpackChunkdocumentation=self.webpackChunkdocumentation||[]).push([["27427"],{78941:function(e,n,t){t.r(n),t.d(n,{metadata:()=>i,contentTitle:()=>o,default:()=>p,assets:()=>c,toc:()=>m,frontMatter:()=>l});var i=JSON.parse('{"id":"develop/extensions/entries/trigger/dialogue","title":"DialogueEntry","description":"The DialogueEntry is used to define a type of dialogue. When a DialogueEntry is triggered it\'s associated DialogueMessenger will be used to display the dialogue to the player.","source":"@site/versioned_docs/version-0.6.0/develop/02-extensions/04-entries/trigger/dialogue.mdx","sourceDirName":"develop/02-extensions/04-entries/trigger","slug":"/develop/extensions/entries/trigger/dialogue","permalink":"/develop/extensions/entries/trigger/dialogue","draft":false,"unlisted":false,"editUrl":"https://github.com/gabber235/TypeWriter/tree/develop/documentation/versioned_docs/version-0.6.0/develop/02-extensions/04-entries/trigger/dialogue.mdx","tags":[],"version":"0.6.0","lastUpdatedBy":"dependabot[bot]","lastUpdatedAt":1731173593000,"frontMatter":{},"sidebar":"develop","previous":{"title":"CustomTriggeringActionEntry","permalink":"/develop/extensions/entries/trigger/custom_triggering_action"},"next":{"title":"EventEntry","permalink":"/develop/extensions/entries/trigger/event"}}'),r=t("85893"),a=t("50065"),s=t("48506");let l={},o="DialogueEntry",c={},m=[{value:"Usage",id:"usage",level:2},{value:"Multiple Messengers",id:"multiple-messengers",level:3},{value:"Lifecycle",id:"lifecycle",level:3}];function d(e){let n={admonition:"admonition",code:"code",h1:"h1",h2:"h2",h3:"h3",header:"header",li:"li",p:"p",ul:"ul",...(0,a.a)(),...e.components};return(0,r.jsxs)(r.Fragment,{children:[(0,r.jsx)(n.header,{children:(0,r.jsx)(n.h1,{id:"dialogueentry",children:"DialogueEntry"})}),"\n",(0,r.jsxs)(n.p,{children:["The ",(0,r.jsx)(n.code,{children:"DialogueEntry"})," is used to define a type of dialogue. When a ",(0,r.jsx)(n.code,{children:"DialogueEntry"})," is triggered it's associated ",(0,r.jsx)(n.code,{children:"DialogueMessenger"})," will be used to display the dialogue to the player.\nMultiple ",(0,r.jsx)(n.code,{children:"DialogueMessenger"}),"'s can be associated with a single ",(0,r.jsx)(n.code,{children:"DialogueEntry"})," and the ",(0,r.jsx)(n.code,{children:"DialogueMessenger"})," that is used is determined by the ",(0,r.jsx)(n.code,{children:"DialogueMessenger"}),"'s ",(0,r.jsx)(n.code,{children:"MessengerFilter"}),"."]}),"\n",(0,r.jsx)(n.admonition,{type:"info",children:(0,r.jsxs)(n.p,{children:["There can always be at most one ",(0,r.jsx)(n.code,{children:"DialogueEntry"})," active for a player.\nThis is automatically handled by Typewriter."]})}),"\n",(0,r.jsx)(n.h2,{id:"usage",children:"Usage"}),"\n",(0,r.jsx)(s.Z,{tag:"dialogue_entry",json:t(44174)}),"\n",(0,r.jsxs)(n.p,{children:["To define the messenger that will be used to display the dialogue to the player, you must create a class that implements the ",(0,r.jsx)(n.code,{children:"DialogueMessenger"})," interface."]}),"\n",(0,r.jsx)(s.Z,{tag:"dialogue_messenger",json:t(44174)}),"\n",(0,r.jsx)(n.h3,{id:"multiple-messengers",children:"Multiple Messengers"}),"\n",(0,r.jsxs)(n.p,{children:["The ",(0,r.jsx)(n.code,{children:"DialogueMessenger"})," has a ",(0,r.jsx)(n.code,{children:"MessengerFilter"})," that is used to determine if the messenger should be used to display the dialogue to the player. When having multiple ",(0,r.jsx)(n.code,{children:"MessageFilter"}),"'s make sure that they are deterministic. So if you have some condition, such as if they are bedrock players. One message check that the player is a bedrock player and the other filter check that they are not."]}),"\n",(0,r.jsx)(n.h3,{id:"lifecycle",children:"Lifecycle"}),"\n",(0,r.jsxs)(n.p,{children:["The ",(0,r.jsx)(n.code,{children:"state"})," of the messenger determines what happens to the messenger."]}),"\n",(0,r.jsxs)(n.ul,{children:["\n",(0,r.jsxs)(n.li,{children:[(0,r.jsx)(n.code,{children:"MessengerState.FINISHED"})," - The dialogue is finished and the next dialogue in the chain will be triggered."]}),"\n",(0,r.jsxs)(n.li,{children:[(0,r.jsx)(n.code,{children:"MessengerState.CANCELLED"})," - The dialogue is cancelled and dialogue chain is stopped, even if there are more dialogues in the chain."]}),"\n",(0,r.jsxs)(n.li,{children:[(0,r.jsx)(n.code,{children:"MessengerState.RUNNING"})," - The dialogue is still running and will continue to run until the state is changed."]}),"\n"]}),"\n",(0,r.jsxs)(n.p,{children:["The state object can be changed inside the ",(0,r.jsx)(n.code,{children:"tick"})," method or from outside. It can even be changed from the plugin itself. For example when the user runs a command the dialogue will be cancelled."]}),"\n",(0,r.jsx)(n.p,{children:"There are some additional lifecycle methods that can be overridden."}),"\n",(0,r.jsxs)(n.ul,{children:["\n",(0,r.jsxs)(n.li,{children:[(0,r.jsx)(n.code,{children:"init"})," - Called when the messenger is initialized. Will be called before the first ",(0,r.jsx)(n.code,{children:"tick"})," call."]}),"\n",(0,r.jsxs)(n.li,{children:[(0,r.jsx)(n.code,{children:"dispose"})," - Called when the messenger is disposed. By default this will unregister any listeners that were registered by the messenger."]}),"\n",(0,r.jsxs)(n.li,{children:[(0,r.jsx)(n.code,{children:"end"})," - Normally this does not need to be overwritten. Only if you do not want to resend the chat history for some reason."]}),"\n"]})]})}function p(e={}){let{wrapper:n}={...(0,a.a)(),...e.components};return n?(0,r.jsx)(n,{...e,children:(0,r.jsx)(d,{...e})}):d(e)}},48506:function(e,n,t){t.d(n,{Z:()=>s});var i=t("85893");let r={initializer:{file:"src/main/kotlin/com/typewritermc/example/ExampleInitializer.kt",content:"import com.typewritermc.core.extension.Initializable\nimport com.typewritermc.core.extension.annotations.Initializer\n\n@Initializer\nobject ExampleInitializer : Initializable {\n    override fun initialize() {\n        // Do something when the extension is initialized\n    }\n\n    override fun shutdown() {\n        // Do something when the extension is shutdown\n    }\n}"},cinematic_entry:{file:"src/main/kotlin/com/typewritermc/example/entries/cinematic/ExampleCinematicEntry.kt",content:'@Entry("example_cinematic", "An example cinematic entry", Colors.BLUE, "material-symbols:cinematic-blur")\nclass ExampleCinematicEntry(\n    override val id: String = "",\n    override val name: String = "",\n    override val criteria: List<Criteria> = emptyList(),\n    @Segments(Colors.BLUE, "material-symbols:cinematic-blur")\n    val segments: List<ExampleSegment> = emptyList(),\n) : CinematicEntry {\n    override fun create(player: Player): CinematicAction {\n        return ExampleCinematicAction(player, this)\n    }\n}'},cinematic_segment_with_min_max:{file:"src/main/kotlin/com/typewritermc/example/entries/cinematic/ExampleCinematicEntry.kt",content:'    @Segments(Colors.BLUE, "material-symbols:cinematic-blur")\n    @InnerMin(Min(10))\n    @InnerMax(Max(20))\n    val segments: List<ExampleSegment> = emptyList(),'},cinematic_create_actions:{file:"src/main/kotlin/com/typewritermc/example/entries/cinematic/ExampleCinematicEntry.kt",content:"    // This will be used when the cinematic is normally displayed to the player.\n    override fun create(player: Player): CinematicAction {\n        return DefaultCinematicAction(player, this)\n    }\n\n    // This is used during content mode to display the cinematic to the player.\n    // It may be null to not show it during simulation.\n    override fun createSimulating(player: Player): CinematicAction? {\n        return SimulatedCinematicAction(player, this)\n    }\n\n    // This is used during content mode to record the cinematic.\n    // It may be null to not record it during simulation.\n    override fun createRecording(player: Player): CinematicAction? {\n        return RecordingCinematicAction(player, this)\n    }"},cinematic_segment:{file:"src/main/kotlin/com/typewritermc/example/entries/cinematic/ExampleCinematicEntry.kt",content:"data class ExampleSegment(\n    override val startFrame: Int = 0,\n    override val endFrame: Int = 0,\n) : Segment"},cinematic_action:{file:"src/main/kotlin/com/typewritermc/example/entries/cinematic/ExampleCinematicEntry.kt",content:"class ExampleCinematicAction(\n    val player: Player,\n    val entry: ExampleCinematicEntry,\n) : CinematicAction {\n    override suspend fun setup() {\n        // Initialize variables, spawn entities, etc.\n    }\n\n    override suspend fun tick(frame: Int) {\n        val segment = entry.segments activeSegmentAt frame\n        // Can be null if no segment is active\n\n        // The `frame` parameter is not necessarily next frame: `frame != old(frame)+1`\n\n        // Execute tick logic for the segment\n    }\n\n    override suspend fun teardown() {\n        // Remove entities, etc.\n    }\n\n    override fun canFinish(frame: Int): Boolean = entry.segments canFinishAt frame\n}"},cinematic_simple_action:{file:"src/main/kotlin/com/typewritermc/example/entries/cinematic/ExampleCinematicEntry.kt",content:"class ExampleSimpleCinematicAction(\n    val player: Player,\n    entry: ExampleCinematicEntry,\n) : SimpleCinematicAction<ExampleSegment>() {\n    override val segments: List<ExampleSegment> = entry.segments\n\n    override suspend fun startSegment(segment: ExampleSegment) {\n        super.startSegment(segment) // Keep this\n        // Called when a segment starts\n    }\n\n    override suspend fun tickSegment(segment: ExampleSegment, frame: Int) {\n        super.tickSegment(segment, frame) // Keep this\n        // Called every tick while the segment is active\n        // Will always be called after startSegment and never after stopSegment\n\n        // The `frame` parameter is not necessarily next frame: `frame != old(frame)+1`\n    }\n\n    override suspend fun stopSegment(segment: ExampleSegment) {\n        super.stopSegment(segment) // Keep this\n        // Called when the segment ends\n        // Will also be called if the cinematic is stopped early\n    }\n}"},audience_entry:{file:"src/main/kotlin/com/typewritermc/example/entries/manifest/ExampleAudienceEntry.kt",content:'@Entry("example_audience", "An example audience entry.", Colors.GREEN, "material-symbols:chat-rounded")\nclass ExampleAudienceEntry(\n    override val id: String = "",\n    override val name: String = "",\n) : AudienceEntry {\n    override fun display(): AudienceDisplay {\n        return ExampleAudienceDisplay()\n    }\n}'},audience_display:{file:"src/main/kotlin/com/typewritermc/example/entries/manifest/ExampleAudienceEntry.kt",content:"class ExampleAudienceDisplay : AudienceDisplay() {\n    override fun initialize() {\n        // This is called when the first player is added to the audience.\n        super.initialize()\n        // Do something when the audience is initialized\n    }\n\n    override fun onPlayerAdd(player: Player) {\n        // Do something when a player gets added to the audience\n    }\n\n    override fun onPlayerRemove(player: Player) {\n        // Do something when a player gets removed from the audience\n    }\n\n    override fun dispose() {\n        super.dispose()\n        // Do something when the audience is disposed\n        // It will always call onPlayerRemove for all players.\n        // So no player cleanup is needed here.\n    }\n}"},tickable_audience_display:{file:"src/main/kotlin/com/typewritermc/example/entries/manifest/ExampleAudienceEntry.kt",content:"// highlight-next-line\nclass TickableAudienceDisplay : AudienceDisplay(), TickableDisplay {\n    override fun onPlayerAdd(player: Player) {}\n    override fun onPlayerRemove(player: Player) {}\n\n    // highlight-start\n    override fun tick() {\n        // Do something when the audience is ticked\n        players.forEach { player ->\n            // Do something with the player\n        }\n\n        // This is running asynchronously\n        // If you need to do something on the main thread\n        ThreadType.SYNC.launch {\n            // Though this will run a tick later, to sync with the bukkit scheduler.\n        }\n    }\n    // highlight-end\n}"},audience_display_with_events:{file:"src/main/kotlin/com/typewritermc/example/entries/manifest/ExampleAudienceEntry.kt",content:"class AudienceDisplayWithEvents : AudienceDisplay() {\n    override fun onPlayerAdd(player: Player) {}\n    override fun onPlayerRemove(player: Player) {}\n\n    // highlight-start\n    @EventHandler\n    fun onSomeEvent(event: SomeBukkitEvent) {\n        // Do something when the event is triggered\n        // This will trigger for all players, not just the ones in the audience.\n        // So we need to check if the player is in the audience.\n        if (event.player in this) {\n            // Do something with the player\n        }\n    }\n    // highlight-end\n}"},artifact_entry:{file:"src/main/kotlin/com/typewritermc/example/entries/static/ExampleArtifactEntry.kt",content:'@Entry("example_artifact", "An example artifact entry.", Colors.BLUE, "material-symbols:home-storage-rounded")\nclass ExampleArtifactEntry(\n    override val id: String = "",\n    override val name: String = "",\n    override val artifactId: String = "",\n) : ArtifactEntry'},artifact_access:{file:"src/main/kotlin/com/typewritermc/example/entries/static/ExampleArtifactEntry.kt",content:"suspend fun accessArtifactData(ref: Ref<out ArtifactEntry>) {\n    val assetManager = KoinJavaComponent.get<AssetManager>(AssetManager::class.java)\n    val entry = ref.get() ?: return\n    val content: String? = assetManager.fetchAsset(entry)\n    // Do something with the content\n}"},asset_entry:{file:"src/main/kotlin/com/typewritermc/example/entries/static/ExampleAssetEntry.kt",content:'@Entry("example_asset", "An example asset entry.", Colors.BLUE, "material-symbols:home-storage-rounded")\nclass ExampleAssetEntry(\n    override val id: String = "",\n    override val name: String = "",\n    override val path: String = "",\n) : AssetEntry'},asset_access:{file:"src/main/kotlin/com/typewritermc/example/entries/static/ExampleAssetEntry.kt",content:"suspend fun accessAssetData(ref: Ref<out AssetEntry>) {\n    val assetManager = KoinJavaComponent.get<AssetManager>(AssetManager::class.java)\n    val entry = ref.get() ?: return\n    val content: String? = assetManager.fetchAsset(entry)\n    // Do something with the content\n}"},sound_id_entry:{file:"src/main/kotlin/com/typewritermc/example/entries/static/ExampleSoundIdEntry.kt",content:'@Entry("example_sound", "An example sound entry.", Colors.BLUE, "icon-park-solid:volume-up")\nclass ExampleSoundIdEntry(\n    override val id: String = "",\n    override val name: String = "",\n    override val soundId: String = "",\n) : SoundIdEntry'},sound_source_entry:{file:"src/main/kotlin/com/typewritermc/example/entries/static/ExampleSoundSourceEntry.kt",content:'@Entry("example_sound_source", "An example sound source entry.", Colors.BLUE, "ic:round-spatial-audio-off")\nclass ExampleSoundSourceEntry(\n    override val id: String = "",\n    override val name: String = "",\n) : SoundSourceEntry {\n    override fun getEmitter(player: Player): SoundEmitter {\n        // Return the emitter that should be used for the sound.\n        // An entity should be provided.\n        return SoundEmitter(player.entityId)\n    }\n}'},speaker_entry:{file:"src/main/kotlin/com/typewritermc/example/entries/static/ExampleSpeakerEntry.kt",content:'@Entry("example_speaker", "An example speaker entry.", Colors.BLUE, "ic:round-spatial-audio-off")\nclass ExampleSpeakerEntry(\n    override val id: String = "",\n    override val name: String = "",\n    override val displayName: String = "",\n    override val sound: Sound = Sound.EMPTY,\n) : SpeakerEntry'},action_entry:{file:"src/main/kotlin/com/typewritermc/example/entries/trigger/ExampleActionEntry.kt",content:'@Entry("example_action", "An example action entry.", Colors.RED, "material-symbols:touch-app-rounded")\nclass ExampleActionEntry(\n    override val id: String = "",\n    override val name: String = "",\n    override val criteria: List<Criteria> = emptyList(),\n    override val modifiers: List<Modifier> = emptyList(),\n    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),\n) : ActionEntry {\n    override fun execute(player: Player) {\n        super.execute(player) // This will apply all the modifiers.\n        // Do something with the player\n    }\n}'},custom_triggering_action_entry:{file:"src/main/kotlin/com/typewritermc/example/entries/trigger/ExampleCustomTriggeringActionEntry.kt",content:'@Entry(\n    "example_custom_triggering_action",\n    "An example custom triggering entry.",\n    Colors.RED,\n    "material-symbols:touch-app-rounded"\n)\nclass ExampleCustomTriggeringActionEntry(\n    override val id: String = "",\n    override val name: String = "",\n    override val criteria: List<Criteria> = emptyList(),\n    override val modifiers: List<Modifier> = emptyList(),\n    @SerializedName("triggers")\n    override val customTriggers: List<Ref<TriggerableEntry>> = emptyList(),\n) : CustomTriggeringActionEntry {\n    override fun execute(player: Player) {\n        super.execute(player) // This will apply the modifiers.\n        // Do something with the player\n        player.triggerCustomTriggers() // Can be called later to trigger the next entries.\n    }\n}'},dialogue_entry:{file:"src/main/kotlin/com/typewritermc/example/entries/trigger/ExampleDialogueEntry.kt",content:'@Entry("example_dialogue", "An example dialogue entry.", Colors.BLUE, "material-symbols:chat-rounded")\nclass ExampleDialogueEntry(\n    override val id: String = "",\n    override val name: String = "",\n    override val criteria: List<Criteria> = emptyList(),\n    override val modifiers: List<Modifier> = emptyList(),\n    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),\n    override val speaker: Ref<SpeakerEntry> = emptyRef(),\n    @MultiLine\n    @Placeholder\n    @Colored\n    @Help("The text to display to the player.")\n    val text: String = "",\n) : DialogueEntry'},dialogue_messenger:{file:"src/main/kotlin/com/typewritermc/example/entries/trigger/ExampleDialogueEntry.kt",content:'@Messenger(ExampleDialogueEntry::class)\nclass ExampleDialogueDialogueMessenger(player: Player, entry: ExampleDialogueEntry) :\n    DialogueMessenger<ExampleDialogueEntry>(player, entry) {\n\n    companion object : MessengerFilter {\n        override fun filter(player: Player, entry: DialogueEntry): Boolean = true\n    }\n\n    // Called every game tick (20 times per second).\n    // The cycle is a parameter that is incremented every tick, starting at 0.\n    override fun tick(context: TickContext) {\n        super.tick(context)\n        if (state != MessengerState.RUNNING) return\n\n        player.sendMessage("${entry.speakerDisplayName}: ${entry.text}".parsePlaceholders(player).asMini())\n\n        // When we want the dialogue to end, we can set the state to FINISHED.\n        state = MessengerState.FINISHED\n    }\n}'},event_entry:{file:"src/main/kotlin/com/typewritermc/example/entries/trigger/ExampleEventEntry.kt",content:'@Entry("example_event", "An example event entry.", Colors.YELLOW, "material-symbols:bigtop-updates")\nclass ExampleEventEntry(\n    override val id: String = "",\n    override val name: String = "",\n    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),\n) : EventEntry'},event_entry_listener:{file:"src/main/kotlin/com/typewritermc/example/entries/trigger/ExampleEventEntry.kt",content:"// Must be scoped to be public\n@EntryListener(ExampleEventEntry::class)\nfun onEvent(event: SomeBukkitEvent, query: Query<ExampleEventEntry>) {\n    // Do something\n    val entries = query.find() // Find all the entries of this type, for more information see the Query section\n    // Do something with the entries, for example trigger them\n    entries triggerAllFor event.player\n}"}};var a=t("59126");function s(e){let n,{tag:t,json:s}=e;if(!s)throw Error("JSON not provided");if(null==(n=Object.keys(s).length>0?s[t]:r[t]))return(0,i.jsxs)("div",{className:"text-red-500 dark:text-red-400 text-xs",children:["Code snippet not found: ",t]});let{file:l,content:o}=n;if(null==l||null==o)return(0,i.jsxs)("div",{className:"text-red-500 dark:text-red-400 text-xs",children:["Code snippet not found: ",t," (",n,")"]});let c=l.split("/").pop();return(0,i.jsx)(a.Z,{language:"kotlin",showLineNumbers:!0,title:c,children:o})}},44174:function(e){e.exports=JSON.parse('{"initializer":{"file":"src/main/kotlin/com/typewritermc/example/ExampleInitializer.kt","content":"import com.typewritermc.core.extension.Initializable\\nimport com.typewritermc.core.extension.annotations.Initializer\\n\\n@Initializer\\nobject ExampleInitializer : Initializable {\\n    override fun initialize() {\\n        // Do something when the extension is initialized\\n    }\\n\\n    override fun shutdown() {\\n        // Do something when the extension is shutdown\\n    }\\n}"},"cinematic_entry":{"file":"src/main/kotlin/com/typewritermc/example/entries/cinematic/ExampleCinematicEntry.kt","content":"@Entry(\\"example_cinematic\\", \\"An example cinematic entry\\", Colors.BLUE, \\"material-symbols:cinematic-blur\\")\\nclass ExampleCinematicEntry(\\n    override val id: String = \\"\\",\\n    override val name: String = \\"\\",\\n    override val criteria: List<Criteria> = emptyList(),\\n    @Segments(Colors.BLUE, \\"material-symbols:cinematic-blur\\")\\n    val segments: List<ExampleSegment> = emptyList(),\\n) : CinematicEntry {\\n    override fun create(player: Player): CinematicAction {\\n        return ExampleCinematicAction(player, this)\\n    }\\n}"},"cinematic_segment_with_min_max":{"file":"src/main/kotlin/com/typewritermc/example/entries/cinematic/ExampleCinematicEntry.kt","content":"    @Segments(Colors.BLUE, \\"material-symbols:cinematic-blur\\")\\n    @InnerMin(Min(10))\\n    @InnerMax(Max(20))\\n    val segments: List<ExampleSegment> = emptyList(),"},"cinematic_create_actions":{"file":"src/main/kotlin/com/typewritermc/example/entries/cinematic/ExampleCinematicEntry.kt","content":"    // This will be used when the cinematic is normally displayed to the player.\\n    override fun create(player: Player): CinematicAction {\\n        return DefaultCinematicAction(player, this)\\n    }\\n\\n    // This is used during content mode to display the cinematic to the player.\\n    // It may be null to not show it during simulation.\\n    override fun createSimulating(player: Player): CinematicAction? {\\n        return SimulatedCinematicAction(player, this)\\n    }\\n\\n    // This is used during content mode to record the cinematic.\\n    // It may be null to not record it during simulation.\\n    override fun createRecording(player: Player): CinematicAction? {\\n        return RecordingCinematicAction(player, this)\\n    }"},"cinematic_segment":{"file":"src/main/kotlin/com/typewritermc/example/entries/cinematic/ExampleCinematicEntry.kt","content":"data class ExampleSegment(\\n    override val startFrame: Int = 0,\\n    override val endFrame: Int = 0,\\n) : Segment"},"cinematic_action":{"file":"src/main/kotlin/com/typewritermc/example/entries/cinematic/ExampleCinematicEntry.kt","content":"class ExampleCinematicAction(\\n    val player: Player,\\n    val entry: ExampleCinematicEntry,\\n) : CinematicAction {\\n    override suspend fun setup() {\\n        // Initialize variables, spawn entities, etc.\\n    }\\n\\n    override suspend fun tick(frame: Int) {\\n        val segment = entry.segments activeSegmentAt frame\\n        // Can be null if no segment is active\\n\\n        // The `frame` parameter is not necessarily next frame: `frame != old(frame)+1`\\n\\n        // Execute tick logic for the segment\\n    }\\n\\n    override suspend fun teardown() {\\n        // Remove entities, etc.\\n    }\\n\\n    override fun canFinish(frame: Int): Boolean = entry.segments canFinishAt frame\\n}"},"cinematic_simple_action":{"file":"src/main/kotlin/com/typewritermc/example/entries/cinematic/ExampleCinematicEntry.kt","content":"class ExampleSimpleCinematicAction(\\n    val player: Player,\\n    entry: ExampleCinematicEntry,\\n) : SimpleCinematicAction<ExampleSegment>() {\\n    override val segments: List<ExampleSegment> = entry.segments\\n\\n    override suspend fun startSegment(segment: ExampleSegment) {\\n        super.startSegment(segment) // Keep this\\n        // Called when a segment starts\\n    }\\n\\n    override suspend fun tickSegment(segment: ExampleSegment, frame: Int) {\\n        super.tickSegment(segment, frame) // Keep this\\n        // Called every tick while the segment is active\\n        // Will always be called after startSegment and never after stopSegment\\n\\n        // The `frame` parameter is not necessarily next frame: `frame != old(frame)+1`\\n    }\\n\\n    override suspend fun stopSegment(segment: ExampleSegment) {\\n        super.stopSegment(segment) // Keep this\\n        // Called when the segment ends\\n        // Will also be called if the cinematic is stopped early\\n    }\\n}"},"audience_entry":{"file":"src/main/kotlin/com/typewritermc/example/entries/manifest/ExampleAudienceEntry.kt","content":"@Entry(\\"example_audience\\", \\"An example audience entry.\\", Colors.GREEN, \\"material-symbols:chat-rounded\\")\\nclass ExampleAudienceEntry(\\n    override val id: String = \\"\\",\\n    override val name: String = \\"\\",\\n) : AudienceEntry {\\n    override fun display(): AudienceDisplay {\\n        return ExampleAudienceDisplay()\\n    }\\n}"},"audience_display":{"file":"src/main/kotlin/com/typewritermc/example/entries/manifest/ExampleAudienceEntry.kt","content":"class ExampleAudienceDisplay : AudienceDisplay() {\\n    override fun initialize() {\\n        // This is called when the first player is added to the audience.\\n        super.initialize()\\n        // Do something when the audience is initialized\\n    }\\n\\n    override fun onPlayerAdd(player: Player) {\\n        // Do something when a player gets added to the audience\\n    }\\n\\n    override fun onPlayerRemove(player: Player) {\\n        // Do something when a player gets removed from the audience\\n    }\\n\\n    override fun dispose() {\\n        super.dispose()\\n        // Do something when the audience is disposed\\n        // It will always call onPlayerRemove for all players.\\n        // So no player cleanup is needed here.\\n    }\\n}"},"tickable_audience_display":{"file":"src/main/kotlin/com/typewritermc/example/entries/manifest/ExampleAudienceEntry.kt","content":"// highlight-next-line\\nclass TickableAudienceDisplay : AudienceDisplay(), TickableDisplay {\\n    override fun onPlayerAdd(player: Player) {}\\n    override fun onPlayerRemove(player: Player) {}\\n\\n    // highlight-start\\n    override fun tick() {\\n        // Do something when the audience is ticked\\n        players.forEach { player ->\\n            // Do something with the player\\n        }\\n\\n        // This is running asynchronously\\n        // If you need to do something on the main thread\\n        ThreadType.SYNC.launch {\\n            // Though this will run a tick later, to sync with the bukkit scheduler.\\n        }\\n    }\\n    // highlight-end\\n}"},"audience_display_with_events":{"file":"src/main/kotlin/com/typewritermc/example/entries/manifest/ExampleAudienceEntry.kt","content":"class AudienceDisplayWithEvents : AudienceDisplay() {\\n    override fun onPlayerAdd(player: Player) {}\\n    override fun onPlayerRemove(player: Player) {}\\n\\n    // highlight-start\\n    @EventHandler\\n    fun onSomeEvent(event: SomeBukkitEvent) {\\n        // Do something when the event is triggered\\n        // This will trigger for all players, not just the ones in the audience.\\n        // So we need to check if the player is in the audience.\\n        if (event.player in this) {\\n            // Do something with the player\\n        }\\n    }\\n    // highlight-end\\n}"},"artifact_entry":{"file":"src/main/kotlin/com/typewritermc/example/entries/static/ExampleArtifactEntry.kt","content":"@Entry(\\"example_artifact\\", \\"An example artifact entry.\\", Colors.BLUE, \\"material-symbols:home-storage-rounded\\")\\nclass ExampleArtifactEntry(\\n    override val id: String = \\"\\",\\n    override val name: String = \\"\\",\\n    override val artifactId: String = \\"\\",\\n) : ArtifactEntry"},"artifact_access":{"file":"src/main/kotlin/com/typewritermc/example/entries/static/ExampleArtifactEntry.kt","content":"suspend fun accessArtifactData(ref: Ref<out ArtifactEntry>) {\\n    val assetManager = KoinJavaComponent.get<AssetManager>(AssetManager::class.java)\\n    val entry = ref.get() ?: return\\n    val content: String? = assetManager.fetchAsset(entry)\\n    // Do something with the content\\n}"},"asset_entry":{"file":"src/main/kotlin/com/typewritermc/example/entries/static/ExampleAssetEntry.kt","content":"@Entry(\\"example_asset\\", \\"An example asset entry.\\", Colors.BLUE, \\"material-symbols:home-storage-rounded\\")\\nclass ExampleAssetEntry(\\n    override val id: String = \\"\\",\\n    override val name: String = \\"\\",\\n    override val path: String = \\"\\",\\n) : AssetEntry"},"asset_access":{"file":"src/main/kotlin/com/typewritermc/example/entries/static/ExampleAssetEntry.kt","content":"suspend fun accessAssetData(ref: Ref<out AssetEntry>) {\\n    val assetManager = KoinJavaComponent.get<AssetManager>(AssetManager::class.java)\\n    val entry = ref.get() ?: return\\n    val content: String? = assetManager.fetchAsset(entry)\\n    // Do something with the content\\n}"},"sound_id_entry":{"file":"src/main/kotlin/com/typewritermc/example/entries/static/ExampleSoundIdEntry.kt","content":"@Entry(\\"example_sound\\", \\"An example sound entry.\\", Colors.BLUE, \\"icon-park-solid:volume-up\\")\\nclass ExampleSoundIdEntry(\\n    override val id: String = \\"\\",\\n    override val name: String = \\"\\",\\n    override val soundId: String = \\"\\",\\n) : SoundIdEntry"},"sound_source_entry":{"file":"src/main/kotlin/com/typewritermc/example/entries/static/ExampleSoundSourceEntry.kt","content":"@Entry(\\"example_sound_source\\", \\"An example sound source entry.\\", Colors.BLUE, \\"ic:round-spatial-audio-off\\")\\nclass ExampleSoundSourceEntry(\\n    override val id: String = \\"\\",\\n    override val name: String = \\"\\",\\n) : SoundSourceEntry {\\n    override fun getEmitter(player: Player): SoundEmitter {\\n        // Return the emitter that should be used for the sound.\\n        // An entity should be provided.\\n        return SoundEmitter(player.entityId)\\n    }\\n}"},"speaker_entry":{"file":"src/main/kotlin/com/typewritermc/example/entries/static/ExampleSpeakerEntry.kt","content":"@Entry(\\"example_speaker\\", \\"An example speaker entry.\\", Colors.BLUE, \\"ic:round-spatial-audio-off\\")\\nclass ExampleSpeakerEntry(\\n    override val id: String = \\"\\",\\n    override val name: String = \\"\\",\\n    override val displayName: String = \\"\\",\\n    override val sound: Sound = Sound.EMPTY,\\n) : SpeakerEntry"},"action_entry":{"file":"src/main/kotlin/com/typewritermc/example/entries/trigger/ExampleActionEntry.kt","content":"@Entry(\\"example_action\\", \\"An example action entry.\\", Colors.RED, \\"material-symbols:touch-app-rounded\\")\\nclass ExampleActionEntry(\\n    override val id: String = \\"\\",\\n    override val name: String = \\"\\",\\n    override val criteria: List<Criteria> = emptyList(),\\n    override val modifiers: List<Modifier> = emptyList(),\\n    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),\\n) : ActionEntry {\\n    override fun execute(player: Player) {\\n        super.execute(player) // This will apply all the modifiers.\\n        // Do something with the player\\n    }\\n}"},"custom_triggering_action_entry":{"file":"src/main/kotlin/com/typewritermc/example/entries/trigger/ExampleCustomTriggeringActionEntry.kt","content":"@Entry(\\n    \\"example_custom_triggering_action\\",\\n    \\"An example custom triggering entry.\\",\\n    Colors.RED,\\n    \\"material-symbols:touch-app-rounded\\"\\n)\\nclass ExampleCustomTriggeringActionEntry(\\n    override val id: String = \\"\\",\\n    override val name: String = \\"\\",\\n    override val criteria: List<Criteria> = emptyList(),\\n    override val modifiers: List<Modifier> = emptyList(),\\n    @SerializedName(\\"triggers\\")\\n    override val customTriggers: List<Ref<TriggerableEntry>> = emptyList(),\\n) : CustomTriggeringActionEntry {\\n    override fun execute(player: Player) {\\n        super.execute(player) // This will apply the modifiers.\\n        // Do something with the player\\n        player.triggerCustomTriggers() // Can be called later to trigger the next entries.\\n    }\\n}"},"dialogue_entry":{"file":"src/main/kotlin/com/typewritermc/example/entries/trigger/ExampleDialogueEntry.kt","content":"@Entry(\\"example_dialogue\\", \\"An example dialogue entry.\\", Colors.BLUE, \\"material-symbols:chat-rounded\\")\\nclass ExampleDialogueEntry(\\n    override val id: String = \\"\\",\\n    override val name: String = \\"\\",\\n    override val criteria: List<Criteria> = emptyList(),\\n    override val modifiers: List<Modifier> = emptyList(),\\n    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),\\n    override val speaker: Ref<SpeakerEntry> = emptyRef(),\\n    @MultiLine\\n    @Placeholder\\n    @Colored\\n    @Help(\\"The text to display to the player.\\")\\n    val text: String = \\"\\",\\n) : DialogueEntry"},"dialogue_messenger":{"file":"src/main/kotlin/com/typewritermc/example/entries/trigger/ExampleDialogueEntry.kt","content":"@Messenger(ExampleDialogueEntry::class)\\nclass ExampleDialogueDialogueMessenger(player: Player, entry: ExampleDialogueEntry) :\\n    DialogueMessenger<ExampleDialogueEntry>(player, entry) {\\n\\n    companion object : MessengerFilter {\\n        override fun filter(player: Player, entry: DialogueEntry): Boolean = true\\n    }\\n\\n    // Called every game tick (20 times per second).\\n    // The cycle is a parameter that is incremented every tick, starting at 0.\\n    override fun tick(context: TickContext) {\\n        super.tick(context)\\n        if (state != MessengerState.RUNNING) return\\n\\n        player.sendMessage(\\"${entry.speakerDisplayName}: ${entry.text}\\".parsePlaceholders(player).asMini())\\n\\n        // When we want the dialogue to end, we can set the state to FINISHED.\\n        state = MessengerState.FINISHED\\n    }\\n}"},"event_entry":{"file":"src/main/kotlin/com/typewritermc/example/entries/trigger/ExampleEventEntry.kt","content":"@Entry(\\"example_event\\", \\"An example event entry.\\", Colors.YELLOW, \\"material-symbols:bigtop-updates\\")\\nclass ExampleEventEntry(\\n    override val id: String = \\"\\",\\n    override val name: String = \\"\\",\\n    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),\\n) : EventEntry"},"event_entry_listener":{"file":"src/main/kotlin/com/typewritermc/example/entries/trigger/ExampleEventEntry.kt","content":"// Must be scoped to be public\\n@EntryListener(ExampleEventEntry::class)\\nfun onEvent(event: SomeBukkitEvent, query: Query<ExampleEventEntry>) {\\n    // Do something\\n    val entries = query.find() // Find all the entries of this type, for more information see the Query section\\n    // Do something with the entries, for example trigger them\\n    entries triggerAllFor event.player\\n}"}}')}}]);