// @ts-check
// Note: type annotations allow type checking and IDEs autocompletion

import { themes } from 'prism-react-renderer';
const lightTheme = themes.vsLight;
const darkTheme = themes.vsDark;

/** @type {import('@docusaurus/types').Config} */
const config = {
    title: 'Typewriter',
    tagline: 'The next generation of Minecraft Questing',
    favicon: 'img/favicon.ico',

    // Set the production url of your site here
    url: 'https://gabber235.github.io',
    // Set the /<baseUrl>/ pathname under which your site is served
    // For GitHub pages deployment, it is often '/<projectName>/'
    baseUrl: '/TypeWriter/',

    // GitHub pages deployment config.
    // If you aren't using GitHub pages, you don't need these.
    organizationName: 'gabber235', // Usually your GitHub org/user name.
    projectName: 'Typewriter', // Usually your repo name.
    trailingSlash: false,

    onBrokenLinks: 'throw',
    onBrokenMarkdownLinks: 'warn',

    // Even if you don't use internalization, you can use this field to set useful
    // metadata like html lang. For example, if your site is Chinese, you may want
    // to replace "en" with "zh-Hans".
    i18n: {
        defaultLocale: 'en',
        locales: ['en'],
    },

    presets: [
        [
            'classic',
            /** @type {import('@docusaurus/preset-classic').Options} */
            ({
                docs: {
                    sidebarPath: require.resolve('./sidebars.js'),
                    editUrl:
                        'https://github.com/gabber235/TypeWriter/tree/main/documentation/',
                    routeBasePath: '/',
                },
                blog: {
                    showReadingTime: true,
                    editUrl:
                        'https://github.com/gabber235/TypeWriter/tree/main/documentation/',
                },
                theme: {
                    customCss: require.resolve('./src/css/custom.css'),
                },
            }),
        ],
    ],

    themeConfig:
        /** @type {import('@docusaurus/preset-classic').ThemeConfig} */
        ({
            // Replace with your project's social card
            image: 'img/typewriter.png',
            navbar: {
                title: 'TypeWriter',
                logo: {
                    alt: 'TypeWriter Logo',
                    src: 'img/typewriter.png',
                },
                items: [
                    {
                        type: 'doc',
                        docId: 'docs/home',
                        position: 'left',
                        label: 'Documentation',
                    },
                    {
                        type: 'docSidebar',
                        sidebarId: 'adapters',
                        position: 'left',
                        label: 'Adapters',
                    },
                    {
                        type: 'docSidebar',
                        sidebarId: 'develop',
                        position: 'left',
                        label: 'Develop',
                    },
                    { to: '/blog', label: 'Blog', position: 'left' },
                    {
                        href: 'https://github.com/gabber235/TypeWriter',
                        label: 'GitHub',
                        position: 'right',
                    },
                ],
            },
            footer: {
                style: 'dark',
                links: [
                    {
                        title: 'Docs',
                        items: [
                            {
                                label: 'Documentation',
                                to: '/docs/home',
                            },
                        ],
                    },
                    {
                        title: 'Community',
                        items: [
                            {
                                label: 'Discord',
                                href: 'https://discord.gg/gs5QYhfv9x',
                            },

                        ],
                    },
                    {
                        title: 'More',
                        items: [
                            {
                                label: 'Blog',
                                to: '/blog',
                            },
                            {
                                label: 'Github',
                                href: 'https://github.com/gabber235/TypeWriter',
                            },
                        ],
                    },
                ],
                copyright: `Copyright © ${new Date().getFullYear()} Typewriter`,
            },
            prism: {
                theme: lightTheme,
                darkTheme: darkTheme,
                additionalLanguages: ['kotlin', 'yaml', 'java', 'log'],
                magicComments: [
                    {
                        className: 'highlight-red',
                        line: 'highlight-red', // For single line
                        block: { start: 'highlight-red-start', end: 'highlight-red-end' }, // For block
                    },
                    {
                        className: 'highlight-green',
                        line: 'highlight-green', // For single line
                        block: { start: 'highlight-green-start', end: 'highlight-green-end' }, // For block
                    },
                ]
            },
            algolia: {
                appId: 'GE6F02MN59',
                apiKey: '57ae467d6c3f66ac2cae2c98e4275f49',
                indexName: 'typewriter',
            }
        }),
    plugins: [
        "rive-loader",
        // [
        //     '@docusaurus/plugin-content-docs',
        //     {
        //         id: 'adapters',
        //         path: 'adapters',
        //         routeBasePath: 'adapters',
        //         sidebarPath: require.resolve('./sidebars.js'),
        //         editUrl:
        //             'https://github.com/gabber235/TypeWriter/tree/main/documentation/',
        //     },
        // ]
    ]
};

export default config;
