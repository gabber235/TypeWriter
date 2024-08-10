import React from 'react';
import clsx from 'clsx';
import {useThemeConfig} from '@docusaurus/theme-common';
import type {Props} from '@theme/AnnouncementBar/Content';

export default function AnnouncementBarContent(
  props: Props,
): JSX.Element | null {
  const {announcementBar} = useThemeConfig();
  const {content} = announcementBar!;

  return (
    <div
      {...props}
      className={clsx('flex items-center justify-center text-center text-xl font-bold', props.className)}
      dangerouslySetInnerHTML={{__html: content}}
    />
  );
}
