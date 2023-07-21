const isPro = process.env.NODE_ENV === 'production'
module.exports = {
    root: true,
    env: {
        node: true
    },
    'extends': [
        'plugin:vue/base',
        'eslint:recommended'
    ],
    parserOptions: {
        parser: '@babel/eslint-parser'
    },
    rules: {
        'no-console': isPro ? 'warn' : 'off',
        'no-debugger': isPro ? 'warn' : 'off'
    }
}
