(self.webpackChunkdocumentation=self.webpackChunkdocumentation||[]).push([[83878],{64470:(e,i,n)=>{"use strict";n.r(i),n.d(i,{assets:()=>A,contentTitle:()=>a,default:()=>h,frontMatter:()=>l,metadata:()=>s,toc:()=>d});let s=JSON.parse('{"id":"docs/troubleshooting/index","title":"Troubleshooting Guide","description":"Welcome to the Typewriter Plugin Troubleshooting Guide. This document aims to assist users in resolving common issues encountered while using the Typewriter plugin for Paper Spigot. Before reaching out for assistance on Discord or GitHub, we encourage you to review this comprehensive list of frequently encountered issues and their solutions. This proactive approach can help you solve your problem more quickly and efficiently.","source":"@site/versioned_docs/version-0.6.1/docs/06-troubleshooting/index.mdx","sourceDirName":"docs/06-troubleshooting","slug":"/docs/troubleshooting/","permalink":"/0.6.1/docs/troubleshooting/","draft":false,"unlisted":false,"editUrl":"https://github.com/gabber235/TypeWriter/tree/develop/documentation/versioned_docs/version-0.6.1/docs/06-troubleshooting/index.mdx","tags":[],"version":"0.6.1","lastUpdatedBy":"Gabber235","lastUpdatedAt":1734557994000,"frontMatter":{"difficulty":"Normal"},"sidebar":"tutorialSidebar","previous":{"title":"Snippets","permalink":"/0.6.1/docs/helpful-features/snippets"},"next":{"title":"Extensions Troubleshooting","permalink":"/0.6.1/docs/troubleshooting/extensions"}}');var t=n(74848),o=n(28453),r=n(5453);let l={difficulty:"Normal"},a="Troubleshooting Guide",A={},d=[{value:"Panel Not Loading",id:"panel-not-loading",level:2},{value:"No Entries Showing",id:"no-entries-showing",level:2},{value:"Error: &quot;plugin.yml not found&quot; When Installing an Extension",id:"error-pluginyml-not-found-when-installing-an-extension",level:2},{value:"Placeholders Not Parsing Correctly",id:"placeholders-not-parsing-correctly",level:2}];function c(e){let i={a:"a",code:"code",h1:"h1",h2:"h2",header:"header",li:"li",ol:"ol",p:"p",pre:"pre",strong:"strong",...(0,o.R)(),...e.components};return(0,t.jsxs)(t.Fragment,{children:[(0,t.jsx)(i.header,{children:(0,t.jsx)(i.h1,{id:"troubleshooting-guide",children:"Troubleshooting Guide"})}),"\n",(0,t.jsx)(i.p,{children:"Welcome to the Typewriter Plugin Troubleshooting Guide. This document aims to assist users in resolving common issues encountered while using the Typewriter plugin for Paper Spigot. Before reaching out for assistance on Discord or GitHub, we encourage you to review this comprehensive list of frequently encountered issues and their solutions. This proactive approach can help you solve your problem more quickly and efficiently."}),"\n",(0,t.jsx)(i.h2,{id:"panel-not-loading",children:"Panel Not Loading"}),"\n",(0,t.jsx)(i.p,{children:"If you encounter issues with the panel not loading, please follow these steps:"}),"\n",(0,t.jsxs)(i.ol,{children:["\n",(0,t.jsx)(i.li,{children:"Confirm that your server is running and accessible."}),"\n",(0,t.jsx)(i.li,{children:"Check if there are any network connectivity issues."}),"\n",(0,t.jsx)(i.li,{children:"Ensure your firewall settings are not blocking access."}),"\n"]}),"\n",(0,t.jsxs)(i.p,{children:["For more detailed troubleshooting steps, refer to: ",(0,t.jsx)(i.a,{href:"troubleshooting/ports",children:"Panel not loading"}),"."]}),"\n",(0,t.jsx)(i.h2,{id:"no-entries-showing",children:"No Entries Showing"}),"\n",(0,t.jsx)(r.A,{img:n(17854),alt:"No Entries"}),"\n",(0,t.jsxs)(i.p,{children:["This issue typically occurs when no extensions are installed. To resolve this, ensure that extensions are correctly installed by following the steps outlined in the ",(0,t.jsx)(i.strong,{children:(0,t.jsx)(i.a,{href:"/0.6.1/docs/getting-started/installation#basic-extension",children:"Basic Extension Installation Tutorial"})}),"."]}),"\n",(0,t.jsx)(i.h2,{id:"error-pluginyml-not-found-when-installing-an-extension",children:'Error: "plugin.yml not found" When Installing an Extension'}),"\n",(0,t.jsx)(i.p,{children:"If you encounter an error message similar to the following when installing an extension:"}),"\n",(0,t.jsx)(i.pre,{children:(0,t.jsx)(i.code,{className:"language-log",children:"[08:49:35 ERROR]: [DirectoryProviderSource] Error loading plugin: Directory 'plugins/BasicExtension.jar' failed to load!\njava.lang.RuntimeException: Directory 'plugins/BasicExtension.jar' failed to load!\n        ...\nCaused by: java.lang.IllegalArgumentException: Directory 'plugins/BasicExtension.jar' does not contain a paper-plugin.yml or plugin.yml! Could not determine plugin type, cannot load a plugin from it!\n"})}),"\n",(0,t.jsxs)(i.p,{children:["This typically indicates that the extension is not placed in the correct folder. For instructions on proper placement, see ",(0,t.jsx)(i.a,{href:"/0.6.1/docs/troubleshooting/extensions#correct-extension-placement",children:"Correct Extension Placement"}),"."]}),"\n",(0,t.jsx)(i.h2,{id:"placeholders-not-parsing-correctly",children:"Placeholders Not Parsing Correctly"}),"\n",(0,t.jsxs)(i.p,{children:["If placeholders from PlaceholderAPI, such as ",(0,t.jsx)(i.code,{children:"%player_name%"}),", aren't being parsed correctly in Typewriter, follow these troubleshooting steps:"]}),"\n",(0,t.jsxs)(i.ol,{children:["\n",(0,t.jsxs)(i.li,{children:["Identify the required PlaceholderAPI placeholder extension. A list of extensions can be found ",(0,t.jsx)(i.a,{href:"https://github.com/PlaceholderAPI/PlaceholderAPI/wiki/Placeholders",children:"here"}),"."]}),"\n",(0,t.jsxs)(i.li,{children:["Install the necessary extension using ",(0,t.jsx)(i.code,{children:"/papi ecloud download [extension_name]"}),". For ",(0,t.jsx)(i.code,{children:"%player_name%"}),", use ",(0,t.jsx)(i.code,{children:"/papi ecloud download player"}),"."]}),"\n",(0,t.jsxs)(i.li,{children:["Check if the placeholder is parsed correctly in normal usage by running ",(0,t.jsx)(i.code,{children:"/papi parse me <placeholder>"}),". For ",(0,t.jsx)(i.code,{children:"%player_name%"}),", use ",(0,t.jsx)(i.code,{children:"/papi parse me %player_name%"}),"."]}),"\n",(0,t.jsx)(i.li,{children:"If the placeholder works in normal usage but still not in Typewriter, try restarting your server."}),"\n"]}),"\n",(0,t.jsxs)(i.p,{children:["If these steps do not resolve the issue, please contact us for further support by creating a ticket on our ",(0,t.jsx)(i.a,{href:"https://discord.gg/HtbKyuDDBw",children:"Discord"}),"."]})]})}function h(e={}){let{wrapper:i}={...(0,o.R)(),...e.components};return i?(0,t.jsx)(i,{...e,children:(0,t.jsx)(c,{...e})}):c(e)}},17854:(e,i,n)=>{e.exports={srcSet:n.p+"assets/optimized-img/no-entries.5ef1720.320.avif 320w,"+n.p+"assets/optimized-img/no-entries.7d159df.640.avif 640w,"+n.p+"assets/optimized-img/no-entries.9a9c932.960.avif 960w,"+n.p+"assets/optimized-img/no-entries.40ba0cc.1280.avif 1280w,"+n.p+"assets/optimized-img/no-entries.fb3cc2e.1600.avif 1600w,"+n.p+"assets/optimized-img/no-entries.e27d6b4.1877.avif 1877w",images:[{path:n.p+"assets/optimized-img/no-entries.5ef1720.320.avif",width:320,height:156},{path:n.p+"assets/optimized-img/no-entries.7d159df.640.avif",width:640,height:313},{path:n.p+"assets/optimized-img/no-entries.9a9c932.960.avif",width:960,height:469},{path:n.p+"assets/optimized-img/no-entries.40ba0cc.1280.avif",width:1280,height:625},{path:n.p+"assets/optimized-img/no-entries.fb3cc2e.1600.avif",width:1600,height:782},{path:n.p+"assets/optimized-img/no-entries.e27d6b4.1877.avif",width:1877,height:917}],src:n.p+"assets/optimized-img/no-entries.e27d6b4.1877.avif",toString:function(){return n.p+"assets/optimized-img/no-entries.e27d6b4.1877.avif"},placeholder:"data:image/avif;base64,AAAAHGZ0eXBhdmlmAAAAAGF2aWZtaWYxbWlhZgAAAYRtZXRhAAAAAAAAACFoZGxyAAAAAAAAAABwaWN0AAAAAAAAAAAAAAAAAAAAAA5waXRtAAAAAAABAAAANGlsb2MAAAAAREAAAgACAAAAAAGoAAEAAAAAAAAAFQABAAAAAAG9AAEAAAAAAAAAdwAAADhpaW5mAAAAAAACAAAAFWluZmUCAAAAAAEAAGF2MDEAAAAAFWluZmUCAAAAAAIAAGF2MDEAAAAAw2lwcnAAAACdaXBjbwAAABNjb2xybmNseAABAA0ABoAAAAAMYXYxQ4EAHAAAAAAUaXNwZQAAAAAAAAAoAAAAFAAAAA5waXhpAAAAAAEIAAAAOGF1eEMAAAAAdXJuOm1wZWc6bXBlZ0I6Y2ljcDpzeXN0ZW1zOmF1eGlsaWFyeTphbHBoYQAAAAAMYXYxQ4EgAgAAAAAQcGl4aQAAAAADCAgIAAAAHmlwbWEAAAAAAAAAAgABBAGGAwcAAgSCAwSFAAAAGmlyZWYAAAAAAAAADmF1eGwAAgABAAEAAACUbWRhdBIACgYYFSebCoAyCRZAAAEABjT3qBIACgk4FSebCAhoNIAyaBZABBBRhQDxzCabwkiCa4BS4pxnkSnc3vOQtz5zU6jsvaHaQ08XhC3T6Fm3tZ2NNO2WhZMMOT5mKFCUjDmZZAUjv4KcpBTBnmFUOhm6DUN1B3QnDuYwfPCQ7v/djVVVBwwLkZQGr8nk",width:1877,height:917}},5453:(e,i,n)=>{"use strict";n.d(i,{A:()=>o});var s=n(74848),t=n(96540);function o(e){let{img:i,...n}=e;if("string"==typeof i||"default"in i)return(0,s.jsx)("div",{className:"w-full h-full flex justify-center items-center pb-10",children:(0,s.jsx)("img",{src:"string"==typeof i?i:i.default,loading:"lazy",decoding:"async",className:"rounded-md",...n})});let[o,r]=(0,t.useState)(!1);return(0,s.jsxs)("div",{className:"w-full h-full flex justify-center items-center relative",children:[(0,s.jsx)("img",{src:i.src,srcSet:i.srcSet,sizes:"(max-width: 320px) 280px, (max-width: 640px) 600px, 1200px",loading:"lazy",decoding:"async",onLoad:()=>r(!0),className:`rounded-md transition-opacity duration-300 ${o?"opacity-100":"opacity-0"}`,...n}),!o&&(0,s.jsx)("div",{className:"absolute inset-0 bg-cover bg-center rounded-md",style:{backgroundImage:`url(${i.placeholder})`}})]})}}}]);