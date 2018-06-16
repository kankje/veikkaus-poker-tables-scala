// @flow

import React from 'react';
import Helmet from 'react-helmet';

type Props = {
  children: ?React.Element<any>,
};

export default function App({ children }: Props): ?React.Element<any> {
  return (
    <div className="container">
      <Helmet defaultTitle="Pokeripöytien tilanne" titleTemplate="%s - Pokeripöytien tilanne" />
      {children}
    </div>
  );
}
