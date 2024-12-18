(self.webpackChunkdocumentation=self.webpackChunkdocumentation||[]).push([[30119],{38511:(e,A,t)=>{"use strict";t.r(A),t.d(A,{assets:()=>o,contentTitle:()=>d,default:()=>h,frontMatter:()=>r,metadata:()=>i,toc:()=>c});let i=JSON.parse('{"id":"docs/helpful-features/placeholderapi","title":"PlaceHolderAPI","description":"To follow this tutorial, you must have the PlaceHolderAPI installed.","source":"@site/versioned_docs/version-0.6.1/docs/05-helpful-features/03-placeholderapi.mdx","sourceDirName":"docs/05-helpful-features","slug":"/docs/helpful-features/placeholderapi","permalink":"/0.6.1/docs/helpful-features/placeholderapi","draft":false,"unlisted":false,"editUrl":"https://github.com/gabber235/TypeWriter/tree/develop/documentation/versioned_docs/version-0.6.1/docs/05-helpful-features/03-placeholderapi.mdx","tags":[],"version":"0.6.1","lastUpdatedBy":"Gabber235","lastUpdatedAt":1734557994000,"sidebarPosition":3,"frontMatter":{"difficulty":"Easy"},"sidebar":"tutorialSidebar","previous":{"title":"Commands","permalink":"/0.6.1/docs/helpful-features/commands"},"next":{"title":"Shortcuts","permalink":"/0.6.1/docs/helpful-features/shortcuts"}}');var s=t(74848),a=t(28453),l=t(5453);let r={difficulty:"Easy"},d="PlaceHolderAPI",o={},c=[{value:"Use PlaceHolderAPI&#39;s Placeholders",id:"use-placeholderapis-placeholders",level:2},{value:"Use TypeWriter&#39;s Placeholders",id:"use-typewriters-placeholders",level:2},{value:"Custom placeholders",id:"custom-placeholders",level:3}];function n(e){let A={a:"a",admonition:"admonition",br:"br",code:"code",h1:"h1",h2:"h2",h3:"h3",header:"header",li:"li",p:"p",ul:"ul",...(0,a.R)(),...e.components};return(0,s.jsxs)(s.Fragment,{children:[(0,s.jsx)(A.header,{children:(0,s.jsx)(A.h1,{id:"placeholderapi",children:"PlaceHolderAPI"})}),"\n",(0,s.jsx)(A.admonition,{title:"plugin needed",type:"warning",children:(0,s.jsxs)(A.p,{children:["To follow this tutorial, you must have the ",(0,s.jsx)(A.a,{href:"https://www.spigotmc.org/resources/placeholderapi.6245/",children:"PlaceHolderAPI"})," installed."]})}),"\n",(0,s.jsx)(A.h2,{id:"use-placeholderapis-placeholders",children:"Use PlaceHolderAPI's Placeholders"}),"\n",(0,s.jsx)(A.p,{children:"If you want to expand your story with PlaceHolderAPI's placeholders, you can do that by checking if the PlaceHolderAPI icon is placed above the text box (see image):"}),"\n",(0,s.jsx)(l.A,{img:t(71567),alt:"PlaceholderAPI Icon",width:400}),"\n",(0,s.jsxs)(A.p,{children:["By just using the PlaceHolderAPI's format, you can make them work. For example, ",(0,s.jsx)(A.code,{children:"%player_name%"})," would give the name of a player."]}),"\n",(0,s.jsx)(A.admonition,{title:"Not working",type:"danger",children:(0,s.jsxs)(A.p,{children:["If placeholders are not working, make sure you have the placeholder installed. For instance, with ",(0,s.jsx)(A.code,{children:"%player_name%"}),", you must have installed ",(0,s.jsx)(A.code,{children:"player"})," via the PlaceHolderAPI ecloud. To do this, just run: ",(0,s.jsx)(A.code,{children:"/papi ecloud download player"}),"."]})}),"\n",(0,s.jsx)(A.h2,{id:"use-typewriters-placeholders",children:"Use TypeWriter's Placeholders"}),"\n",(0,s.jsx)(A.p,{children:"TypeWriter provides two placeholders for use with the PlaceHolderAPI:"}),"\n",(0,s.jsxs)(A.ul,{children:["\n",(0,s.jsx)(A.li,{children:(0,s.jsx)(A.code,{children:"%typewriter_entryid%"})}),"\n",(0,s.jsx)(A.li,{children:(0,s.jsx)(A.code,{children:"%typewriter_entryname%"})}),"\n"]}),"\n",(0,s.jsx)(A.p,{children:"Both placeholders provide the same information, but you may choose to access them in different ways. It's generally recommended to use the entry ID for more reliable results, but the entry name is also an option."}),"\n",(0,s.jsx)(A.p,{children:"To find the entry ID, refer to the following image:"}),"\n",(0,s.jsx)(l.A,{img:t(42409),alt:"PlaceholderAPI Icon",width:500}),"\n",(0,s.jsxs)(A.p,{children:["Your placeholder for TypeWriter would be something like ",(0,s.jsx)(A.code,{children:"%typewriter_W2X2ZbG0pzXGsS6%"}),".",(0,s.jsx)(A.br,{}),"\n","Then, when calling the placeholder, it checks the ID and:"]}),"\n",(0,s.jsxs)(A.ul,{children:["\n",(0,s.jsx)(A.li,{children:"If their entry is a speaker, it gives the displayName back."}),"\n",(0,s.jsx)(A.li,{children:"If their entry is a fact, it gives the fact value back."}),"\n",(0,s.jsx)(A.li,{children:"If the entry is a sound, it gives the ID of the resource pack sound."}),"\n",(0,s.jsx)(A.li,{children:"If the entry is a entity, it gives the displayName of the entity"}),"\n",(0,s.jsx)(A.li,{children:"If the entry is a lines entry, it gives the content of the lines entry."}),"\n",(0,s.jsx)(A.li,{children:"If the entry is a quest, it gives the displayName of the quest."}),"\n",(0,s.jsx)(A.li,{children:"If the entry is a objective, it gives the formatted displayName of the objective."}),"\n",(0,s.jsx)(A.li,{children:"If the entry is a sidebar, it gives the title of the sidebar."}),"\n"]}),"\n",(0,s.jsx)(A.h3,{id:"custom-placeholders",children:"Custom placeholders"}),"\n",(0,s.jsx)(A.p,{children:"Typewriter also has 2 custom placeholders, these are:"}),"\n",(0,s.jsxs)(A.ul,{children:["\n",(0,s.jsxs)(A.li,{children:[(0,s.jsx)(A.code,{children:"%typewriter_tracked_quest%"}),": Shows the displayName of the currently tracked quest."]}),"\n",(0,s.jsxs)(A.li,{children:[(0,s.jsx)(A.code,{children:"%typewriter_tracked_objectives%"}),": Shows the objectives displayNames for the active objectives of the tracked quest."]}),"\n"]})]})}function h(e={}){let{wrapper:A}={...(0,a.R)(),...e.components};return A?(0,s.jsx)(A,{...e,children:(0,s.jsx)(n,{...e})}):n(e)}},42409:(e,A,t)=>{e.exports={srcSet:t.p+"assets/optimized-img/papi-factid.da020cf.320.avif 320w,"+t.p+"assets/optimized-img/papi-factid.fd56332.518.avif 518w",images:[{path:t.p+"assets/optimized-img/papi-factid.da020cf.320.avif",width:320,height:65},{path:t.p+"assets/optimized-img/papi-factid.fd56332.518.avif",width:518,height:106}],src:t.p+"assets/optimized-img/papi-factid.fd56332.518.avif",toString:function(){return t.p+"assets/optimized-img/papi-factid.fd56332.518.avif"},placeholder:"data:image/avif;base64,AAAAHGZ0eXBhdmlmAAAAAGF2aWZtaWYxbWlhZgAAAYRtZXRhAAAAAAAAACFoZGxyAAAAAAAAAABwaWN0AAAAAAAAAAAAAAAAAAAAAA5waXRtAAAAAAABAAAANGlsb2MAAAAAREAAAgACAAAAAAGoAAEAAAAAAAAAFAABAAAAAAG8AAEAAAAAAAAAhwAAADhpaW5mAAAAAAACAAAAFWluZmUCAAAAAAEAAGF2MDEAAAAAFWluZmUCAAAAAAIAAGF2MDEAAAAAw2lwcnAAAACdaXBjbwAAABNjb2xybmNseAABAA0ABoAAAAAMYXYxQ4EAHAAAAAAUaXNwZQAAAAAAAAAoAAAACAAAAA5waXhpAAAAAAEIAAAAOGF1eEMAAAAAdXJuOm1wZWc6bXBlZ0I6Y2ljcDpzeXN0ZW1zOmF1eGlsaWFyeTphbHBoYQAAAAAMYXYxQ4EgAgAAAAAQcGl4aQAAAAADCAgIAAAAHmlwbWEAAAAAAAAAAgABBAGGAwcAAgSCAwSFAAAAGmlyZWYAAAAAAAAADmF1eGwAAgABAAEAAACjbWRhdBIACgUYFKfsKjIJFkAAAQAGNPeoEgAKCDgUp+wgIaDSMnkWQAQQkgUA/wSEcUo64vVnWINBJVJ78BkIdfI5rPYRT7U61PFWESMg4YMa6kM1aJxZL5UGRePrT+NdlbFz1UxMNcN3eWf9ippTgPlIVQ+oj7tUJ03sfRj/FIB82axT4QFIDq1A/ccCCXMsrNTJBoUxZPIuBU1+NxsQ",width:518,height:106}},71567:(e,A,t)=>{e.exports={srcSet:t.p+"assets/optimized-img/papi-icon.f8a3539.320.avif 320w,"+t.p+"assets/optimized-img/papi-icon.9e33ece.565.avif 565w",images:[{path:t.p+"assets/optimized-img/papi-icon.f8a3539.320.avif",width:320,height:82},{path:t.p+"assets/optimized-img/papi-icon.9e33ece.565.avif",width:565,height:144}],src:t.p+"assets/optimized-img/papi-icon.9e33ece.565.avif",toString:function(){return t.p+"assets/optimized-img/papi-icon.9e33ece.565.avif"},placeholder:"data:image/avif;base64,AAAAHGZ0eXBhdmlmAAAAAGF2aWZtaWYxbWlhZgAAAYRtZXRhAAAAAAAAACFoZGxyAAAAAAAAAABwaWN0AAAAAAAAAAAAAAAAAAAAAA5waXRtAAAAAAABAAAANGlsb2MAAAAAREAAAgACAAAAAAGoAAEAAAAAAAAAFAABAAAAAAG8AAEAAAAAAAAAZgAAADhpaW5mAAAAAAACAAAAFWluZmUCAAAAAAEAAGF2MDEAAAAAFWluZmUCAAAAAAIAAGF2MDEAAAAAw2lwcnAAAACdaXBjbwAAABNjb2xybmNseAABAA0ABoAAAAAMYXYxQ4EAHAAAAAAUaXNwZQAAAAAAAAAoAAAACgAAAA5waXhpAAAAAAEIAAAAOGF1eEMAAAAAdXJuOm1wZWc6bXBlZ0I6Y2ljcDpzeXN0ZW1zOmF1eGlsaWFyeTphbHBoYQAAAAAMYXYxQ4EgAgAAAAAQcGl4aQAAAAADCAgIAAAAHmlwbWEAAAAAAAAAAgABBAGGAwcAAgSCAwSFAAAAGmlyZWYAAAAAAAAADmF1eGwAAgABAAEAAACCbWRhdBIACgUYFOeWFTIJFkAAAQAGNPeoEgAKCDgU55YQENBpMlgWQAQQQgUA+mFNXteRLNKTPlda/MZVtXdvAHs5FI7QYpfBvddHzqBgAFvMQbMIqiaND/MbucKLlJEGiGodVY1UW1hupGeIB2CWx6m0CpgSfYz433ixZgZG",width:565,height:144}},5453:(e,A,t)=>{"use strict";t.d(A,{A:()=>a});var i=t(74848),s=t(96540);function a(e){let{img:A,...t}=e;if("string"==typeof A||"default"in A)return(0,i.jsx)("div",{className:"w-full h-full flex justify-center items-center pb-10",children:(0,i.jsx)("img",{src:"string"==typeof A?A:A.default,loading:"lazy",decoding:"async",className:"rounded-md",...t})});let[a,l]=(0,s.useState)(!1);return(0,i.jsxs)("div",{className:"w-full h-full flex justify-center items-center relative",children:[(0,i.jsx)("img",{src:A.src,srcSet:A.srcSet,sizes:"(max-width: 320px) 280px, (max-width: 640px) 600px, 1200px",loading:"lazy",decoding:"async",onLoad:()=>l(!0),className:`rounded-md transition-opacity duration-300 ${a?"opacity-100":"opacity-0"}`,...t}),!a&&(0,i.jsx)("div",{className:"absolute inset-0 bg-cover bg-center rounded-md",style:{backgroundImage:`url(${A.placeholder})`}})]})}}}]);