const path = require('path');
const webpack = require('webpack');
const autoprefixer = require('autoprefixer');
const createConfig = require('./webpack.base');

const config = createConfig({
  postcss: {
    plugins: () => [
      autoprefixer(),
    ],
  },
});

module.exports = Object.assign(
  {},
  config,
  {
    mode: 'development',
    target: 'web',
    entry: './public/js/app',
    output: {
      path: path.join(__dirname, '../public/dist'),
      filename: 'app.js',
      publicPath: 'http://localhost:8080/dist/',
    },
    serve: {
      dev: {
        publicPath: 'http://localhost:8080/dist/',
      },
    }
  },
);
