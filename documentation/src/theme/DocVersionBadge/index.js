import React from 'react';
import Translate from '@docusaurus/Translate';
import { ThemeClassNames } from '@docusaurus/theme-common';
import { useDocsVersion } from '@docusaurus/plugin-content-docs/client';
import useDifficulty from '../../hooks/useDifficulty'; // Import the custom hook

export default function DocVersionBadge() {
    const versionMetadata = useDocsVersion();
    const difficultyLevel = useDifficulty(); // Get the difficulty level

    // Function to determine badge class based on difficulty level
    const getBadgeClass = (difficulty) => {
        switch (difficulty.toLowerCase()) {
            case 'easy':
                return 'badge--success';
            case 'normal':
                return 'badge--warning';
            case 'hard':
                return 'badge--danger';
            default:
                return 'badge--secondary';
        }
    };

    if (versionMetadata.badge) {
        return (
            <div className="flex">
                <div className="inline-block mb-2 mr-2">
                    <span
                        className={` ${ThemeClassNames.docs.docVersionBadge} badge badge--primary`}>
                        <Translate
                            id="theme.docs.versionBadge.label"
                            values={{ versionLabel: versionMetadata.label }}>
                            {'Version: {versionLabel}'}
                        </Translate>
                    </span>
                </div>
                {difficultyLevel && ( // Display difficulty level if available
                    <div className="inline-block mb-2">
                        <span
                            className={`badge ${getBadgeClass(difficultyLevel)}`}>
                            <Translate
                                id="theme.docs.difficultyBadge.label"
                                values={{ difficultyLevel }}>
                                {'Difficulty: {difficultyLevel}'}
                            </Translate>
                        </span>
                    </div>
                )}
            </div>
        );
    }
    return null;
}
