import {
  addTeam as addTeamAPI,
  getTeams as getTeamsAPI,
  updateTeamStatus as updateTeamStatusAPI,
  removeTeam as removeTeamAPI} from '../api/teams';
import {syncTeams} from './types';

export const addTeam = (user, login) => () => {
  return addTeamAPI(user, login);
};

export const removeTeam = (team) => (dispatch, getState) => {
  const state = getState();
  return removeTeamAPI(team, state);
};

export const getTeams =  () => (dispatch) => {
  return getTeamsAPI().then((response) => {
    if (response.error) {
      return Promise.reject();
    }
    dispatch(syncTeams(response.teams));
    return Promise.resolve();
  }).catch((error) => {
    return Promise.reject(error);
  });
};

export const updateTeamStatus = (teamInfo) => (dispatch, getState) => {
  const state = getState();
  return updateTeamStatusAPI(teamInfo, state).then((response) => {
    if (response.error) {
      return Promise.reject();
    }
    return Promise.resolve();
  }).catch((error) => {
    return Promise.reject(error);
  });
};
