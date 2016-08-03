import {updateLadder as updateLadderAPI} from '../api/ladder';

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
