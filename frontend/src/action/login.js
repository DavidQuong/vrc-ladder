import {logInUser as logInAPI} from '../api/login';

export const logInUser = (user) => () => {
  return logInAPI(user).then((response) => {
    if (response.error) {
      return Promise.reject();
    }
    return Promise.resolve(response);
  }).catch((error) => {
    return Promise.reject(error);
  });
};
