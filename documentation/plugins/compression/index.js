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
                            test: /\.(png|jpe?g|webp)$/i,
                            use: [
                                {
                                    loader: 'responsive-loader',
                                    options: {
                                        adapter: require('responsive-loader/sharp'),
                                        name: 'assets/optimized-img/[name].[hash:hex:7].[width].[ext]',
                                        sizes: [320, 640, 960, 1280, 1600, 1920],
                                        format: 'avif',
                                        quality: 60,
                                        placeholder: true,
                                        placeholderSize: 40,
                                        emitFile: !isServer,
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
