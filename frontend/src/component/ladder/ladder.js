import {createElement, Element} from 'react';
// import {FormattedMessage} from 'react-intl';
import {Well} from 'react-bootstrap';
import {connect} from 'react-redux';
import map from 'lodash/fp/map';
import sortBy from 'lodash/fp/sortBy';
import styles from './ladder.css';

const getTime = (time) => {
  if (time === 'TIME_SLOT_A') {
    return '8:00 pm';
  } else if (time === 'TIME_SLOT_B') {
    return '9:30 pm';
  }
  return 'N/A';
};

const getGameTimeStyle = (time) => {
  if (time === 'TIME_SLOT_A' || time === 'TIME_SLOT_B') {
    return styles.ladderAttendingGameTime;
  }
  return styles.ladderNotAttendingGameTime;
};

const getLadderRowStyle = (team, loggedIn) => {
  const currentUserId = loggedIn.userId;
  if (team.firstPlayer.userId === currentUserId ||
    team.secondPlayer.userId === currentUserId) {
    return styles.currentUserLadderRow;
  }
  return styles.ladderTeamRow;
};

const orderTeams = (teams, loggedIn) => {
  return map((team) => (
    <tr className={getLadderRowStyle(team, loggedIn)} key={team.teamId}>
      <td className={styles.ladderTeamPlace}>
        <span>{team.ladderPosition}</span>
      </td>
      <td className={styles.ladderTeamPlayer}>
        <span>{team.firstPlayer.name}</span>
      </td>
      <td className={styles.ladderTeamPlayer}>
        <span>{team.secondPlayer.name}</span>
      </td>
      <td className={getGameTimeStyle(team.playTime)}>
        <span>{getTime(team.playTime)}</span>
      </td>
    </tr>), teams);
};

const Ladder = ({
  teams,
  loggedIn,
}) : Element => (
  <Well className={`${styles.ladderTableContainer} table-responsive`}>
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
        {orderTeams(teams, loggedIn)}
      </tbody>
    </table>
  </Well>
);

export default connect(
  (state) => ({
    players: sortBy('firstName', state.app.players),
    teams: sortBy('ladderPosition', state.app.teams),
    loggedIn: state.app.loggedIn,
  }),
  {}
)(Ladder);
