// @flow

import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { push } from 'react-router-redux';
import Wrapped from '../components/table/TablePage';
import { fetchRooms } from '../table/room-duck';

export default connect(
  state => ({
    rooms: state.room.get('rooms'),
  }),
  dispatch => bindActionCreators({
    push,
    fetchRooms,
  }, dispatch),
)(Wrapped);
