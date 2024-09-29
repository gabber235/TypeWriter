"use strict";(self.webpackChunkdocumentation=self.webpackChunkdocumentation||[]).push([[65134],{96599:(e,n,t)=>{t.r(n),t.d(n,{assets:()=>l,contentTitle:()=>a,default:()=>h,frontMatter:()=>r,metadata:()=>o,toc:()=>c});var i=t(74848),s=t(28453);const r={},a="Create Entries",o={id:"develop/adapters/entries/index",title:"Create Entries",description:"Creating adapters for the TypeWriter Spigot plugin involves working with various entry types, each serving a specific",source:"@site/versioned_docs/version-0.5.1/develop/02-adapters/03-entries/index.mdx",sourceDirName:"develop/02-adapters/03-entries",slug:"/develop/adapters/entries/",permalink:"/develop/adapters/entries/",draft:!1,unlisted:!1,editUrl:"https://github.com/gabber235/TypeWriter/tree/develop/documentation/versioned_docs/version-0.5.1/develop/02-adapters/03-entries/index.mdx",tags:[],version:"0.5.1",lastUpdatedBy:"Gabber235",lastUpdatedAt:1727613594e3,frontMatter:{},sidebar:"develop",previous:{title:"Getting Started",permalink:"/develop/adapters/getting_started"},next:{title:"CinematicEntry",permalink:"/develop/adapters/entries/cinematic/"}},l={},c=[{value:"Base Entry Interfaces",id:"base-entry-interfaces",level:2},{value:"1. StaticEntry",id:"1-staticentry",level:3},{value:"2. TriggerEntry",id:"2-triggerentry",level:3},{value:"2a. TriggerableEntry (an extension of TriggerEntry)",id:"2a-triggerableentry-an-extension-of-triggerentry",level:4},{value:"3. CinematicEntry",id:"3-cinematicentry",level:3},{value:"4. ManifestEntry",id:"4-manifestentry",level:3},{value:"Implementation and Usage",id:"implementation-and-usage",level:2},{value:"Defining a Entry",id:"defining-a-entry",level:3}];function d(e){const n={a:"a",admonition:"admonition",code:"code",h1:"h1",h2:"h2",h3:"h3",h4:"h4",header:"header",hr:"hr",li:"li",ol:"ol",p:"p",strong:"strong",ul:"ul",...(0,s.R)(),...e.components};return(0,i.jsxs)(i.Fragment,{children:[(0,i.jsx)(n.header,{children:(0,i.jsx)(n.h1,{id:"create-entries",children:"Create Entries"})}),"\n",(0,i.jsx)(n.p,{children:"Creating adapters for the TypeWriter Spigot plugin involves working with various entry types, each serving a specific\npurpose in crafting immersive player experiences.\nThis documentation explains the roles and functionalities of these entry types, providing clear guidance for developers on how to effectively use them."}),"\n",(0,i.jsx)(n.h2,{id:"base-entry-interfaces",children:"Base Entry Interfaces"}),"\n",(0,i.jsx)(n.p,{children:"There are four base interfaces that all entries extend one of. These are:"}),"\n",(0,i.jsxs)(n.ol,{children:["\n",(0,i.jsxs)(n.li,{children:[(0,i.jsx)(n.strong,{children:"StaticEntry"}),": Represents static pages. These are used for content that does not change dynamically or trigger any actions. Examples include static text or images."]}),"\n",(0,i.jsxs)(n.li,{children:[(0,i.jsx)(n.strong,{children:"TriggerEntry"}),": Designed for entries that initiate other actions or events. These entries can trigger one or more additional entries, making them crucial for interactive sequences."]}),"\n",(0,i.jsxs)(n.li,{children:[(0,i.jsx)(n.strong,{children:"CinematicEntry"}),": Used for cinematic experiences. These entries are ideal for creating immersive story-driven sequences that engage players in a more visually dynamic way."]}),"\n",(0,i.jsxs)(n.li,{children:[(0,i.jsx)(n.strong,{children:"ManifestEntry"}),": Used for creating manifest pages, which are used to display statful content to the player."]}),"\n"]}),"\n",(0,i.jsx)(n.h3,{id:"1-staticentry",children:"1. StaticEntry"}),"\n",(0,i.jsxs)(n.ul,{children:["\n",(0,i.jsxs)(n.li,{children:[(0,i.jsx)(n.strong,{children:"AssetEntry"}),": Handles external assets, like images or files."]}),"\n",(0,i.jsxs)(n.li,{children:[(0,i.jsx)(n.strong,{children:"ArtifactEntry"}),": Manages plugin-generated assets, such as JSON files."]}),"\n",(0,i.jsxs)(n.li,{children:[(0,i.jsx)(n.strong,{children:"EntityEntry"}),": Serves as a base for static entities in the game.","\n",(0,i.jsxs)(n.ul,{children:["\n",(0,i.jsxs)(n.li,{children:[(0,i.jsx)(n.strong,{children:"SpeakerEntry"}),": Extends EntityEntry for entities capable of speaking, with defined display names and sounds."]}),"\n"]}),"\n"]}),"\n",(0,i.jsxs)(n.li,{children:[(0,i.jsx)(n.strong,{children:"FactEntry"}),": Represents static facts or data points."]}),"\n",(0,i.jsxs)(n.li,{children:[(0,i.jsx)(n.strong,{children:"SoundIdEntry"}),": Holds identifiers for specific sounds."]}),"\n",(0,i.jsxs)(n.li,{children:[(0,i.jsx)(n.strong,{children:"SoundSourceEntry"}),": Deals with the sources of sound emissions."]}),"\n"]}),"\n",(0,i.jsx)(n.h3,{id:"2-triggerentry",children:"2. TriggerEntry"}),"\n",(0,i.jsxs)(n.ul,{children:["\n",(0,i.jsxs)(n.li,{children:[(0,i.jsx)(n.strong,{children:"EventEntry"}),": A base for entries that are event-driven.","\n",(0,i.jsxs)(n.ul,{children:["\n",(0,i.jsxs)(n.li,{children:[(0,i.jsx)(n.strong,{children:"CustomCommandEntry"}),": Extends EventEntry to allow for the creation of custom in-game commands."]}),"\n"]}),"\n"]}),"\n"]}),"\n",(0,i.jsx)(n.h4,{id:"2a-triggerableentry-an-extension-of-triggerentry",children:"2a. TriggerableEntry (an extension of TriggerEntry)"}),"\n",(0,i.jsx)(n.p,{children:"These are entries that can be triggered by other entries. They are the most common type of entry, and are used for creating interactive sequences."}),"\n",(0,i.jsxs)(n.ul,{children:["\n",(0,i.jsxs)(n.li,{children:[(0,i.jsx)(n.strong,{children:"DialogueEntry"}),": Specialized for dialogues with specific speakers, enhancing NPC interactions."]}),"\n",(0,i.jsxs)(n.li,{children:[(0,i.jsx)(n.strong,{children:"ActionEntry"}),": Executes actions based on player interactions, capable of modifying facts or triggering events.","\n",(0,i.jsxs)(n.ul,{children:["\n",(0,i.jsxs)(n.li,{children:[(0,i.jsx)(n.strong,{children:"CustomTriggeringActionEntry"}),": A variant of ActionEntry, allowing for custom trigger mechanisms and manual triggering of actions."]}),"\n"]}),"\n"]}),"\n"]}),"\n",(0,i.jsx)(n.h3,{id:"3-cinematicentry",children:"3. CinematicEntry"}),"\n",(0,i.jsxs)(n.ul,{children:["\n",(0,i.jsx)(n.li,{children:"Primarily used for crafting cinematic experiences in-game, this base interface doesn't have listed specialized interfaces, but it's pivotal for creating story-driven, visually dynamic sequences."}),"\n"]}),"\n",(0,i.jsx)(n.h3,{id:"4-manifestentry",children:"4. ManifestEntry"}),"\n",(0,i.jsxs)(n.p,{children:["The biggest ",(0,i.jsx)(n.code,{children:"ManifestEntry"})," is the ",(0,i.jsx)(n.code,{children:"AudienceEntry"}),".\nIt is used to define audiences that can be used to show stateful content to players.\nAlmost all other ",(0,i.jsx)(n.code,{children:"ManifestEntry"})," are sub-types of ",(0,i.jsx)(n.code,{children:"AudienceEntry"}),".\nThough this is not required.\nAn example of an entry that is not a sub-type of ",(0,i.jsx)(n.code,{children:"AudienceEntry"})," is the ",(0,i.jsx)(n.code,{children:"EntityDefinitionEntry"}),"."]}),"\n",(0,i.jsxs)(n.p,{children:["The following are the sub-types of ",(0,i.jsx)(n.code,{children:"AudienceEntry"}),":"]}),"\n",(0,i.jsxs)(n.ul,{children:["\n",(0,i.jsxs)(n.li,{children:[(0,i.jsx)(n.strong,{children:"AudienceFilterEntry"}),": A variant of AudienceEntry that filters the audience passed to the children of the entry."]}),"\n",(0,i.jsxs)(n.li,{children:[(0,i.jsx)(n.strong,{children:"QuestEntry"}),": Specifies quest types."]}),"\n",(0,i.jsxs)(n.li,{children:[(0,i.jsx)(n.strong,{children:"ObjectiveEntry"}),": Base for objectives in quests."]}),"\n",(0,i.jsxs)(n.li,{children:[(0,i.jsx)(n.strong,{children:"LinesEntry"}),": Displays lines of text in things like sidebars or tablist."]}),"\n"]}),"\n",(0,i.jsxs)(n.p,{children:["A whole nother category of ",(0,i.jsx)(n.code,{children:"ManifestEntry"})," is meant for ",(0,i.jsx)(n.strong,{children:"Entities"}),".\nThis has it's seperate page in the documentation."]}),"\n",(0,i.jsx)(n.h2,{id:"implementation-and-usage",children:"Implementation and Usage"}),"\n",(0,i.jsx)(n.p,{children:"Each interface is designed with specific tags and methods to facilitate unique interactions within the TypeWriter plugin.\nImplementing these interfaces allows developers to craft a wide range of player experiences, from simple static displays to complex, multi-step interactive quests and dialogues."}),"\n",(0,i.jsx)(n.p,{children:"For instance, a TriggerableEntry can be used to set up a quest that only activates under certain conditions, while a DialogueEntry can bring an NPC to life with personalized dialogues.\nSimilarly, an ActionEntry can be used to create dynamic effects that change based on player actions, and a CinematicEntry can be used to create a visually dynamic story sequence."}),"\n",(0,i.jsx)(n.h3,{id:"defining-a-entry",children:"Defining a Entry"}),"\n",(0,i.jsx)(n.p,{children:"Typewriter takes care of the heavy lifting when it comes to creating and using entries.\nDevelopers only need to define the entry's class and its fields (and sometimes additional methods). The rest is handled by Typewriter. From scanning the adapters jar file for all the different entry classes to triggering them."}),"\n",(0,i.jsx)(n.p,{children:"To define an entry, it needs to meet the following requirements:"}),"\n",(0,i.jsxs)(n.ol,{children:["\n",(0,i.jsx)(n.li,{children:"It must be a class that implements one of the base entry interfaces."}),"\n",(0,i.jsx)(n.li,{children:"It must have a no-args constructor."}),"\n",(0,i.jsxs)(n.li,{children:["It must have a ",(0,i.jsx)(n.code,{children:"@Entry"})," annotation with all the required fields."]}),"\n"]}),"\n",(0,i.jsxs)(n.p,{children:["The ",(0,i.jsx)(n.code,{children:"@Entry"})," annotation is used to define the entry's type, name, and other properties. It has the following fields:"]}),"\n",(0,i.jsxs)(n.ul,{children:["\n",(0,i.jsxs)(n.li,{children:[(0,i.jsx)(n.strong,{children:"name"}),": The name of the entry. This is used to identify the entry."]}),"\n",(0,i.jsxs)(n.li,{children:[(0,i.jsx)(n.strong,{children:"description"}),": A short description of the entry."]}),"\n",(0,i.jsxs)(n.li,{children:[(0,i.jsx)(n.strong,{children:"color"}),": The color of the entry in the editor. It can be one from the ",(0,i.jsx)(n.code,{children:"Colors"})," class or a hex color code string."]}),"\n",(0,i.jsxs)(n.li,{children:[(0,i.jsx)(n.strong,{children:"icon"}),": The icon of the entry in the editor. All available icons can be found on ",(0,i.jsx)(n.a,{href:"https://icones.js.org/",children:"Iconify"}),". It needs to be the name of the icon."]}),"\n"]}),"\n",(0,i.jsx)(n.p,{children:"To find out specific requirements for each entry type, check the documentation for the entry's interface."}),"\n",(0,i.jsx)(n.admonition,{type:"caution",children:(0,i.jsxs)(n.p,{children:["Enties are not allowed to have any state. This means that there can't be any fields that are not final.\nIf you need to have state, use a ",(0,i.jsx)(n.code,{children:"AudienceEntry"}),"."]})}),"\n",(0,i.jsx)(n.hr,{}),"\n",(0,i.jsx)(n.p,{children:"In summary, these entry interfaces form the backbone of the TypeWriter plugin's functionality, offering a robust framework for creating immersive and interactive content within Minecraft.\nBy understanding and utilizing these interfaces, developers can greatly enhance the player experience, making it more engaging and dynamic."})]})}function h(e={}){const{wrapper:n}={...(0,s.R)(),...e.components};return n?(0,i.jsx)(n,{...e,children:(0,i.jsx)(d,{...e})}):d(e)}}}]);