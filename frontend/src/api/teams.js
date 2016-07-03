const root = 'http://vrcladder.p76biyyfpm.us-west-2.elasticbeanstalk.com/';

export const addTeam = (team, login) => {
  return fetch(`${root}teams`, {
    method: 'POST',
    body: JSON.stringify(team),
    headers: new Headers({
      'Content-Type': 'application.json',
      Authorization: login.authorizationToken,
    }),
  }).then((response) => {
    const body = response.json();
    if (response.ok) {
      Promise.resolve(body).then((response) => {
        const body = response.json();
        if (response.ok) {
          return Promise.resolve(body);
        }
        return Promise.reject(body);
      });
    }
    return Promise.reject(body);
  });
};

export const getTeams = () => {
  return fetch(`${root}teams`).then((response) => {
    return response.json();
  });
};
