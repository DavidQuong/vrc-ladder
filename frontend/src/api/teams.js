const root = 'http://localhost:4567/';

export const addTeam = (team) => {
  return fetch(`${root}teams`, {
    method: 'POST',
    body: JSON.stringify(team),
  }).then((response) => {
    const body = response.json();
    if (response.ok) {
      return Promise.resolve(body);
    }
    // console.log('response: ', response);
    return Promise.reject(body);
  });
};

export const getTeams = () => {
  return fetch(`${root}teams`).then((response) => {
    // console.log('response: ', response);
    return response.json();
  });
};
