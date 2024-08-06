import { useDoc } from '@docusaurus/theme-common/internal';

export default function useDifficulty() {
  const { frontMatter } = useDoc();
  return frontMatter.difficulty;
}
