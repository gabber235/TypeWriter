import React from 'react';
import clsx from 'clsx';
import Translate from '@docusaurus/Translate';
import { ThemeClassNames } from '@docusaurus/theme-common';
import { useDocsVersion } from '@docusaurus/theme-common/internal';
import styles from './styles.module.css';

export default function DocVersionBadge({ className }) {
    const versionMetadata = useDocsVersion();
    if (versionMetadata.badge) {
        return (
            <div className={styles.badge_version}>
                <span
                    className={clsx(
                        className,
                        ThemeClassNames.docs.docVersionBadge,
                        'badge badge--primary',
                    )}>
                    <Translate
                        id="theme.docs.versionBadge.label"
                        values={{ versionLabel: versionMetadata.label }}>
                        {'Version: {versionLabel}'}
                    </Translate>
                </span>
            </div >
        );
    }
    return null;
}
