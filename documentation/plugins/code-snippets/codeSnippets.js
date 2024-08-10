const fs = require('fs');
const path = require('path');

const codeBockRegex = /\/\/<(?<closing>\/)?code-block:(?<tag>[a-zA-Z0-9_]*)>$/;

module.exports = function() {
    const snippets = {};

    function processFile(filePath, relativeFilePath) {
        const content = fs.readFileSync(filePath, 'utf8');
        const lines = content.split('\n');
        const blocks = {};

        lines.forEach((line, index) => {
            const match = line.match(codeBockRegex);
            if (match) {
                const { closing, tag } = match.groups;
                if (closing) {
                    const code = blocks[tag];
                    if (!code) {
                        throw new Error(`Code block not closed: ${tag} (${relativeFilePath}:${index + 1})`);
                    }
                    blocks[tag] = null;
                    snippets[tag] = {
                        file: relativeFilePath,
                        content: code.join('\n')
                    };
                    return;
                }

                blocks[tag] = [];
            } else {
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
            if (!code) continue;
            snippets[tag] = {
                file: relativeFilePath,
                content: code.join('\n')
            };
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
                processFile(filePath, relativeFilePath);
            }
        });
    }

    // Assuming the adapters directory is at the root of your project
    const adaptersDir = path.resolve(__dirname, '../../../adapters/_DocsAdapter');
    traverseDirectory(adaptersDir, adaptersDir);

    // console.log(`Exporting ${Object.keys(snippets).length} snippets: ${Object.keys(snippets)}`);

    this.cacheable(false);

    const code = `${JSON.stringify(snippets, null, 2)}`;

    fs.writeSync(fs.openSync(path.resolve(__dirname, 'snippets.json'), 'w'), code);

    return `export default ${JSON.stringify(snippets, null, 2)}`;
};
