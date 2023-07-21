module.exports = {
    publicPath: '/dataoperationtools',
    devServer: {
        proxy: {
            '/api': {
                target: 'http://localhost:8082/api',
                pathRewrite: {'^/api': '/'}
            },
            '/upload': {
                target: 'http://localhost:8082/upload',
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
