const root = 'http://localhost:4567/';

export const logInUser = (user) => {
  return fetch(`${root}login`, {
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
