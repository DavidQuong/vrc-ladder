import {
  addTeam as addTeamAPI,
  getTeams as getTeamsAPI,
  updateTeamPlayTime as updateTeamPlayTimeAPI,
  updateTeamAttendanceStatus as updateTeamAttendanceStatusAPI}
    from '../api/teams';
import {syncTeams} from './types';

export const addTeam = (user, login) => () => {
  return addTeamAPI(user, login);
};

export const getTeams = () => (dispatch) => {
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

export const updateTeamPlayTime = (teamInfo) => () => {
  return updateTeamPlayTimeAPI(teamInfo).then((response) => {
    if (response.error) {
      return Promise.reject();
    }
    return Promise.resolve();
  }).catch((error) => {
    return Promise.reject(error);
  });
};

export const updateTeamAttendanceStatus = (team, attendanceStatus) =>
(dispatch, getState) => {
  const state = getState();
  return updateTeamAttendanceStatusAPI(team, attendanceStatus, state)
  .then((response) => {
    if (response.error) {
      return Promise.reject();
    }
    return Promise.resolve();
  }).catch((error) => {
    return Promise.reject(error);
  });
};
