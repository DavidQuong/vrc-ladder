const root = 'http://vrcladder.p76biyyfpm.us-west-2.elasticbeanstalk.com/';

export const addUser = (user) => {
  return fetch(`${root}users`, {
    method: 'POST',
    body: JSON.stringify(user),
  }).then((response) => {
    const body = response.json();
    if (response.ok) {
      return Promise.resolve(body);
    }
    return Promise.reject(body);
  });
};

export const getCurrentActiveUserInfo = (state) => {
  return fetch(`${root}users/self`, {
    headers: {
      'Content-Type': 'application/json',
      Authorization: state.app.loggedIn.authorizationToken,
    },
  }).then((response) => {
    return response.json();
  });
};

export const getPlayer = (state) => {
  return fetch(`${root}players`, {
    headers: {
      'Content-Type': 'application/json',
      Authorization: state.app.loggedIn.authorizationToken,
    },
  }).then((response) => {
    return response.json();
  });
};

export const getTeamInfo = (state) => {
  return fetch(`${root}user/${state.app.loggedIn.userId}/teams`, {
    headers: {
      'Content-Type': 'application/json',
      Authorization: state.app.loggedIn.authorizationToken,
    },
  }).then((response) => {
    return response.json();
  });
};
/*
export const updateProfieInfo = (state) => {
  return fetch(`${root}user/${state.app.loggedIn.userId}/user`, {
    method: 'PUT',
    body: JSON.stringify({}),
    headers: {
      'Content-Type': 'application/json',
      Authorization: state.app.loggedIn.authorizationToken,
    },
  }).then((response) => {
    return response.json;
  });
};
/*
export const updateTeamStatus = (team) => {
  return fetch(
    `${root}team/${team.teamId}/attendance/playtime`, {
      method: 'PUT',
      body: JSON.stringify({playTime: team.playTime}),
      headers: new Headers({
        'Content-Type': 'application.json',
        Authorization: team.authorizationToken,
      }),
    }).then((response) => {
      const body = response.json();
      if (response.ok) {
        return Promise.resolve(body);
      }
      return Promise.reject(body);
    });
};*/
