"use strict";(self.webpackChunkdocumentation=self.webpackChunkdocumentation||[]).push([[37744],{57043:(e,n,i)=>{i.r(n),i.d(n,{assets:()=>l,contentTitle:()=>o,default:()=>y,frontMatter:()=>d,metadata:()=>t,toc:()=>a});let t=JSON.parse('{"id":"develop/extensions/querying","title":"Query Entries","description":"Sometimes you need to find an entry by any of it\'s fields or by type. This can be done with the Query class.","source":"@site/versioned_docs/version-0.6.1/develop/02-extensions/05-querying.mdx","sourceDirName":"develop/02-extensions","slug":"/develop/extensions/querying","permalink":"/0.6.1/develop/extensions/querying","draft":false,"unlisted":false,"editUrl":"https://github.com/gabber235/TypeWriter/tree/develop/documentation/versioned_docs/version-0.6.1/develop/02-extensions/05-querying.mdx","tags":[],"version":"0.6.1","lastUpdatedBy":"Gabber235","lastUpdatedAt":1734557994000,"sidebarPosition":5,"frontMatter":{},"sidebar":"develop","previous":{"title":"EventEntry","permalink":"/0.6.1/develop/extensions/entries/trigger/event"},"next":{"title":"Triggering Entries","permalink":"/0.6.1/develop/extensions/triggering"}}');var r=i(74848),s=i(28453);let d={},o="Query Entries",l={},a=[];function c(e){let n={code:"code",h1:"h1",header:"header",p:"p",pre:"pre",...(0,s.R)(),...e.components};return(0,r.jsxs)(r.Fragment,{children:[(0,r.jsx)(n.header,{children:(0,r.jsx)(n.h1,{id:"query-entries",children:"Query Entries"})}),"\n",(0,r.jsxs)(n.p,{children:["Sometimes you need to find an entry by any of it's fields or by type. This can be done with the ",(0,r.jsx)(n.code,{children:"Query"})," class."]}),"\n",(0,r.jsx)(n.p,{children:"If you need to find all entries of a specific type:"}),"\n",(0,r.jsx)(n.pre,{children:(0,r.jsx)(n.code,{className:"language-kotlin",children:"val entries = Query.find<MyEntry>()\n"})}),"\n",(0,r.jsx)(n.p,{children:"Sometimes you need it by a specific criteria:"}),"\n",(0,r.jsx)(n.pre,{children:(0,r.jsx)(n.code,{className:"language-kotlin",children:'val entries = Query.findWhere<MyEntry> {\n    it.someField == "some value"\n}\n'})}),"\n",(0,r.jsx)(n.p,{children:"You can also find a single entry:"}),"\n",(0,r.jsx)(n.pre,{children:(0,r.jsx)(n.code,{className:"language-kotlin",children:'val entry = Query.findFirstWhere<MyEntry> {\n    it.someField == "some value"\n}\n'})}),"\n",(0,r.jsx)(n.p,{children:"Sometimes you need to find an entry by it's id:"}),"\n",(0,r.jsx)(n.pre,{children:(0,r.jsx)(n.code,{className:"language-kotlin",children:"val entry = Query.findById<MyEntry>(id)\n"})}),"\n",(0,r.jsx)(n.p,{children:"Other times you need to find entries by their page:"}),"\n",(0,r.jsx)(n.pre,{children:(0,r.jsx)(n.code,{className:"language-kotlin",children:'val entries = Query.findWhereFromPage<MyEntry>(pageId) {\n    it.someField == "some value"\n}\n'})})]})}function y(e={}){let{wrapper:n}={...(0,s.R)(),...e.components};return n?(0,r.jsx)(n,{...e,children:(0,r.jsx)(c,{...e})}):c(e)}}}]);