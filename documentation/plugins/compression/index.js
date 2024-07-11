import { resolve } from "path";
import { Joi } from "@docusaurus/utils-validation";

export default function pluginMediaOptimizer(context, options) {
    return {
        name: "compression",

        // getThemePath() {
        //     return resolve(__dirname, "./theme");
        // },
        //
        // getTypeScriptThemePath() {
        //     return resolve(__dirname, "./theme");
        // },
        //
        // getDefaultCodeTranslationMessages() {
        //     return {};
        // },

        configureWebpack(_config, isServer) {
            const { disableInDev, ...loaderOptions } = options;
            if (disableInDev && process.env.NODE_ENV !== "production") {
                return {};
            }

            return {
                mergeStrategy: {
                    "module.rules": "prepend",
                },
                module: {
                    rules: [
                        {
                            test: /\.(png|jpe?g)$/i,
                            use: [
                                require.resolve('@docusaurus/lqip-loader'),
                                {
                                    loader: require.resolve("@docusaurus/responsive-loader"),
                                    options: {
                                        emitFile: !isServer,
                                        adapter: require("@docusaurus/responsive-loader/sharp"),
                                        name: "assets/optimized-img/[name].[hash:hex:7].[width].[ext]",
                                        sizes: [320, 640, 960, 1280, 1600, 1920],
                                        format: "avif",
                                        quality: 60,
                                        ...loaderOptions.image,
                                    },
                                },
                            ],
                        },
                        // {
                        //     test: /\.(mp4|webm|mkv)$/i,
                        //     use: [
                        //         {
                        //             loader: require.resolve("file-loader"),
                        //             options: {
                        //                 name: "assets/videos/[name].[ext]",
                        //             },
                        //         },
                        //         {
                        //             loader: require.resolve("ffmpeg-loader"),
                        //             options: {
                        //                 formats: ["webm"],
                        //                 ...loaderOptions.video,
                        //             },
                        //         },
                        //     ],
                        // },
                    ],
                },
            };
        },
    };
};

export function validateOptions({ validate, options }) {
    const pluginOptionsSchema = Joi.object({
        disableInDev: Joi.boolean().default(false),
        image: Joi.object().default({}),
        video: Joi.object().default({}),
    });
    return validate(pluginOptionsSchema, options);
}
