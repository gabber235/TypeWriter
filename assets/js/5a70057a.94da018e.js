"use strict";(self.webpackChunkdocumentation=self.webpackChunkdocumentation||[]).push([["69039"],{86035:function(e,t,s){s.r(t),s.d(t,{metadata:()=>a,contentTitle:()=>c,default:()=>u,assets:()=>o,toc:()=>l,frontMatter:()=>n});var a=JSON.parse('{"id":"adapters/BasicAdapter/entries/fact/quest_status_fact","title":"Quest Status Fact","description":"The QuestStatusFact is a fact that returns the status of a specific quest.","source":"@site/versioned_docs/version-0.6.0/adapters/BasicAdapter/entries/fact/quest_status_fact.mdx","sourceDirName":"adapters/BasicAdapter/entries/fact","slug":"/adapters/BasicAdapter/entries/fact/quest_status_fact","permalink":"/adapters/BasicAdapter/entries/fact/quest_status_fact","draft":false,"unlisted":false,"editUrl":"https://github.com/gabber235/TypeWriter/tree/develop/documentation/versioned_docs/version-0.6.0/adapters/BasicAdapter/entries/fact/quest_status_fact.mdx","tags":[],"version":"0.6.0","lastUpdatedBy":"dependabot[bot]","lastUpdatedAt":1731173593000,"frontMatter":{},"sidebar":"adapters","previous":{"title":"Permanent Fact","permalink":"/adapters/BasicAdapter/entries/fact/permanent_fact"},"next":{"title":"Session Fact","permalink":"/adapters/BasicAdapter/entries/fact/session_fact"}}'),d=s("85893"),i=s("50065"),r=s("656");s("49270"),s("31183");let n={},c="Quest Status Fact",o={},l=[{value:"How could this be used?",id:"how-could-this-be-used",level:2},{value:"Fields",id:"fields",level:2}];function h(e){let t={admonition:"admonition",code:"code",h1:"h1",h2:"h2",header:"header",p:"p",strong:"strong",table:"table",tbody:"tbody",td:"td",th:"th",thead:"thead",tr:"tr",...(0,i.a)(),...e.components};return!r&&p("fields",!1),!r.CommentField&&p("fields.CommentField",!0),!r.EntryField&&p("fields.EntryField",!0),!r.ReadonlyFactInfo&&p("fields.ReadonlyFactInfo",!0),(0,d.jsxs)(d.Fragment,{children:[(0,d.jsx)(t.header,{children:(0,d.jsx)(t.h1,{id:"quest-status-fact",children:"Quest Status Fact"})}),"\n",(0,d.jsxs)(t.p,{children:["The ",(0,d.jsx)(t.code,{children:"QuestStatusFact"})," is a fact that returns the status of a specific quest."]}),"\n",(0,d.jsx)(r.ReadonlyFactInfo,{}),"\n",(0,d.jsxs)(t.table,{children:[(0,d.jsx)(t.thead,{children:(0,d.jsxs)(t.tr,{children:[(0,d.jsx)(t.th,{children:"Status"}),(0,d.jsx)(t.th,{children:"Value"})]})}),(0,d.jsxs)(t.tbody,{children:[(0,d.jsxs)(t.tr,{children:[(0,d.jsx)(t.td,{children:"Inactive"}),(0,d.jsx)(t.td,{children:"0"})]}),(0,d.jsxs)(t.tr,{children:[(0,d.jsx)(t.td,{children:"Active"}),(0,d.jsx)(t.td,{children:"1"})]}),(0,d.jsxs)(t.tr,{children:[(0,d.jsx)(t.td,{children:"Active and Tracked"}),(0,d.jsx)(t.td,{children:"2"})]}),(0,d.jsxs)(t.tr,{children:[(0,d.jsx)(t.td,{children:"Completed"}),(0,d.jsx)(t.td,{children:"-1"})]})]})]}),"\n",(0,d.jsx)(t.admonition,{type:"warning",children:(0,d.jsxs)(t.p,{children:["The ",(0,d.jsx)(t.strong,{children:"completed"})," status has a value of ",(0,d.jsx)(t.strong,{children:"-1"}),", this is to make it easy to query if the quest is active by ",(0,d.jsx)(t.code,{children:">= 1"}),"."]})}),"\n",(0,d.jsx)(t.h2,{id:"how-could-this-be-used",children:"How could this be used?"}),"\n",(0,d.jsx)(t.p,{children:"This can be used to abstract the status of a quest.\nIf a quest has multiple criteria, you can check for this fact instead of checking for each criterion."}),"\n",(0,d.jsx)(t.h2,{id:"fields",children:"Fields"}),"\n",(0,d.jsx)(r.CommentField,{}),"\n",(0,d.jsx)(r.EntryField,{name:"Group",required:!0}),"\n",(0,d.jsx)(r.EntryField,{name:"Quest",required:!0})]})}function u(e={}){let{wrapper:t}={...(0,i.a)(),...e.components};return t?(0,d.jsx)(t,{...e,children:(0,d.jsx)(h,{...e})}):h(e)}function p(e,t){throw Error("Expected "+(t?"component":"object")+" `"+e+"` to be defined: you likely forgot to import, pass, or provide it.")}}}]);