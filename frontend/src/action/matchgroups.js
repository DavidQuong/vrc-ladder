import {
  getMatchGroups as getMatchGroupsAPI,
  generateMatchGroups as generateMatchGroupsAPI,
  reportMatchResults as reportMatchResultsAPI,
  getMatchResults as getMatchResultsAPI,
  regenerateMatchGroups as regenerateMatchGroupsAPI,
  getMatchSchedule as getMatchScheduleAPI} from '../api/matchgroups';
import {syncMatchGroups, syncMatchSchedule, syncMatchResults} from './types';

export const getMatchGroups = () => (dispatch, getState) => {
  const state = getState();
  return getMatchGroupsAPI(state).then((response) => {
    if (response.error) {
      return Promise.reject();
    }
    dispatch(syncMatchGroups(response.matchGroups));
    return Promise.resolve();
  }).catch((error) => {
    return Promise.reject(error);
  });
};

export const getMatchSchedule = () => (dispatch, getState) => {
  const state = getState();
  return getMatchScheduleAPI(state).then((response) => {
    if (response.error) {
      return Promise.reject();
    }
    dispatch(syncMatchSchedule(response.courts));
    return Promise.resolve();
  });
};

export const generateMatchGroups = () => (dispatch, getState) => {
  const state = getState();
  return generateMatchGroupsAPI(state).then(() => {
    return Promise.resolve();
  }).catch((error) => {
    return Promise.reject(error);
  });
};

export const regenerateMatchGroups = () => (dispatch, getState) => {
  const state = getState();
  return regenerateMatchGroupsAPI(state).then(() => {
    return Promise.resolve();
  }).catch((error) => {
    return Promise.reject(error);
  });
};

export const reportMatchResults = (props) => (dispatch, getState) => {
  const state = getState();
  return reportMatchResultsAPI(props, state);
};

export const getMatchResults = (matchGroup) => (dispatch, getState) => {
  const state = getState();
  return getMatchResultsAPI(matchGroup, state).then((response) => {
    if (response.error) {
      return Promise.reject();
    }
    dispatch(syncMatchResults(response.scores));
    return Promise.resolve();
  }).catch((error) => {
    return Promise.reject(error);
  });
};
