(self.webpackChunkdocumentation=self.webpackChunkdocumentation||[]).push([["66721"],{28796:function(e,n,t){"use strict";t.r(n),t.d(n,{metadata:()=>i,contentTitle:()=>d,default:()=>p,assets:()=>c,toc:()=>h,frontMatter:()=>a});var i=JSON.parse('{"id":"docs/getting-started/installation","title":"Installation Guide","description":"TypeWriter only works on paper servers. It will not work on Spigot or Bukkit servers.","source":"@site/versioned_docs/version-0.6.0/docs/02-getting-started/01-installation.mdx","sourceDirName":"docs/02-getting-started","slug":"/docs/getting-started/installation","permalink":"/docs/getting-started/installation","draft":false,"unlisted":false,"editUrl":"https://github.com/gabber235/TypeWriter/tree/develop/documentation/versioned_docs/version-0.6.0/docs/02-getting-started/01-installation.mdx","tags":[],"version":"0.6.0","lastUpdatedBy":"dependabot[bot]","lastUpdatedAt":1731173593000,"sidebarPosition":1,"frontMatter":{"difficulty":"Easy"},"sidebar":"tutorialSidebar","previous":{"title":"Getting Started","permalink":"/docs/getting-started/"},"next":{"title":"Layout","permalink":"/docs/getting-started/layout"}}'),s=t("85893"),o=t("50065"),r=t("99642"),l=t("68329");let a={difficulty:"Easy"},d="Installation Guide",c={},h=[{value:"Installing the TypeWriter Plugin",id:"installing-the-typewriter-plugin",level:2},{value:"Plugin Installation",id:"plugin-installation",level:3},{value:"Handling Dependencies",id:"handling-dependencies",level:3},{value:"Basic Extension",id:"basic-extension",level:3},{value:"Configuring the Web Panel",id:"configuring-the-web-panel",level:2},{value:"Enabling the Web Panel",id:"enabling-the-web-panel",level:3},{value:"Connecting to the Web Panel",id:"connecting-to-the-web-panel",level:2},{value:"Troubleshooting",id:"troubleshooting",level:2},{value:"What&#39;s Next?",id:"whats-next",level:2}];function A(e){let n={a:"a",admonition:"admonition",code:"code",h1:"h1",h2:"h2",h3:"h3",header:"header",img:"img",li:"li",ol:"ol",p:"p",pre:"pre",strong:"strong",...(0,o.a)(),...e.components};return(0,s.jsxs)(s.Fragment,{children:[(0,s.jsx)(n.header,{children:(0,s.jsx)(n.h1,{id:"installation-guide",children:"Installation Guide"})}),"\n",(0,s.jsx)(n.admonition,{title:"Platform Compatibility",type:"danger",children:(0,s.jsxs)(n.p,{children:["TypeWriter only works on ",(0,s.jsx)(n.strong,{children:"paper"})," servers. It will not work on Spigot or Bukkit servers."]})}),"\n",(0,s.jsx)(n.h2,{id:"installing-the-typewriter-plugin",children:"Installing the TypeWriter Plugin"}),"\n",(0,s.jsx)(n.p,{children:"Ensure a smooth installation process for the TypeWriter plugin on your Paper Minecraft server by following these steps:"}),"\n",(0,s.jsx)(n.h3,{id:"plugin-installation",children:"Plugin Installation"}),"\n",(0,s.jsxs)(n.ol,{children:["\n",(0,s.jsxs)(n.li,{children:["Start by downloading ",(0,s.jsx)(r.Z,{}),"."]}),"\n",(0,s.jsxs)(n.li,{children:["Place the downloaded plugin into your server's ",(0,s.jsx)(n.code,{children:"plugins"})," folder."]}),"\n"]}),"\n",(0,s.jsx)(n.h3,{id:"handling-dependencies",children:"Handling Dependencies"}),"\n",(0,s.jsx)(n.p,{children:"TypeWriter relies on additional plugins that need to be installed for proper functioning."}),"\n",(0,s.jsxs)(n.ol,{start:"3",children:["\n",(0,s.jsxs)(n.li,{children:["\n",(0,s.jsxs)(n.p,{children:["Download and add TypeWriter's dependency, ",(0,s.jsx)(n.a,{href:"https://modrinth.com/plugin/packetevents/versions?l=paper",children:"PacketEvents"}),", to your ",(0,s.jsx)(n.code,{children:"plugins"})," directory."]}),"\n"]}),"\n",(0,s.jsxs)(n.li,{children:["\n",(0,s.jsxs)(n.p,{children:["Verify that you ",(0,s.jsx)(n.strong,{children:"don't"})," have the ",(0,s.jsx)(n.a,{href:"https://www.spigotmc.org/resources/interactivechat-show-items-inventory-in-chat-custom-chat-keywords-bungee-velocity-support.75870/",children:"InteractiveChat"})," or ",(0,s.jsx)(n.a,{href:"https://polymart.org/resource/eco.773",children:"Eco"})," plugin installed, as it may cause conflicts with TypeWriter."]}),"\n"]}),"\n"]}),"\n",(0,s.jsx)(n.h3,{id:"basic-extension",children:"Basic Extension"}),"\n",(0,s.jsxs)(n.p,{children:["The TypeWriter offers various extensions for customization, but it's crucial to have the ",(0,s.jsx)(n.code,{children:"BasicExtension"})," installed."]}),"\n",(0,s.jsxs)(n.ol,{start:"5",children:["\n",(0,s.jsxs)(n.li,{children:["Download the latest ",(0,s.jsx)(n.code,{children:"BasicExtension"})," from ",(0,s.jsx)(r.Z,{})," and add it to the ",(0,s.jsx)(n.code,{children:"plugins/TypeWriter/extensions"})," folder."]}),"\n"]}),"\n",(0,s.jsxs)(n.p,{children:["For a comprehensive list of available extensions, refer to the ",(0,s.jsx)(n.a,{href:"../../adapters",children:"extensions section"}),"."]}),"\n",(0,s.jsxs)(n.ol,{start:"6",children:["\n",(0,s.jsx)(n.li,{children:"With all components in place, restart your Minecraft server to complete the TypeWriter installation."}),"\n"]}),"\n",(0,s.jsx)(n.admonition,{title:"Out of Sync",type:"danger",children:(0,s.jsxs)(n.p,{children:["When updating the plugin, it's crucial to ",(0,s.jsx)(n.strong,{children:"always"})," install the corresponding extensions for that update. Failure to match the versions of the extension and plugin can result in the plugin not functioning correctly!"]})}),"\n",(0,s.jsx)(n.h2,{id:"configuring-the-web-panel",children:"Configuring the Web Panel"}),"\n",(0,s.jsxs)(n.admonition,{title:"External Server Providers",type:"caution",children:[(0,s.jsxs)(n.p,{children:["Typewriter's web panel is ",(0,s.jsx)(n.strong,{children:"not compatible"})," with some external server providers, including ",(0,s.jsx)(n.strong,{children:"Minehut"}),", ",(0,s.jsx)(n.strong,{children:"Aternos"}),", and ",(0,s.jsx)(n.strong,{children:"Apex"}),". These providers typically don\u2019t support multiple ports, though their support team may be able to open additional ports upon request."]}),(0,s.jsx)(n.p,{children:"If this isn't possible, you can still use all other features in Typewriter or consider setting up a local server with Typewriter installed."}),(0,s.jsxs)(n.p,{children:["For further assistance, feel free to reach out with questions on our ",(0,s.jsx)(n.a,{href:"https://discord.gg/HtbKyuDDBw",children:"Discord"}),"."]})]}),"\n",(0,s.jsx)(n.admonition,{title:"Resource consumption",type:"info",children:(0,s.jsx)(n.p,{children:"Please note that the web panel and web socket use precious resources on your server, and it is best to host the panel on your development server instead of on a production server."})}),"\n",(0,s.jsx)(n.p,{children:"Now that we have installed the plugin, we need to configure the web panel."}),"\n",(0,s.jsxs)(n.p,{children:["The web panel needs two ports to be open. These can be changed, but it does need at least two new ports to be open. The\nThe default ports are ",(0,s.jsx)(n.code,{children:"8080"})," and ",(0,s.jsx)(n.code,{children:"9092"}),"."]}),"\n",(0,s.jsx)(n.p,{children:"To enable the web panel, please follow these steps:"}),"\n",(0,s.jsx)(n.h3,{id:"enabling-the-web-panel",children:"Enabling the Web Panel"}),"\n",(0,s.jsxs)(n.ol,{children:["\n",(0,s.jsxs)(n.li,{children:["Open the ",(0,s.jsx)(n.code,{children:"plugins/Typewriter/config.yml"})," file."]}),"\n",(0,s.jsx)(n.li,{children:"Change the settings to the following:"}),"\n"]}),"\n",(0,s.jsx)(n.pre,{children:(0,s.jsx)(n.code,{className:"language-yaml",metastring:'title="plugins/Typewriter/config.yml"',children:'# Whether the web panel and web sockets are enabled.\nenabled: true\n# The hostname of the server. CHANGE THIS to your server\'s IP.\nhostname: "127.0.0.1"\n# The panel uses web sockets to sync changes to the server and allows you to work with multiple people at the same time.\nwebsocket:\n  # The port of the websocket server. Make sure this port is open.\n  port: 9092\n  # The authentication that is used. Leave unchanged if you don\'t know what you are doing.\n  auth: "session"\npanel:\n  # The panel can be disabled while the sockets are still open. Only disable this if you know what you are doing.\n  # If the web sockets are disabled, then the panel will always be disabled.\n  enabled: true\n  # The port of the web panel. Make sure this port is open.\n  port: 8080\n'})}),"\n",(0,s.jsxs)(n.ol,{start:"3",children:["\n",(0,s.jsxs)(n.li,{children:["Please use ",(0,s.jsx)(n.a,{href:"https://portchecker.co/",children:"portchecker"})," to check if your ports are open. If not, please open the ports yourself or contact your hosting company."]}),"\n",(0,s.jsx)(n.li,{children:"Restart your server to complete the installation."}),"\n"]}),"\n",(0,s.jsx)(n.h2,{id:"connecting-to-the-web-panel",children:"Connecting to the Web Panel"}),"\n",(0,s.jsx)(n.p,{children:"To connect to the web panel, follow these steps:"}),"\n",(0,s.jsxs)(n.ol,{children:["\n",(0,s.jsxs)(n.li,{children:["Run the command ",(0,s.jsx)(n.code,{children:"/typewriter connect"})," in-game. This will provide you with a link to access the web panel."]}),"\n"]}),"\n",(0,s.jsx)(n.p,{children:(0,s.jsx)(n.img,{alt:"Connect-command",src:t(86286).Z+"",width:"352",height:"42"})}),"\n",(0,s.jsxs)(n.ol,{start:"2",children:["\n",(0,s.jsx)(n.li,{children:"Open the link in your preferred web browser."}),"\n"]}),"\n",(0,s.jsx)(l.Z,{img:t(47586),width:400,alt:"Connect to the web panel in the book"}),"\n",(0,s.jsx)(n.p,{children:"Once you've successfully connected, you can use the web panel to create and configure quests, NPC dialogues, and more. The panel also allows you to view and edit your server's existing player interactions."}),"\n",(0,s.jsx)(n.h2,{id:"troubleshooting",children:"Troubleshooting"}),"\n",(0,s.jsxs)(n.p,{children:["Encountering problems? Check out the ",(0,s.jsx)(n.a,{href:"/docs/troubleshooting/",children:"troubleshooting"})," page for solutions to common issues.\nIf you still have issues, feel free to ask for help in the ",(0,s.jsx)(n.a,{href:"https://discord.gg/HtbKyuDDBw",children:"Discord"}),"."]}),"\n",(0,s.jsx)(n.h2,{id:"whats-next",children:"What's Next?"}),"\n",(0,s.jsxs)(n.p,{children:["Try creating your first interaction by following the ",(0,s.jsx)(n.a,{href:"/docs/creating-stories/interactions/",children:"Interactions"})," guide. If you have any questions, feel free to ask them in the ",(0,s.jsx)(n.a,{href:"https://discord.gg/HtbKyuDDBw",children:"Discord"}),"."]})]})}function p(e={}){let{wrapper:n}={...(0,o.a)(),...e.components};return n?(0,s.jsx)(n,{...e,children:(0,s.jsx)(A,{...e})}):A(e)}},47586:function(e,n,t){e.exports={srcSet:t.p+"assets/optimized-img/connect-book.9cd4a9b.320.avif 320w,"+t.p+"assets/optimized-img/connect-book.c84f416.445.avif 445w",images:[{path:t.p+"assets/optimized-img/connect-book.9cd4a9b.320.avif",width:320,height:392},{path:t.p+"assets/optimized-img/connect-book.c84f416.445.avif",width:445,height:545}],src:t.p+"assets/optimized-img/connect-book.c84f416.445.avif",toString:function(){return t.p+"assets/optimized-img/connect-book.c84f416.445.avif"},placeholder:"data:image/avif;base64,AAAAHGZ0eXBhdmlmAAAAAGF2aWZtaWYxbWlhZgAAAYRtZXRhAAAAAAAAACFoZGxyAAAAAAAAAABwaWN0AAAAAAAAAAAAAAAAAAAAAA5waXRtAAAAAAABAAAANGlsb2MAAAAAREAAAgACAAAAAAGoAAEAAAAAAAAAFQABAAAAAAG9AAEAAAAAAAACjgAAADhpaW5mAAAAAAACAAAAFWluZmUCAAAAAAEAAGF2MDEAAAAAFWluZmUCAAAAAAIAAGF2MDEAAAAAw2lwcnAAAACdaXBjbwAAABNjb2xybmNseAABAA0ABoAAAAAMYXYxQ4EAHAAAAAAUaXNwZQAAAAAAAAAoAAAAMQAAAA5waXhpAAAAAAEIAAAAOGF1eEMAAAAAdXJuOm1wZWc6bXBlZ0I6Y2ljcDpzeXN0ZW1zOmF1eGlsaWFyeTphbHBoYQAAAAAMYXYxQ4EgAgAAAAAQcGl4aQAAAAADCAgIAAAAHmlwbWEAAAAAAAAAAgABBAGGAwcAAgSCAwSFAAAAGmlyZWYAAAAAAAAADmF1eGwAAgABAAEAAAKrbWRhdBIACgYYFWfBhUAyCRZAAAEABT8uqRIACgk4FWfBhAQ0GkAy/gQWQAYYIIUA3Z4n5/H42zd+wGt/i/KX+wfhDlUi/fyrQ3YBLKmVlR4aajGLbhIe5fbK3XGzd8NoG/f9t1Oax8sb+a4BxJhx2jENTdP1V0JVCFZyUYBpvkUWapn+zvq+pgFUbl264U/Gg1XZPZUPchoW6C3Ld0C9zRdhzsPXnG8pp4MG/3lN+ews4DmUOD72FU1IlZhmJG9yMII3ZcOHaK8kfP3gTZEP5/pzNQiXutIhbHXk9rjXOpHlQu2/NuQRkuTI6/6aOU1i/ddYYYJ497F8K4cdvxu8xGohllE280sGcYmX+62vX0WVYLMdK3ClNL707lP8AmWT2YjEXmb/b9pL+CCLv2glF2D5CZuLdBQrxDCFUHT9+qsCEnT5J1EobHjZJea1FeeeXZ3lzqSnQju9hX4WOl/anhFYwbOga0lzLm3VusL5myj7WwiuOEWMLMfpj/g4iKh2IHK6PPqCn6R8USQEuLV6caQw+3EfSAccTxJJZgSoDWkKEbG6TNw7w2iFY0wqc4B0nNQlhnzIYNFiGogeO+UoDLW57mxGiE7JXez9i61oJ92i+8vznwBr2UY0HQiCLrclICjfO4bdn1B6yS0ajbyYvrHQOjyU3p1kS1QGRAdXVA/90g3jNOMNVbmfOlnUHBC2IBmLiOvybxT4aNO8DWzHbOK4WFPCfZ8Hz77/3d9SbHrcNfQuurLWap4eK+5UT25d5yYw9T28mUUyZi2r1almxIpS1Xyue6plHMBT4mU0rl7q9EgJ2KtkyExz6dVVs4i+vZ16oR0s/a6voORHsyJaeQn1JUGlrU2GtQYOXdMimMJTTiXeD8XUVxpGITTa4DhNrNXrc1EqGA==",width:445,height:545}},86286:function(e,n,t){"use strict";t.d(n,{Z:function(){return i}});let i=t.p+"assets/images/connect-command-00f3ad6eb18bd0022adde6278b4cfab7.gif"},99642:function(e,n,t){"use strict";t.d(n,{Z:function(){return r}});var i=t(85893);t(67294);var s=t(50475),o=t(31183);function r(){let e=s.zu(void 0)?.name,n=s.yW(void 0)?.name;return"current"===e?(0,i.jsxs)("span",{children:["the ",(0,i.jsx)("b",{children:"Beta"})," version via",(0,i.jsxs)(o.Z,{to:"https://modrinth.com/plugin/typewriter/versions?c=beta",children:[" ","this Modrinth link"]})]}):n===e?(0,i.jsxs)("span",{children:["The ",(0,i.jsx)("b",{children:"Latest"})," version via",(0,i.jsxs)(o.Z,{to:`https://modrinth.com/plugin/typewriter/version/${encodeURIComponent(e)}`,children:[" ","this Modrinth link"]})]}):(0,i.jsxs)("span",{children:["The ",(0,i.jsx)("b",{children:"Outdated"})," version ",e," via",(0,i.jsxs)(o.Z,{to:`https://modrinth.com/plugin/typewriter/version/${encodeURIComponent(e)}`,children:[" ","this Modrinth link"]})]})}},68329:function(e,n,t){"use strict";t.d(n,{Z:function(){return o}});var i=t(85893),s=t(67294);function o(e){let{img:n,...t}=e;if("string"==typeof n||"default"in n)return(0,i.jsx)("div",{className:"w-full h-full flex justify-center items-center pb-10",children:(0,i.jsx)("img",{src:"string"==typeof n?n:n.default,loading:"lazy",decoding:"async",className:"rounded-md",...t})});let[o,r]=(0,s.useState)(!1);return(0,i.jsxs)("div",{className:"w-full h-full flex justify-center items-center relative",children:[(0,i.jsx)("img",{src:n.src,srcSet:n.srcSet,sizes:"(max-width: 320px) 280px, (max-width: 640px) 600px, 1200px",loading:"lazy",decoding:"async",onLoad:()=>r(!0),className:`rounded-md transition-opacity duration-300 ${o?"opacity-100":"opacity-0"}`,...t}),!o&&(0,i.jsx)("div",{className:"absolute inset-0 bg-cover bg-center rounded-md",style:{backgroundImage:`url(${n.placeholder})`}})]})}}}]);