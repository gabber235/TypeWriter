import codeSnippets from "./snippets";
import CodeBlock from '@theme/CodeBlock';

interface CodeSnippetProps {
    tag: string;
    json: any;
}

export default function CodeSnippet({ tag, json }: CodeSnippetProps) {
    if (!json) {
        throw new Error("JSON not provided");
    }

    let codeSnippet: any;

    // If the json is provided, we use it to get the code snippet. 
    // This is for older versions so that they remain with the same code.
    if (Object.keys(json).length > 0) {
        codeSnippet = json[tag];
    } else {
        codeSnippet = codeSnippets[tag];
    }

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
