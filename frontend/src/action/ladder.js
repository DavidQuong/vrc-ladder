import {
  updateLadder as updateLadderAPI,
  regenerateLadder as regenerateLadderAPI,
  requestPDF as requestPDFAPI} from '../api/ladder';

export const regenerateLadder = () => (dispatch, getState) => {
  const state = getState();
  return regenerateLadderAPI(state).then((request) => {
    return Promise.resolve(request);
  }).catch((error) => {
    return Promise.reject(error);
  });
};

export const updateLadder = (teamIds) => (dispatch, getState) => {
  const state = getState();
  return updateLadderAPI(teamIds, state).then((response) => {
    if (response.error) {
      return Promise.reject();
    }
    return Promise.resolve();
  }).catch((error) => {
    return Promise.reject(error);
  });
};

export const requestPDF = () => (dispatch, getState) => {
  const state = getState();
  return requestPDFAPI(state).then(() => {
    return Promise.resolve();
  }).catch((error) => {
    return Promise.reject(error);
  });
};
