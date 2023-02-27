import React from 'react';
import {useColorMode} from '@docusaurus/theme-common';
import styles from './styles.module.css';

// Create a Badge component that'll render a <span> tag with some styles. Make the name of the badge, and the color configurable.
// Use the color for the background color, and the name for the content of the badge.

interface BadgeProps {
    name: string;
    color: string;
}

export default function Badge({name, color}: BadgeProps) {
    const isDarkTheme = useColorMode().colorMode === "dark";
    const backgroundColor = isDarkTheme ? `${color}CC` : `${color}44`;
    const textColor = isDarkTheme ? "white" : color;
    return (
        <span
            style={{
                backgroundColor: backgroundColor,
                color: textColor,
            }}
            className={styles.badge}
        >
        {name}
    </span>
    );
}


