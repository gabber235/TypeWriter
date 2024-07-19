const { fontFamily } = require("tailwindcss/defaultTheme");

/** @type {import('tailwindcss').Config} */
module.exports = {
    corePlugins: {
        preflight: false,
        container: false,
    },
    darkMode: ["class", '[data-theme="dark"]'],
    content: ["./src/**/*.{jsx,tsx,html}", "./docs/**/*.mdx"],
    theme: {
        extend: {
            fontFamily: {
                sans: ['"Inter"', ...fontFamily.sans],
                jakarta: ['"Plus Jakarta Sans"', ...fontFamily.sans],
                mono: ['"Fira Code"', ...fontFamily.mono],
            },
            borderRadius: {
                sm: "4px",
            },
            screens: {
                lg: "997px",
            },
            colors: {
                primary: 'var(--ifm-color-primary)',
                secondary: 'var(--ifm-color-secondary)',
            },
        },
    },
    plugins: [],
};
