import {createElement, Element} from 'react';
import {connect} from 'react-redux';
import {getMatchGroups, generateMatchGroups} from '../../action/matchgroups';
import {withRouter} from 'react-router';

import styles from './match-groups.css';

const MatchGroups = withRouter(({
  getMatchGroups,
  generateMatchGroups,
}) : Element => (
  <div className={styles.matchGroupPage}>
    <div>
      <button onClick={() => generateMatchGroups()}>GENERATE</button>
      <button onClick={() => getMatchGroups()}>FETCH</button>
    </div>
  </div>
));

export default connect(
  (state) => ({
    teams: state.app.teams,
    login: state.app.loggedIn,
  }), {
    getMatchGroups,
    generateMatchGroups}
)(MatchGroups);
