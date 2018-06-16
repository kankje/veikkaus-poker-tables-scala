// @flow

import React from 'react';
import { Provider } from 'react-redux';
import { Route, Redirect, Switch } from 'react-router-dom';
import { ConnectedRouter } from 'react-router-redux';
import { hot } from 'react-hot-loader';

import App from './containers/App';

import Table from './containers/Table';

type Props = {
  store: Object,
  history: Object,
};

const Root = ({ store, history }: Props): React$Node => (
  <Provider store={store}>
    <ConnectedRouter history={history}>
      <App>
        <Switch>
          <Route exact path="/" component={Table} />
          <Redirect to="/" />
        </Switch>
      </App>
    </ConnectedRouter>
  </Provider>
);

export default hot(module)(Root);
