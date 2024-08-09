import React from 'react';
import {HtmlClassNameProvider, PageMetadata} from '@docusaurus/theme-common';
import {
  docVersionSearchTag,
  DocsVersionProvider,
} from '@docusaurus/theme-common/internal';
import renderRoutes from '@docusaurus/renderRoutes';
import SearchMetadata from '@theme/SearchMetadata';
function DocVersionRootMetadata(props) {
  const {version} = props;
  return (
    <>
      <SearchMetadata
        version={version.version}
        tag={docVersionSearchTag(version.pluginId, version.version)}
      />
      <PageMetadata>
        {version.noIndex && <meta name="robots" content="noindex, nofollow" />}
      </PageMetadata>
    </>
  );
}
function DocVersionRootContent(props) {
  const {version, route} = props;
  return (
    <HtmlClassNameProvider className={version.className}>
      <DocsVersionProvider version={version}>
        {renderRoutes(route.routes)}
      </DocsVersionProvider>
    </HtmlClassNameProvider>
  );
}
export default function DocVersionRoot(props) {
  return (
    <>
      <DocVersionRootMetadata {...props} />
      <DocVersionRootContent {...props} />
    </>
  );
}
