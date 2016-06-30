import {addUser as addUserAPI} from '../api/users';
import {syncPlayers} from './types';

export const addUser = (potato) => (dispatch) => {
  addUserAPI(potato).then((users) => {
    dispatch(syncPlayers(users));
  });
};
