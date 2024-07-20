import React from 'react';
import Translate from '@docusaurus/Translate';
import { ThemeClassNames } from '@docusaurus/theme-common';
import { useDocsVersion } from '@docusaurus/theme-common/internal';

export default function DocVersionBadge({ className }) {
    const versionMetadata = useDocsVersion();

    if (versionMetadata.badge) {
        return (
            <div className="inline-block mb-2">
                <span
                    className={`${className} ${ThemeClassNames.docs.docVersionBadge} badge badge--primary`}>
                    <Translate
                        id="theme.docs.versionBadge.label"
                        values={{ versionLabel: versionMetadata.label }}>
                        {'Version: {versionLabel}'}
                    </Translate>
                </span>
            </div>
        );
    }
    return null;
}
