(self.webpackChunkdocumentation=self.webpackChunkdocumentation||[]).push([[15950],{86513:(e,t,s)=>{"use strict";s.r(t),s.d(t,{assets:()=>c,contentTitle:()=>l,default:()=>p,frontMatter:()=>n,metadata:()=>o,toc:()=>d});var A=s(74848),a=s(28453),r=s(8777),i=s(70689);const n={difficulty:"Easy"},l="Chapters",o={id:"docs/helpfull-features/chapters",title:"Chapters",description:"Chapters add a touch of convenience to your typewriter workflow, resembling the familiar structure of chapters in a book. Similar to files forming folders on a PC, TypeWriter's chapter system organizes pages within chapters.",source:"@site/versioned_docs/version-0.5.1/docs/05-helpfull-features/01-chapters.mdx",sourceDirName:"docs/05-helpfull-features",slug:"/docs/helpfull-features/chapters",permalink:"/docs/helpfull-features/chapters",draft:!1,unlisted:!1,editUrl:"https://github.com/gabber235/TypeWriter/tree/develop/documentation/versioned_docs/version-0.5.1/docs/05-helpfull-features/01-chapters.mdx",tags:[],version:"0.5.1",lastUpdatedBy:"dependabot[bot]",lastUpdatedAt:1729791453e3,sidebarPosition:1,frontMatter:{difficulty:"Easy"},sidebar:"tutorialSidebar",previous:{title:"Manifest Entries",permalink:"/docs/concepts/Manifest/"},next:{title:"Commands",permalink:"/docs/helpfull-features/commands"}},c={},d=[{value:"Setting Chapters",id:"setting-chapters",level:3},{value:"Setting Sub-Chapters",id:"setting-sub-chapters",level:3}];function h(e){const t={code:"code",h1:"h1",h3:"h3",header:"header",p:"p",...(0,a.R)(),...e.components};return(0,A.jsxs)(A.Fragment,{children:[(0,A.jsx)(t.header,{children:(0,A.jsx)(t.h1,{id:"chapters",children:"Chapters"})}),"\n",(0,A.jsx)(t.p,{children:"Chapters add a touch of convenience to your typewriter workflow, resembling the familiar structure of chapters in a book. Similar to files forming folders on a PC, TypeWriter's chapter system organizes pages within chapters."}),"\n",(0,A.jsx)(t.h3,{id:"setting-chapters",children:"Setting Chapters"}),"\n",(0,A.jsxs)(t.p,{children:["Easily create a chapter by right-clicking on a page and selecting ",(0,A.jsx)(t.code,{children:"Change Chapter"}),". Enter the desired chapter name and click ",(0,A.jsx)(t.code,{children:"Change"}),"."]}),"\n",(0,A.jsx)(r.A,{url:s(78983).A}),"\n",(0,A.jsx)(t.h3,{id:"setting-sub-chapters",children:"Setting Sub-Chapters"}),"\n",(0,A.jsxs)(t.p,{children:["Creating subchapters is super simple. Simply use the main chapter name and add a ",(0,A.jsx)(t.code,{children:"."})," for example, ",(0,A.jsx)(t.code,{children:"hello.test"})," will generate a sub-chapter. As shown below:"]}),"\n",(0,A.jsx)(i.A,{img:s(44090),alt:"Subchapters",width:400}),"\n",(0,A.jsxs)(t.p,{children:["Feel free to extend the story with as many subchapters as needed. For instance, ",(0,A.jsx)(t.code,{children:"hello.test.morning"})," would work to. TypeWriter automatically collects pages with the same chapter name and groups them together."]})]})}function p(e={}){const{wrapper:t}={...(0,a.R)(),...e.components};return t?(0,A.jsx)(t,{...e,children:(0,A.jsx)(h,{...e})}):h(e)}},70689:(e,t,s)=>{"use strict";s.d(t,{A:()=>r});var A=s(96540),a=s(74848);function r(e){const{img:t,...s}=e;if("string"==typeof t||"default"in t)return(0,a.jsx)("div",{className:"w-full h-full flex justify-center items-center pb-10",children:(0,a.jsx)("img",{src:"string"==typeof t?t:t.default,loading:"lazy",decoding:"async",className:"rounded-md",...s})});const[r,i]=(0,A.useState)(!1);return(0,a.jsxs)("div",{className:"w-full h-full flex justify-center items-center relative",children:[(0,a.jsx)("img",{src:t.src,srcSet:t.srcSet,sizes:"(max-width: 320px) 280px, (max-width: 640px) 600px, 1200px",loading:"lazy",decoding:"async",onLoad:()=>i(!0),className:"rounded-md transition-opacity duration-300 "+(r?"opacity-100":"opacity-0"),...s}),!r&&(0,a.jsx)("div",{className:"absolute inset-0 bg-cover bg-center rounded-md",style:{backgroundImage:`url(${t.placeholder})`}})]})}},8777:(e,t,s)=>{"use strict";s.d(t,{A:()=>o});var A=s(96540),a=s(13554),r=s.n(a),i=s(37399),n=s(45041),l=s(74848);function o(e){let{url:t}=e;const[s,a]=(0,A.useState)(0),[o,d]=(0,A.useState)(!0),[h,p]=(0,A.useState)(!1),u=(0,A.useRef)(null),m=(0,A.useRef)(null);return(0,A.useEffect)((()=>{if(n.A.isEnabled)return n.A.on("change",(()=>{p(n.A.isFullscreen)})),()=>{n.A.off("change",(()=>{p(n.A.isFullscreen)}))}}),[]),(0,l.jsxs)("div",{ref:m,className:"relative w-full h-full",children:[(0,l.jsx)(c,{progress:s,onSeek:e=>{const t=parseFloat(e.target.value);a(t),u.current?.seekTo(t/100,"fraction")}}),(0,l.jsx)(r(),{ref:u,url:t,playing:o,loop:!0,muted:!0,playsInline:!0,playsinline:!0,controls:!1,width:"100%",height:"100%",progressInterval:100,onProgress:e=>a(100*e.played)}),(0,l.jsxs)("div",{className:"opacity-0 hover:opacity-100 transition-opacity duration-300 w-full h-full flex items-center justify-center",children:[(0,l.jsx)("div",{className:"absolute bottom-0 left-0 right-0 flex items-center justify-center cursor-pointer h-[97%]",onClick:()=>{d((e=>!e))},children:(0,l.jsx)("div",{children:(0,l.jsx)(i.In,{icon:o?"mdi:pause-circle":"mdi:play-circle",fontSize:50,color:"white"})})}),(0,l.jsx)("div",{className:"absolute bottom-2 right-2 p-2",children:(0,l.jsx)(i.In,{onClick:()=>{n.A.isEnabled&&n.A.toggle(m.current)},className:"cursor-pointer hover:scale-110 transition duration-150",icon:h?"mdi:fullscreen-exit":"mdi:fullscreen",fontSize:40,color:"white"})})]})]})}function c(e){let{progress:t,onSeek:s}=e;return(0,l.jsx)("div",{className:"w-full flex items-center text-white",children:(0,l.jsx)("div",{className:"flex-grow",children:(0,l.jsx)(d,{progress:t,onSeek:s})})})}function d(e){let{progress:t,onSeek:s}=e;return(0,l.jsxs)("div",{className:"relative h-[5px] rounded-t-lg overflow-hidden pb-2",children:[(0,l.jsx)("input",{type:"range",min:"0",max:"100",value:t,onChange:s,className:"absolute top-0 left-0 w-full h-[5px] opacity-0 cursor-pointer",style:{WebkitAppearance:"none",MozAppearance:"none",appearance:"none"}}),(0,l.jsx)("div",{className:"h-full bg-primary transition-width duration-200 pb-2",style:{width:`${t}%`}})]})}},44090:(e,t,s)=>{e.exports={srcSet:s.p+"assets/optimized-img/sub-chapters.0b32a82.266.avif 266w",images:[{path:s.p+"assets/optimized-img/sub-chapters.0b32a82.266.avif",width:266,height:140}],src:s.p+"assets/optimized-img/sub-chapters.0b32a82.266.avif",toString:function(){return s.p+"assets/optimized-img/sub-chapters.0b32a82.266.avif"},placeholder:"data:image/avif;base64,AAAAHGZ0eXBhdmlmAAAAAGF2aWZtaWYxbWlhZgAAAYRtZXRhAAAAAAAAACFoZGxyAAAAAAAAAABwaWN0AAAAAAAAAAAAAAAAAAAAAA5waXRtAAAAAAABAAAANGlsb2MAAAAAREAAAgACAAAAAAGoAAEAAAAAAAAAFQABAAAAAAG9AAEAAAAAAAAAswAAADhpaW5mAAAAAAACAAAAFWluZmUCAAAAAAEAAGF2MDEAAAAAFWluZmUCAAAAAAIAAGF2MDEAAAAAw2lwcnAAAACdaXBjbwAAABNjb2xybmNseAABAA0ABoAAAAAMYXYxQ4EAHAAAAAAUaXNwZQAAAAAAAAAoAAAAFQAAAA5waXhpAAAAAAEIAAAAOGF1eEMAAAAAdXJuOm1wZWc6bXBlZ0I6Y2ljcDpzeXN0ZW1zOmF1eGlsaWFyeTphbHBoYQAAAAAMYXYxQ4EgAgAAAAAQcGl4aQAAAAADCAgIAAAAHmlwbWEAAAAAAAAAAgABBAGGAwcAAgSCAwSFAAAAGmlyZWYAAAAAAAAADmF1eGwAAgABAAEAAADQbWRhdBIACgYYFSejCoAyCRZAAAEABjT3qBIACgk4FSejCAhoNIAyowEWQAMMEEUA8V2Fl1F/mu7oz66PX/ms9XcYDJlWMOXp7rXEEhUPXs+u6OlJfIFlX1h/fXnQug2j9AiXyLti9K3JCs76EDweJtDIP8FFElw8/j2pi2vR/NYsthC04Qez15Y41sHLHS+pPiPJoY9FfoehXlMSTdMJVdxNm2eRbLspat5slkMK0U7p8F8WXaU3o+0LpX8tSqedYMnFKnPV0eK6A1yg",width:266,height:140}},78983:(e,t,s)=>{"use strict";s.d(t,{A:()=>A});const A=s.p+"assets/medias/chapters-4273ef3c8ac55c99fb4cde08c830636f.webm"}}]);