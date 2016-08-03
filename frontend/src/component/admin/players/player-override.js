import {createElement, Element} from 'react';
import {connect} from 'react-redux';
import {reduxForm} from 'redux-form';
import {removeUser} from '../../../action/users';
import {Table, Button, Checkbox} from 'react-bootstrap';

import sortBy from 'lodash/fp/sortBy';

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
  <tr>
    <td>{player.userId}</td>
    <td>{player.name}</td>
    <td><Checkbox {...check} /></td>
  </tr>
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
  <Table responsive fill>
    <thead>
      <tr>
        <th><strong>User ID</strong></th>
        <th><strong>Player Name</strong></th>
        <th><strong>Delete?</strong></th>
      </tr>
    </thead>
    <tbody>
      {players.map((player) => (
        <PlayerDelete
          key={player.userId}
          player={player}
        />
      ))}
    </tbody>
    <Button
      bsStyle='danger'
      onClick={() => DeletePlayers({players, removeUser})}
    >Delete Selected Players</Button>
  </Table>
);
export default connect(
  (state) => ({
    players: sortBy('userId', state.app.players),
  }), {
    removeUser}
)(PlayerOverride);
