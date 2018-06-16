// @flow

/* eslint-disable global-require */

import 'babel-polyfill';

import React from 'react';
import ReactDOM from 'react-dom';
import { createBrowserHistory } from 'history';
import moment from 'moment';

import Root from './Root';
import createStore from './store';
import '../sass/app.scss';

moment.locale('fi');

const history = createBrowserHistory();
const store = createStore(history);

function render(Component: any): void {
  ReactDOM.render(
    <Component store={store} history={history} />,
    document.getElementById('root'),
  );
}

render(Root);

/* eslint-disable */
if (module.hot) {
  module.hot.accept(['./store'], () => render(require('./Root').default));
}
/* eslint-enable */
