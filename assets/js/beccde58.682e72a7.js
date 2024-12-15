(self.webpackChunkdocumentation=self.webpackChunkdocumentation||[]).push([[31828],{70822:(e,t,A)=>{"use strict";A.r(t),A.d(t,{assets:()=>c,contentTitle:()=>l,default:()=>u,frontMatter:()=>o,metadata:()=>i,toc:()=>d});let i=JSON.parse('{"id":"docs/getting-started/layout","title":"Layout","description":"On this page, you will learn how to use the TypeWriter Web panel and create certain things.","source":"@site/docs/docs/02-getting-started/02-layout.mdx","sourceDirName":"docs/02-getting-started","slug":"/docs/getting-started/layout","permalink":"/beta/docs/getting-started/layout","draft":false,"unlisted":false,"editUrl":"https://github.com/gabber235/TypeWriter/tree/develop/documentation/docs/docs/02-getting-started/02-layout.mdx","tags":[],"version":"current","lastUpdatedBy":"Marten Mrfc","lastUpdatedAt":1734273100000,"sidebarPosition":2,"frontMatter":{"difficulty":"Easy"},"sidebar":"tutorialSidebar","previous":{"title":"Installation Guide","permalink":"/beta/docs/getting-started/installation"},"next":{"title":"First Interaction","permalink":"/beta/docs/creating-stories/interactions/"}}');var a=A(74848),s=A(28453),n=A(12325),r=A(5453);let o={difficulty:"Easy"},l="Layout",c={},d=[{value:"Creating a page",id:"creating-a-page",level:2},{value:"Base Layout",id:"base-layout",level:2},{value:"Sequence, static and manifest layout",id:"sequence-static-and-manifest-layout",level:2},{value:"Cinematic layout",id:"cinematic-layout",level:2}];function h(e){let t={code:"code",h1:"h1",h2:"h2",header:"header",li:"li",ol:"ol",p:"p",strong:"strong",ul:"ul",...(0,s.R)(),...e.components};return(0,a.jsxs)(a.Fragment,{children:[(0,a.jsx)(t.header,{children:(0,a.jsx)(t.h1,{id:"layout",children:"Layout"})}),"\n",(0,a.jsx)(t.p,{children:"On this page, you will learn how to use the TypeWriter Web panel and create certain things."}),"\n",(0,a.jsx)(t.h2,{id:"creating-a-page",children:"Creating a page"}),"\n",(0,a.jsxs)(t.p,{children:["To create a page, simply click the button ",(0,a.jsx)(t.code,{children:"Add Page"})," then enter the name of the page. It can't be a duplicate name of an already existing page. Select one of the four types:"]}),"\n",(0,a.jsxs)(t.ul,{children:["\n",(0,a.jsxs)(t.li,{children:[(0,a.jsx)(t.strong,{children:"Sequence"}),": Create interactions that can be played after each other."]}),"\n",(0,a.jsxs)(t.li,{children:[(0,a.jsx)(t.strong,{children:"Static"})," : Referencing to data that doesn't change over the lifetime of the program."]}),"\n",(0,a.jsxs)(t.li,{children:[(0,a.jsx)(t.strong,{children:"Cinematic"}),": Timed sequences of actions."]}),"\n",(0,a.jsxs)(t.li,{children:[(0,a.jsx)(t.strong,{children:"Manifest"}),":  In the runtime calculated state of the interaction."]}),"\n"]}),"\n",(0,a.jsx)(n.A,{url:A(34436).A}),"\n",(0,a.jsx)(t.p,{children:"Now that we have created a sequence page, I will explain how the different layouts works."}),"\n",(0,a.jsx)(t.h2,{id:"base-layout",children:"Base Layout"}),"\n",(0,a.jsx)(r.A,{img:A(49865),alt:"Sequence Page Layout"}),"\n",(0,a.jsxs)(t.ol,{children:["\n",(0,a.jsxs)(t.li,{children:[(0,a.jsx)(t.strong,{children:"Page list"}),": This is where you can see all the pages that you have created. You can also create new pages here."]}),"\n",(0,a.jsxs)(t.li,{children:[(0,a.jsx)(t.strong,{children:"Action bar"}),": There are multiple actions that you can perform by clicking on the buttons from left to right:","\n",(0,a.jsxs)(t.ul,{children:["\n",(0,a.jsxs)(t.li,{children:[(0,a.jsx)(t.strong,{children:"Staging Indicator"}),': orange indicates that changes are not active on the server, and green means they are. Just hover over it to reveal the "Publish" button, and click it if you want to publish your staged pages to the server.']}),"\n",(0,a.jsxs)(t.li,{children:[(0,a.jsx)(t.strong,{children:"Search bar"}),": Use this to search for entries or create new ones."]}),"\n",(0,a.jsxs)(t.li,{children:[(0,a.jsx)(t.strong,{children:"+ button"}),": Click here to create new entries."]}),"\n"]}),"\n"]}),"\n"]}),"\n",(0,a.jsxs)(t.p,{children:[(0,a.jsx)(t.strong,{children:"Inspector"}),": This is where you can edit the properties of the selected entry."]}),"\n",(0,a.jsxs)(t.ol,{start:"3",children:["\n",(0,a.jsxs)(t.li,{children:[(0,a.jsx)(t.strong,{children:"Entry Information"}),": Here you can view all the information about an entry, such as the ID and name."]}),"\n",(0,a.jsxs)(t.li,{children:[(0,a.jsx)(t.strong,{children:"Fields"}),": Use this section to edit the properties of the entries."]}),"\n",(0,a.jsxs)(t.li,{children:[(0,a.jsx)(t.strong,{children:"Operations"}),": This section contains various actions that you can perform with the entry."]}),"\n"]}),"\n",(0,a.jsx)(t.h2,{id:"sequence-static-and-manifest-layout",children:"Sequence, static and manifest layout"}),"\n",(0,a.jsxs)(t.ul,{children:["\n",(0,a.jsx)(t.li,{children:"In the center, all your entries are shown."}),"\n",(0,a.jsx)(t.li,{children:"Use your mouse's scroll wheel to zoom in or out."}),"\n",(0,a.jsx)(t.li,{children:"Click on an entry to open it in the inspector."}),"\n",(0,a.jsx)(t.li,{children:"When holding an entry for a few seconds, it makes it drag and drop so you can connect it with other entries."}),"\n"]}),"\n",(0,a.jsx)(t.h2,{id:"cinematic-layout",children:"Cinematic layout"}),"\n",(0,a.jsx)(r.A,{img:A(55853),alt:"Cinematic Page Layout"}),"\n",(0,a.jsxs)(t.ol,{children:["\n",(0,a.jsxs)(t.li,{children:[(0,a.jsx)(t.strong,{children:"Segments"}),": Inside a cinematic entry, you can create segments, which are displayed on your track. Segments can't overlap each other."]}),"\n",(0,a.jsxs)(t.li,{children:[(0,a.jsx)(t.strong,{children:"Track"}),": This is where you can see all the segments that you have created and edit their show duration."]}),"\n",(0,a.jsxs)(t.li,{children:[(0,a.jsx)(t.strong,{children:"Track Duration"}),": Here, you can edit the duration of the track. The track duration is in Minecraft ticks, so 20 ticks equals 1 second."]}),"\n"]})]})}function u(e={}){let{wrapper:t}={...(0,s.R)(),...e.components};return t?(0,a.jsx)(t,{...e,children:(0,a.jsx)(h,{...e})}):h(e)}},55853:(e,t,A)=>{e.exports={srcSet:A.p+"assets/optimized-img/layout-cinematic.47d712a.320.avif 320w,"+A.p+"assets/optimized-img/layout-cinematic.44860b7.640.avif 640w,"+A.p+"assets/optimized-img/layout-cinematic.9071c79.960.avif 960w,"+A.p+"assets/optimized-img/layout-cinematic.fd2b55f.1280.avif 1280w,"+A.p+"assets/optimized-img/layout-cinematic.cd4cf0c.1600.avif 1600w,"+A.p+"assets/optimized-img/layout-cinematic.01cad0e.1920.avif 1920w",images:[{path:A.p+"assets/optimized-img/layout-cinematic.47d712a.320.avif",width:320,height:180},{path:A.p+"assets/optimized-img/layout-cinematic.44860b7.640.avif",width:640,height:360},{path:A.p+"assets/optimized-img/layout-cinematic.9071c79.960.avif",width:960,height:540},{path:A.p+"assets/optimized-img/layout-cinematic.fd2b55f.1280.avif",width:1280,height:720},{path:A.p+"assets/optimized-img/layout-cinematic.cd4cf0c.1600.avif",width:1600,height:900},{path:A.p+"assets/optimized-img/layout-cinematic.01cad0e.1920.avif",width:1920,height:1080}],src:A.p+"assets/optimized-img/layout-cinematic.01cad0e.1920.avif",toString:function(){return A.p+"assets/optimized-img/layout-cinematic.01cad0e.1920.avif"},placeholder:"data:image/avif;base64,AAAAHGZ0eXBhdmlmAAAAAGF2aWZtaWYxbWlhZgAAAYRtZXRhAAAAAAAAACFoZGxyAAAAAAAAAABwaWN0AAAAAAAAAAAAAAAAAAAAAA5waXRtAAAAAAABAAAANGlsb2MAAAAAREAAAgACAAAAAAGoAAEAAAAAAAAAFQABAAAAAAG9AAEAAAAAAAAAzAAAADhpaW5mAAAAAAACAAAAFWluZmUCAAAAAAEAAGF2MDEAAAAAFWluZmUCAAAAAAIAAGF2MDEAAAAAw2lwcnAAAACdaXBjbwAAABNjb2xybmNseAABAA0ABoAAAAAMYXYxQ4EAHAAAAAAUaXNwZQAAAAAAAAAoAAAAFwAAAA5waXhpAAAAAAEIAAAAOGF1eEMAAAAAdXJuOm1wZWc6bXBlZ0I6Y2ljcDpzeXN0ZW1zOmF1eGlsaWFyeTphbHBoYQAAAAAMYXYxQ4EgAgAAAAAQcGl4aQAAAAADCAgIAAAAHmlwbWEAAAAAAAAAAgABBAGGAwcAAgSCAwSFAAAAGmlyZWYAAAAAAAAADmF1eGwAAgABAAEAAADpbWRhdBIACgYYFSezCoAyCRZAAAEABjT3qBIACgk4FSezCAhoNIAyvAEWQAwwcoUA8b9qNI2RGCmdAxZN6QfNWZkFYsR+L/NSQF7pqONIBBk7vr4KODYXek1nJ4nLmNq2ArPSRIO/odAINzHZqsyCUMRPodYTj28XbpCa0YIg0m325yQXixQrBnR3c2HU0HrKJb7x7R1iYOubf3gR/lnK+v2bcwYoMlb/I141NR0wj1nKpb3JEYqwFRhtvOOmOlPfwK2qEvl6ouLztF47dkeIZetLzDy51WKgH0+3p3iwOPqA98eS+A==",width:1920,height:1080}},49865:(e,t,A)=>{e.exports={srcSet:A.p+"assets/optimized-img/layout.6e1146d.320.avif 320w,"+A.p+"assets/optimized-img/layout.45c566b.640.avif 640w,"+A.p+"assets/optimized-img/layout.0384327.960.avif 960w,"+A.p+"assets/optimized-img/layout.8b87d71.1280.avif 1280w,"+A.p+"assets/optimized-img/layout.bef05ef.1600.avif 1600w,"+A.p+"assets/optimized-img/layout.9816b40.1920.avif 1920w",images:[{path:A.p+"assets/optimized-img/layout.6e1146d.320.avif",width:320,height:180},{path:A.p+"assets/optimized-img/layout.45c566b.640.avif",width:640,height:360},{path:A.p+"assets/optimized-img/layout.0384327.960.avif",width:960,height:540},{path:A.p+"assets/optimized-img/layout.8b87d71.1280.avif",width:1280,height:720},{path:A.p+"assets/optimized-img/layout.bef05ef.1600.avif",width:1600,height:900},{path:A.p+"assets/optimized-img/layout.9816b40.1920.avif",width:1920,height:1080}],src:A.p+"assets/optimized-img/layout.9816b40.1920.avif",toString:function(){return A.p+"assets/optimized-img/layout.9816b40.1920.avif"},placeholder:"data:image/avif;base64,AAAAHGZ0eXBhdmlmAAAAAGF2aWZtaWYxbWlhZgAAAYRtZXRhAAAAAAAAACFoZGxyAAAAAAAAAABwaWN0AAAAAAAAAAAAAAAAAAAAAA5waXRtAAAAAAABAAAANGlsb2MAAAAAREAAAgACAAAAAAGoAAEAAAAAAAAAFQABAAAAAAG9AAEAAAAAAAABLQAAADhpaW5mAAAAAAACAAAAFWluZmUCAAAAAAEAAGF2MDEAAAAAFWluZmUCAAAAAAIAAGF2MDEAAAAAw2lwcnAAAACdaXBjbwAAABNjb2xybmNseAABAA0ABoAAAAAMYXYxQ4EAHAAAAAAUaXNwZQAAAAAAAAAoAAAAFwAAAA5waXhpAAAAAAEIAAAAOGF1eEMAAAAAdXJuOm1wZWc6bXBlZ0I6Y2ljcDpzeXN0ZW1zOmF1eGlsaWFyeTphbHBoYQAAAAAMYXYxQ4EgAgAAAAAQcGl4aQAAAAADCAgIAAAAHmlwbWEAAAAAAAAAAgABBAGGAwcAAgSCAwSFAAAAGmlyZWYAAAAAAAAADmF1eGwAAgABAAEAAAFKbWRhdBIACgYYFSezCoAyCRZAAAEABjT3qBIACgk4FSezCAhoNIAynQIWQAQQwQUA8cglCIWehqqvIV72uBdKKTwqhV7KyRm9NCAqH5w9bTvhz6FoPtD3HF7xZP83nFATy5hY3neTsme45lGhk+H6yujDnWYSHfh1moVVkWZQmeyJAQ7E5PUOzqn7NRmutq4cD+ZuAGHZQj3hpg+gRZO4h1YO0+aOWEyevOcp30w0P2srUSw12Fkme+BMLhurmz7AlFt584mOljM0LjKXNCubiATrh7o6JrUs+9S8uuWIn0njM/TXWmnAeQZ2YaCgTNyuxmRR0aQ5C1eLM2G6e8x0BUM+uTFE9qC2Pf3GPgL1JZ328S++rBVg/IpIfLnZVzlNkWtZF+fmsy8mDVjeyMsqpi/H8xgQrFnLXUPKyDThuQGaeXNYokA=",width:1920,height:1080}},5453:(e,t,A)=>{"use strict";A.d(t,{A:()=>s});var i=A(74848),a=A(96540);function s(e){let{img:t,...A}=e;if("string"==typeof t||"default"in t)return(0,i.jsx)("div",{className:"w-full h-full flex justify-center items-center pb-10",children:(0,i.jsx)("img",{src:"string"==typeof t?t:t.default,loading:"lazy",decoding:"async",className:"rounded-md",...A})});let[s,n]=(0,a.useState)(!1);return(0,i.jsxs)("div",{className:"w-full h-full flex justify-center items-center relative",children:[(0,i.jsx)("img",{src:t.src,srcSet:t.srcSet,sizes:"(max-width: 320px) 280px, (max-width: 640px) 600px, 1200px",loading:"lazy",decoding:"async",onLoad:()=>n(!0),className:`rounded-md transition-opacity duration-300 ${s?"opacity-100":"opacity-0"}`,...A}),!s&&(0,i.jsx)("div",{className:"absolute inset-0 bg-cover bg-center rounded-md",style:{backgroundImage:`url(${t.placeholder})`}})]})}},12325:(e,t,A)=>{"use strict";A.d(t,{A:()=>l});var i=A(74848),a=A(96540),s=A(13554),n=A.n(s),r=A(37399),o=A(45041);function l(e){let{url:t}=e,[A,s]=(0,a.useState)(0),[l,d]=(0,a.useState)(!0),[h,u]=(0,a.useState)(!1),m=(0,a.useRef)(null),p=(0,a.useRef)(null);return(0,a.useEffect)(()=>{if(o.A.isEnabled)return o.A.on("change",()=>{u(o.A.isFullscreen)}),()=>{o.A.off("change",()=>{u(o.A.isFullscreen)})}},[]),(0,i.jsxs)("div",{ref:p,className:"relative w-full h-full",children:[(0,i.jsx)(c,{progress:A,onSeek:e=>{let t=parseFloat(e.target.value);s(t),m.current?.seekTo(t/100,"fraction")}}),(0,i.jsx)(n(),{ref:m,url:t,playing:l,loop:!0,muted:!0,playsInline:!0,playsinline:!0,controls:!1,width:"100%",height:"100%",progressInterval:100,onProgress:e=>s(100*e.played)}),(0,i.jsxs)("div",{className:"opacity-0 hover:opacity-100 transition-opacity duration-300 w-full h-full flex items-center justify-center",children:[(0,i.jsx)("div",{className:"absolute bottom-0 left-0 right-0 flex items-center justify-center cursor-pointer h-[97%]",onClick:()=>{d(e=>!e)},children:(0,i.jsx)("div",{children:(0,i.jsx)(r.In,{icon:l?"mdi:pause-circle":"mdi:play-circle",fontSize:50,color:"white"})})}),(0,i.jsx)("div",{className:"absolute bottom-2 right-2 p-2",children:(0,i.jsx)(r.In,{onClick:()=>{o.A.isEnabled&&o.A.toggle(p.current)},className:"cursor-pointer hover:scale-110 transition duration-150",icon:h?"mdi:fullscreen-exit":"mdi:fullscreen",fontSize:40,color:"white"})})]})]})}function c(e){let{progress:t,onSeek:A}=e;return(0,i.jsx)("div",{className:"w-full flex items-center text-white",children:(0,i.jsx)("div",{className:"flex-grow",children:(0,i.jsx)(d,{progress:t,onSeek:A})})})}function d(e){let{progress:t,onSeek:A}=e;return(0,i.jsxs)("div",{className:"relative h-[5px] rounded-t-lg overflow-hidden pb-2",children:[(0,i.jsx)("input",{type:"range",min:"0",max:"100",value:t,onChange:A,className:"absolute top-0 left-0 w-full h-[5px] opacity-0 cursor-pointer",style:{WebkitAppearance:"none",MozAppearance:"none",appearance:"none"}}),(0,i.jsx)("div",{className:"h-full bg-primary transition-width duration-200 pb-2",style:{width:`${t}%`}})]})}},34436:(e,t,A)=>{"use strict";A.d(t,{A:()=>i});let i=A.p+"assets/medias/add-page-dfed7156e9f8958d43ea365a2195bf46.webm"}}]);