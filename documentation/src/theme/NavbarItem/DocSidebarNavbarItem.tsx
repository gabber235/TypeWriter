import React from 'react';
import {
  useActiveDocContext,
  useLayoutDocsSidebar,
  useActiveVersion,
} from '@docusaurus/plugin-content-docs/client';
import DefaultNavbarItem from '@theme/NavbarItem/DefaultNavbarItem';
import type {Props} from '@theme/NavbarItem/DocSidebarNavbarItem';

export default function DocSidebarNavbarItem({
  sidebarId,
  label,
  docsPluginId,
  ...props
}: Props): JSX.Element {
  const {activeDoc} = useActiveDocContext(docsPluginId);
  const sidebarLink = useLayoutDocsSidebar(sidebarId, docsPluginId).link;

  const activeVersion = useActiveVersion(docsPluginId)?.name;

  if (!sidebarLink) {
    throw new Error(
      `DocSidebarNavbarItem: Sidebar with ID "${sidebarId}" doesn't have anything to be linked to.`,
    );
  }

  // Adjust label based on the active version
  const adjustedLabel =
    label === 'Extensions' && activeVersion && activeVersion <= '0.5.2'
      ? 'Adapters'
      : label;

  return (
    <DefaultNavbarItem
      exact
      {...props}
      isActive={() => activeDoc?.sidebar === sidebarId}
      label={adjustedLabel ?? sidebarLink.label}
      to={sidebarLink.path}
    />
  );
}
