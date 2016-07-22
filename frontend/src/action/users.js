import {
  addUser as addUserAPI,
  getUser as getUserAPI,
  getPlayer as getPlayerAPI,
  getUserInfo as getUserInfoAPI,
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

export const getUserInfo = () => (dispatch, getState) => {
  const state = getState();
  return getUserInfoAPI(state).then((response) => {
    if (response.error) {
      return Promise.reject();
    }
    dispatch(syncUserInfo(response.user));
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

export const getUser =  () => (dispatch, getState) => {
  const state = getState();
  return getUserAPI(state).then((response) => {
    if (response.error) {
      return Promise.reject();
    }
    dispatch(syncPlayers(response.users));
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
