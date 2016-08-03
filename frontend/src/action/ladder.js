import {
  updateLadder as updateLadderAPI,
  requestPDF as requestPDFAPI} from '../api/ladder';

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
