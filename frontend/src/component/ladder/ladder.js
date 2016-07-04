import {createElement, Element} from 'react';
import {createAction} from 'redux-actions';
import {FormattedMessage} from 'react-intl';
import {connect} from 'react-redux';
import map from 'lodash/fp/map';
import sortBy from 'lodash/fp/sortBy';
import styles from './ladder.css';

export const syncPlayers = createAction('PLAYER_SYNC');

const orderTeams = map((team) => (
  <div className={`panel panel-default ${styles.panel}`}>
    <div className={`panel-heading ${styles.panelHeading}`}>
    Rank {team.ladderPosition}
      <span className='pull-right'>Playing at 8&#58;00PM</span>
    </div>
    <div className={`panel-body ${styles.panelBody}`}>
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
  </div>
));

const Ladder = ({
  teams,
}) : Element => (
  <div>
    <div>
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
