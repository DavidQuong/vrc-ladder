// const root = 'http://vrcladder.p76biyyfpm.us-west-2.elasticbeanstalk.com/';
const root = 'http://localhost:4567/';

export const getMatchGroups = (state) => {
  return fetch(`${root}matchgroups`, {
    headers: {
      'Content-Type': 'application/json',
      Authorization: state.app.loggedIn.authorizationToken,
    },
  }).then((response) => {
    return response.json();
  });
};

export const generateMatchGroups = (state) => {
  return fetch(`${root}matchgroups/generate`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: state.app.loggedIn.authorizationToken,
    },
  }).then((response) => {
    return response.json();
  });
};
