"use strict";(self.webpackChunkdocumentation=self.webpackChunkdocumentation||[]).push([[73201],{63217:(e,i,t)=>{t.r(i),t.d(i,{assets:()=>o,contentTitle:()=>l,default:()=>p,frontMatter:()=>s,metadata:()=>c,toc:()=>h});var a=t(74848),n=t(28453),d=t(50494),r=t(6178);t(14783);const s={},l="In Dialogue Activity",c={id:"adapters/EntityAdapter/entries/activity/in_dialogue_activity",title:"In Dialogue Activity",description:"The InDialogueActivityEntry is an activity that activates child activities when a player is in a dialogue with the NPC.",source:"@site/docs/adapters/EntityAdapter/entries/activity/in_dialogue_activity.mdx",sourceDirName:"adapters/EntityAdapter/entries/activity",slug:"/adapters/EntityAdapter/entries/activity/in_dialogue_activity",permalink:"/beta/adapters/EntityAdapter/entries/activity/in_dialogue_activity",draft:!1,unlisted:!1,editUrl:"https://github.com/gabber235/TypeWriter/tree/develop/documentation/docs/adapters/EntityAdapter/entries/activity/in_dialogue_activity.mdx",tags:[],version:"current",lastUpdatedBy:"Gabber235",lastUpdatedAt:17263361e5,frontMatter:{},sidebar:"adapters",previous:{title:"Game Time Activity",permalink:"/beta/adapters/EntityAdapter/entries/activity/game_time_activity"},next:{title:"Look Close Activity",permalink:"/beta/adapters/EntityAdapter/entries/activity/look_close_activity"}},o={},h=[{value:"How could this be used?",id:"how-could-this-be-used",level:2},{value:"Fields",id:"fields",level:2}];function u(e){const i={code:"code",h1:"h1",h2:"h2",header:"header",p:"p",...(0,n.R)(),...e.components};return d||y("fields",!1),d.EntryField||y("fields.EntryField",!0),(0,a.jsxs)(a.Fragment,{children:[(0,a.jsx)(i.header,{children:(0,a.jsx)(i.h1,{id:"in-dialogue-activity",children:"In Dialogue Activity"})}),"\n",(0,a.jsxs)(i.p,{children:["The ",(0,a.jsx)(i.code,{children:"InDialogueActivityEntry"})," is an activity that activates child activities when a player is in a dialogue with the NPC."]}),"\n",(0,a.jsx)(i.p,{children:"The activity will only activate when the player is in a dialogue with the NPC."}),"\n",(0,a.jsx)(i.h2,{id:"how-could-this-be-used",children:"How could this be used?"}),"\n",(0,a.jsx)(i.p,{children:"This can be used to stop a npc from moving when a player is in a dialogue with it."}),"\n",(0,a.jsx)(i.h2,{id:"fields",children:"Fields"}),"\n",(0,a.jsxs)(d.EntryField,{name:"Dialogue Idle Duration",required:!0,duration:!0,children:[(0,a.jsx)(i.p,{children:"The duration a player can be idle in the same dialogue before the activity deactivates."}),(0,a.jsx)(i.p,{children:"When set to 0, it won't use the timer."}),(0,a.jsx)(r.A,{type:"info",children:(0,a.jsx)(i.p,{children:"When the dialogue priority is higher than this activity's priority, this timer will be ignored.\nAnd will only exit when the dialogue is finished."})})]}),"\n",(0,a.jsx)(d.EntryField,{name:"Talking Activity",required:!0,children:(0,a.jsx)(i.p,{children:"The activity that will be used when the npc is in a dialogue"})}),"\n",(0,a.jsx)(d.EntryField,{name:"Idle Activity",required:!0,children:(0,a.jsx)(i.p,{children:"The activity that will be used when the npc is not in a dialogue"})})]})}function p(e={}){const{wrapper:i}={...(0,n.R)(),...e.components};return i?(0,a.jsx)(i,{...e,children:(0,a.jsx)(u,{...e})}):u(e)}function y(e,i){throw new Error("Expected "+(i?"component":"object")+" `"+e+"` to be defined: you likely forgot to import, pass, or provide it.")}}}]);