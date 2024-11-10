"use strict";(self.webpackChunkdocumentation=self.webpackChunkdocumentation||[]).push([["45602"],{26050:function(e,t,n){n.r(t),n.d(t,{metadata:()=>r,contentTitle:()=>l,default:()=>v,assets:()=>o,toc:()=>d,frontMatter:()=>a});var r=JSON.parse('{"id":"develop/adapters/entries/trigger/event","title":"EventEntry","description":"The EventEntry is used as a starting point for any sequence. It can have external event listeners listening to events and trigger based on that.","source":"@site/versioned_docs/version-0.4.2/develop/02-adapters/03-entries/trigger/event.mdx","sourceDirName":"develop/02-adapters/03-entries/trigger","slug":"/develop/adapters/entries/trigger/event","permalink":"/0.4.2/develop/adapters/entries/trigger/event","draft":false,"unlisted":false,"editUrl":"https://github.com/gabber235/TypeWriter/tree/develop/documentation/versioned_docs/version-0.4.2/develop/02-adapters/03-entries/trigger/event.mdx","tags":[],"version":"0.4.2","lastUpdatedBy":"Gabber235","lastUpdatedAt":1731262043000,"frontMatter":{},"sidebar":"develop","previous":{"title":"DialogueEntry","permalink":"/0.4.2/develop/adapters/entries/trigger/dialogue"},"next":{"title":"Query Entries","permalink":"/0.4.2/develop/adapters/querying"}}'),i=n("85893"),s=n("50065");let a={},l="EventEntry",o={},d=[{value:"Usage",id:"usage",level:2}];function p(e){let t={code:"code",h1:"h1",h2:"h2",header:"header",p:"p",pre:"pre",...(0,s.a)(),...e.components};return(0,i.jsxs)(i.Fragment,{children:[(0,i.jsx)(t.header,{children:(0,i.jsx)(t.h1,{id:"evententry",children:"EventEntry"})}),"\n",(0,i.jsxs)(t.p,{children:["The ",(0,i.jsx)(t.code,{children:"EventEntry"})," is used as a starting point for any sequence. It can have external event listeners listening to events and trigger based on that."]}),"\n",(0,i.jsx)(t.h2,{id:"usage",children:"Usage"}),"\n",(0,i.jsx)(t.pre,{children:(0,i.jsx)(t.code,{className:"language-kotlin",children:'@Entry("example_event", "An example event entry.", Colors.YELLOW, Icons.CIRCLE)\nclass ExampleEventEntry(\n    override val id: String,\n    override val name: String,\n    override val triggers: List<String> = emptyList(),\n): EventEntry\n\n// Must be scoped to be public\n@EntryListener(ExampleEventEntry::class)\nfun onEvent(event: SomeBukkitEvent, query: Query<ExampleEventEntry>) {\n    // Do something\n    val entries = query.find() // Find all the entries of this type, for more information see the Query section\n    // Do something with the entries, for example trigger them\n    entries triggerAllFor event.player\n}\n'})}),"\n",(0,i.jsxs)(t.p,{children:["If the function is not scoped to be public, it will not be registered as a listener.\nThe function will automatically be registered as a listener for the event by Typewriter and be called when the Bukkit event is trigger. An optional ",(0,i.jsx)(t.code,{children:"Query"})," parameter can be added to easily fetch all the different event entries."]})]})}function v(e={}){let{wrapper:t}={...(0,s.a)(),...e.components};return t?(0,i.jsx)(t,{...e,children:(0,i.jsx)(p,{...e})}):p(e)}}}]);