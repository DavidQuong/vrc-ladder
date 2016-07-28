import {
  addUser as addUserAPI,
  getPlayer as getPlayerAPI,
  getCurrentActiveUserInfo as getCurrentActiveUserInfoAPI,
  getTeamInfo as getTeamInfoAPI} from '../api/users';
import {syncPlayers, syncUserInfo, syncTeamInfo} from './types';

export const addUser = (user) => () => {
  return addUserAPI(user);
};

export const getCurrentActiveUserInfo = () => (dispatch, getState) => {
  const state = getState();
  return getCurrentActiveUserInfoAPI(state).then((response) => {
    if (response.error) {
      return Promise.reject();
    }
    dispatch(syncUserInfo(response.user));
    return Promise.resolve();
  }).catch((error) => {
    return Promise.reject(error);
  });
};

export const getPlayer =  () => (dispatch, getState) => {
  const state = getState();
  return getPlayerAPI(state).then((response) => {
    if (response.error) {
      return Promise.reject();
    }
    dispatch(syncPlayers(response.users));
    return Promise.resolve();
  }).catch((error) => {
    return Promise.reject(error);
  });
};

export const getTeamInfo = () => (dispatch, getState) => {
  const state = getState();
  return getTeamInfoAPI(state).then((response) => {
    if (response.error) {
      return Promise.reject();
    }
    dispatch(syncTeamInfo(response.teams));
    return Promise.resolve();
  }).catch((error) => {
    return Promise.reject(error);
  });
};
// TODO update this for actually updating user.
export const updateUser = (user) => () => {
  return addUserAPI(user);
};
