import React, { useState, useRef, useEffect } from "react";
import ReactPlayer from "react-player";
import { Icon } from "@iconify/react";
import screenfull from "screenfull";

interface PlayerProps {
  url: string;
}

export default function Player({ url }: PlayerProps) {
  const [progress, setProgress] = useState(0);
  const [playing, setPlaying] = useState(true);
  const [isFullscreen, setIsFullscreen] = useState(false);
  const playerRef = useRef<ReactPlayer>(null);
  const playerContainerRef = useRef<HTMLDivElement>(null);

  const togglePlayPause = () => {
    setPlaying((prev) => !prev);
  };

  const handleSeek = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newProgress = parseFloat(e.target.value);
    setProgress(newProgress);
    playerRef.current?.seekTo(newProgress / 100, "fraction");
  };

  const handleFullscreenToggle = () => {
    if (screenfull.isEnabled) {
      screenfull.toggle(playerContainerRef.current);
    }
  };

  useEffect(() => {
    if (screenfull.isEnabled) {
      screenfull.on("change", () => {
        setIsFullscreen(screenfull.isFullscreen);
      });

      return () => {
        screenfull.off("change", () => {
          setIsFullscreen(screenfull.isFullscreen);
        });
      };
    }
  }, []);

  return (
    <div ref={playerContainerRef} className="relative w-full h-full">
      <ProgressBar progress={progress} onSeek={handleSeek} />
      <ReactPlayer
        ref={playerRef}
        url={url}
        playing={playing}
        loop
        muted
        playsInline={true}
        playsinline={true}
        controls={false}
        width="100%"
        height="100%"
        progressInterval={100}
        onProgress={(state) => setProgress(state.played * 100)}
      />
      <div className="opacity-0 hover:opacity-100 transition-opacity duration-300 w-full h-full flex items-center justify-center">
        <div
          className="absolute bottom-0 left-0 right-0 flex items-center justify-center cursor-pointer h-[97%]"
          onClick={togglePlayPause}
        >
          <div>
            <Icon
              icon={playing ? "mdi:pause-circle" : "mdi:play-circle"}
              fontSize={50}
              color="white"
            />
          </div>
        </div>
        <div className="absolute bottom-2 right-2 p-2">
          <Icon
            onClick={handleFullscreenToggle}
            className="cursor-pointer hover:scale-110 transition duration-150"
            icon={isFullscreen ? "mdi:fullscreen-exit" : "mdi:fullscreen"}
            fontSize={40}
            color="white"
          />
        </div>
      </div>
    </div>
  );
}

interface ProgressBarProps {
  progress: number;
  onSeek: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

function ProgressBar({ progress, onSeek }: ProgressBarProps) {
  return (
    <div className="w-full flex items-center text-white">
      <div className="flex-grow">
        <Bar progress={progress} onSeek={onSeek} />
      </div>
    </div>
  );
}

interface BarProps {
  progress: number;
  onSeek: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

function Bar({ progress, onSeek }: BarProps) {
  return (
    <div className="relative h-[5px] rounded-t-lg overflow-hidden pb-2">
      <input
        type="range"
        min="0"
        max="100"
        value={progress}
        onChange={onSeek}
        className="absolute top-0 left-0 w-full h-[5px] opacity-0 cursor-pointer"
        style={{
          WebkitAppearance: "none",
          MozAppearance: "none",
          appearance: "none",
        }}
      />
      <div
        className="h-full bg-primary transition-width duration-200 pb-2"
        style={{ width: `${progress}%` }}
      />
    </div>
  );
}
