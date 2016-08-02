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
      return Promise.resolve(body);
    }
    return Promise.reject(body);
  });
};

export const getTeams = () => {
  return fetch(`${root}teams`).then((response) => {
    return response.json();
  });
};

export const updateTeamPlayTime = (team) => {
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
};

export const updateTeamAttendanceStatus = (team, attendanceStatus, state) => {
  return fetch(
    `${root}team/${team.teamId}/attendance/status`, {
      method: 'PUT',
      body: JSON.stringify({AttendanceStatus: attendanceStatus}),
      headers: new Headers({
        'Content-Type': 'application.json',
        Authorization: state.app.loggedIn.authorizationToken,
      }),
    }).then((response) => {
      const body = response.json();
      if (response.ok) {
        return Promise.resolve(body);
      }
      return Promise.reject(body);
    });
};

export const getTeamAttendanceCard = (team) => {
  return fetch(
    `${root}team/${team.teamId}/attendance`, {
      method: 'GET',
      headers: new Headers({
        'Content-Type': 'application.json',
        Authorization: team.autorizationToken,
      }),
    }).then((response) => {
      const body = response.json();
      if (response.ok) {
        return Promise.resolve(body);
      }
      return Promise.reject(body);
    });
};
