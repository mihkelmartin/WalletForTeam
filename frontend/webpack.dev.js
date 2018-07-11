const merge = require('webpack-merge');
const common = require('./webpack.common.js');
const webpack = require('webpack');

module.exports = merge(common, {
    mode : 'development',
    devtool: 'inline-source-map',
    devServer: {
      port: 8090,
      historyApiFallback: true,
      publicPath: '/built/'
    },
    plugins: [
        new webpack.DefinePlugin({
          'process.env.NODE_ENV' : JSON.stringify('development')
        })
    ]
});