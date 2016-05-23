/* global window */

import {createStore as baseCreateStore, applyMiddleware, compose} from 'redux';
import reducer from './reducer';
import thunk from 'redux-thunk';

const createStore = compose(
  applyMiddleware(thunk),
  window.devToolsExtension ? window.devToolsExtension() : (f) => f
)(baseCreateStore);

export default (initialState) => createStore(reducer, initialState);
