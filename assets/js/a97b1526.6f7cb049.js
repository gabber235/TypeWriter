"use strict";(self.webpackChunkdocumentation=self.webpackChunkdocumentation||[]).push([[59928],{68130:(e,a,t)=>{t.r(a),t.d(a,{assets:()=>c,contentTitle:()=>n,default:()=>p,frontMatter:()=>s,metadata:()=>i,toc:()=>o});var d=t(74848),r=t(28453),l=t(50494);t(6178),t(14783);const s={},n="Value Placeholder Fact",i={id:"adapters/BasicAdapter/entries/fact/value_placeholder",title:"Value Placeholder Fact",description:"A fact that is computed from a placeholder.",source:"@site/versioned_docs/version-0.4.2/adapters/BasicAdapter/entries/fact/value_placeholder.mdx",sourceDirName:"adapters/BasicAdapter/entries/fact",slug:"/adapters/BasicAdapter/entries/fact/value_placeholder",permalink:"/0.4.2/adapters/BasicAdapter/entries/fact/value_placeholder",draft:!1,unlisted:!1,editUrl:"https://github.com/gabber235/TypeWriter/tree/develop/documentation/versioned_docs/version-0.4.2/adapters/BasicAdapter/entries/fact/value_placeholder.mdx",tags:[],version:"0.4.2",lastUpdatedBy:"Gabber235",lastUpdatedAt:17263361e5,frontMatter:{},sidebar:"adapters",previous:{title:"Timed Fact",permalink:"/0.4.2/adapters/BasicAdapter/entries/fact/timed_fact"},next:{title:"Custom Sound",permalink:"/0.4.2/adapters/BasicAdapter/entries/sound/custom_sound"}},c={},o=[{value:"How could this be used?",id:"how-could-this-be-used",level:2},{value:"Fields",id:"fields",level:2}];function h(e){const a={a:"a",code:"code",h1:"h1",h2:"h2",header:"header",p:"p",pre:"pre",...(0,r.R)(),...e.components};return l||u("fields",!1),l.CommentField||u("fields.CommentField",!0),l.EntryField||u("fields.EntryField",!0),l.ReadonlyFactInfo||u("fields.ReadonlyFactInfo",!0),(0,d.jsxs)(d.Fragment,{children:[(0,d.jsx)(a.header,{children:(0,d.jsx)(a.h1,{id:"value-placeholder-fact",children:"Value Placeholder Fact"})}),"\n",(0,d.jsxs)(a.p,{children:["A ",(0,d.jsx)(a.a,{href:"/docs/creating-stories/facts",children:"fact"})," that is computed from a placeholder.\nThis placeholder is evaluated when the fact is read and can return anything.\nThe value will be computed based on the ",(0,d.jsx)(a.code,{children:"values"})," specified."]}),"\n",(0,d.jsx)(l.ReadonlyFactInfo,{}),"\n",(0,d.jsx)(a.h2,{id:"how-could-this-be-used",children:"How could this be used?"}),"\n",(0,d.jsx)(a.p,{children:"If you only want to run certain actions if the player is in creative mode.\nOr depending on the weather, change the dialogue of the NPC."}),"\n",(0,d.jsx)(a.h2,{id:"fields",children:"Fields"}),"\n",(0,d.jsx)(l.CommentField,{}),"\n",(0,d.jsx)(l.EntryField,{name:"Placeholder",required:!0,placeholders:!0,children:(0,d.jsx)(a.p,{children:"Placeholder to parse (e.g. %player_gamemode%"})}),"\n",(0,d.jsxs)(l.EntryField,{name:"Values",required:!0,map:!0,regex:!0,children:[(0,d.jsx)(a.p,{children:"The values to match the placeholder with and their corresponding fact value."}),(0,d.jsx)(a.p,{children:"An example would be:"}),(0,d.jsx)(a.pre,{children:(0,d.jsx)(a.code,{className:"language-yaml",children:"values:\nSURVIVAL: 0\nCREATIVE: 1\nADVENTURE: 2\nSPECTATOR: 3\n"})}),(0,d.jsxs)(a.p,{children:["If the placeholder returns ",(0,d.jsx)(a.code,{children:"CREATIVE"}),", the fact value will be ",(0,d.jsx)(a.code,{children:"1"}),"."]}),(0,d.jsxs)(a.p,{children:["If no value matches, the fact value will be ",(0,d.jsx)(a.code,{children:"0"}),"."]})]})]})}function p(e={}){const{wrapper:a}={...(0,r.R)(),...e.components};return a?(0,d.jsx)(a,{...e,children:(0,d.jsx)(h,{...e})}):h(e)}function u(e,a){throw new Error("Expected "+(a?"component":"object")+" `"+e+"` to be defined: you likely forgot to import, pass, or provide it.")}}}]);