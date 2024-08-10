import { useRive } from '@rive-app/react-canvas';
import { useEffect, useRef, useState } from 'react';

interface RiveBoardProps {
    src: any;
    artboard?: string;
    stateMachines?: string | string[];
}

export default function RiveBoard(props: RiveBoardProps) {
    const ref = useRef();
    const { width } = useContainerDimensions(ref);
    const { RiveComponent } = useRive({
        src: props.src,
        artboard: props.artboard,
        stateMachines: props.stateMachines,
        autoplay: true,
    });
    const usedWidth = width * 0.8;
    return (
        <div ref={ref} style={{ width: "100%", height: "100%", display: "flex", justifyContent: "center" }} >
            <div style={{
                width: usedWidth,
                height: usedWidth,
            }}>
                <RiveComponent />
            </div >
        </div>
    );
}

export const useContainerDimensions = myRef => {
    const [dimensions, setDimensions] = useState({ width: 0, height: 0 })

    useEffect(() => {
        const getDimensions = () => ({
            width: myRef.current.offsetWidth,
            height: myRef.current.offsetHeight
        })

        const handleResize = () => {
            setDimensions(getDimensions())
        }

        if (myRef.current) {
            setDimensions(getDimensions())
        }

        window.addEventListener("resize", handleResize)

        return () => {
            window.removeEventListener("resize", handleResize)
        }
    }, [myRef])

    return dimensions;
};
