module.exports = {
    chainWebpack: cfg => {
        cfg.resolve.set('fallback', {
            crypto: require.resolve('crypto-browserify'),
            path: require.resolve('path-browserify'),
            stream: require.resolve('stream-browserify'),
            fs: false
        })
    },
    devServer: {
        proxy: {
            '/api': {
                target: 'http://127.0.0.1:8088/api',
                pathRewrite: {'^/api': '/'}
            },
            '/upload': {
                target: 'http://127.0.0.1:8088/upload',
                pathRewrite: {'^/upload': '/'}
            },
            '/pdf': {
                target: 'http://127.0.0.1:8088/pdf',
                pathRewrite: {'^/pdf': '/'}
            }
        }
    },
    pluginOptions: {
        dr: {
            limit: {
                maxChunks: 600,
                minChunkSize: 64 * 1000
            }
        }
    }
}
