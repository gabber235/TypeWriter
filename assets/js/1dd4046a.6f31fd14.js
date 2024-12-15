"use strict";(self.webpackChunkdocumentation=self.webpackChunkdocumentation||[]).push([[51367],{81300:(e,n,i)=>{i.r(n),i.d(n,{assets:()=>l,contentTitle:()=>a,default:()=>h,frontMatter:()=>o,metadata:()=>t,toc:()=>c});var t=i(74595),s=i(74848),r=i(28453);let o={slug:"0.6.0-release",title:"Extension Update",authors:["gabber235"],tags:["release"]},a="Typewriter v0.6 \u2013 The Extensions Update",l={authorsImageUrls:[void 0]},c=[{value:"Critical Changes",id:"critical-changes",level:2},{value:"From Adapters to Extensions",id:"from-adapters-to-extensions",level:3},{value:"New Item System",id:"new-item-system",level:3},{value:"Important Features",id:"important-features",level:2},{value:"New Entries",id:"new-entries",level:2}];function d(e){let n={a:"a",br:"br",code:"code",h2:"h2",h3:"h3",li:"li",ol:"ol",p:"p",strong:"strong",ul:"ul",...(0,r.R)(),...e.components};return(0,s.jsxs)(s.Fragment,{children:[(0,s.jsx)(n.p,{children:"This release brings a foundational shift in Typewriter\u2019s core structure and a major update to the item system, along with new features and important usability improvements. Below are the highlights:"}),"\n","\n",(0,s.jsx)(n.h2,{id:"critical-changes",children:"Critical Changes"}),"\n",(0,s.jsx)(n.h3,{id:"from-adapters-to-extensions",children:"From Adapters to Extensions"}),"\n",(0,s.jsxs)(n.p,{children:["Typewriter has transitioned from using ",(0,s.jsx)(n.code,{children:"Adapters"})," to ",(0,s.jsx)(n.code,{children:"Extensions"}),", marking a significant evolution in how custom functionality is integrated. Key improvements with this change include:"]}),"\n",(0,s.jsxs)(n.ul,{children:["\n",(0,s.jsxs)(n.li,{children:[(0,s.jsx)(n.strong,{children:"Gradle Plugin for Extensions"}),": A new Gradle plugin simplifies the process of developing Extensions, making setup and management easier for developers."]}),"\n",(0,s.jsxs)(n.li,{children:[(0,s.jsx)(n.strong,{children:"Compile-Time Discovery"}),": Discovery of Extension internals, such as entries, now occurs at compile-time instead of runtime. This not only future-proofs Typewriter but also lays the groundwork for the upcoming marketplace, where entries in each Extension will be viewable without running the Extension."]}),"\n",(0,s.jsxs)(n.li,{children:[(0,s.jsx)(n.strong,{children:"Hot Reloading"}),": Extensions can now be reloaded on-the-fly using ",(0,s.jsx)(n.code,{children:"/tw reload"}),", enabling real-time updates without the need for a server restart."]}),"\n",(0,s.jsxs)(n.li,{children:[(0,s.jsx)(n.strong,{children:"Optimized Loading"}),": Only the necessary classes are loaded from Extensions. For example, if an Extension contains thousands of entries but only one is used, Typewriter loads only that specific entry, enhancing memory and processing efficiency."]}),"\n",(0,s.jsxs)(n.li,{children:[(0,s.jsx)(n.strong,{children:"Extension Validators"}),": Built-in validators now check Extensions against Typewriter\u2019s standards, helping to ensure reliability and consistency across user-created Extensions."]}),"\n"]}),"\n",(0,s.jsx)(n.h3,{id:"new-item-system",children:"New Item System"}),"\n",(0,s.jsxs)(n.p,{children:["Typewriter introduces a completely new item system, which is ",(0,s.jsx)(n.strong,{children:"incompatible with previously defined items"}),". Users will need to recreate items to align with the new structure. Based on user feedback, no migrator will be provided, as the majority found it unnecessary. ",(0,s.jsx)(n.strong,{children:"Please test this update on a development server before upgrading your production environment."})]}),"\n",(0,s.jsx)(n.h2,{id:"important-features",children:"Important Features"}),"\n",(0,s.jsxs)(n.ol,{children:["\n",(0,s.jsxs)(n.li,{children:["\n",(0,s.jsxs)(n.p,{children:[(0,s.jsx)(n.strong,{children:"Skip Cinematic Segments"}),(0,s.jsx)(n.br,{}),"\n","A new ",(0,s.jsx)(n.code,{children:"SkipCinematicEntry"})," gives players the option to skip sections of cinematics by pressing a configured key, offering more control over in-game cinematic experiences."]}),"\n"]}),"\n",(0,s.jsxs)(n.li,{children:["\n",(0,s.jsxs)(n.p,{children:[(0,s.jsx)(n.strong,{children:"UI and Web Panel Upgrades"}),(0,s.jsx)(n.br,{}),"\n","Improvements to the web panel and UI components provide a smoother experience. These upgrades include the ability to unselect Sound IDs, layout enhancements, compatibility improvements behind reverse proxies, and visual indicators for empty fields, all of which contribute to a more intuitive and flexible panel."]}),"\n"]}),"\n"]}),"\n",(0,s.jsx)(n.h2,{id:"new-entries",children:"New Entries"}),"\n",(0,s.jsxs)(n.ul,{children:["\n",(0,s.jsxs)(n.li,{children:[(0,s.jsx)(n.strong,{children:"Cinematic Entries"}),": ",(0,s.jsx)(n.code,{children:"GameTimeCinematicEntry"}),", ",(0,s.jsx)(n.code,{children:"WeatherCinematicEntry"}),", ",(0,s.jsx)(n.code,{children:"SkipCinematicEntry"}),", ",(0,s.jsx)(n.code,{children:"BlockCommandCinematicEntry"})]}),"\n",(0,s.jsxs)(n.li,{children:[(0,s.jsx)(n.strong,{children:"Dialogue"}),": ",(0,s.jsx)(n.code,{children:"ActionbarDialogueEntry"}),", ",(0,s.jsx)(n.code,{children:"SimpleMessageActionEntry"})]}),"\n",(0,s.jsxs)(n.li,{children:[(0,s.jsx)(n.strong,{children:"Entity and Activity Entries"}),": ",(0,s.jsx)(n.code,{children:"MythicMobKillPlayerEventEntry"}),", ",(0,s.jsx)(n.code,{children:"RemovePotionEffectActionEntry"}),", ",(0,s.jsx)(n.code,{children:"LookAtBlockActivity"}),", ",(0,s.jsx)(n.code,{children:"LookAtPitchYawActivity"}),", ",(0,s.jsx)(n.code,{children:"RandomPatrolActivity"}),", ",(0,s.jsx)(n.code,{children:"AmbientSoundActivity"}),", ",(0,s.jsx)(n.code,{children:"ScaleData"}),", ",(0,s.jsx)(n.code,{children:"InteractionEntity"}),", ",(0,s.jsx)(n.code,{children:"PillagerEntity"}),", ",(0,s.jsx)(n.code,{children:"VindicatorEntity"}),", ",(0,s.jsx)(n.code,{children:"Llama Entity"})]}),"\n",(0,s.jsxs)(n.li,{children:[(0,s.jsx)(n.strong,{children:"Miscellaneous"}),": ",(0,s.jsx)(n.code,{children:"WeatherAudienceEntry"}),", ",(0,s.jsx)(n.code,{children:"FireworkActionEntry"})," (with ",(0,s.jsx)(n.code,{children:"flight duration"})," setting)"]}),"\n"]}),"\n",(0,s.jsx)(n.p,{children:(0,s.jsxs)(n.strong,{children:["If you consider Typewriter to be valuable to your server, please consider ",(0,s.jsx)(n.a,{href:"https://github.com/sponsors/gabber235",children:"Sponsoring the Project"})]})})]})}function h(e={}){let{wrapper:n}={...(0,r.R)(),...e.components};return n?(0,s.jsx)(n,{...e,children:(0,s.jsx)(d,{...e})}):d(e)}},74595:e=>{e.exports=JSON.parse('{"permalink":"/devlog/0.6.0-release","source":"@site/devlog/releases/2024-09-11-0.6.0-release.mdx","title":"Extension Update","description":"This release brings a foundational shift in Typewriter\u2019s core structure and a major update to the item system, along with new features and important usability improvements. Below are the highlights:","date":"2024-09-11T00:00:00.000Z","tags":[{"inline":true,"label":"release","permalink":"/devlog/tags/release"}],"readingTime":2.01,"hasTruncateMarker":true,"authors":[{"name":"Gabber235","title":"TypeWriter Maintainer","url":"https://github.com/gabber235","imageURL":"https://github.com/gabber235.png","key":"gabber235","page":null}],"frontMatter":{"slug":"0.6.0-release","title":"Extension Update","authors":["gabber235"],"tags":["release"]},"unlisted":false,"nextItem":{"title":"Manifesting Stories Update","permalink":"/devlog/0.5.0-release"}}')}}]);