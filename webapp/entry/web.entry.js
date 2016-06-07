/* eslint-env browser */
import {createElement} from 'react';
import {render} from 'react-dom';

// Main parts of the app.
import Root from '../src';
import createStore from '../src/store';

// These styles are not specific to any particular component. Things like
// styling `html` or `body` tags.
import '../src/global.css';

// Create the initial state of the application.
const store = createStore();

// Create the application.
const app = <Root store={store}/>;

// Find the element on the page it should be mounted at.
const container = document.getElementById('content');

// Mount the app into the page.
render(app, container);
