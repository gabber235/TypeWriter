(self.webpackChunkdocumentation=self.webpackChunkdocumentation||[]).push([[62645],{58565:(e,t,i)=>{"use strict";i.r(t),i.d(t,{assets:()=>l,contentTitle:()=>d,default:()=>m,frontMatter:()=>o,metadata:()=>n,toc:()=>h});let n=JSON.parse('{"id":"docs/first-cinematic","title":"First Cinematic","description":"This guide will lead you through making your first cinematic.","source":"@site/versioned_docs/version-0.4.2/docs/04-first-cinematic.mdx","sourceDirName":"docs","slug":"/docs/first-cinematic","permalink":"/0.4.2/docs/first-cinematic","draft":false,"unlisted":false,"editUrl":"https://github.com/gabber235/TypeWriter/tree/develop/documentation/versioned_docs/version-0.4.2/docs/04-first-cinematic.mdx","tags":[],"version":"0.4.2","lastUpdatedBy":"Marten Mrfc","lastUpdatedAt":1734273100000,"sidebarPosition":4,"frontMatter":{},"sidebar":"tutorialSidebar","previous":{"title":"First Interaction","permalink":"/0.4.2/docs/first-interaction"},"next":{"title":"Facts","permalink":"/0.4.2/docs/facts"}}');var a=i(74848),s=i(28453),r=i(12325),c=i(5453);let o={},d="First Cinematic",l={},h=[{value:"What are Cinematics?",id:"what-are-cinematics",level:2},{value:"Creating a Cinematic",id:"creating-a-cinematic",level:2},{value:"Creating the Page",id:"creating-the-page",level:3},{value:"Adding a Camera Entry",id:"adding-a-camera-entry",level:3},{value:"Track Duration (1)",id:"track-duration-1",level:4},{value:"Segments (2)",id:"segments-2",level:4},{value:"Segments Track (3)",id:"segments-track-3",level:4},{value:"Triggering the Cinematic",id:"triggering-the-cinematic",level:2},{value:"Adding dialogue",id:"adding-dialogue",level:2},{value:"Actionbar Dialogue Cinematic",id:"actionbar-dialogue-cinematic",level:4},{value:"Subtitle Dialogue Cinematic",id:"subtitle-dialogue-cinematic",level:4},{value:"Spoken Dialogue Cinematic",id:"spoken-dialogue-cinematic",level:4},{value:"Adding Particles",id:"adding-particles",level:2},{value:"Adding a Self NPC",id:"adding-a-self-npc",level:2},{value:"Next steps",id:"next-steps",level:2}];function A(e){let t={a:"a",admonition:"admonition",blockquote:"blockquote",code:"code",h1:"h1",h2:"h2",h3:"h3",h4:"h4",header:"header",li:"li",p:"p",strong:"strong",ul:"ul",...(0,s.R)(),...e.components};return(0,a.jsxs)(a.Fragment,{children:[(0,a.jsx)(t.header,{children:(0,a.jsx)(t.h1,{id:"first-cinematic",children:"First Cinematic"})}),"\n",(0,a.jsx)(t.p,{children:"This guide will lead you through making your first cinematic."}),"\n",(0,a.jsx)(t.admonition,{type:"info",children:(0,a.jsxs)(t.p,{children:["This guide uses the ",(0,a.jsx)(t.a,{href:"../adapters/BasicAdapter",children:"Basic Adapter"}),", hence it must be installed before following this guide."]})}),"\n",(0,a.jsx)(t.admonition,{type:"info",children:(0,a.jsxs)(t.p,{children:["This guide builds upon the ",(0,a.jsx)(t.a,{href:"first-interaction",children:"First Interaction"})," guide.\nWe strongly recommend reading through it before following this guide."]})}),"\n",(0,a.jsx)(t.h2,{id:"what-are-cinematics",children:"What are Cinematics?"}),"\n",(0,a.jsx)(t.p,{children:"Cinematics are a way to have different actions happen in a precise order over time. This includes having the player's camera move around\nfrom location to location, having particles show up at different locations at different times, having NPCs move around,\nand much more!"}),"\n",(0,a.jsx)(t.p,{children:"This makes cinematics useful in a wide variety of scenarios, with some examples being:"}),"\n",(0,a.jsxs)(t.ul,{children:["\n",(0,a.jsx)(t.li,{children:"A cutscene in a quest"}),"\n",(0,a.jsx)(t.li,{children:"As part of a tutorial"}),"\n",(0,a.jsx)(t.li,{children:"Fast travel"}),"\n",(0,a.jsx)(t.li,{children:"Random encounters"}),"\n",(0,a.jsx)(t.li,{children:"And a lot more"}),"\n"]}),"\n",(0,a.jsx)(t.h2,{id:"creating-a-cinematic",children:"Creating a Cinematic"}),"\n",(0,a.jsxs)(t.p,{children:["We'll start off by making a simple cinematic with a cinematic camera entry, with the cinematic being triggered through\na command. Later in the guide we'll continue the ",(0,a.jsx)(t.a,{href:"first-interaction",children:"First Interaction"})," guide to trigger the cinematic\nfrom a flower."]}),"\n",(0,a.jsx)(t.h3,{id:"creating-the-page",children:"Creating the Page"}),"\n",(0,a.jsxs)(t.p,{children:["Start off by clicking add page. Here, choose the ",(0,a.jsx)(t.strong,{children:"Cinematic"})," type in the dropdown menu, and name the page ",(0,a.jsx)(t.code,{children:"flower_cinematic"}),"."]}),"\n",(0,a.jsx)(r.A,{url:i(29874).A}),"\n",(0,a.jsx)(t.h3,{id:"adding-a-camera-entry",children:"Adding a Camera Entry"}),"\n",(0,a.jsxs)(t.p,{children:["Let's now add an entry to the newly created page. Click the ",(0,a.jsx)(t.code,{children:"Add Entry"})," button or the ",(0,a.jsx)(t.code,{children:"+"})," in the top right corner,\nthen search for ",(0,a.jsx)(t.code,{children:"add: camera"}),", and add the ",(0,a.jsx)(t.code,{children:"Add Camera Cinematic"})," entry. You should now see the entry on your screen.\nLet's rename the entry to ",(0,a.jsx)(t.code,{children:"camera_view"}),". Then, click ",(0,a.jsx)(t.code,{children:"Add Segment"})," to add a new segment to the entry."]}),"\n",(0,a.jsx)(t.p,{children:"You'll now see a lot of new fields that aren't in the other pages. Let's go through those."}),"\n",(0,a.jsx)(c.A,{img:i(98593),alt:"Cinematics Layout"}),"\n",(0,a.jsxs)(t.blockquote,{children:["\n",(0,a.jsx)(t.h4,{id:"track-duration-1",children:"Track Duration (1)"}),"\n"]}),"\n",(0,a.jsx)(t.p,{children:"The track duration field is where you decide the duration of the entire cinematic.\nIt's important to note that the duration is in amount of frames, and not in seconds.\nYou can however easily convert from seconds to frames, as 20 frames is equal to 1 second."}),"\n",(0,a.jsxs)(t.blockquote,{children:["\n",(0,a.jsx)(t.h4,{id:"segments-2",children:"Segments (2)"}),"\n"]}),"\n",(0,a.jsx)(t.p,{children:"A segment is what the entry does in that specific time frame. Entries can have multiple segments, but an entry can't\nhave overlapping segments. For that, create another entry."}),"\n",(0,a.jsxs)(t.blockquote,{children:["\n",(0,a.jsx)(t.h4,{id:"segments-track-3",children:"Segments Track (3)"}),"\n"]}),"\n",(0,a.jsxs)(t.p,{children:["This is where the segments of entries are displayed to you. Here you can change when a segment starts and when it ends.\nKeep in mind you can also do this on each segment's sidebar by modifying the ",(0,a.jsx)(t.code,{children:"Start Frame"})," & ",(0,a.jsx)(t.code,{children:"End Frame"})," fields."]}),"\n",(0,a.jsxs)(t.p,{children:["Now that that's explained, we need to add a segment to the camera_view entry.\nMake sure you have the camera view entry selected, and then click ",(0,a.jsx)(t.code,{children:"Add Segment"})," on the entry sidebar.\nThis creates a new segment that spans the entire track."]}),"\n",(0,a.jsxs)(t.p,{children:["We now want to make this segment do something, and to do that, we need to add paths.\nFor the camera entry, this will be the locations it moves the player's POV between.\nLet's start with making two paths. To do that, click the ",(0,a.jsx)(t.code,{children:"+"})," symbol next to Path on the sidebar twice.\nExpanding ",(0,a.jsx)(t.code,{children:"Path #1"}),", you'll see a new area called ",(0,a.jsx)(t.code,{children:"Location"})," with several fields to specify the location of this path.\nWe now need to decide on two locations. To more easily continue the guide later on, I recommend picking two locations\naround a red tulip. We then fill in ",(0,a.jsx)(t.code,{children:"Path #1"}),"'s location, and expand ",(0,a.jsx)(t.code,{children:"Path #2"})," and add the location of that path.\nRemember to add two different locations. You can see example paths in the screenshot below."]}),"\n",(0,a.jsx)(c.A,{img:i(84200),alt:"Example Camera Paths",width:400}),"\n",(0,a.jsxs)(t.p,{children:["You should now have a functioning cinematic!\nLet's ",(0,a.jsx)(t.code,{children:"Publish"})," and check it out. Type ",(0,a.jsx)(t.code,{children:"/tw cinematic start flower_cinematic"})," in chat, and\nthe cinematic should start playing!"]}),"\n",(0,a.jsx)(r.A,{url:i(96106).A}),"\n",(0,a.jsxs)(t.p,{children:["As you see in the beginning of the camera path, the camera weirdly turns around.\nThis is because when starting the cinematic, Minecraft needs a few frames to set everything up.\nTo make it not noticeable, we can start the cinematic a few frames later.\nThe recommended amount is ",(0,a.jsx)(t.code,{children:"20 frames"}),", which is 1 second. To do this, we can either change the ",(0,a.jsx)(t.code,{children:"Start Frame"})," field\non the segment, or drag the segment on the track to our desired starting point."]}),"\n",(0,a.jsx)(t.admonition,{type:"tip",children:(0,a.jsxs)(t.p,{children:["To make it really feel cinematic, you can add a black screen at the beginning of the cinematic.\nUsing the ",(0,a.jsx)(t.a,{href:"/adapters/BasicAdapter/entries/cinematic/blinding_cinematic",children:"Blinding Cinematic"})," entry, you can make the screen\ngo black for the first ",(0,a.jsx)(t.code,{children:"20 frames"}),"."]})}),"\n",(0,a.jsx)(t.p,{children:"Currently we only have one segment, which means the cinematic will end after the segment is done.\nWe can add more segments to create different camera paths."}),"\n",(0,a.jsx)(t.p,{children:"Try adding a new segment, and then adding mutliple paths to that segment.\nIf you have done everything correctly, you should now have a cinematic that looks something like this:"}),"\n",(0,a.jsx)(r.A,{url:i(89419).A}),"\n",(0,a.jsx)(t.h2,{id:"triggering-the-cinematic",children:"Triggering the Cinematic"}),"\n",(0,a.jsx)(t.admonition,{type:"info",children:(0,a.jsx)(t.p,{children:"I strongly recommend picking a specific red tulip and changing the cinematic paths to be around it if you havn't done so already."})}),"\n",(0,a.jsxs)(t.p,{children:["Now, you probably want the cinematic to be triggered when a player does something, and not through a command.\nLet's do that by modifying the flower example from ",(0,a.jsx)(t.a,{href:"first-interaction",children:"First Interaction"})," so that one of the\noptions causes the cinematic to play."]}),"\n",(0,a.jsxs)(t.p,{children:["Let's head over to the Flower page by clicking on it in the Pages sidebar. We then simply want to add a new entry.\nSearch for ",(0,a.jsx)(t.code,{children:"add: cinematic"})," when adding a new entry, and select ",(0,a.jsx)(t.code,{children:"Add Cinematic"})," entry. We'll then rename this entry to\n",(0,a.jsx)(t.code,{children:"play_flower_cinematic"}),", and in the entry sidebar, click on the ",(0,a.jsx)(t.code,{children:"Page"})," field. Here you should see the ",(0,a.jsx)(t.code,{children:"Flower Cinematic"})," page\nthat we made earlier. Select that page, and then select the ",(0,a.jsx)(t.code,{children:"Inspect Flower"})," entry. Here we want to add a new option.\nLet's call it ",(0,a.jsx)(t.code,{children:"Look at flower"}),". In the new option, add a new Trigger, and select ",(0,a.jsx)(t.code,{children:"Play Flower Cinematic"})," option in the search box."]}),"\n",(0,a.jsx)(r.A,{url:i(88069).A}),"\n",(0,a.jsx)(t.admonition,{type:"info",children:(0,a.jsx)(t.p,{children:"Strongly recommended to pick a specific red tulip and changing the cinematic paths to be around it."})}),"\n",(0,a.jsxs)(t.p,{children:["You should now be able to select the option ",(0,a.jsx)(t.code,{children:"Smell the flower"})," when clicking a red tulip, and it'll play the cinematic."]}),"\n",(0,a.jsx)(t.h2,{id:"adding-dialogue",children:"Adding dialogue"}),"\n",(0,a.jsxs)(t.p,{children:["Let's make the cinematic have dialogue when it's playing. To do this, head back to the ",(0,a.jsx)(t.code,{children:"Flower Cinematic"})," page.\nHere, add a new entry and search for ",(0,a.jsx)(t.code,{children:"add: actionbar"}),", and select the ",(0,a.jsx)(t.code,{children:"Add Actionbar Dialogue Cinematic"})," entry.\nWe'll then rename this entry to ",(0,a.jsx)(t.code,{children:"flower_dialogue"}),". Now, let's first add a speaker to the entry. Let's use the Flower Speaker.\nAfter that, click ",(0,a.jsx)(t.code,{children:"Add Segment"})," to add a segment to the dialogue entry, and select it."]}),"\n",(0,a.jsx)(t.p,{children:"Let's change the time frame of the dialogue to start after 1 second. To do this, we can either change the Start Frame field, or drag the segment on the track\nto our desired starting point. We also have to remember that duration is in frames, not seconds, and remembering from earlier, 1 second equals 20 frames, change the Start Frame to 20."}),"\n",(0,a.jsxs)(t.p,{children:["We then have to add the text to be displayed. Insert ",(0,a.jsx)(t.code,{children:"That flower looks <red><bold>stunning"})," to the Text field."]}),"\n",(0,a.jsx)(t.p,{children:"And we're done! Dialogue should now appear 1 second into the cinematic.\nThere are a few different dialogue entries as well. Here's a quick overview:"}),"\n",(0,a.jsxs)(t.blockquote,{children:["\n",(0,a.jsx)(t.h4,{id:"actionbar-dialogue-cinematic",children:"Actionbar Dialogue Cinematic"}),"\n"]}),"\n",(0,a.jsx)(t.p,{children:"Will send the speaker & the message in the actionbar."}),"\n",(0,a.jsxs)(t.blockquote,{children:["\n",(0,a.jsx)(t.h4,{id:"subtitle-dialogue-cinematic",children:"Subtitle Dialogue Cinematic"}),"\n"]}),"\n",(0,a.jsx)(t.p,{children:"Will send the speaker in the actionbar, and sends the message as a subtitle (middle of screen)."}),"\n",(0,a.jsxs)(t.blockquote,{children:["\n",(0,a.jsx)(t.h4,{id:"spoken-dialogue-cinematic",children:"Spoken Dialogue Cinematic"}),"\n"]}),"\n",(0,a.jsx)(t.p,{children:"Will send the speaker & message in chat."}),"\n",(0,a.jsx)(r.A,{url:i(9804).A}),"\n",(0,a.jsx)(t.h2,{id:"adding-particles",children:"Adding Particles"}),"\n",(0,a.jsxs)(t.p,{children:["You can also add particles to be displayed during the cinematic. Let's do so hearts appear around the red tulip.\nTo do this, add a new entry and search ",(0,a.jsx)(t.code,{children:"add: particle"}),", and add the ",(0,a.jsx)(t.code,{children:"Add Particle Cinematic"})," entry, and rename it to\n",(0,a.jsx)(t.code,{children:"flower_particle"}),"\nFor this entry, we modify the particle effect on the entry itself and not on the segments.\nLet's start by selecting the particle type. Click the ",(0,a.jsx)(t.code,{children:"Particle"})," field in the entry sidebar, and scroll down until\nyou find the ",(0,a.jsx)(t.code,{children:"HEART"})," particle and then select it. Then, enter the location of your Red Tulip in the location field above."]}),"\n",(0,a.jsxs)(t.p,{children:["We will then define the particle offsets, speed & spawnCountPerTick. Think of the offset fields as a box around the location\nyou've specified, and particles can spawn anywhere inside the box.\nWe will set offsetX and offsetZ to ",(0,a.jsx)(t.code,{children:"1"}),", and leave offsetY at ",(0,a.jsx)(t.code,{children:"0.5"}),". We'll then set the speed to ",(0,a.jsx)(t.code,{children:"0.1"})," as to not get a lot of\nhearts spawning at the same time, and we will set spawnCountPerTick to 1, meaning that 1 heart will spawn every tick."]}),"\n",(0,a.jsx)(t.p,{children:"Now, we will add a segment to the particle entry, and we're finished! If you now play your cinematic, heart particles\nshould spawn around the red tulip."}),"\n",(0,a.jsx)(r.A,{url:i(85349).A}),"\n",(0,a.jsx)(t.h2,{id:"adding-a-self-npc",children:"Adding a Self NPC"}),"\n",(0,a.jsx)(t.admonition,{type:"info",children:(0,a.jsxs)(t.p,{children:["For this step, you have to type ",(0,a.jsx)(t.code,{children:"/tw connect"})," from in game as a player, otherwise you won't be able to record the NPC movement."]})}),"\n",(0,a.jsxs)(t.p,{children:['Want players to be able to see "themselves" while in a cinematic? Well, you can! Let\'s add a self NPC that walks over and looks at the flower\nduring the cinematic. To do this, add a new entry and search ',(0,a.jsx)(t.code,{children:"add: self npc"}),", add the ",(0,a.jsx)(t.code,{children:"Add Self Npc Cinematic"})," entry, and rename it\nto ",(0,a.jsx)(t.code,{children:"player_walk"}),". We then want to click ",(0,a.jsx)(t.code,{children:"Add RecordedSegmeent"}),", and select the new segment that has been made. Now, you'll first need\nto create an artifact by clicking the Artifact field. There, select ",(0,a.jsx)(t.code,{children:"Add Npc Movement Artifact"}),", and select ",(0,a.jsx)(t.code,{children:"Flower Static"})," as the static page\nto store the artifact in. Then, rename the artifact to ",(0,a.jsx)(t.code,{children:"player_look_at_flower"}),"."]}),"\n",(0,a.jsxs)(t.p,{children:["We then first want to ",(0,a.jsx)(t.code,{children:"Publish"}),", before heading back to the cinematic page, select the player walk segment again,\nand now we want to click on the camera button to the right of ",(0,a.jsx)(t.code,{children:"Artifact"}),", and open our Minecraft. Here you'll see a bossbar telling you\nto press ",(0,a.jsx)(t.code,{children:"F"})," to start recording. Pressing ",(0,a.jsx)(t.code,{children:"F"})," will cause the cinematic to start playing without the Camera Entry, and all your movements during the\ncinematic will be saved and be used for the NPC movement. You'll want to move to where you want to start the NPC movement, and hit ",(0,a.jsx)(t.code,{children:"F"}),", before\nfollowing the cinematic to add movement to it."]}),"\n",(0,a.jsx)(t.p,{children:"If you now play the cinematic again, an NPC with the skin of the player watching the cinematic should appear, with all the movement you did."}),"\n",(0,a.jsx)(r.A,{url:i(27110).A}),"\n",(0,a.jsx)(t.h2,{id:"next-steps",children:"Next steps"}),"\n",(0,a.jsx)(t.p,{children:"You should now know how to make simple cinematics, but remember, cinematics have a lot more to offer!\nTry messing about with the other options like NPCs that walk around, playing sounds, blinding cinematic & more!"})]})}function m(e={}){let{wrapper:t}={...(0,s.R)(),...e.components};return t?(0,a.jsx)(t,{...e,children:(0,a.jsx)(A,{...e})}):A(e)}},84200:(e,t,i)=>{e.exports={srcSet:i.p+"assets/optimized-img/camera-paths-example.f80a1b2.320.avif 320w,"+i.p+"assets/optimized-img/camera-paths-example.366de9a.419.avif 419w",images:[{path:i.p+"assets/optimized-img/camera-paths-example.f80a1b2.320.avif",width:320,height:486},{path:i.p+"assets/optimized-img/camera-paths-example.366de9a.419.avif",width:419,height:637}],src:i.p+"assets/optimized-img/camera-paths-example.366de9a.419.avif",toString:function(){return i.p+"assets/optimized-img/camera-paths-example.366de9a.419.avif"},placeholder:"data:image/avif;base64,AAAAHGZ0eXBhdmlmAAAAAGF2aWZtaWYxbWlhZgAAAOptZXRhAAAAAAAAACFoZGxyAAAAAAAAAABwaWN0AAAAAAAAAAAAAAAAAAAAAA5waXRtAAAAAAABAAAAImlsb2MAAAAAREAAAQABAAAAAAEOAAEAAAAAAAABjgAAACNpaW5mAAAAAAABAAAAFWluZmUCAAAAAAEAAGF2MDEAAAAAamlwcnAAAABLaXBjbwAAABNjb2xybmNseAABAA0ABoAAAAAMYXYxQ4EgAgAAAAAUaXNwZQAAAAAAAAAoAAAAPQAAABBwaXhpAAAAAAMICAgAAAAXaXBtYQAAAAAAAAABAAEEAYIDBAAAAZZtZGF0EgAKCTgVZ/GEBDQaQDL+AhZACCBihQDdT0npdT8of9mK1kch0MOJ3nmzhtTtrm7Yp7hfAC86q5/LaBTXzQ90YPcSEFV2+O5+a8csJBA6dRIAx6uhFo0ukwtWPu76MrH0N4oTdQ50U43c53VvXKWHj2ocV7k+pk5Sv/V/rawrEF1lDxaTLyu9YKBPAVUNCKgXA3896TaH1KXH4XkuxwG/Q2a8uKHifdEkBlBs8algHPQRZmcpbKNuroQlWhNPPXKmnt9wRopXXl9zURYj9kZGtUpr9wXCfgW9OruWuLZqiZnczy5b8tE45pD7zu/xyQzdjp9vkXBQXoBNOOvhz5rnm9wSmJr1oN6LK9PT15VxDGMAAgrrQTB6kSGbxrPAIl98SNCR1hAlFo3lYxKvQq6KzleSJ1ZpZgQwYxssun66tCTFDSDbr2MantAVGEBPy0FoXkgNOIx5RDINsKxelaX9ZxETleqskKeC0bz38Obec2dhrkgJUQ18tZ6LNnvL0SH4w+YCqegepo7i9rc4FGM=",width:419,height:637}},98593:(e,t,i)=>{e.exports={srcSet:i.p+"assets/optimized-img/cinematic-layout.260761f.320.avif 320w,"+i.p+"assets/optimized-img/cinematic-layout.8c128c3.640.avif 640w,"+i.p+"assets/optimized-img/cinematic-layout.e385120.960.avif 960w,"+i.p+"assets/optimized-img/cinematic-layout.11c564d.1280.avif 1280w,"+i.p+"assets/optimized-img/cinematic-layout.93e639c.1600.avif 1600w,"+i.p+"assets/optimized-img/cinematic-layout.af60f64.1621.avif 1621w",images:[{path:i.p+"assets/optimized-img/cinematic-layout.260761f.320.avif",width:320,height:97},{path:i.p+"assets/optimized-img/cinematic-layout.8c128c3.640.avif",width:640,height:194},{path:i.p+"assets/optimized-img/cinematic-layout.e385120.960.avif",width:960,height:291},{path:i.p+"assets/optimized-img/cinematic-layout.11c564d.1280.avif",width:1280,height:388},{path:i.p+"assets/optimized-img/cinematic-layout.93e639c.1600.avif",width:1600,height:485},{path:i.p+"assets/optimized-img/cinematic-layout.af60f64.1621.avif",width:1621,height:491}],src:i.p+"assets/optimized-img/cinematic-layout.af60f64.1621.avif",toString:function(){return i.p+"assets/optimized-img/cinematic-layout.af60f64.1621.avif"},placeholder:"data:image/avif;base64,AAAAHGZ0eXBhdmlmAAAAAGF2aWZtaWYxbWlhZgAAAOptZXRhAAAAAAAAACFoZGxyAAAAAAAAAABwaWN0AAAAAAAAAAAAAAAAAAAAAA5waXRtAAAAAAABAAAAImlsb2MAAAAAREAAAQABAAAAAAEOAAEAAAAAAAAAlwAAACNpaW5mAAAAAAABAAAAFWluZmUCAAAAAAEAAGF2MDEAAAAAamlwcnAAAABLaXBjbwAAABNjb2xybmNseAABAA0ABoAAAAAMYXYxQ4EgAgAAAAAUaXNwZQAAAAAAAAAoAAAADAAAABBwaXhpAAAAAAMICAgAAAAXaXBtYQAAAAAAAAABAAEEAYIDBAAAAJ9tZGF0EgAKCDgU57YQENBpMogBFkAFFBFFAPvJwMArS3E1irj7ihb4rvC0IFDho0qcH8g8ND0vdcBccyMjbc/snlNW+SlCQEjAYOm5wTbyhAaNeAFpurz3yJX/M4gcWhFDs94HzEKV4bZ2IQe4nQtSPGhtU4/3IhrPGFMNavKI7ybQ5lcyLnsyHExZg6yhAqHE2apHCcaR8x2IkA==",width:1621,height:491}},5453:(e,t,i)=>{"use strict";i.d(t,{A:()=>s});var n=i(74848),a=i(96540);function s(e){let{img:t,...i}=e;if("string"==typeof t||"default"in t)return(0,n.jsx)("div",{className:"w-full h-full flex justify-center items-center pb-10",children:(0,n.jsx)("img",{src:"string"==typeof t?t:t.default,loading:"lazy",decoding:"async",className:"rounded-md",...i})});let[s,r]=(0,a.useState)(!1);return(0,n.jsxs)("div",{className:"w-full h-full flex justify-center items-center relative",children:[(0,n.jsx)("img",{src:t.src,srcSet:t.srcSet,sizes:"(max-width: 320px) 280px, (max-width: 640px) 600px, 1200px",loading:"lazy",decoding:"async",onLoad:()=>r(!0),className:`rounded-md transition-opacity duration-300 ${s?"opacity-100":"opacity-0"}`,...i}),!s&&(0,n.jsx)("div",{className:"absolute inset-0 bg-cover bg-center rounded-md",style:{backgroundImage:`url(${t.placeholder})`}})]})}},12325:(e,t,i)=>{"use strict";i.d(t,{A:()=>d});var n=i(74848),a=i(96540),s=i(13554),r=i.n(s),c=i(37399),o=i(45041);function d(e){let{url:t}=e,[i,s]=(0,a.useState)(0),[d,h]=(0,a.useState)(!0),[A,m]=(0,a.useState)(!1),p=(0,a.useRef)(null),u=(0,a.useRef)(null);return(0,a.useEffect)(()=>{if(o.A.isEnabled)return o.A.on("change",()=>{m(o.A.isFullscreen)}),()=>{o.A.off("change",()=>{m(o.A.isFullscreen)})}},[]),(0,n.jsxs)("div",{ref:u,className:"relative w-full h-full",children:[(0,n.jsx)(l,{progress:i,onSeek:e=>{let t=parseFloat(e.target.value);s(t),p.current?.seekTo(t/100,"fraction")}}),(0,n.jsx)(r(),{ref:p,url:t,playing:d,loop:!0,muted:!0,playsInline:!0,playsinline:!0,controls:!1,width:"100%",height:"100%",progressInterval:100,onProgress:e=>s(100*e.played)}),(0,n.jsxs)("div",{className:"opacity-0 hover:opacity-100 transition-opacity duration-300 w-full h-full flex items-center justify-center",children:[(0,n.jsx)("div",{className:"absolute bottom-0 left-0 right-0 flex items-center justify-center cursor-pointer h-[97%]",onClick:()=>{h(e=>!e)},children:(0,n.jsx)("div",{children:(0,n.jsx)(c.In,{icon:d?"mdi:pause-circle":"mdi:play-circle",fontSize:50,color:"white"})})}),(0,n.jsx)("div",{className:"absolute bottom-2 right-2 p-2",children:(0,n.jsx)(c.In,{onClick:()=>{o.A.isEnabled&&o.A.toggle(u.current)},className:"cursor-pointer hover:scale-110 transition duration-150",icon:A?"mdi:fullscreen-exit":"mdi:fullscreen",fontSize:40,color:"white"})})]})]})}function l(e){let{progress:t,onSeek:i}=e;return(0,n.jsx)("div",{className:"w-full flex items-center text-white",children:(0,n.jsx)("div",{className:"flex-grow",children:(0,n.jsx)(h,{progress:t,onSeek:i})})})}function h(e){let{progress:t,onSeek:i}=e;return(0,n.jsxs)("div",{className:"relative h-[5px] rounded-t-lg overflow-hidden pb-2",children:[(0,n.jsx)("input",{type:"range",min:"0",max:"100",value:t,onChange:i,className:"absolute top-0 left-0 w-full h-[5px] opacity-0 cursor-pointer",style:{WebkitAppearance:"none",MozAppearance:"none",appearance:"none"}}),(0,n.jsx)("div",{className:"h-full bg-primary transition-width duration-200 pb-2",style:{width:`${t}%`}})]})}},96106:(e,t,i)=>{"use strict";i.d(t,{A:()=>n});let n=i.p+"assets/medias/cinematic-1-5b55a6f5685f40c8ca434b4b914f78ae.webm"},89419:(e,t,i)=>{"use strict";i.d(t,{A:()=>n});let n=i.p+"assets/medias/cinematic-2-2f7acba03bda847d1b6569ab9935b312.webm"},9804:(e,t,i)=>{"use strict";i.d(t,{A:()=>n});let n=i.p+"assets/medias/cinematic-3-f5df516150bb3f079b94723b64d6858b.webm"},85349:(e,t,i)=>{"use strict";i.d(t,{A:()=>n});let n=i.p+"assets/medias/cinematic-4-6234ff3b39882804c10a9d1b74476372.webm"},27110:(e,t,i)=>{"use strict";i.d(t,{A:()=>n});let n=i.p+"assets/medias/cinematic-5-24df74db0aa65e25c985d37d370bdc8b.webm"},88069:(e,t,i)=>{"use strict";i.d(t,{A:()=>n});let n=i.p+"assets/medias/cinematic-trigger-1d972979e950f19ccaa8e0f439051331.webm"},29874:(e,t,i)=>{"use strict";i.d(t,{A:()=>n});let n=i.p+"assets/medias/page-creation-a0bee4b28600df8d03e484e8b1f63852.webm"}}]);