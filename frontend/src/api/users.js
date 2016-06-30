export const addUser = (potato) => {
  return fetch(`${root}players/new`, {
    method: 'POST',
    body: JSON.stringify(potato),
  }).then((response) => {
    return response.json();
  });
};
