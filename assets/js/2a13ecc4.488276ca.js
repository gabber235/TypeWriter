(self.webpackChunkdocumentation=self.webpackChunkdocumentation||[]).push([[19725],{49018:(e,i,t)=>{"use strict";t.r(i),t.d(i,{assets:()=>A,contentTitle:()=>l,default:()=>h,frontMatter:()=>a,metadata:()=>n,toc:()=>d});let n=JSON.parse('{"id":"docs/troubleshooting/index","title":"Troubleshooting Guide","description":"Welcome to the Typewriter Plugin Troubleshooting Guide. This document aims to assist users in resolving common issues encountered while using the Typewriter plugin for Paper Spigot. Before reaching out for assistance on Discord or GitHub, we encourage you to review this comprehensive list of frequently encountered issues and their solutions. This proactive approach can help you solve your problem more quickly and efficiently.","source":"@site/versioned_docs/version-0.5.1/docs/06-troubleshooting/index.mdx","sourceDirName":"docs/06-troubleshooting","slug":"/docs/troubleshooting/","permalink":"/0.5.1/docs/troubleshooting/","draft":false,"unlisted":false,"editUrl":"https://github.com/gabber235/TypeWriter/tree/develop/documentation/versioned_docs/version-0.5.1/docs/06-troubleshooting/index.mdx","tags":[],"version":"0.5.1","lastUpdatedBy":"Gabber235","lastUpdatedAt":1734557994000,"frontMatter":{"difficulty":"Normal"},"sidebar":"tutorialSidebar","previous":{"title":"Snippets","permalink":"/0.5.1/docs/helpful-features/snippets"},"next":{"title":"Adapters Troubleshooting","permalink":"/0.5.1/docs/troubleshooting/adapters"}}');var s=t(74848),o=t(28453),r=t(5453);let a={difficulty:"Normal"},l="Troubleshooting Guide",A={},d=[{value:"Panel Not Loading",id:"panel-not-loading",level:2},{value:"No Entries Showing",id:"no-entries-showing",level:2},{value:"Error: &quot;plugin.yml not found&quot; When Installing an Adapter",id:"error-pluginyml-not-found-when-installing-an-adapter",level:2},{value:"Placeholders Not Parsing Correctly",id:"placeholders-not-parsing-correctly",level:2}];function c(e){let i={a:"a",code:"code",h1:"h1",h2:"h2",header:"header",li:"li",ol:"ol",p:"p",pre:"pre",strong:"strong",...(0,o.R)(),...e.components};return(0,s.jsxs)(s.Fragment,{children:[(0,s.jsx)(i.header,{children:(0,s.jsx)(i.h1,{id:"troubleshooting-guide",children:"Troubleshooting Guide"})}),"\n",(0,s.jsx)(i.p,{children:"Welcome to the Typewriter Plugin Troubleshooting Guide. This document aims to assist users in resolving common issues encountered while using the Typewriter plugin for Paper Spigot. Before reaching out for assistance on Discord or GitHub, we encourage you to review this comprehensive list of frequently encountered issues and their solutions. This proactive approach can help you solve your problem more quickly and efficiently."}),"\n",(0,s.jsx)(i.h2,{id:"panel-not-loading",children:"Panel Not Loading"}),"\n",(0,s.jsx)(i.p,{children:"If you encounter issues with the panel not loading, please follow these steps:"}),"\n",(0,s.jsxs)(i.ol,{children:["\n",(0,s.jsx)(i.li,{children:"Confirm that your server is running and accessible."}),"\n",(0,s.jsx)(i.li,{children:"Check if there are any network connectivity issues."}),"\n",(0,s.jsx)(i.li,{children:"Ensure your firewall settings are not blocking access."}),"\n"]}),"\n",(0,s.jsxs)(i.p,{children:["For more detailed troubleshooting steps, refer to: ",(0,s.jsx)(i.a,{href:"troubleshooting/ports",children:"Panel not loading"}),"."]}),"\n",(0,s.jsx)(i.h2,{id:"no-entries-showing",children:"No Entries Showing"}),"\n",(0,s.jsx)(r.A,{img:t(53083),alt:"No Entries"}),"\n",(0,s.jsxs)(i.p,{children:["This issue typically occurs when no adapters are installed. To resolve this, ensure that adapters are correctly installed by following the steps outlined in the ",(0,s.jsx)(i.strong,{children:(0,s.jsx)(i.a,{href:"/0.5.1/docs/getting-started/installation#basic-adapter",children:"Basic Adapter Installation Tutorial"})}),"."]}),"\n",(0,s.jsx)(i.h2,{id:"error-pluginyml-not-found-when-installing-an-adapter",children:'Error: "plugin.yml not found" When Installing an Adapter'}),"\n",(0,s.jsx)(i.p,{children:"If you encounter an error message similar to the following when installing an adapter:"}),"\n",(0,s.jsx)(i.pre,{children:(0,s.jsx)(i.code,{className:"language-log",children:"[08:49:35 ERROR]: [DirectoryProviderSource] Error loading plugin: Directory 'plugins/BasicAdapter.jar' failed to load!\njava.lang.RuntimeException: Directory 'plugins/BasicAdapter.jar' failed to load!\n        ...\nCaused by: java.lang.IllegalArgumentException: Directory 'plugins/BasicAdapter.jar' does not contain a paper-plugin.yml or plugin.yml! Could not determine plugin type, cannot load a plugin from it!\n"})}),"\n",(0,s.jsxs)(i.p,{children:["This typically indicates that the adapter is not placed in the correct folder. For instructions on proper placement, see ",(0,s.jsx)(i.a,{href:"/0.5.1/docs/troubleshooting/adapters#correct-adapter-placement",children:"Correct Adapter Placement"}),"."]}),"\n",(0,s.jsx)(i.h2,{id:"placeholders-not-parsing-correctly",children:"Placeholders Not Parsing Correctly"}),"\n",(0,s.jsxs)(i.p,{children:["If placeholders from PlaceholderAPI, such as ",(0,s.jsx)(i.code,{children:"%player_name%"}),", aren't being parsed correctly in Typewriter, follow these troubleshooting steps:"]}),"\n",(0,s.jsxs)(i.ol,{children:["\n",(0,s.jsxs)(i.li,{children:["Identify the required PlaceholderAPI placeholder extension. A list of extensions can be found ",(0,s.jsx)(i.a,{href:"https://github.com/PlaceholderAPI/PlaceholderAPI/wiki/Placeholders",children:"here"}),"."]}),"\n",(0,s.jsxs)(i.li,{children:["Install the necessary extension using ",(0,s.jsx)(i.code,{children:"/papi ecloud install [extension_name]"}),". For ",(0,s.jsx)(i.code,{children:"%player_name%"}),", use ",(0,s.jsx)(i.code,{children:"/papi ecloud download player"}),"."]}),"\n",(0,s.jsxs)(i.li,{children:["Check if the placeholder is parsed correctly in normal usage by running ",(0,s.jsx)(i.code,{children:"/papi parse me <placeholder>"}),". For ",(0,s.jsx)(i.code,{children:"%player_name%"}),", use ",(0,s.jsx)(i.code,{children:"/papi parse me %player_name%"}),"."]}),"\n",(0,s.jsx)(i.li,{children:"If the placeholder works in normal usage but still not in Typewriter, try restarting your server."}),"\n"]}),"\n",(0,s.jsxs)(i.p,{children:["If these steps do not resolve the issue, please contact us for further support by creating a ticket on our ",(0,s.jsx)(i.a,{href:"https://discord.gg/HtbKyuDDBw",children:"Discord"}),"."]})]})}function h(e={}){let{wrapper:i}={...(0,o.R)(),...e.components};return i?(0,s.jsx)(i,{...e,children:(0,s.jsx)(c,{...e})}):c(e)}},53083:(e,i,t)=>{e.exports={srcSet:t.p+"assets/optimized-img/no-entries.5ef1720.320.avif 320w,"+t.p+"assets/optimized-img/no-entries.7d159df.640.avif 640w,"+t.p+"assets/optimized-img/no-entries.9a9c932.960.avif 960w,"+t.p+"assets/optimized-img/no-entries.40ba0cc.1280.avif 1280w,"+t.p+"assets/optimized-img/no-entries.fb3cc2e.1600.avif 1600w,"+t.p+"assets/optimized-img/no-entries.e27d6b4.1877.avif 1877w",images:[{path:t.p+"assets/optimized-img/no-entries.5ef1720.320.avif",width:320,height:156},{path:t.p+"assets/optimized-img/no-entries.7d159df.640.avif",width:640,height:313},{path:t.p+"assets/optimized-img/no-entries.9a9c932.960.avif",width:960,height:469},{path:t.p+"assets/optimized-img/no-entries.40ba0cc.1280.avif",width:1280,height:625},{path:t.p+"assets/optimized-img/no-entries.fb3cc2e.1600.avif",width:1600,height:782},{path:t.p+"assets/optimized-img/no-entries.e27d6b4.1877.avif",width:1877,height:917}],src:t.p+"assets/optimized-img/no-entries.e27d6b4.1877.avif",toString:function(){return t.p+"assets/optimized-img/no-entries.e27d6b4.1877.avif"},placeholder:"data:image/avif;base64,AAAAHGZ0eXBhdmlmAAAAAGF2aWZtaWYxbWlhZgAAAYRtZXRhAAAAAAAAACFoZGxyAAAAAAAAAABwaWN0AAAAAAAAAAAAAAAAAAAAAA5waXRtAAAAAAABAAAANGlsb2MAAAAAREAAAgACAAAAAAGoAAEAAAAAAAAAFQABAAAAAAG9AAEAAAAAAAAAdwAAADhpaW5mAAAAAAACAAAAFWluZmUCAAAAAAEAAGF2MDEAAAAAFWluZmUCAAAAAAIAAGF2MDEAAAAAw2lwcnAAAACdaXBjbwAAABNjb2xybmNseAABAA0ABoAAAAAMYXYxQ4EAHAAAAAAUaXNwZQAAAAAAAAAoAAAAFAAAAA5waXhpAAAAAAEIAAAAOGF1eEMAAAAAdXJuOm1wZWc6bXBlZ0I6Y2ljcDpzeXN0ZW1zOmF1eGlsaWFyeTphbHBoYQAAAAAMYXYxQ4EgAgAAAAAQcGl4aQAAAAADCAgIAAAAHmlwbWEAAAAAAAAAAgABBAGGAwcAAgSCAwSFAAAAGmlyZWYAAAAAAAAADmF1eGwAAgABAAEAAACUbWRhdBIACgYYFSebCoAyCRZAAAEABjT3qBIACgk4FSebCAhoNIAyaBZABBBRhQDxzCabwkiCa4BS4pxnkSnc3vOQtz5zU6jsvaHaQ08XhC3T6Fm3tZ2NNO2WhZMMOT5mKFCUjDmZZAUjv4KcpBTBnmFUOhm6DUN1B3QnDuYwfPCQ7v/djVVVBwwLkZQGr8nk",width:1877,height:917}},5453:(e,i,t)=>{"use strict";t.d(i,{A:()=>o});var n=t(74848),s=t(96540);function o(e){let{img:i,...t}=e;if("string"==typeof i||"default"in i)return(0,n.jsx)("div",{className:"w-full h-full flex justify-center items-center pb-10",children:(0,n.jsx)("img",{src:"string"==typeof i?i:i.default,loading:"lazy",decoding:"async",className:"rounded-md",...t})});let[o,r]=(0,s.useState)(!1);return(0,n.jsxs)("div",{className:"w-full h-full flex justify-center items-center relative",children:[(0,n.jsx)("img",{src:i.src,srcSet:i.srcSet,sizes:"(max-width: 320px) 280px, (max-width: 640px) 600px, 1200px",loading:"lazy",decoding:"async",onLoad:()=>r(!0),className:`rounded-md transition-opacity duration-300 ${o?"opacity-100":"opacity-0"}`,...t}),!o&&(0,n.jsx)("div",{className:"absolute inset-0 bg-cover bg-center rounded-md",style:{backgroundImage:`url(${i.placeholder})`}})]})}}}]);