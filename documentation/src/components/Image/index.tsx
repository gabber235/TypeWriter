import { ComponentProps } from "react";

export type SrcType = {
    width: number;
    path?: string;
    size?: number;
    format?: 'webp' | 'jpeg' | 'png' | 'gif';
};

export type SrcImage = {
    height?: number;
    width?: number;
    preSrc: string;
    src: string;
    srcSet: string;
    images: SrcType[];
};

export interface Props extends ComponentProps<'img'> {
    readonly img: { default: string } | { src: SrcImage; preSrc: string } | string;
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

    return (
        <div className="w-full h-full flex justify-center items-center">
            <img
                src={img.src.src}
                srcSet={img.src.srcSet}
                loading="lazy"
                decoding="async"
                className="rounded-md"
                {...propsRest} />
        </div>
    );
}
