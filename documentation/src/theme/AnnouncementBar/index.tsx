import React from 'react';
import {useThemeConfig} from '@docusaurus/theme-common';
import {useAnnouncementBar} from '@docusaurus/theme-common/internal';
import AnnouncementBarCloseButton from '@theme/AnnouncementBar/CloseButton';
import AnnouncementBarContent from '@theme/AnnouncementBar/Content';

export default function AnnouncementBar(): JSX.Element | null {
  const {announcementBar} = useThemeConfig();
  const {isActive, close} = useAnnouncementBar();

  if (!isActive) {
    return null;
  }

  const {isCloseable} = announcementBar!;

  return (
    <div
      className={`flex items-center justify-between p-0 bg-[#227c9d] text-white shadow-lg transition-all duration-300 ease-in-out ${
        isCloseable ? 'relative' : 'border-b'
      }`}
      role="banner">
      {isCloseable && <div className="flex-none w-12" />}
      <AnnouncementBarContent className="flex-1 text-center text-2xl" />
      {isCloseable && (
        <AnnouncementBarCloseButton
          onClick={close}
          className="flex-none mr-2 w-12 h-full cursor-pointer"
        />
      )}
    </div>
  );
}
