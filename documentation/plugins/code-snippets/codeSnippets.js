const fs = require('fs');
const path = require('path');

const codeBlockRegex = /\/\/<(?<closing>\/)?code-block:(?<tag>[a-zA-Z0-9_]*)>$/;

module.exports = function() {
    const snippets = {};

    function processFile(filePath, relativeFilePath) {
        const content = fs.readFileSync(filePath, 'utf8');
        const lines = content.split('\n');
        const blocks = {};
        const blockStartLines = {};

        lines.forEach((line, index) => {
            const match = line.match(codeBlockRegex);
            if (match) {
                const { closing, tag } = match.groups;
                if (closing) {
                    const code = blocks[tag];
                    if (!code) {
                        // Handle the case where a closing tag does not have an opening tag
                        snippets[tag] = {
                            file: relativeFilePath,
                            content: "Not found", // Temporary fix so the docs can be build...
                            startLine: "N/A",
                            endLine: index + 1
                        };
                    } else {
                        // Close the block and save the snippet
                        blocks[tag] = null;
                        snippets[tag] = {
                            file: relativeFilePath,
                            content: code.join('\n'),
                            startLine: blockStartLines[tag] + 1,
                            endLine: index + 1
                        };
                    }
                } else {
                    if (blocks[tag]) {
                        // Handle the case where an opening tag already exists
                        snippets[tag] = {
                            file: relativeFilePath,
                            content: "Not found", // Temporary fix so the docs can be build...
                            startLine: blockStartLines[tag] + 1,
                            endLine: index + 1
                        };
                    }
                    // Start a new block
                    blocks[tag] = [];
                    blockStartLines[tag] = index;
                }
            } else {
                // Add lines to the current blocks
                for (const tag in blocks) {
                    const code = blocks[tag];
                    if (!code) continue;
                    code.push(line);
                }
            }
        });

        // Handle any unclosed blocks
        for (const tag in blocks) {
            const code = blocks[tag];
            if (code) {
                snippets[tag] = {
                    file: relativeFilePath,
                    content: "Not found", // Temporary fix so the docs can be build...
                    startLine: blockStartLines[tag] + 1,
                    endLine: "N/A"
                };
            }
        }
    }

    function traverseDirectory(dir, baseDir) {
        const files = fs.readdirSync(dir);
        files.forEach(file => {
            const filePath = path.join(dir, file);
            const relativeFilePath = path.relative(baseDir, filePath);
            const stat = fs.statSync(filePath);
            if (stat.isDirectory()) {
                traverseDirectory(filePath, baseDir);
            } else if (path.extname(filePath) === '.kt') {
                try {
                    processFile(filePath, relativeFilePath);
                } catch (error) {
                    // Improve error handling by showing which file failed
                    console.error(`Error processing file: ${relativeFilePath}`);
                    throw error;
                }
            }
        });
    }

    // Assuming the adapters directory is at the root of your project
    const adaptersDir = path.resolve(__dirname, '../../../extensions/_DocsExtension');
    traverseDirectory(adaptersDir, adaptersDir);

    this.cacheable(false);

    const code = `${JSON.stringify(snippets, null, 2)}`;

    fs.writeFileSync(path.resolve(__dirname, 'snippets.json'), code);

    return `export default ${JSON.stringify(snippets, null, 2)}`;
};