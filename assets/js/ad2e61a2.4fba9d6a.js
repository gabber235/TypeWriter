"use strict";(self.webpackChunkdocumentation=self.webpackChunkdocumentation||[]).push([[7933],{99949:(e,t,r)=>{r.r(t),r.d(t,{assets:()=>a,contentTitle:()=>l,default:()=>h,frontMatter:()=>i,metadata:()=>s,toc:()=>c});let s=JSON.parse('{"id":"docs/troubleshooting/packetevent","title":"PacketEvents Troubleshooting","description":"If you encounter errors when enabling the Typewriter plugin, such as java.lang.NoClassDefFoundError related to io.github.retrooper.packetevents, this could be due to an incompatible version of the PacketEvents plugin.","source":"@site/versioned_docs/version-0.7.0/docs/06-troubleshooting/packetevent.mdx","sourceDirName":"docs/06-troubleshooting","slug":"/docs/troubleshooting/packetevent","permalink":"/docs/troubleshooting/packetevent","draft":false,"unlisted":false,"editUrl":"https://github.com/gabber235/TypeWriter/tree/develop/documentation/versioned_docs/version-0.7.0/docs/06-troubleshooting/packetevent.mdx","tags":[],"version":"0.7.0","lastUpdatedBy":"Marten Mrfc","lastUpdatedAt":1734273100000,"frontMatter":{"difficulty":"Normal"},"sidebar":"tutorialSidebar","previous":{"title":"Extensions Troubleshooting","permalink":"/docs/troubleshooting/extensions"},"next":{"title":"Playit.gg","permalink":"/docs/troubleshooting/playitgg"}}');var n=r(74848),o=r(28453);let i={difficulty:"Normal"},l="PacketEvents Troubleshooting",a={},c=[{value:"Error Message",id:"error-message",level:2},{value:"Cause of the Issue",id:"cause-of-the-issue",level:2},{value:"Steps to Fix",id:"steps-to-fix",level:3},{value:"Verifying the Fix",id:"verifying-the-fix",level:2}];function d(e){let t={a:"a",code:"code",h1:"h1",h2:"h2",h3:"h3",header:"header",li:"li",ol:"ol",p:"p",pre:"pre",strong:"strong",...(0,o.R)(),...e.components};return(0,n.jsxs)(n.Fragment,{children:[(0,n.jsx)(t.header,{children:(0,n.jsx)(t.h1,{id:"packetevents-troubleshooting",children:"PacketEvents Troubleshooting"})}),"\n",(0,n.jsxs)(t.p,{children:["If you encounter errors when enabling the Typewriter plugin, such as ",(0,n.jsx)(t.code,{children:"java.lang.NoClassDefFoundError"})," related to ",(0,n.jsx)(t.code,{children:"io.github.retrooper.packetevents"}),", this could be due to an incompatible version of the PacketEvents plugin."]}),"\n",(0,n.jsx)(t.h2,{id:"error-message",children:"Error Message"}),"\n",(0,n.jsx)(t.p,{children:"An example of the error message you might see:"}),"\n",(0,n.jsx)(t.pre,{children:(0,n.jsx)(t.code,{className:"language-bash",children:"[12:41:54 ERROR]: Error occurred while enabling Typewriter v0.5.1 (Is it up to date?)\njava.lang.NoClassDefFoundError: io/github/retrooper/packetevents/bstats/Metrics$CustomChart\n        at me.gabber235.typewriter.entry.entity.EntityHandler.initialize(EntityHandler.kt:23) ~[typewriter (5).jar:?]\n        at me.gabber235.typewriter.Typewriter.onEnableAsync(Typewriter.kt:121) ~[typewriter (5).jar:?]\n"})}),"\n",(0,n.jsx)(t.h2,{id:"cause-of-the-issue",children:"Cause of the Issue"}),"\n",(0,n.jsxs)(t.p,{children:["This error occurs because the installed version of PacketEvents is not compatible with the version of Typewriter you are using. ",(0,n.jsx)(t.strong,{children:"Typewriter v0.5.1 requires PacketEvents version 2.5.x."})," Using a newer version of PacketEvents can cause compatibility issues due to changes in the API."]}),"\n",(0,n.jsx)(t.h3,{id:"steps-to-fix",children:"Steps to Fix"}),"\n",(0,n.jsxs)(t.ol,{children:["\n",(0,n.jsxs)(t.li,{children:["\n",(0,n.jsxs)(t.p,{children:[(0,n.jsx)(t.strong,{children:"Remove"})," the current PacketEvents plugin from your server's ",(0,n.jsx)(t.code,{children:"plugins"})," folder."]}),"\n"]}),"\n",(0,n.jsxs)(t.li,{children:["\n",(0,n.jsxs)(t.p,{children:[(0,n.jsx)(t.strong,{children:"Download"})," PacketEvents version ",(0,n.jsx)(t.strong,{children:"2.5.0"})," ",(0,n.jsx)(t.a,{href:"https://modrinth.com/plugin/packetevents/version/QLgJReg5",children:"via this url"}),"."]}),"\n"]}),"\n",(0,n.jsxs)(t.li,{children:["\n",(0,n.jsxs)(t.p,{children:[(0,n.jsx)(t.strong,{children:"Place"})," the downloaded PacketEvents jar into your server's ",(0,n.jsx)(t.code,{children:"plugins"})," folder."]}),"\n"]}),"\n",(0,n.jsxs)(t.li,{children:["\n",(0,n.jsxs)(t.p,{children:[(0,n.jsx)(t.strong,{children:"Restart"})," your server to apply the changes."]}),"\n"]}),"\n"]}),"\n",(0,n.jsx)(t.h2,{id:"verifying-the-fix",children:"Verifying the Fix"}),"\n",(0,n.jsxs)(t.p,{children:["After following the steps above, the Typewriter plugin should load without errors. Check your server logs to ensure there are no longer any ",(0,n.jsx)(t.code,{children:"NoClassDefFoundError"})," errors related to PacketEvents."]})]})}function h(e={}){let{wrapper:t}={...(0,o.R)(),...e.components};return t?(0,n.jsx)(t,{...e,children:(0,n.jsx)(d,{...e})}):d(e)}}}]);