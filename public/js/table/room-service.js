// @flow

import client from '../api/client';

export default {
  fetchAll: async (): Object => (await client.get('/rooms')).data,
};
