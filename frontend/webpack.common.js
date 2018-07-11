var path = require('path');

module.exports = {
    entry: './src/main/js/walletforteam.js',
    cache: true,
    output: {
        filename: 'bundle.js',
        path: path.join(__dirname,'/src/main/resources/static/built'),
    },
    module: {
        rules: [
            {
                test: path.join(__dirname, '.'),
                exclude: /(node_modules)/,
                loader: 'babel-loader',
                query: {
                    cacheDirectory: true,
                    presets: ['env', 'react', 'stage-0']
                }
            }
        ]
    }
};
