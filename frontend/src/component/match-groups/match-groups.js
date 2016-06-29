import {createElement, Element} from 'react';
import {FormattedMessage} from 'react-intl';
import {connect} from 'react-redux';
// import map from 'lodash/fp/map';
import sortBy from 'lodash/fp/sortBy';
import styles from './match-groups.css';

const MatchGroup = ({
}) : Element => (
  <div className={styles.matchGroupPage}>
    <div className={styles.matchGroupTitle}>
      <FormattedMessage
        id='Matchgroups_title'
        defaultMessage='This weeks&quot; match group'
      />
    </div>
    <div>
      <br/>
      <div className={styles.matchGroup}>
        <div>
          <FormattedMessage
            id='court #1'
            defaultMessage='Court #1'
          />
        </div>
          <FormattedMessage
            id='match1'
            defaultMessage='Team 1 vs Team 2 @8:30pm --> Winner Results: 1'
          /><br/>
          <FormattedMessage
            id='match2'
            defaultMessage='Team 1 vs Team 3 @8:30pm --> Winner Results: 1'
          /><br/>
          <FormattedMessage
            id='match3'
            defaultMessage='Team 2 vs Team 3 @8:30pm --> Winner Results: 3'
          /><br/>
      </div>
      <div className={styles.matchGroup}>
        <div>
        </div>
        <FormattedMessage
          id='matchGroup2'
          defaultMessage='MatchGroup 2'
        /><br/>
          <FormattedMessage
            id='match1'
            defaultMessage='Team 4 vs Team 5 @8:30pm -->    Winner Results:   4'
          /><br/>
          <FormattedMessage
            id='match2'
            defaultMessage='Team 4 vs Team 6 @8:30pm -->    Winner Results:   6'
          /><br/>
          <FormattedMessage
            id='match3'
            defaultMessage='Team 5 vs Team 6 @8:30pm -->    Winner Results:   4'
          /><br/>
      </div>
      <div className={styles.matchGroup}>
        <div>
        </div>
        <FormattedMessage
          id='matchGroup3'
          defaultMessage='MatchGroup 3'
        /><br/>
          <FormattedMessage
            id='match1'
            defaultMessage='Team 7 vs Team 8 @8:30pm -->    Winner Results:   7'
          /><br/>
          <FormattedMessage
            id='match2'
            defaultMessage='Team 7 vs Team 9 @8:30pm -->    Winner Results:   7'
          /><br/>
          <FormattedMessage
            id='match3'
            defaultMessage='Team 9 vs Team W @8:30pm -->    Winner Results:   9'
          /><br/>
          <FormattedMessage
            id='match3'
            defaultMessage='Team 10 vs Team D @8:30pm -->    Winner Results:  ~'
          /><br/>
      </div>
    </div>
  </div>
);

export default connect(
  (state) => ({
    players: sortBy('firstName', state.app.players),
    teams: sortBy('rank', state.app.teams),
  }),
  {}
)(MatchGroup);
