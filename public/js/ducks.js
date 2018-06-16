// @flow

import { combineReducers } from 'redux';
import { routerReducer } from 'react-router-redux';
import room from './table/room-duck';

export default combineReducers({
  router: routerReducer,
  room,
});
