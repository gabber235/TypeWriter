module.exports = function (context, options) {
    return {
        name: "rive-loader",
        configureWebpack(config, isServer, utils) {
            // Add for .riv files a url-loader
            return {
                module: {
                    rules: [
                        {
                            test: /\.riv$/,
                            use: [
                                {
                                    loader: "url-loader",
                                }
                            ]
                        },
                    ]
                }
            }
        },
    }
}