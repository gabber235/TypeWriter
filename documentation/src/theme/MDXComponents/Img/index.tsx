import React from "react";
import clsx from "clsx";
import type { Props } from "@theme/MDXComponents/Img";

import styles from "./styles.module.css";

function transformImgClassName(className?: string): string {
    return clsx(className, styles.img);
}

export default function MDXImg(props: Props): JSX.Element {
    // If the type is not gif. We should use the Image component
    if (!props.src.endsWith(".gif")) {
        return <div className="bg-red-500 text-white p-4 rounded">Invalid image, please use the Image component, please report this in the TypeWriter Discord.</div>;
    }
    return (
        // eslint-disable-next-line jsx-a11y/alt-text
        <img
            decoding="async"
            loading="lazy"
            {...props}
            className={transformImgClassName(props.className)}
        />
    );
}
