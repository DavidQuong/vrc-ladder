import {createElement, Element} from 'react';
import {connect} from 'react-redux';
import {reduxForm} from 'redux-form';
import {removeUser} from '../../../action/users';
import {Well} from 'react-bootstrap';

import classNames from 'classnames';
import styles from '../admin.css';
import sortBy from 'lodash/fp/sortBy';
import Heading from '../../heading/heading';

const validate = (values, {player}) => {
  player.check = values.check;
};

const PlayerDeleteForm = reduxForm({
  fields: ['check'],
  validate,
})(({
  fields: {check},
  player,
}) => (
  <form>
    <label
      className={classNames(styles.colXsTitle)}
    >
    </label>
    <div>
      {player.userId} & {player.name}
      <input
        type='checkbox'
        {...check}
      />
    </div>
  </form>
));

const PlayerDelete = ({player}) => {
  return (
    <PlayerDeleteForm
      player={player}
      form={`deletePlayer-${player.userId}`}
    />
  );
};

const DeletePlayers = ({players, removeUser}) => {
  players.map((player) => {
    if (player.check) {
      removeUser(player);
    }
  });
};

const PlayerOverride = ({
  players,
  removeUser,
}) : Element => (
  <Well className={`${styles.ladderTableContainer} table-responsive`}>
    <div>
      {players.map((player) => (
        <PlayerDelete
          key={player.userId}
          player={player}
        />
      ))}
      <div>
        <button
          onClick={() => DeletePlayers({players, removeUser})}
        >Send Update</button>
      </div>
    </div>
  </Well>
);
export default connect(
  (state) => ({
    players: sortBy('userId', state.app.players),
  }), {
    removeUser}
)(PlayerOverride);
