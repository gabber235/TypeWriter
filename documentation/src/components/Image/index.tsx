import { ComponentProps, useState } from "react";

export type SrcType = {
    width: number;
    path?: string;
    height?: number;
};

export type SrcImage = {
    height?: number;
    width?: number;
    src: string;
    srcSet: string;
    placeholder?: string;
    images: SrcType[];
};

export interface Props extends ComponentProps<'img'> {
    readonly img: { default: string } | SrcImage | string;
}
export default function Image(props: Props) {
    const { img, ...propsRest } = props;

    // In dev env just use regular img with original file
    if (typeof img === 'string' || 'default' in img) {
        return (
            <div className="w-full h-full flex justify-center items-center pb-10">
                {/* eslint-disable-next-line jsx-a11y/alt-text */}
                <img src={typeof img === 'string' ? img : img.default} loading="lazy" decoding="async" className="rounded-md" {...propsRest} />
            </div>
        );
    }

    const [loaded, setLoaded] = useState(false);


    return (
        <div className="w-full h-full flex justify-center items-center relative">
            <img
                src={img.src}
                srcSet={img.srcSet}
                sizes="(max-width: 320px) 280px, (max-width: 640px) 600px, 1200px"
                loading="lazy"
                decoding="async"
                onLoad={() => setLoaded(true)}
                className={`rounded-md transition-opacity duration-300 ${loaded ? 'opacity-100' : 'opacity-0'
                    }`}
                {...propsRest}
            />
            {!loaded && (
                <div
                    className="absolute inset-0 bg-cover bg-center rounded-md"
                    style={{ backgroundImage: `url(${img.placeholder})` }}
                />
            )}
        </div>
    );
}
