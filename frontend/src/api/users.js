const root = 'http://localhost:4567/';

export const addUser = (user) => {
//  console.log('Users :', user);
  return fetch(`${root}users`, {
    method: 'POST',
    body: JSON.stringify(user),
  }).then((response) => {
    const body = response.json();
    if (response.ok) {
      return Promise.resolve(body);
    }
//    console.log('response: ', response);
    return Promise.reject(body);
  });
};

export const getUser = () => {
  return fetch(`${root}users`).then((response) => {
    return response.json();
  });
};
