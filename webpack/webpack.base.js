module.exports = function (options) {
  return {
    resolve: {
      extensions: ['.js', '.jsx'],
    },

    module: {
      rules: [
        {
          test: /\.jsx?$/,
          exclude: /node_modules/,
          use: [
            {
              loader: 'babel-loader',
              options: {
                cacheDirectory: true,
              },
            },
          ],
        },
        {
          test: /\.scss$/,
          exclude: /node_modules/,
          use: [
            { loader: 'style-loader' },
            { loader: 'css-loader', options: { importLoaders: 2 } },
            { loader: 'postcss-loader', options: options.postcss },
            { loader: 'sass-loader' },
          ],
        },
        {
          test: /\.css$/,
          use: [
            { loader: 'style-loader' },
            { loader: 'css-loader', options: { importLoaders: 1 } },
            { loader: 'postcss-loader', options: options.postcss },
          ],
        },
        {
          test: /\.(ico|png|jpg|jpeg|gif|svg|eot|woff|woff2|ttf)(\?v=\d+\.\d+\.\d+)?$/,
          exclude: /node_modules/,
          use: [
            { loader: 'url-loader', options: { limit: 10000 }},
          ],
        },
      ],
    },

    // Knex externals:
    externals: [
      'sqlite3',
      'mariasql',
      'mysql2',
      'mssql',
      'mysql',
      'oracle',
      'pg-query-stream',
      'oracledb',
      'strong-oracle',
      'pg-native',
    ],
  };
}
