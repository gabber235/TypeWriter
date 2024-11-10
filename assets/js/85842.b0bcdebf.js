"use strict";(self.webpackChunkdocumentation=self.webpackChunkdocumentation||[]).push([["85842"],{15903:function(e,t,s){s.d(t,{diagram:function(){return N}});var i=s(54909),a=s(50043),o=s(41461),r=s(54147),l=s(13316);s(27484),s(17967),s(27856),s(42752),s(2024);let d="rect",n="rectWithTitle",c="statediagram",p=`${c}-state`,b="transition",g=`${b} note-edge`,u=`${c}-note`,h=`${c}-cluster`,y=`${c}-cluster-alt`,f="parent",w="note",m="----",x=`${m}${w}`,$=`${m}${f}`,T="fill:none",S="fill: #333",k="text",D="normal",A={},v=0;function B(e="",t=0,s="",i=m){let a=null!==s&&s.length>0?`${i}${s}`:"";return`state-${e}${a}-${t}`}let C=(e,t,s,a,o,l)=>{var c;let b=s.id;let m=null==(c=a[b])?"":c.classes?c.classes.join(" "):"";if("root"!==b){let t=d;!0===s.start&&(t="start"),!1===s.start&&(t="end"),s.type!==i.D&&(t=s.type),!A[b]&&(A[b]={id:b,shape:t,description:r.e.sanitizeText(b,(0,r.c)()),classes:`${m} ${p}`});let a=A[b];s.description&&(Array.isArray(a.description)?(a.shape=n,a.description.push(s.description)):a.description.length>0?(a.shape=n,a.description===b?a.description=[s.description]:a.description=[a.description,s.description]):(a.shape=d,a.description=s.description),a.description=r.e.sanitizeTextOrArray(a.description,(0,r.c)())),1===a.description.length&&a.shape===n&&(a.shape=d),!a.type&&s.doc&&(r.l.info("Setting cluster for ",b,R(s)),a.type="group",a.dir=R(s),a.shape=s.type===i.a?"divider":"roundedWithTitle",a.classes=a.classes+" "+h+" "+(l?y:""));let o={labelStyle:"",shape:a.shape,labelText:a.description,classes:a.classes,style:"",id:b,dir:a.dir,domId:B(b,v),type:a.type,padding:15};if(o.centerLabel=!0,s.note){let t={labelStyle:"",shape:"note",labelText:s.note.text,classes:u,style:"",id:b+x+"-"+v,domId:B(b,v,w),type:a.type,padding:15},i={labelStyle:"",shape:"noteGroup",labelText:s.note.text,classes:a.classes,style:"",id:b+$,domId:B(b,v,f),type:"group",padding:0};v++;let r=b+$;e.setNode(r,i),e.setNode(t.id,t),e.setNode(b,o),e.setParent(b,r),e.setParent(t.id,r);let l=b,d=t.id;"left of"===s.note.position&&(l=t.id,d=b),e.setEdge(l,d,{arrowhead:"none",arrowType:"",style:T,labelStyle:"",classes:g,arrowheadStyle:S,labelpos:"c",labelType:k,thickness:D})}else e.setNode(b,o)}t&&"root"!==t.id&&(r.l.trace("Setting node ",b," to be child of its parent ",t.id),e.setParent(b,t.id)),s.doc&&(r.l.trace("Adding nodes children "),E(e,s,s.doc,a,o,!l))},E=(e,t,s,a,o,l)=>{r.l.trace("items",s),s.forEach(s=>{switch(s.stmt){case i.b:case i.D:C(e,t,s,a,o,l);break;case i.S:{C(e,t,s.state1,a,o,l),C(e,t,s.state2,a,o,l);let i={id:"edge"+v,arrowhead:"normal",arrowTypeEnd:"arrow_barb",style:T,labelStyle:"",label:r.e.sanitizeText(s.description,(0,r.c)()),arrowheadStyle:S,labelpos:"c",labelType:k,thickness:D,classes:b};e.setEdge(s.state1.id,s.state2.id,i,v),v++}}})},R=(e,t=i.c)=>{let s=t;if(e.doc)for(let t=0;t<e.doc.length;t++){let i=e.doc[t];"dir"===i.stmt&&(s=i.value)}return s},V=async function(e,t,s,i){let n;r.l.info("Drawing state diagram (v2)",t),A={},i.db.getDirection();let{securityLevel:p,state:b}=(0,r.c)(),g=b.nodeSpacing||50,u=b.rankSpacing||50;r.l.info(i.db.getRootDocV2()),i.db.extract(i.db.getRootDocV2()),r.l.info(i.db.getRootDocV2());let h=i.db.getStates(),y=new a.k({multigraph:!0,compound:!0}).setGraph({rankdir:R(i.db.getRootDocV2()),nodesep:g,ranksep:u,marginx:8,marginy:8}).setDefaultEdgeLabel(function(){return{}});C(y,void 0,i.db.getRootDocV2(),h,i.db,!0),"sandbox"===p&&(n=(0,o.Ys)("#i"+t));let f="sandbox"===p?(0,o.Ys)(n.nodes()[0].contentDocument.body):(0,o.Ys)("body"),w=f.select(`[id="${t}"]`),m=f.select("#"+t+" g");await (0,l.r)(m,y,["barb"],c,t);r.u.insertTitle(w,"statediagramTitleText",b.titleTopMargin,i.db.getDiagramTitle());let x=w.node().getBBox(),$=x.width+16,T=x.height+16;w.attr("class",c);let S=w.node().getBBox();(0,r.i)(w,T,$,b.useMaxWidth);let k=`${S.x-8} ${S.y-8} ${$} ${T}`;for(let e of(r.l.debug(`viewBox ${k}`),w.attr("viewBox",k),document.querySelectorAll('[id="'+t+'"] .edgeLabel .label'))){let t=e.getBBox(),s=document.createElementNS("http://www.w3.org/2000/svg",d);s.setAttribute("rx",0),s.setAttribute("ry",0),s.setAttribute("width",t.width),s.setAttribute("height",t.height),e.insertBefore(s,e.firstChild)}},N={parser:i.p,db:i.d,renderer:{setConf:function(e){for(let t of Object.keys(e))e[t]},getClasses:function(e,t){return t.db.extract(t.db.getRootDocV2()),t.db.getClasses()},draw:V},styles:i.s,init:e=>{!e.state&&(e.state={}),e.state.arrowMarkerAbsolute=e.arrowMarkerAbsolute,i.d.clear()}}}}]);