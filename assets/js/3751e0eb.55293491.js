"use strict";(self.webpackChunkdocumentation=self.webpackChunkdocumentation||[]).push([["18243"],{22863:function(e,t,i){i.r(t),i.d(t,{metadata:()=>a,contentTitle:()=>c,default:()=>p,assets:()=>o,toc:()=>l,frontMatter:()=>d});var a=JSON.parse('{"id":"adapters/BasicAdapter/entries/cinematic/camera_cinematic","title":"Camera Cinematic","description":"The Camera Cinematic entry is used to create a cinematic camera path.","source":"@site/versioned_docs/version-0.5.1/adapters/BasicAdapter/entries/cinematic/camera_cinematic.mdx","sourceDirName":"adapters/BasicAdapter/entries/cinematic","slug":"/adapters/BasicAdapter/entries/cinematic/camera_cinematic","permalink":"/0.5.1/adapters/BasicAdapter/entries/cinematic/camera_cinematic","draft":false,"unlisted":false,"editUrl":"https://github.com/gabber235/TypeWriter/tree/develop/documentation/versioned_docs/version-0.5.1/adapters/BasicAdapter/entries/cinematic/camera_cinematic.mdx","tags":[],"version":"0.5.1","lastUpdatedBy":"dependabot[bot]","lastUpdatedAt":1731173533000,"frontMatter":{},"sidebar":"adapters","previous":{"title":"Blinding Cinematic","permalink":"/0.5.1/adapters/BasicAdapter/entries/cinematic/blinding_cinematic"},"next":{"title":"Cinematic Console Command","permalink":"/0.5.1/adapters/BasicAdapter/entries/cinematic/cinematic_console_command"}}'),n=i("85893"),r=i("50065"),s=i("656");i("49270"),i("31183");let d={},c="Camera Cinematic",o={},l=[{value:"How could this be used?",id:"how-could-this-be-used",level:2},{value:"Fields",id:"fields",level:2}];function h(e){let t={code:"code",h1:"h1",h2:"h2",header:"header",p:"p",table:"table",tbody:"tbody",td:"td",th:"th",thead:"thead",tr:"tr",...(0,r.a)(),...e.components};return!s&&m("fields",!1),!s.CriteriaField&&m("fields.CriteriaField",!0),!s.EntryField&&m("fields.EntryField",!0),(0,n.jsxs)(n.Fragment,{children:[(0,n.jsx)(t.header,{children:(0,n.jsx)(t.h1,{id:"camera-cinematic",children:"Camera Cinematic"})}),"\n",(0,n.jsxs)(t.p,{children:["The ",(0,n.jsx)(t.code,{children:"Camera Cinematic"})," entry is used to create a cinematic camera path."]}),"\n",(0,n.jsx)(t.p,{children:"Durations for path points calculated based on the total duration of each segment and specified path point's duration.\nSuppose you have a segment with a duration of 300 ticks, and it has 3 path points.\nThen we specify the duration on the second path point as 200 ticks.\nThe resulting durations between path points are as follows:"}),"\n",(0,n.jsxs)(t.table,{children:[(0,n.jsx)(t.thead,{children:(0,n.jsxs)(t.tr,{children:[(0,n.jsx)(t.th,{children:"From"}),(0,n.jsx)(t.th,{children:"To"}),(0,n.jsx)(t.th,{children:"Duration"})]})}),(0,n.jsxs)(t.tbody,{children:[(0,n.jsxs)(t.tr,{children:[(0,n.jsx)(t.td,{children:"1"}),(0,n.jsx)(t.td,{children:"2"}),(0,n.jsx)(t.td,{children:"100"})]}),(0,n.jsxs)(t.tr,{children:[(0,n.jsx)(t.td,{children:"2"}),(0,n.jsx)(t.td,{children:"3"}),(0,n.jsx)(t.td,{children:"200"})]})]})]}),"\n",(0,n.jsxs)(t.p,{children:["::: warning\nSince the duration of a path point is the duration from that point to the next point,\nthe last path point will always have a duration of ",(0,n.jsx)(t.code,{children:"0"}),".\nRegardless of the duration specified on the last path point.\n:::"]}),"\n",(0,n.jsx)(t.h2,{id:"how-could-this-be-used",children:"How could this be used?"}),"\n",(0,n.jsx)(t.p,{children:"When you want to direct the player's attention to a specific object/location.\nOr when you want to show off a build."}),"\n",(0,n.jsx)(t.h2,{id:"fields",children:"Fields"}),"\n",(0,n.jsx)(s.CriteriaField,{}),"\n",(0,n.jsx)(s.EntryField,{name:"Segments",required:!0,multiple:!0,segment:!0})]})}function p(e={}){let{wrapper:t}={...(0,r.a)(),...e.components};return t?(0,n.jsx)(t,{...e,children:(0,n.jsx)(h,{...e})}):h(e)}function m(e,t){throw Error("Expected "+(t?"component":"object")+" `"+e+"` to be defined: you likely forgot to import, pass, or provide it.")}}}]);