import codeSnippets from "./snippets";
import CodeBlock from '@theme/CodeBlock';

interface CodeSnippetProps {
    tag: string;
}

export default function CodeSnippet({ tag }: CodeSnippetProps) {
    const codeSnippet = codeSnippets[tag];
    if (codeSnippet == null) {
        return <div className="text-red-500 dark:text-red-400 text-xs">Code snippet not found: {tag}</div>;
    }
    const { file, content } = codeSnippet;
    if (file == null || content == null) {
        return <div className="text-red-500 dark:text-red-400 text-xs">Code snippet not found: {tag} ({codeSnippet})</div>;
    }
    const fileName = file.split('/').pop();
    return (
        <CodeBlock language="kotlin" showLineNumbers title={fileName}>
            {content}
        </CodeBlock>
    );
}
