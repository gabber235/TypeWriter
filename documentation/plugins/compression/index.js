import { Joi } from "@docusaurus/utils-validation";

export default function pluginMediaOptimizer(context, options) {
    return {
        name: "compression",

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
                                        progressive: true,
                                        quality: 60,
                                        ...loaderOptions.image,
                                    },
                                },
                            ],
                        },
                    ],
                },
            };
        },
    };
};

export function validateOptions({ validate, options }) {
    const pluginOptionsSchema = Joi.object({
        disableInDev: Joi.boolean().default(true),
        image: Joi.object().default({}),
        video: Joi.object().default({}),
    });
    return validate(pluginOptionsSchema, options);
}
