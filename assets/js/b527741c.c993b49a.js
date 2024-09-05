"use strict";(self.webpackChunkdocumentation=self.webpackChunkdocumentation||[]).push([[83768],{84411:(e,r,i)=>{i.r(r),i.d(r,{assets:()=>d,contentTitle:()=>a,default:()=>c,frontMatter:()=>s,metadata:()=>l,toc:()=>g});var t=i(74848),n=i(28453);const s={},a="Triggering Entries",l={id:"develop/adapters/triggering",title:"Triggering Entries",description:"There are easy ways to trigger all the next entries in a TriggerEntry.",source:"@site/versioned_docs/version-0.5.0/develop/02-adapters/05-triggering.mdx",sourceDirName:"develop/02-adapters",slug:"/develop/adapters/triggering",permalink:"/develop/adapters/triggering",draft:!1,unlisted:!1,editUrl:"https://github.com/gabber235/TypeWriter/tree/develop/documentation/versioned_docs/version-0.5.0/develop/02-adapters/05-triggering.mdx",tags:[],version:"0.5.0",lastUpdatedBy:"Marten Mrfc",lastUpdatedAt:172555283e4,sidebarPosition:5,frontMatter:{},sidebar:"develop",previous:{title:"Query Entries",permalink:"/develop/adapters/querying"},next:{title:"API Changes",permalink:"/develop/adapters/api-changes/"}},d={},g=[{value:"Custom triggers",id:"custom-triggers",level:2},{value:"SystemTrigger",id:"systemtrigger",level:3},{value:"CinematicStartTrigger",id:"cinematicstarttrigger",level:3}];function o(e){const r={admonition:"admonition",code:"code",h1:"h1",h2:"h2",h3:"h3",header:"header",li:"li",p:"p",pre:"pre",ul:"ul",...(0,n.R)(),...e.components};return(0,t.jsxs)(t.Fragment,{children:[(0,t.jsx)(r.header,{children:(0,t.jsx)(r.h1,{id:"triggering-entries",children:"Triggering Entries"})}),"\n",(0,t.jsxs)(r.p,{children:["There are easy ways to trigger all the next entries in a ",(0,t.jsx)(r.code,{children:"TriggerEntry"}),".\nThe most important is that you have a ",(0,t.jsx)(r.code,{children:"player"})," to trigger the entries on."]}),"\n",(0,t.jsx)(r.admonition,{type:"info",children:(0,t.jsx)(r.p,{children:"Typewriter takes care of only triggering the entries that should be triggered.\nIf criteria are not met, the entries are not triggered."})}),"\n",(0,t.jsxs)(r.p,{children:["If you have a single ",(0,t.jsx)(r.code,{children:"TriggerEntry"}),":"]}),"\n",(0,t.jsx)(r.pre,{children:(0,t.jsx)(r.code,{className:"language-kotlin",children:"val triggerEntry: TriggerEntry = ...\nval player: Player = ...\ntriggerEntry triggerAllFor player\n"})}),"\n",(0,t.jsxs)(r.p,{children:["If you have list of ",(0,t.jsx)(r.code,{children:"TriggerEntry"}),":"]}),"\n",(0,t.jsx)(r.pre,{children:(0,t.jsx)(r.code,{className:"language-kotlin",children:"val triggerEntries: List<TriggerEntry> = ...\nval player: Player = ...\ntriggerEntries triggerAllFor player\n"})}),"\n",(0,t.jsxs)(r.p,{children:["If you have a list of id's of ",(0,t.jsx)(r.code,{children:"TriggerEntry"}),":"]}),"\n",(0,t.jsx)(r.pre,{children:(0,t.jsx)(r.code,{className:"language-kotlin",children:"val triggerEntryIds: List<String> = ...\nval player: Player = ...\ntriggerEntryIds triggerEntriesFor player\n"})}),"\n",(0,t.jsxs)(r.p,{children:["Sometimes you don't want to trigger the entries when the player is in a dialogue.\nFor example, when the player is in a dialogue with a NPC, you don't want to trigger the first entry of the NPC again.\nYou expect when the player clicks on the NPC again, the next dialogue is triggered.\nTo facilitate this, you can use the ",(0,t.jsx)(r.code,{children:"startDialogueWithOrNextDialogue"})," function."]}),"\n",(0,t.jsx)(r.pre,{children:(0,t.jsx)(r.code,{className:"language-kotlin",children:"val triggerEntries: List<TriggerEntry> = ...\nval player: Player = ...\ntriggerEntries startDialogueWithOrNextDialogue player\n"})}),"\n",(0,t.jsx)(r.p,{children:"Or if you want to trigger something completely different when the player is in a dialogue:"}),"\n",(0,t.jsx)(r.pre,{children:(0,t.jsx)(r.code,{className:"language-kotlin",children:"val triggerEntries: List<TriggerEntry> = ...\nval player: Player = ...\nval customTrigger: EventTrigger = ...\ntriggerEntries.startDialogueWithOrTrigger(player, customTrigger)\n"})}),"\n",(0,t.jsx)(r.h2,{id:"custom-triggers",children:"Custom triggers"}),"\n",(0,t.jsxs)(r.p,{children:["Typewriter triggers based on the ",(0,t.jsx)(r.code,{children:"EventTrigger"})," interface.\nSo all the entries that are triggered are wrapped in a ",(0,t.jsx)(r.code,{children:"EntryTrigger"}),"."]}),"\n",(0,t.jsxs)(r.p,{children:["There are some triggers that are defined in Typewriter.\nThe two are ",(0,t.jsx)(r.code,{children:"SystemTrigger"})," and ",(0,t.jsx)(r.code,{children:"CinematicStartTrigger"}),"."]}),"\n",(0,t.jsx)(r.h3,{id:"systemtrigger",children:"SystemTrigger"}),"\n",(0,t.jsxs)(r.p,{children:[(0,t.jsx)(r.code,{children:"SystemTrigger"}),"'s can be used to indicate to either the ",(0,t.jsx)(r.code,{children:"DialogueSequence"})," or the ",(0,t.jsx)(r.code,{children:"CinematicSequence"})," that something needs to happen."]}),"\n",(0,t.jsxs)(r.ul,{children:["\n",(0,t.jsxs)(r.li,{children:[(0,t.jsx)(r.code,{children:"SystemTrigger.DIALOGUE_NEXT"})," indicates that the next dialogue should be triggered."]}),"\n",(0,t.jsxs)(r.li,{children:[(0,t.jsx)(r.code,{children:"SystemTrigger.DIALOGUE_END"})," indicates that the dialogue should end."]}),"\n",(0,t.jsxs)(r.li,{children:[(0,t.jsx)(r.code,{children:"SystemTrigger.CINEMATIC_END"})," indicates that the cinematic should end."]}),"\n"]}),"\n",(0,t.jsx)(r.h3,{id:"cinematicstarttrigger",children:"CinematicStartTrigger"}),"\n",(0,t.jsxs)(r.p,{children:[(0,t.jsx)(r.code,{children:"CinematicStartTrigger"}),"'s can be used to indicate to the ",(0,t.jsx)(r.code,{children:"CinematicSequence"})," that a cinematic should start."]}),"\n",(0,t.jsx)(r.p,{children:"It has several properties that can be set:"}),"\n",(0,t.jsxs)(r.ul,{children:["\n",(0,t.jsxs)(r.li,{children:[(0,t.jsx)(r.code,{children:"pageId: String"})," is the id of the cinematic page that should be shown. This is required."]}),"\n",(0,t.jsxs)(r.li,{children:[(0,t.jsx)(r.code,{children:"triggers: List<String>"})," is a list of trigger id's that should be triggered when the cinematic is finished. This is optional."]}),"\n",(0,t.jsxs)(r.li,{children:[(0,t.jsx)(r.code,{children:"override: Boolean"})," indicates if the cinematic should override the current cinematic. This is optional and defaults to ",(0,t.jsx)(r.code,{children:"false"}),"."]}),"\n",(0,t.jsxs)(r.li,{children:[(0,t.jsx)(r.code,{children:"simulate: Boolean"})," is used to run a cinematic for recording purposes. When this is enable it disables some entries from running. This is optional and defaults to ",(0,t.jsx)(r.code,{children:"false"}),"."]}),"\n",(0,t.jsxs)(r.li,{children:[(0,t.jsx)(r.code,{children:"ignoreEntries: List<String>"})," is a list of entry id's that should not be triggered. This is optional."]}),"\n",(0,t.jsxs)(r.li,{children:[(0,t.jsx)(r.code,{children:"minEndTime: Optional<Int>"})," is the minimum amount of frames the cinematic should run. If the cinematic is shorter than this, it will be extended. This is optional."]}),"\n"]})]})}function c(e={}){const{wrapper:r}={...(0,n.R)(),...e.components};return r?(0,t.jsx)(r,{...e,children:(0,t.jsx)(o,{...e})}):o(e)}}}]);