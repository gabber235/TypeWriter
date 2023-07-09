import React, { useState } from "react";
import ReactPlayer from "react-player";
import styles from "./styles.module.css";

interface PlayerProps {
    url: string;
}

export default function Player({ url }: PlayerProps) {
    let [progress, setProgress] = useState(0);
    return (
        <div className={styles.player}>
            <Bar progress={progress} />
            <ReactPlayer
                url={url}
                playing
                loop
                muted
                width="100%"
                height="100%"
                progressInterval={100}
                onProgress={(state) => setProgress(state.played * 100)}
                className={styles.player}
            />
        </div>
    )
}

function Bar({ progress }: { progress: number }) {
    return (
        <div className={styles.bar}>
            <div className={styles.progress} style={{ width: `${progress}%` }} />
        </div>
    )
}
