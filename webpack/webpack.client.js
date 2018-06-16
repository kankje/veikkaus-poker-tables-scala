const path = require('path');
const webpack = require('webpack');
const autoprefixer = require('autoprefixer');
const cssnano = require('cssnano');
const createConfig = require('./webpack.base');

const config = createConfig({
  postcss: {
    plugins: () => [
      autoprefixer(),
      cssnano({ discardComments: { removeAll: true } }),
    ],
  },
});

module.exports = Object.assign(
  {},
  config,
  {
    mode: 'production',
    target: 'web',
    entry: './public/js/app',
    output: {
      path: path.join(__dirname, '../public/dist'),
      filename: 'app.js',
      publicPath: '/dist/',
    }
  },
);
