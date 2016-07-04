import {createElement, Element} from 'react';
import {FormattedMessage} from 'react-intl';
import {connect} from 'react-redux';
import map from 'lodash/fp/map';
import sortBy from 'lodash/fp/sortBy';
import styles from './ladder.css';

import Heading from '../heading/heading';

const orderPlayers = map((player) => (
  <div key={player.userId}>
    {player.firstName} {player.lastName}
  </div>
));

const orderTeams = map((team) => (
  <div
    className={styles.table}
    key={team.teamId}
  >
    <div className={styles.entry}>
      <FormattedMessage
        id='rank'
        defaultMessage='Rank: '
      />
      {team.ladderPosition}
    </div>
    <div className={styles.entry}>
      <FormattedMessage
        id='player1'
        defaultMessage='First Player: '
      />
      {team.firstPlayer.name}
    </div>
    <div className={styles.entry}>
      <FormattedMessage
        id='player2'
        defaultMessage='Second Player: '
      />
      {team.secondPlayer.name}
    </div>
  </div>
));

const Ladder = ({
  players,
  teams,
}) : Element => (
  <div className={styles.ladder}>
    <div>
      <Heading kind='huge'>
        <FormattedMessage
          id='playerList'
          defaultMessage='Players'
        />
      </Heading>
    {orderPlayers(players)}
    </div>
    <div>
      <Heading kind='huge'>
        <FormattedMessage
          id='teamList'
          defaultMessage='Teams'
        />
      </Heading>
      {orderTeams(teams)}
    </div>
  </div>
);

export default connect(
  (state) => ({
    players: sortBy('firstName', state.app.players),
    teams: sortBy('ladderPosition', state.app.teams),
  }),
  {}
)(Ladder);
