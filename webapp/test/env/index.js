/*
const app = <Elem/>;
const res = react.render(app, elem);
foo = new ReactWrapper(res, elem)
foo.find()
 */

import Server from 'leadfoot/Server';
import chromedriver from 'chromedriver';

const server = new Server('http://localhost:9515');

const up = () => {
  const session = server.createSession({}, {});
  return Promise.all([session]).then(([session]) => {
    return {session};
  });
};

const down = ({session}) => {
  return Promise.all([session.quit()]);
};

const exec = (stuff) => {
  const init = up();
  init.then(stuff).then(
    (result) => {
      return init.then(down).then(() => Promise.resolve(result));
    },
    (err) => {
      return init.then(down).then(() => Promise.reject(err));
    }
  );
};
