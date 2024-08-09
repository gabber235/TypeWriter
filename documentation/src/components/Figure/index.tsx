import Image, { Props } from "../Image";

interface FigureProps extends Props {
    title: string;
}

export default function Figure({ title, ...props }: FigureProps) {
    return (
        <figure className="flex flex-col items-center justify-center space-y-2">
            <Image {...props} />
            <figcaption className="text-center text-gray-500 dark:text-gray-400">{title}</figcaption>
        </figure>
    );
}
