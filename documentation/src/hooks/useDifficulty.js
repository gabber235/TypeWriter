import { useDoc } from '@docusaurus/plugin-content-docs/client';

export default function useDifficulty() {
    const { frontMatter } = useDoc();
    return frontMatter.difficulty;
}
