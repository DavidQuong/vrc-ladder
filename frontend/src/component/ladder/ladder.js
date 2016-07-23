import {createElement, Element} from 'react';
import {FormattedMessage} from 'react-intl';
import {connect} from 'react-redux';
import map from 'lodash/fp/map';
import sortBy from 'lodash/fp/sortBy';
import styles from './ladder.css';

const getTime = (time) => {
  if (time === 'TIME_SLOT_A') {
    return '8:30';
  } else if (time === 'TIME_SLOT_B') {
    return '9:30';
  }
  return 'N/A';
};

const orderTeams = map((team) => (
  <tr className={styles.ladderTeamRow} key={team.teamId}>
    <td className={styles.ladderTeamPlace}>
      <span>{team.ladderPosition}</span>
    </td>
    <td className={styles.ladderTeamPlayer}>
      <span>{team.firstPlayer.name}</span>
    </td>
    <td className={styles.ladderTeamPlayer}>
      <span>{team.secondPlayer.name}</span>
    </td>
    <td className={styles.ladderTeamGameTime}>
      <span>{getTime(team.playTime)}</span>
    </td>
  </tr>
));

const Ladder = ({
  teams,
}) : Element => (
  <div>
    <table className={styles.ladderTable}>
      <thead>
        <tr className={styles.ladderTableHeading}>
          <th className={styles.ladderTableHeadingCol}>
            <h3 className={styles.columnHeadline}>#</h3>
          </th>
          <th className={styles.ladderTableHeadingCol}>
            <h3 className={styles.columnHeadline}>First Player</h3>
          </th>
          <th className={styles.ladderTableHeadingCol}>
            <h3 className={styles.columnHeadline}>Second Player</h3>
          </th>
          <th className={styles.ladderTableHeadingCol}>
            <h3 className={styles.columnHeadline}>Game Time</h3>
          </th>
        </tr>
      </thead>
      <tbody className={styles.ladderTableBody}>
        {orderTeams(teams)}
      </tbody>
    </table>
  </div>
);

export default connect(
  (state) => ({
    players: sortBy('firstName', state.app.players),
    teams: sortBy('ladderPosition', state.app.teams),
  }),
  {}
)(Ladder);
