import {createElement, Element} from 'react';
import {connect} from 'react-redux';
import {createAction} from 'redux-actions';

import SignUp from './component/signup/signup';
import CreateTeam from './component/create-team/create-team';
import Ladder from './component/ladder/ladder';

import styles from './app.css';

const removePlayer = createAction('PLAYER_REMOVE');

const restoreState = createAction('STATE_RESTORE');
const saveState = createAction('STATE_SAVE');

const restoreFromBrowser = () => (dispatch) => {
  const state = JSON.parse(localStorage.getItem('yolo'));
  dispatch(restoreState(state));
};

const saveToBrowser = () => (dispatch, getState) => {
  const state = getState().app;
  localStorage.setItem('yolo', JSON.stringify(state));
  dispatch(saveState());
};

const App = ({
  restoreFromBrowser,
  saveToBrowser,
}) : Element => (
  <div className={styles.app}>
    <button onClick={saveToBrowser}>SAVE</button>
    <button onClick={restoreFromBrowser}>RESTORE</button>
    <SignUp/>
    <CreateTeam/>
    <Ladder/>
  </div>
);

export default connect(
  (state) => ({
    players: state.app.players,
    teams: state.app.teams,
  }),
  {removePlayer, restoreFromBrowser, saveToBrowser}
)(App);
