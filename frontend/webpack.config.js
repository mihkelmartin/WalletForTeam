var path = require('path');


module.exports = {
    mode : 'none',
    entry: './src/main/js/walletforteam.js',
    devtool: 'sourcemaps',
    devServer: {
      port: 8080,
      historyApiFallback: true
    },
    cache: true,
    output: {
        path: path.join(__dirname,'/src/main/resources/static'),
        filename: 'built/bundle.js',
        publicPath: '/'
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