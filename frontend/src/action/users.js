import {addUser as addUserAPI, getUser as getUserAPI} from '../api/users';
import {syncPlayers} from './types';

export const addUser = (user) => () => {
  return addUserAPI(user);
};

export const getUser =  () => (dispatch) => {
  return getUserAPI().then((response) => {
    if (response.error) {
      return Promise.reject();
    }
    dispatch(syncPlayers(response.users));
    return Promise.resolve();
  }).catch((error) => {
    return Promise.reject(error);
  });
};
