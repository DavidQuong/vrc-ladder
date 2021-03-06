const root = 'http://vrcladder.p76biyyfpm.us-west-2.elasticbeanstalk.com/';

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

export const getMatchSchedule = (state) => {
  return fetch(`${root}matchgroups/schedule`, {
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
    if (!response.ok) {
      return Promise.reject(response);
    }
    return response.json();
  });
};

export const reportMatchResults = (props, state) => {
  return fetch(`${root}matchgroup/${props.matchGroupId}/scores`, {
    method: 'PUT',
    body: JSON.stringify(props.results),
    headers: {
      'Content-Type': 'application/json',
      Authorization: state.app.loggedIn.authorizationToken,
    },
  }).then((response) => {
    return response.json();
  });
};

export const getMatchResults = (matchGroup, state) => {
  return fetch(`${root}matchgroup/${matchGroup.matchGroupId}/scores`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      Authorization: state.app.loggedIn.authorizationToken,
    },
  }).then((response) => {
    return response.json();
  });
};
