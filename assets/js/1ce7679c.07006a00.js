"use strict";(self.webpackChunkdocumentation=self.webpackChunkdocumentation||[]).push([[89650],{75210:(e,t,i)=>{i.r(t),i.d(t,{assets:()=>c,contentTitle:()=>d,default:()=>h,frontMatter:()=>s,metadata:()=>l,toc:()=>o});var a=i(74848),r=i(28453),n=i(50494);i(6178),i(14783);const s={},d="Player Close By Activity",l={id:"adapters/EntityAdapter/entries/activity/player_close_by_activity",title:"Player Close By Activity",description:"The PlayerCloseByActivityEntry is an activity that activates child activities when a viewer is close by.",source:"@site/versioned_docs/version-0.5.0/adapters/EntityAdapter/entries/activity/player_close_by_activity.mdx",sourceDirName:"adapters/EntityAdapter/entries/activity",slug:"/adapters/EntityAdapter/entries/activity/player_close_by_activity",permalink:"/adapters/EntityAdapter/entries/activity/player_close_by_activity",draft:!1,unlisted:!1,editUrl:"https://github.com/gabber235/TypeWriter/tree/develop/documentation/versioned_docs/version-0.5.0/adapters/EntityAdapter/entries/activity/player_close_by_activity.mdx",tags:[],version:"0.5.0",lastUpdatedBy:"Gabber235",lastUpdatedAt:17263361e5,frontMatter:{},sidebar:"adapters",previous:{title:"Patrol Activity",permalink:"/adapters/EntityAdapter/entries/activity/patrol_activity"},next:{title:"Random Look Activity",permalink:"/adapters/EntityAdapter/entries/activity/random_look_activity"}},c={},o=[{value:"How could this be used?",id:"how-could-this-be-used",level:2},{value:"Fields",id:"fields",level:2}];function y(e){const t={code:"code",h1:"h1",h2:"h2",header:"header",p:"p",...(0,r.R)(),...e.components};return n||p("fields",!1),n.EntryField||p("fields.EntryField",!0),(0,a.jsxs)(a.Fragment,{children:[(0,a.jsx)(t.header,{children:(0,a.jsx)(t.h1,{id:"player-close-by-activity",children:"Player Close By Activity"})}),"\n",(0,a.jsxs)(t.p,{children:["The ",(0,a.jsx)(t.code,{children:"PlayerCloseByActivityEntry"})," is an activity that activates child activities when a viewer is close by."]}),"\n",(0,a.jsx)(t.p,{children:"The activity will only activate when the viewer is within the defined range."}),"\n",(0,a.jsx)(t.p,{children:"When the maximum idle duration is reached, the activity will deactivate.\nIf the maximum idle duration is set to 0, then it won't use the timer."}),"\n",(0,a.jsx)(t.h2,{id:"how-could-this-be-used",children:"How could this be used?"}),"\n",(0,a.jsx)(t.p,{children:"When the player has to follow the NPC and walks away, let the NPC wander around (or stand still) around the point the player walked away. When the player returns, resume its path."}),"\n",(0,a.jsx)(t.p,{children:"When the npc is walking, and a player comes in range Stand still."}),"\n",(0,a.jsx)(t.h2,{id:"fields",children:"Fields"}),"\n",(0,a.jsx)(n.EntryField,{name:"Range",required:!0,children:(0,a.jsx)(t.p,{children:"The range in which the player has to be close by to activate the activity."})}),"\n",(0,a.jsx)(n.EntryField,{name:"Max Idle Duration",required:!0,duration:!0,children:(0,a.jsx)(t.p,{children:"The maximum duration a player can be idle in the same range before the activity deactivates."})}),"\n",(0,a.jsx)(n.EntryField,{name:"Close By Activity",required:!0,children:(0,a.jsx)(t.p,{children:"The activity that will be used when there is a player close by."})}),"\n",(0,a.jsx)(n.EntryField,{name:"Idle Activity",required:!0,children:(0,a.jsx)(t.p,{children:"The activity that will be used when there is no player close by."})})]})}function h(e={}){const{wrapper:t}={...(0,r.R)(),...e.components};return t?(0,a.jsx)(t,{...e,children:(0,a.jsx)(y,{...e})}):y(e)}function p(e,t){throw new Error("Expected "+(t?"component":"object")+" `"+e+"` to be defined: you likely forgot to import, pass, or provide it.")}}}]);