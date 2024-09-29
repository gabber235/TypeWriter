"use strict";(self.webpackChunkdocumentation=self.webpackChunkdocumentation||[]).push([[24292],{66524:(e,t,r)=>{r.r(t),r.d(t,{assets:()=>l,contentTitle:()=>c,default:()=>o,frontMatter:()=>i,metadata:()=>n,toc:()=>a});var s=r(74848),d=r(28453);const i={difficulty:"Easy"},c="Commands",n={id:"docs/helpfull-features/commands",title:"Commands",description:"The TypeWriter plugin has some handy commands. Below is a table of these commands:",source:"@site/docs/docs/05-helpfull-features/02-commands.md",sourceDirName:"docs/05-helpfull-features",slug:"/docs/helpfull-features/commands",permalink:"/beta/docs/helpfull-features/commands",draft:!1,unlisted:!1,editUrl:"https://github.com/gabber235/TypeWriter/tree/develop/documentation/docs/docs/05-helpfull-features/02-commands.md",tags:[],version:"current",lastUpdatedBy:"Gabber235",lastUpdatedAt:1727613594e3,sidebarPosition:2,frontMatter:{difficulty:"Easy"},sidebar:"tutorialSidebar",previous:{title:"Chapters",permalink:"/beta/docs/helpfull-features/chapters"},next:{title:"PlaceHolderAPI",permalink:"/beta/docs/helpfull-features/placeholderapi"}},l={},a=[];function h(e){const t={admonition:"admonition",br:"br",code:"code",h1:"h1",header:"header",p:"p",table:"table",tbody:"tbody",td:"td",th:"th",thead:"thead",tr:"tr",...(0,d.R)(),...e.components};return(0,s.jsxs)(s.Fragment,{children:[(0,s.jsx)(t.header,{children:(0,s.jsx)(t.h1,{id:"commands",children:"Commands"})}),"\n",(0,s.jsx)(t.p,{children:"The TypeWriter plugin has some handy commands. Below is a table of these commands:"}),"\n",(0,s.jsx)(t.admonition,{title:"Types",type:"tip",children:(0,s.jsxs)(t.p,{children:["Some arguments are optional ",(0,s.jsx)(t.code,{children:"[]"})," and some a required ",(0,s.jsx)(t.code,{children:"<>"}),".",(0,s.jsx)(t.br,{}),"\n","An entry argument accepts the entry identifier and the entry name."]})}),"\n",(0,s.jsxs)(t.table,{children:[(0,s.jsx)(t.thead,{children:(0,s.jsxs)(t.tr,{children:[(0,s.jsx)(t.th,{children:"Command Name"}),(0,s.jsx)(t.th,{children:"Description"}),(0,s.jsx)(t.th,{children:"Permissions"})]})}),(0,s.jsxs)(t.tbody,{children:[(0,s.jsxs)(t.tr,{children:[(0,s.jsx)(t.td,{children:(0,s.jsx)(t.code,{children:"/tw connect"})}),(0,s.jsx)(t.td,{children:"Connects you to the typewriter panel."}),(0,s.jsx)(t.td,{children:"typewriter.connect"})]}),(0,s.jsxs)(t.tr,{children:[(0,s.jsx)(t.td,{children:(0,s.jsx)(t.code,{children:"/tw clearChat"})}),(0,s.jsx)(t.td,{children:"Clears the chat for you in the way typewriter does it."}),(0,s.jsx)(t.td,{children:"typewriter.clearChat"})]}),(0,s.jsxs)(t.tr,{children:[(0,s.jsx)(t.td,{children:(0,s.jsx)(t.code,{children:"/tw cinematic <start/stop> <pageName> [player]"})}),(0,s.jsx)(t.td,{children:"plays the chosen cinematic for a specific player."}),(0,s.jsx)(t.td,{children:"typewriter.cinematic.start/stop"})]}),(0,s.jsxs)(t.tr,{children:[(0,s.jsx)(t.td,{children:(0,s.jsx)(t.code,{children:"/tw reload"})}),(0,s.jsx)(t.td,{children:"Reloads the plugin."}),(0,s.jsx)(t.td,{children:"typewriter.reload"})]}),(0,s.jsxs)(t.tr,{children:[(0,s.jsx)(t.td,{children:(0,s.jsx)(t.code,{children:"/tw facts [player]"})}),(0,s.jsx)(t.td,{children:"Gets all the facts of a specific player."}),(0,s.jsx)(t.td,{children:"typewriter.facts"})]}),(0,s.jsxs)(t.tr,{children:[(0,s.jsx)(t.td,{children:(0,s.jsx)(t.code,{children:"/tw facts set <factEntry> <value> [player]"})}),(0,s.jsx)(t.td,{children:"Sets a fact of a specific player"}),(0,s.jsx)(t.td,{children:"typewriter.facts.set"})]}),(0,s.jsxs)(t.tr,{children:[(0,s.jsx)(t.td,{children:(0,s.jsx)(t.code,{children:"/tw facts reset"})}),(0,s.jsx)(t.td,{children:"Resets all the facts of a specific player"}),(0,s.jsx)(t.td,{children:"typewriter.facts.reset"})]}),(0,s.jsxs)(t.tr,{children:[(0,s.jsx)(t.td,{children:(0,s.jsx)(t.code,{children:"/tw trigger <entry> [player]"})}),(0,s.jsx)(t.td,{children:"triggers an entry for a specific player."}),(0,s.jsx)(t.td,{children:"typewriter.trigger"})]}),(0,s.jsxs)(t.tr,{children:[(0,s.jsx)(t.td,{children:(0,s.jsx)(t.code,{children:"/tw assets clean"})}),(0,s.jsx)(t.td,{children:"Clears all assets that are not used."}),(0,s.jsx)(t.td,{children:"typewriter.assets.clean"})]}),(0,s.jsxs)(t.tr,{children:[(0,s.jsx)(t.td,{children:(0,s.jsx)(t.code,{children:"/tw fire <entry> [player]"})}),(0,s.jsx)(t.td,{children:"trigger a fire trigger event entry."}),(0,s.jsx)(t.td,{children:"typewriter.fire"})]}),(0,s.jsxs)(t.tr,{children:[(0,s.jsx)(t.td,{children:(0,s.jsx)(t.code,{children:"/tw manifest inspect [player]"})}),(0,s.jsx)(t.td,{children:"Inspect the active manifests of a player"}),(0,s.jsx)(t.td,{children:"typewriter.manifest.inspect"})]}),(0,s.jsxs)(t.tr,{children:[(0,s.jsx)(t.td,{children:(0,s.jsx)(t.code,{children:"/tw quest track <questEntry> [player]"})}),(0,s.jsx)(t.td,{children:"Start following a quest for a player."}),(0,s.jsx)(t.td,{children:"typewriter.quest.track"})]}),(0,s.jsxs)(t.tr,{children:[(0,s.jsx)(t.td,{children:(0,s.jsx)(t.code,{children:"/tw untrack [player]"})}),(0,s.jsx)(t.td,{children:"Unfollow the quest of a player."}),(0,s.jsx)(t.td,{children:"typewriter.quest.untrack"})]}),(0,s.jsxs)(t.tr,{children:[(0,s.jsx)(t.td,{children:(0,s.jsx)(t.code,{children:"/tw roadNetwork edit <roadNetworkEntry>"})}),(0,s.jsx)(t.td,{children:"Edit a roadNetwork."}),(0,s.jsx)(t.td,{children:"typewriter.roadNetwork.edit"})]})]})]})]})}function o(e={}){const{wrapper:t}={...(0,d.R)(),...e.components};return t?(0,s.jsx)(t,{...e,children:(0,s.jsx)(h,{...e})}):h(e)}}}]);