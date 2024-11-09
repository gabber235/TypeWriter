"use strict";(self.webpackChunkdocumentation=self.webpackChunkdocumentation||[]).push([["73927"],{80266:function(e,n,t){t.r(n),t.d(n,{metadata:()=>i,contentTitle:()=>c,default:()=>p,assets:()=>d,toc:()=>m,frontMatter:()=>o});var i=JSON.parse('{"id":"develop/extensions/getting_started","title":"Getting Started","description":"Introduction","source":"@site/versioned_docs/version-0.6.0/develop/02-extensions/02-getting_started.mdx","sourceDirName":"develop/02-extensions","slug":"/develop/extensions/getting_started","permalink":"/develop/extensions/getting_started","draft":false,"unlisted":false,"editUrl":"https://github.com/gabber235/TypeWriter/tree/develop/documentation/versioned_docs/version-0.6.0/develop/02-extensions/02-getting_started.mdx","tags":[],"version":"0.6.0","lastUpdatedBy":"dependabot[bot]","lastUpdatedAt":1731173593000,"sidebarPosition":2,"frontMatter":{"title":"Getting Started"},"sidebar":"develop","previous":{"title":"Extensions","permalink":"/develop/extensions/"},"next":{"title":"Initializers","permalink":"/develop/extensions/initializers"}}'),r=t("85893"),a=t("50065");t("48506");var l=t("87057"),s=t("74673");let o={title:"Getting Started"},c="Creating an Extension",d={},m=[{value:"Introduction",id:"introduction",level:2},{value:"Prerequisites",id:"prerequisites",level:2},{value:"Setting Up a Gradle Project",id:"setting-up-a-gradle-project",level:2},{value:"Building the Extension",id:"building-the-extension",level:2},{value:"What&#39;s Next?",id:"whats-next",level:2}];function u(e){let n={a:"a",admonition:"admonition",code:"code",h1:"h1",h2:"h2",header:"header",li:"li",p:"p",pre:"pre",ul:"ul",...(0,a.a)(),...e.components};return(0,r.jsxs)(r.Fragment,{children:[(0,r.jsx)(n.header,{children:(0,r.jsx)(n.h1,{id:"creating-an-extension",children:"Creating an Extension"})}),"\n",(0,r.jsx)(n.h2,{id:"introduction",children:"Introduction"}),"\n",(0,r.jsx)(n.p,{children:"Typewriter is a dynamic platform that supports the development of extensions, which are modular components enhancing the overall functionality.\nExtensions are self-contained, easily shareable, and integrate smoothly into the TypeWriter system.\nThey allow you to create custom entries and have them show up in the web panel.\nThis guide is tailored to guide you through the process of creating an extension."}),"\n",(0,r.jsx)(n.h2,{id:"prerequisites",children:"Prerequisites"}),"\n",(0,r.jsxs)(n.ul,{children:["\n",(0,r.jsx)(n.li,{children:"Java Development Kit (JDK) 21 or higher."}),"\n",(0,r.jsx)(n.li,{children:"An Integrated Development Environment (IDE) such as IntelliJ IDEA or Eclipse."}),"\n",(0,r.jsx)(n.li,{children:"A basic understanding of Gradle and the Spigot API."}),"\n"]}),"\n",(0,r.jsx)(n.h2,{id:"setting-up-a-gradle-project",children:"Setting Up a Gradle Project"}),"\n",(0,r.jsx)(n.p,{children:"Begin by establishing a Gradle project for your Typewriter extension. Below is a comprehensive setup for your Gradle project:"}),"\n",(0,r.jsxs)(l.Z,{children:[(0,r.jsxs)(s.Z,{value:"release",label:"Release",default:!0,children:[(0,r.jsxs)(n.p,{children:["Add the following to your ",(0,r.jsx)(n.code,{children:"settings.gradle.kts"})," file:"]}),(0,r.jsx)(n.pre,{children:(0,r.jsx)(n.code,{className:"language-kotlin",metastring:'title="settings.gradle.kts"',children:'pluginManagement {\n    repositories {\n        mavenCentral()\n        gradlePluginPortal()\n        maven("https://maven.typewritermc.com/releases")\n    }\n}\n'})}),(0,r.jsxs)(n.p,{children:["Add the following to your ",(0,r.jsx)(n.code,{children:"build.gradle.kts"})," file:"]}),(0,r.jsx)(n.pre,{children:(0,r.jsx)(n.code,{className:"language-kotlin",metastring:'title="build.gradle.kts"',children:'import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar\n\nplugins {\n    kotlin("jvm") version "2.0.20"\n    id("com.typewritermc.module-plugin") version "<module plugin version>"\n}\n\n// Replace with your own information\ngroup = "me.yourusername"\nversion = "0.0.1"\n\ntypewriter {\n    engine {\n        version = "<latest typewriter version>"\n    }\n    namespace = "<a name for the company which all your extensions are published under>"\n\n    extension {\n        name = "<Name of the extension>"\n        shortDescription = "<Short description of the extension>"\n        description = "<Long description of the extension>"\n\n        paper {\n            // Optional - If you want to make sure a plugin is required to be installed to use this extension\n            dependency("<plugin name>")\n        }\n    }\n}\n\nkotlin {\n    jvmToolchain(21)\n}\n'})})]}),(0,r.jsxs)(s.Z,{value:"beta",label:"Beta",children:[(0,r.jsxs)(n.p,{children:["Add the following to your ",(0,r.jsx)(n.code,{children:"settings.gradle.kts"})," file:"]}),(0,r.jsx)(n.pre,{children:(0,r.jsx)(n.code,{className:"language-kotlin",metastring:'title="settings.gradle.kts"',children:'pluginManagement {\n    repositories {\n        mavenCentral()\n        gradlePluginPortal()\n        maven("https://maven.typewritermc.com/beta")\n    }\n}\n'})}),(0,r.jsxs)(n.p,{children:["Add the following to your ",(0,r.jsx)(n.code,{children:"build.gradle.kts"})," file:"]}),(0,r.jsx)(n.pre,{children:(0,r.jsx)(n.code,{className:"language-kotlin",metastring:'title="build.gradle.kts"',children:'import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar\n\nplugins {\n    kotlin("jvm") version "2.0.20"\n    id("com.typewritermc.module-plugin") version "<module plugin version>"\n}\n\ntypewriter {\n    engine {\n        version = "<latest typewriter version>"\n        channel = com.typewritermc.moduleplugin.ReleaseChannel.BETA\n    }\n    namespace = "<a name for the company which all your extensions are published under>"\n\n    extension {\n        name = "<Name of the extension>"\n        shortDescription = "<Short description of the extension>"\n        description = "<Long description of the extension>"\n\n        paper {\n            // Optional - If you want to make sure a plugin is required to be installed to use this extension\n            dependency("<plugin name>")\n        }\n    }\n}\n\nkotlin {\n    jvmToolchain(21)\n}\n'})})]})]}),"\n",(0,r.jsx)(n.admonition,{title:"Replace your information",type:"info",children:(0,r.jsxs)(n.p,{children:["Ensure to replace placeholders like ",(0,r.jsx)(n.code,{children:"me.yourusername"})," with your project details."]})}),"\n",(0,r.jsx)(n.h2,{id:"building-the-extension",children:"Building the Extension"}),"\n",(0,r.jsxs)(n.p,{children:["After creating the extension class, build the extension. This can be done by running the ",(0,r.jsx)(n.code,{children:"build"})," Gradle task.\nThis will generate a JAR file in the ",(0,r.jsx)(n.code,{children:"build/libs"})," directory.\nThis JAR file can be used as an extension in TypeWriter.\nPlace the JAR file in the ",(0,r.jsx)(n.code,{children:"plugins/TypeWriter/extensions"})," directory.\nTypewriter will automatically load the extension and run it."]}),"\n",(0,r.jsx)(n.admonition,{title:"Reloading Extensions",type:"tip",children:(0,r.jsxs)(n.p,{children:["You can either restart the Minecraft server or reload Typewriter with ",(0,r.jsx)(n.code,{children:"/typewriter reload"})," to reload the extensions."]})}),"\n",(0,r.jsxs)(n.p,{children:["If any problems occur, check the console for errors and ensure that the extension is properly configured.\nIf you need help, join the ",(0,r.jsx)(n.a,{href:"https://discord.gg/HtbKyuDDBw",children:"Discord server"})," and ask for help."]}),"\n",(0,r.jsx)(n.h2,{id:"whats-next",children:"What's Next?"}),"\n",(0,r.jsxs)(n.p,{children:["After creating an extension, you can start adding features to it.\nCheck out the ",(0,r.jsx)(n.a,{href:"entries",children:"Creating Entries"})," guide to learn how to add entries to your extension."]})]})}function p(e={}){let{wrapper:n}={...(0,a.a)(),...e.components};return n?(0,r.jsx)(n,{...e,children:(0,r.jsx)(u,{...e})}):u(e)}},74673:function(e,n,t){t.d(n,{Z:()=>l});var i=t("85893");t("67294");var r=t("67026");let a="tabItem_Ymn6";function l(e){let{children:n,hidden:t,className:l}=e;return(0,i.jsx)("div",{role:"tabpanel",className:(0,r.Z)(a,l),hidden:t,children:n})}},87057:function(e,n,t){t.d(n,{Z:()=>E});var i=t("85893"),r=t("67294"),a=t("67026"),l=t("51770"),s=t("16550"),o=t("55104"),c=t("61906"),d=t("92227"),m=t("56588");function u(e){return r.Children.toArray(e).filter(e=>"\n"!==e).map(e=>{if(!e||r.isValidElement(e)&&function(e){let{props:n}=e;return!!n&&"object"==typeof n&&"value"in n}(e))return e;throw Error(`Docusaurus error: Bad <Tabs> child <${"string"==typeof e.type?e.type:e.type.name}>: all children of the <Tabs> component should be <TabItem>, and every <TabItem> should have a unique "value" prop.`)})?.filter(Boolean)??[]}function p(e){let{value:n,tabValues:t}=e;return t.some(e=>e.value===n)}var h=t("37131");let y="tabList__CuJ",g="tabItem_LNqP";function v(e){let{className:n,block:t,selectedValue:r,selectValue:s,tabValues:o}=e,c=[],{blockElementScrollPositionUntilNextRender:d}=(0,l.o5)(),m=e=>{let n=e.currentTarget,t=o[c.indexOf(n)].value;t!==r&&(d(n),s(t))},u=e=>{let n=null;switch(e.key){case"Enter":m(e);break;case"ArrowRight":{let t=c.indexOf(e.currentTarget)+1;n=c[t]??c[0];break}case"ArrowLeft":{let t=c.indexOf(e.currentTarget)-1;n=c[t]??c[c.length-1]}}n?.focus()};return(0,i.jsx)("ul",{role:"tablist","aria-orientation":"horizontal",className:(0,a.Z)("tabs",{"tabs--block":t},n),children:o.map(e=>{let{value:n,label:t,attributes:l}=e;return(0,i.jsx)("li",{role:"tab",tabIndex:r===n?0:-1,"aria-selected":r===n,ref:e=>c.push(e),onKeyDown:u,onClick:m,...l,className:(0,a.Z)("tabs__item",g,l?.className,{"tabs__item--active":r===n}),children:t??n},n)})})}function f(e){let{lazy:n,children:t,selectedValue:l}=e,s=(Array.isArray(t)?t:[t]).filter(Boolean);if(n){let e=s.find(e=>e.props.value===l);return e?(0,r.cloneElement)(e,{className:(0,a.Z)("margin-top--md",e.props.className)}):null}return(0,i.jsx)("div",{className:"margin-top--md",children:s.map((e,n)=>(0,r.cloneElement)(e,{key:n,hidden:e.props.value!==l}))})}function x(e){let n=function(e){let{defaultValue:n,queryString:t=!1,groupId:i}=e,a=function(e){let{values:n,children:t}=e;return(0,r.useMemo)(()=>{let e=n??u(t).map(e=>{let{props:{value:n,label:t,attributes:i,default:r}}=e;return{value:n,label:t,attributes:i,default:r}});return!function(e){let n=(0,d.lx)(e,(e,n)=>e.value===n.value);if(n.length>0)throw Error(`Docusaurus error: Duplicate values "${n.map(e=>e.value).join(", ")}" found in <Tabs>. Every value needs to be unique.`)}(e),e},[n,t])}(e),[l,h]=(0,r.useState)(()=>(function(e){let{defaultValue:n,tabValues:t}=e;if(0===t.length)throw Error("Docusaurus error: the <Tabs> component requires at least one <TabItem> children component");if(n){if(!p({value:n,tabValues:t}))throw Error(`Docusaurus error: The <Tabs> has a defaultValue "${n}" but none of its children has the corresponding value. Available values are: ${t.map(e=>e.value).join(", ")}. If you intend to show no default tab, use defaultValue={null} instead.`);return n}let i=t.find(e=>e.default)??t[0];if(!i)throw Error("Unexpected error: 0 tabValues");return i.value})({defaultValue:n,tabValues:a})),[y,g]=function(e){let{queryString:n=!1,groupId:t}=e,i=(0,s.k6)(),a=function(e){let{queryString:n=!1,groupId:t}=e;if("string"==typeof n)return n;if(!1===n)return null;if(!0===n&&!t)throw Error('Docusaurus error: The <Tabs> component groupId prop is required if queryString=true, because this value is used as the search param name. You can also provide an explicit value such as queryString="my-search-param".');return t??null}({queryString:n,groupId:t}),l=(0,c._X)(a);return[l,(0,r.useCallback)(e=>{if(!a)return;let n=new URLSearchParams(i.location.search);n.set(a,e),i.replace({...i.location,search:n.toString()})},[a,i])]}({queryString:t,groupId:i}),[v,f]=function(e){var n;let{groupId:t}=e;let i=(n=t)?`docusaurus.tab.${n}`:null,[a,l]=(0,m.Nk)(i);return[a,(0,r.useCallback)(e=>{if(!!i)l.set(e)},[i,l])]}({groupId:i}),x=(()=>{let e=y??v;return p({value:e,tabValues:a})?e:null})();return(0,o.Z)(()=>{x&&h(x)},[x]),{selectedValue:l,selectValue:(0,r.useCallback)(e=>{if(!p({value:e,tabValues:a}))throw Error(`Can't select invalid tab value=${e}`);h(e),g(e),f(e)},[g,f,a]),tabValues:a}}(e);return(0,i.jsxs)("div",{className:(0,a.Z)("tabs-container",y),children:[(0,i.jsx)(v,{...n,...e}),(0,i.jsx)(f,{...n,...e})]})}function E(e){let n=(0,h.Z)();return(0,i.jsx)(x,{...e,children:u(e.children)},String(n))}},48506:function(e,n,t){t.d(n,{Z:()=>l});var i=t("85893");let r={initializer:{file:"src/main/kotlin/com/typewritermc/example/ExampleInitializer.kt",content:"import com.typewritermc.core.extension.Initializable\nimport com.typewritermc.core.extension.annotations.Initializer\n\n@Initializer\nobject ExampleInitializer : Initializable {\n    override fun initialize() {\n        // Do something when the extension is initialized\n    }\n\n    override fun shutdown() {\n        // Do something when the extension is shutdown\n    }\n}"},cinematic_entry:{file:"src/main/kotlin/com/typewritermc/example/entries/cinematic/ExampleCinematicEntry.kt",content:'@Entry("example_cinematic", "An example cinematic entry", Colors.BLUE, "material-symbols:cinematic-blur")\nclass ExampleCinematicEntry(\n    override val id: String = "",\n    override val name: String = "",\n    override val criteria: List<Criteria> = emptyList(),\n    @Segments(Colors.BLUE, "material-symbols:cinematic-blur")\n    val segments: List<ExampleSegment> = emptyList(),\n) : CinematicEntry {\n    override fun create(player: Player): CinematicAction {\n        return ExampleCinematicAction(player, this)\n    }\n}'},cinematic_segment_with_min_max:{file:"src/main/kotlin/com/typewritermc/example/entries/cinematic/ExampleCinematicEntry.kt",content:'    @Segments(Colors.BLUE, "material-symbols:cinematic-blur")\n    @InnerMin(Min(10))\n    @InnerMax(Max(20))\n    val segments: List<ExampleSegment> = emptyList(),'},cinematic_create_actions:{file:"src/main/kotlin/com/typewritermc/example/entries/cinematic/ExampleCinematicEntry.kt",content:"    // This will be used when the cinematic is normally displayed to the player.\n    override fun create(player: Player): CinematicAction {\n        return DefaultCinematicAction(player, this)\n    }\n\n    // This is used during content mode to display the cinematic to the player.\n    // It may be null to not show it during simulation.\n    override fun createSimulating(player: Player): CinematicAction? {\n        return SimulatedCinematicAction(player, this)\n    }\n\n    // This is used during content mode to record the cinematic.\n    // It may be null to not record it during simulation.\n    override fun createRecording(player: Player): CinematicAction? {\n        return RecordingCinematicAction(player, this)\n    }"},cinematic_segment:{file:"src/main/kotlin/com/typewritermc/example/entries/cinematic/ExampleCinematicEntry.kt",content:"data class ExampleSegment(\n    override val startFrame: Int = 0,\n    override val endFrame: Int = 0,\n) : Segment"},cinematic_action:{file:"src/main/kotlin/com/typewritermc/example/entries/cinematic/ExampleCinematicEntry.kt",content:"class ExampleCinematicAction(\n    val player: Player,\n    val entry: ExampleCinematicEntry,\n) : CinematicAction {\n    override suspend fun setup() {\n        // Initialize variables, spawn entities, etc.\n    }\n\n    override suspend fun tick(frame: Int) {\n        val segment = entry.segments activeSegmentAt frame\n        // Can be null if no segment is active\n\n        // The `frame` parameter is not necessarily next frame: `frame != old(frame)+1`\n\n        // Execute tick logic for the segment\n    }\n\n    override suspend fun teardown() {\n        // Remove entities, etc.\n    }\n\n    override fun canFinish(frame: Int): Boolean = entry.segments canFinishAt frame\n}"},cinematic_simple_action:{file:"src/main/kotlin/com/typewritermc/example/entries/cinematic/ExampleCinematicEntry.kt",content:"class ExampleSimpleCinematicAction(\n    val player: Player,\n    entry: ExampleCinematicEntry,\n) : SimpleCinematicAction<ExampleSegment>() {\n    override val segments: List<ExampleSegment> = entry.segments\n\n    override suspend fun startSegment(segment: ExampleSegment) {\n        super.startSegment(segment) // Keep this\n        // Called when a segment starts\n    }\n\n    override suspend fun tickSegment(segment: ExampleSegment, frame: Int) {\n        super.tickSegment(segment, frame) // Keep this\n        // Called every tick while the segment is active\n        // Will always be called after startSegment and never after stopSegment\n\n        // The `frame` parameter is not necessarily next frame: `frame != old(frame)+1`\n    }\n\n    override suspend fun stopSegment(segment: ExampleSegment) {\n        super.stopSegment(segment) // Keep this\n        // Called when the segment ends\n        // Will also be called if the cinematic is stopped early\n    }\n}"},audience_entry:{file:"src/main/kotlin/com/typewritermc/example/entries/manifest/ExampleAudienceEntry.kt",content:'@Entry("example_audience", "An example audience entry.", Colors.GREEN, "material-symbols:chat-rounded")\nclass ExampleAudienceEntry(\n    override val id: String = "",\n    override val name: String = "",\n) : AudienceEntry {\n    override fun display(): AudienceDisplay {\n        return ExampleAudienceDisplay()\n    }\n}'},audience_display:{file:"src/main/kotlin/com/typewritermc/example/entries/manifest/ExampleAudienceEntry.kt",content:"class ExampleAudienceDisplay : AudienceDisplay() {\n    override fun initialize() {\n        // This is called when the first player is added to the audience.\n        super.initialize()\n        // Do something when the audience is initialized\n    }\n\n    override fun onPlayerAdd(player: Player) {\n        // Do something when a player gets added to the audience\n    }\n\n    override fun onPlayerRemove(player: Player) {\n        // Do something when a player gets removed from the audience\n    }\n\n    override fun dispose() {\n        super.dispose()\n        // Do something when the audience is disposed\n        // It will always call onPlayerRemove for all players.\n        // So no player cleanup is needed here.\n    }\n}"},tickable_audience_display:{file:"src/main/kotlin/com/typewritermc/example/entries/manifest/ExampleAudienceEntry.kt",content:"// highlight-next-line\nclass TickableAudienceDisplay : AudienceDisplay(), TickableDisplay {\n    override fun onPlayerAdd(player: Player) {}\n    override fun onPlayerRemove(player: Player) {}\n\n    // highlight-start\n    override fun tick() {\n        // Do something when the audience is ticked\n        players.forEach { player ->\n            // Do something with the player\n        }\n\n        // This is running asynchronously\n        // If you need to do something on the main thread\n        ThreadType.SYNC.launch {\n            // Though this will run a tick later, to sync with the bukkit scheduler.\n        }\n    }\n    // highlight-end\n}"},audience_display_with_events:{file:"src/main/kotlin/com/typewritermc/example/entries/manifest/ExampleAudienceEntry.kt",content:"class AudienceDisplayWithEvents : AudienceDisplay() {\n    override fun onPlayerAdd(player: Player) {}\n    override fun onPlayerRemove(player: Player) {}\n\n    // highlight-start\n    @EventHandler\n    fun onSomeEvent(event: SomeBukkitEvent) {\n        // Do something when the event is triggered\n        // This will trigger for all players, not just the ones in the audience.\n        // So we need to check if the player is in the audience.\n        if (event.player in this) {\n            // Do something with the player\n        }\n    }\n    // highlight-end\n}"},artifact_entry:{file:"src/main/kotlin/com/typewritermc/example/entries/static/ExampleArtifactEntry.kt",content:'@Entry("example_artifact", "An example artifact entry.", Colors.BLUE, "material-symbols:home-storage-rounded")\nclass ExampleArtifactEntry(\n    override val id: String = "",\n    override val name: String = "",\n    override val artifactId: String = "",\n) : ArtifactEntry'},artifact_access:{file:"src/main/kotlin/com/typewritermc/example/entries/static/ExampleArtifactEntry.kt",content:"suspend fun accessArtifactData(ref: Ref<out ArtifactEntry>) {\n    val assetManager = KoinJavaComponent.get<AssetManager>(AssetManager::class.java)\n    val entry = ref.get() ?: return\n    val content: String? = assetManager.fetchAsset(entry)\n    // Do something with the content\n}"},asset_entry:{file:"src/main/kotlin/com/typewritermc/example/entries/static/ExampleAssetEntry.kt",content:'@Entry("example_asset", "An example asset entry.", Colors.BLUE, "material-symbols:home-storage-rounded")\nclass ExampleAssetEntry(\n    override val id: String = "",\n    override val name: String = "",\n    override val path: String = "",\n) : AssetEntry'},asset_access:{file:"src/main/kotlin/com/typewritermc/example/entries/static/ExampleAssetEntry.kt",content:"suspend fun accessAssetData(ref: Ref<out AssetEntry>) {\n    val assetManager = KoinJavaComponent.get<AssetManager>(AssetManager::class.java)\n    val entry = ref.get() ?: return\n    val content: String? = assetManager.fetchAsset(entry)\n    // Do something with the content\n}"},sound_id_entry:{file:"src/main/kotlin/com/typewritermc/example/entries/static/ExampleSoundIdEntry.kt",content:'@Entry("example_sound", "An example sound entry.", Colors.BLUE, "icon-park-solid:volume-up")\nclass ExampleSoundIdEntry(\n    override val id: String = "",\n    override val name: String = "",\n    override val soundId: String = "",\n) : SoundIdEntry'},sound_source_entry:{file:"src/main/kotlin/com/typewritermc/example/entries/static/ExampleSoundSourceEntry.kt",content:'@Entry("example_sound_source", "An example sound source entry.", Colors.BLUE, "ic:round-spatial-audio-off")\nclass ExampleSoundSourceEntry(\n    override val id: String = "",\n    override val name: String = "",\n) : SoundSourceEntry {\n    override fun getEmitter(player: Player): SoundEmitter {\n        // Return the emitter that should be used for the sound.\n        // An entity should be provided.\n        return SoundEmitter(player.entityId)\n    }\n}'},speaker_entry:{file:"src/main/kotlin/com/typewritermc/example/entries/static/ExampleSpeakerEntry.kt",content:'@Entry("example_speaker", "An example speaker entry.", Colors.BLUE, "ic:round-spatial-audio-off")\nclass ExampleSpeakerEntry(\n    override val id: String = "",\n    override val name: String = "",\n    override val displayName: String = "",\n    override val sound: Sound = Sound.EMPTY,\n) : SpeakerEntry'},action_entry:{file:"src/main/kotlin/com/typewritermc/example/entries/trigger/ExampleActionEntry.kt",content:'@Entry("example_action", "An example action entry.", Colors.RED, "material-symbols:touch-app-rounded")\nclass ExampleActionEntry(\n    override val id: String = "",\n    override val name: String = "",\n    override val criteria: List<Criteria> = emptyList(),\n    override val modifiers: List<Modifier> = emptyList(),\n    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),\n) : ActionEntry {\n    override fun execute(player: Player) {\n        super.execute(player) // This will apply all the modifiers.\n        // Do something with the player\n    }\n}'},custom_triggering_action_entry:{file:"src/main/kotlin/com/typewritermc/example/entries/trigger/ExampleCustomTriggeringActionEntry.kt",content:'@Entry(\n    "example_custom_triggering_action",\n    "An example custom triggering entry.",\n    Colors.RED,\n    "material-symbols:touch-app-rounded"\n)\nclass ExampleCustomTriggeringActionEntry(\n    override val id: String = "",\n    override val name: String = "",\n    override val criteria: List<Criteria> = emptyList(),\n    override val modifiers: List<Modifier> = emptyList(),\n    @SerializedName("triggers")\n    override val customTriggers: List<Ref<TriggerableEntry>> = emptyList(),\n) : CustomTriggeringActionEntry {\n    override fun execute(player: Player) {\n        super.execute(player) // This will apply the modifiers.\n        // Do something with the player\n        player.triggerCustomTriggers() // Can be called later to trigger the next entries.\n    }\n}'},dialogue_entry:{file:"src/main/kotlin/com/typewritermc/example/entries/trigger/ExampleDialogueEntry.kt",content:'@Entry("example_dialogue", "An example dialogue entry.", Colors.BLUE, "material-symbols:chat-rounded")\nclass ExampleDialogueEntry(\n    override val id: String = "",\n    override val name: String = "",\n    override val criteria: List<Criteria> = emptyList(),\n    override val modifiers: List<Modifier> = emptyList(),\n    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),\n    override val speaker: Ref<SpeakerEntry> = emptyRef(),\n    @MultiLine\n    @Placeholder\n    @Colored\n    @Help("The text to display to the player.")\n    val text: String = "",\n) : DialogueEntry'},dialogue_messenger:{file:"src/main/kotlin/com/typewritermc/example/entries/trigger/ExampleDialogueEntry.kt",content:'@Messenger(ExampleDialogueEntry::class)\nclass ExampleDialogueDialogueMessenger(player: Player, entry: ExampleDialogueEntry) :\n    DialogueMessenger<ExampleDialogueEntry>(player, entry) {\n\n    companion object : MessengerFilter {\n        override fun filter(player: Player, entry: DialogueEntry): Boolean = true\n    }\n\n    // Called every game tick (20 times per second).\n    // The cycle is a parameter that is incremented every tick, starting at 0.\n    override fun tick(context: TickContext) {\n        super.tick(context)\n        if (state != MessengerState.RUNNING) return\n\n        player.sendMessage("${entry.speakerDisplayName}: ${entry.text}".parsePlaceholders(player).asMini())\n\n        // When we want the dialogue to end, we can set the state to FINISHED.\n        state = MessengerState.FINISHED\n    }\n}'},event_entry:{file:"src/main/kotlin/com/typewritermc/example/entries/trigger/ExampleEventEntry.kt",content:'@Entry("example_event", "An example event entry.", Colors.YELLOW, "material-symbols:bigtop-updates")\nclass ExampleEventEntry(\n    override val id: String = "",\n    override val name: String = "",\n    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),\n) : EventEntry'},event_entry_listener:{file:"src/main/kotlin/com/typewritermc/example/entries/trigger/ExampleEventEntry.kt",content:"// Must be scoped to be public\n@EntryListener(ExampleEventEntry::class)\nfun onEvent(event: SomeBukkitEvent, query: Query<ExampleEventEntry>) {\n    // Do something\n    val entries = query.find() // Find all the entries of this type, for more information see the Query section\n    // Do something with the entries, for example trigger them\n    entries triggerAllFor event.player\n}"}};var a=t("59126");function l(e){let n,{tag:t,json:l}=e;if(!l)throw Error("JSON not provided");if(null==(n=Object.keys(l).length>0?l[t]:r[t]))return(0,i.jsxs)("div",{className:"text-red-500 dark:text-red-400 text-xs",children:["Code snippet not found: ",t]});let{file:s,content:o}=n;if(null==s||null==o)return(0,i.jsxs)("div",{className:"text-red-500 dark:text-red-400 text-xs",children:["Code snippet not found: ",t," (",n,")"]});let c=s.split("/").pop();return(0,i.jsx)(a.Z,{language:"kotlin",showLineNumbers:!0,title:c,children:o})}}}]);