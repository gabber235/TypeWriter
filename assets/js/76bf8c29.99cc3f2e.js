"use strict";(self.webpackChunkdocumentation=self.webpackChunkdocumentation||[]).push([["43967"],{98703:function(e,i,t){t.r(i),t.d(i,{metadata:()=>a,contentTitle:()=>o,default:()=>u,assets:()=>c,toc:()=>h,frontMatter:()=>l});var a=JSON.parse('{"id":"adapters/EntityAdapter/entries/activity/in_dialogue_activity","title":"In Dialogue Activity","description":"The InDialogueActivityEntry is an activity that activates child activities when a player is in a dialogue with the NPC.","source":"@site/versioned_docs/version-0.5.1/adapters/EntityAdapter/entries/activity/in_dialogue_activity.mdx","sourceDirName":"adapters/EntityAdapter/entries/activity","slug":"/adapters/EntityAdapter/entries/activity/in_dialogue_activity","permalink":"/0.5.1/adapters/EntityAdapter/entries/activity/in_dialogue_activity","draft":false,"unlisted":false,"editUrl":"https://github.com/gabber235/TypeWriter/tree/develop/documentation/versioned_docs/version-0.5.1/adapters/EntityAdapter/entries/activity/in_dialogue_activity.mdx","tags":[],"version":"0.5.1","lastUpdatedBy":"dependabot[bot]","lastUpdatedAt":1731173533000,"frontMatter":{},"sidebar":"adapters","previous":{"title":"Game Time Activity","permalink":"/0.5.1/adapters/EntityAdapter/entries/activity/game_time_activity"},"next":{"title":"Look Close Activity","permalink":"/0.5.1/adapters/EntityAdapter/entries/activity/look_close_activity"}}'),n=t("85893"),d=t("50065"),r=t("656"),s=t("49270");t("31183");let l={},o="In Dialogue Activity",c={},h=[{value:"How could this be used?",id:"how-could-this-be-used",level:2},{value:"Fields",id:"fields",level:2}];function p(e){let i={code:"code",h1:"h1",h2:"h2",header:"header",p:"p",...(0,d.a)(),...e.components};return!r&&y("fields",!1),!r.EntryField&&y("fields.EntryField",!0),(0,n.jsxs)(n.Fragment,{children:[(0,n.jsx)(i.header,{children:(0,n.jsx)(i.h1,{id:"in-dialogue-activity",children:"In Dialogue Activity"})}),"\n",(0,n.jsxs)(i.p,{children:["The ",(0,n.jsx)(i.code,{children:"InDialogueActivityEntry"})," is an activity that activates child activities when a player is in a dialogue with the NPC."]}),"\n",(0,n.jsx)(i.p,{children:"The activity will only activate when the player is in a dialogue with the NPC."}),"\n",(0,n.jsx)(i.h2,{id:"how-could-this-be-used",children:"How could this be used?"}),"\n",(0,n.jsx)(i.p,{children:"This can be used to stop a npc from moving when a player is in a dialogue with it."}),"\n",(0,n.jsx)(i.h2,{id:"fields",children:"Fields"}),"\n",(0,n.jsxs)(r.EntryField,{name:"Dialogue Idle Duration",required:!0,duration:!0,children:[(0,n.jsx)(i.p,{children:"The duration a player can be idle in the same dialogue before the activity deactivates."}),(0,n.jsx)(i.p,{children:"When set to 0, it won't use the timer."}),(0,n.jsx)(s.Z,{type:"info",children:(0,n.jsx)(i.p,{children:"When the dialogue priority is higher than this activity's priority, this timer will be ignored.\nAnd will only exit when the dialogue is finished."})})]}),"\n",(0,n.jsx)(r.EntryField,{name:"Talking Activity",required:!0,children:(0,n.jsx)(i.p,{children:"The activity that will be used when the npc is in a dialogue"})}),"\n",(0,n.jsx)(r.EntryField,{name:"Idle Activity",required:!0,children:(0,n.jsx)(i.p,{children:"The activity that will be used when the npc is not in a dialogue"})})]})}function u(e={}){let{wrapper:i}={...(0,d.a)(),...e.components};return i?(0,n.jsx)(i,{...e,children:(0,n.jsx)(p,{...e})}):p(e)}function y(e,i){throw Error("Expected "+(i?"component":"object")+" `"+e+"` to be defined: you likely forgot to import, pass, or provide it.")}}}]);