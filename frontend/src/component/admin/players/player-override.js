import {createElement, Element} from 'react';
import {connect} from 'react-redux';
import {reduxForm} from 'redux-form';
import {Form, FormControl, Well} from 'react-bootstrap';
import {createAction} from 'redux-actions';

import classNames from 'classnames';
import styles from '../admin.css';
import sortBy from 'lodash/fp/sortBy';
import findIndex from 'lodash/fp/findIndex';
import isEmpty from 'lodash/fp/isEmpty';
import Heading from '../../heading/heading';

const PlayerUpdate = ({player}) => {
  return <div>{player.userId} {player.name}</div>;
};

const LadderOverride = ({
  players,
}) : Element => (
  <Well className={`${styles.ladderTableContainer} table-responsive`}>
    <div>
      {players.map((player) => (
        <PlayerUpdate
          key={player.userId}
          player={player}
        />
      ))}
    </div>
  </Well>
);
export default connect(
  (state) => ({
    players: sortBy('userId', state.app.players),
  }), {
  }
)(LadderOverride);
