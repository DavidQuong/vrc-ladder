import {
  addTeam as addTeamAPI,
  getTeams as getTeamsAPI,
  updateTeamStatus as updateTeamStatusAPI} from '../api/teams';
import {syncTeams} from './types';

export const addTeam = (user, login) => () => {
  return addTeamAPI(user, login);
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

export const updateTeamStatus = (teamInfo) => () => {
  return updateTeamStatusAPI(teamInfo).then((response) => {
    if (response.error) {
      return Promise.reject();
    }
    return Promise.resolve();
  }).catch((error) => {
    return Promise.reject(error);
  });
};
