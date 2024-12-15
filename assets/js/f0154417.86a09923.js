"use strict";(self.webpackChunkdocumentation=self.webpackChunkdocumentation||[]).push([[20335],{47981:(e,t,r)=>{r.r(t),r.d(t,{assets:()=>u,contentTitle:()=>c,default:()=>p,frontMatter:()=>o,metadata:()=>n,toc:()=>d});let n=JSON.parse('{"id":"develop/extensions/api-changes/0.6","title":"0.6.X API Changes","description":"As Typewriter changed from Adapters to Extensions, the API has changed significantly.","source":"@site/versioned_docs/version-0.6.1/develop/02-extensions/07-api-changes/0.6.mdx","sourceDirName":"develop/02-extensions/07-api-changes","slug":"/develop/extensions/api-changes/0.6","permalink":"/0.6.1/develop/extensions/api-changes/0.6","draft":false,"unlisted":false,"editUrl":"https://github.com/gabber235/TypeWriter/tree/develop/documentation/versioned_docs/version-0.6.1/develop/02-extensions/07-api-changes/0.6.mdx","tags":[],"version":"0.6.1","lastUpdatedBy":"Marten Mrfc","lastUpdatedAt":1734273100000,"frontMatter":{"title":"0.6.X API Changes"},"sidebar":"develop","previous":{"title":"0.5.X API Changes","permalink":"/0.6.1/develop/extensions/api-changes/0.5"}}');var a=r(74848),s=r(28453),l=r(53720),i=r(5400);let o={title:"0.6.X API Changes"},c="All API changes to 0.6.X",u={},d=[{value:"Messager Tick Parameter",id:"messager-tick-parameter",level:2}];function h(e){let t={a:"a",code:"code",h1:"h1",h2:"h2",header:"header",p:"p",pre:"pre",...(0,s.R)(),...e.components};return(0,a.jsxs)(a.Fragment,{children:[(0,a.jsx)(t.header,{children:(0,a.jsx)(t.h1,{id:"all-api-changes-to-06x",children:"All API changes to 0.6.X"})}),"\n",(0,a.jsxs)(t.p,{children:["As Typewriter changed from ",(0,a.jsx)(t.code,{children:"Adapters"})," to ",(0,a.jsx)(t.code,{children:"Extensions"}),", the API has changed significantly.\nIt is recommended to reread the ",(0,a.jsx)(t.a,{href:"/0.6.1/develop/extensions/getting_started",children:"Getting Started"})," guide to get a better understanding of how to create extensions."]}),"\n",(0,a.jsx)(t.h2,{id:"messager-tick-parameter",children:"Messager Tick Parameter"}),"\n",(0,a.jsxs)(l.A,{children:[(0,a.jsx)(i.A,{value:"old",label:"Old",children:(0,a.jsx)(t.pre,{children:(0,a.jsx)(t.code,{className:"language-kotlin",metastring:"showLineNumbers",children:"override fun tick(playTime: Duration) {\n    super.tick(playTime)\n    // Do something to show the message\n}\n"})})}),(0,a.jsx)(i.A,{value:"new",label:"New",default:!0,children:(0,a.jsx)(t.pre,{children:(0,a.jsx)(t.code,{className:"language-kotlin",metastring:"showLineNumbers",children:"override fun tick(context: TickContext) {\n    super.tick(context)\n    // Do something to show the message\n}\n"})})})]}),"\n",(0,a.jsxs)(t.p,{children:["The ",(0,a.jsx)(t.code,{children:"tick"})," parameter has been changed to ",(0,a.jsx)(t.code,{children:"context"})," to allow for more context to be passed to the ",(0,a.jsx)(t.code,{children:"tick"})," method."]})]})}function p(e={}){let{wrapper:t}={...(0,s.R)(),...e.components};return t?(0,a.jsx)(t,{...e,children:(0,a.jsx)(h,{...e})}):h(e)}},5400:(e,t,r)=>{r.d(t,{A:()=>s});var n=r(74848);r(96540);var a=r(34164);function s(e){let{children:t,hidden:r,className:s}=e;return(0,n.jsx)("div",{role:"tabpanel",className:(0,a.A)("tabItem_Ymn6",s),hidden:r,children:t})}},53720:(e,t,r)=>{r.d(t,{A:()=>x});var n=r(74848),a=r(96540),s=r(34164),l=r(65697),i=r(56347),o=r(56650),c=r(9226),u=r(34387),d=r(10426);function h(e){return a.Children.toArray(e).filter(e=>"\n"!==e).map(e=>{if(!e||a.isValidElement(e)&&function(e){let{props:t}=e;return!!t&&"object"==typeof t&&"value"in t}(e))return e;throw Error(`Docusaurus error: Bad <Tabs> child <${"string"==typeof e.type?e.type:e.type.name}>: all children of the <Tabs> component should be <TabItem>, and every <TabItem> should have a unique "value" prop.`)})?.filter(Boolean)??[]}function p(e){let{value:t,tabValues:r}=e;return r.some(e=>e.value===t)}var m=r(20162);function f(e){let{className:t,block:r,selectedValue:a,selectValue:i,tabValues:o}=e,c=[],{blockElementScrollPositionUntilNextRender:u}=(0,l.a_)(),d=e=>{let t=e.currentTarget,r=o[c.indexOf(t)].value;r!==a&&(u(t),i(r))},h=e=>{let t=null;switch(e.key){case"Enter":d(e);break;case"ArrowRight":{let r=c.indexOf(e.currentTarget)+1;t=c[r]??c[0];break}case"ArrowLeft":{let r=c.indexOf(e.currentTarget)-1;t=c[r]??c[c.length-1]}}t?.focus()};return(0,n.jsx)("ul",{role:"tablist","aria-orientation":"horizontal",className:(0,s.A)("tabs",{"tabs--block":r},t),children:o.map(e=>{let{value:t,label:r,attributes:l}=e;return(0,n.jsx)("li",{role:"tab",tabIndex:a===t?0:-1,"aria-selected":a===t,ref:e=>c.push(e),onKeyDown:h,onClick:d,...l,className:(0,s.A)("tabs__item","tabItem_LNqP",l?.className,{"tabs__item--active":a===t}),children:r??t},t)})})}function g(e){let{lazy:t,children:r,selectedValue:l}=e,i=(Array.isArray(r)?r:[r]).filter(Boolean);if(t){let e=i.find(e=>e.props.value===l);return e?(0,a.cloneElement)(e,{className:(0,s.A)("margin-top--md",e.props.className)}):null}return(0,n.jsx)("div",{className:"margin-top--md",children:i.map((e,t)=>(0,a.cloneElement)(e,{key:t,hidden:e.props.value!==l}))})}function v(e){let t=function(e){let{defaultValue:t,queryString:r=!1,groupId:n}=e,s=function(e){let{values:t,children:r}=e;return(0,a.useMemo)(()=>{let e=t??h(r).map(e=>{let{props:{value:t,label:r,attributes:n,default:a}}=e;return{value:t,label:r,attributes:n,default:a}});return!function(e){let t=(0,u.XI)(e,(e,t)=>e.value===t.value);if(t.length>0)throw Error(`Docusaurus error: Duplicate values "${t.map(e=>e.value).join(", ")}" found in <Tabs>. Every value needs to be unique.`)}(e),e},[t,r])}(e),[l,m]=(0,a.useState)(()=>(function(e){let{defaultValue:t,tabValues:r}=e;if(0===r.length)throw Error("Docusaurus error: the <Tabs> component requires at least one <TabItem> children component");if(t){if(!p({value:t,tabValues:r}))throw Error(`Docusaurus error: The <Tabs> has a defaultValue "${t}" but none of its children has the corresponding value. Available values are: ${r.map(e=>e.value).join(", ")}. If you intend to show no default tab, use defaultValue={null} instead.`);return t}let n=r.find(e=>e.default)??r[0];if(!n)throw Error("Unexpected error: 0 tabValues");return n.value})({defaultValue:t,tabValues:s})),[f,g]=function(e){let{queryString:t=!1,groupId:r}=e,n=(0,i.W6)(),s=function(e){let{queryString:t=!1,groupId:r}=e;if("string"==typeof t)return t;if(!1===t)return null;if(!0===t&&!r)throw Error('Docusaurus error: The <Tabs> component groupId prop is required if queryString=true, because this value is used as the search param name. You can also provide an explicit value such as queryString="my-search-param".');return r??null}({queryString:t,groupId:r});return[(0,c.aZ)(s),(0,a.useCallback)(e=>{if(!s)return;let t=new URLSearchParams(n.location.search);t.set(s,e),n.replace({...n.location,search:t.toString()})},[s,n])]}({queryString:r,groupId:n}),[v,x]=function(e){let{groupId:t}=e,r=t?`docusaurus.tab.${t}`:null,[n,s]=(0,d.Dv)(r);return[n,(0,a.useCallback)(e=>{r&&s.set(e)},[r,s])]}({groupId:n}),b=(()=>{let e=f??v;return p({value:e,tabValues:s})?e:null})();return(0,o.A)(()=>{b&&m(b)},[b]),{selectedValue:l,selectValue:(0,a.useCallback)(e=>{if(!p({value:e,tabValues:s}))throw Error(`Can't select invalid tab value=${e}`);m(e),g(e),x(e)},[g,x,s]),tabValues:s}}(e);return(0,n.jsxs)("div",{className:(0,s.A)("tabs-container","tabList__CuJ"),children:[(0,n.jsx)(f,{...t,...e}),(0,n.jsx)(g,{...t,...e})]})}function x(e){let t=(0,m.A)();return(0,n.jsx)(v,{...e,children:h(e.children)},String(t))}}}]);