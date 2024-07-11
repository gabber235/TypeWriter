import React, { useState } from "react";
import ReactPlayer from "react-player";

interface PlayerProps {
    url: string;
}

export default function Player({ url }: PlayerProps) {
    let [progress, setProgress] = useState(0);
    return (
        <div className="relative">
            <Bar progress={progress} />
            <ReactPlayer
                url={url}
                playing
                loop
                muted
                controls
                width="100%"
                height="100%"
                progressInterval={100}
                onProgress={(state) => setProgress(state.played * 100)}
                className="rounded-b-lg bg-clip-border"
            />
        </div>
    );
}

function Bar({ progress }: { progress: number }) {
    return (
        <div className="h-[5px] bg-gray-200 bg-opacity-25 rounded-t-lg overflow-hidden">
            <div
                className="h-full bg-primary transition-width duration-200"
                style={{ width: `${progress}%` }}
            />
        </div>
    );
}
