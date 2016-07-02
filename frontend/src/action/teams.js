import {addTeam as addTeamAPI, getTeams as getTeamsAPI} from '../api/teams';
import {syncTeams} from './types';

export const addTeam = (user) => () => {
  return addTeamAPI(user);
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
