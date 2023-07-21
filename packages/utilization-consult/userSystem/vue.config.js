module.exports = {
    publicPath: '/utilization',
    devServer: {
        proxy: {
            '/api': {
                target: 'http://127.0.0.1/api',
                pathRewrite: {'^/api': '/'}
            },
            '/upload': {
                target: 'http://127.0.0.1/upload',
                pathRewrite: {'^/upload': '/'}
            }
        }
    },
    pluginOptions: {
        dr: {
            limit: {
                maxChunks: 400
            }
        }
    }
}
