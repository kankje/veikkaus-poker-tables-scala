// @flow

import { Map, List } from 'immutable';
import roomService from './room-service';

const FETCH = 'veikkaus-poker-tables/room/FETCH';
const FETCH_DONE = 'veikkaus-poker-tables/room/FETCH_DONE';
const FETCH_FAILED = 'veikkaus-poker-tables/room/FETCH_FAILED';

const initialState = Map({
  rooms: List(),
});

// TODO: Test.
export default (state: Map<string, any> = initialState, action: Object = {}): Object => {
  switch (action.type) {
    case FETCH_DONE:
      return state
        .set('rooms', List(action.payload));

    default:
      return state;
  }
};

export function fetchRooms(): Function {
  return async (dispatch: Function): Promise<any> => {
    dispatch({ type: FETCH });

    try {
      const response = await roomService.fetchAll();
      dispatch({ type: FETCH_DONE, payload: response });
    } catch (e) {
      dispatch({ type: FETCH_FAILED, payload: e.message });
    }
  };
}
