const path = require('path');
const crypto = require('crypto');

export default function codeSnippets() {
    return {
        name: 'code-snippets',
        configureWebpack(_config, isServer) {
            return {
                module: {
                    rules: [
                        {
                            test: /snippets\.ts$/,
                            use: [
                                {
                                    loader: path.resolve(__dirname, 'codeSnippets.js'),
                                },
                            ],
                        },
                    ],
                },
            };
        },
    };
}
