import React, {createElement} from 'react';
import {connect} from 'react-redux';
import {getMatchSchedule, generateMatchGroups, reportMatchResults}
  from '../../../action/matchgroups';
import map from 'lodash/fp/map';
import {Well, Panel} from 'react-bootstrap';
import styles from './matchgroups.css';
import Heading from '../../heading/heading';

const getMatchGroupTeams = (matchGroup, allTeams) => {
  const teamIds = matchGroup.teamId4 ? [
    matchGroup.teamId1,
    matchGroup.teamId2,
    matchGroup.teamId3,
    matchGroup.teamId4] : [
      matchGroup.teamId1,
      matchGroup.teamId2,
      matchGroup.teamId3];
  return map(
    (teamId) => allTeams.find((team) => (team.teamId === teamId)),
    teamIds);
};

const displayTeams = (teams) => {
  return map((team) => (<div>
    {team.firstPlayer.name} & {team.secondPlayer.name}
  </div>), teams);
};

const displayMatchGroup = (matchGroup, allTeams) => {
  const matchGroupTeams = getMatchGroupTeams(matchGroup, allTeams);
  return displayTeams(matchGroupTeams);
};

const displayCourt = (courtNumber, court, allTeams) => {
  return (
  <Panel
    header={`Court ${courtNumber}`}
    bsStyle='primary'
  >
    <Panel header='8:00PM' bsStyle='info'>
      {court.TIME_SLOT_A ?
        displayMatchGroup(court.TIME_SLOT_A, allTeams) :
        'Nothing Scheduled'}
    </Panel>
    <Panel header='9:30PM' bsStyle='info'>
      {court.TIME_SLOT_B ?
        displayMatchGroup(court.TIME_SLOT_B, allTeams) :
        'Nothing Scheduled'}
    </Panel>
  </Panel>);
};

const displayCourts = (courts, allTeams) => {
  let courtNumber = 0;
  return map((court) => {
    courtNumber++;
    return displayCourt(courtNumber, court, allTeams);
  }, courts);
};

const showCheckAgainMessage = () => {
  return (
    <Heading>
      No match schedule has been created yet for this week.
      <br />
      Please check back later.
    </Heading>
  );
};

const MatchGroupsDummy = React.createClass({
  render: function() {
    return (
      <Well className={styles.wellContainer}>
        {this.props.matchSchedule.length === 0 ?
          showCheckAgainMessage() :
          displayCourts(this.props.matchSchedule, this.props.teams)}
      </Well>
  );},
});

const mapStateToProps = (state) => (
  {
    login: state.app.loggedIn,
    matchSchedule: state.app.matchSchedule,
    teams: state.app.teams,
  }
);

export const MatchGroups =
  connect(mapStateToProps,
    {generateMatchGroups,
    getMatchSchedule,
    reportMatchResults})(MatchGroupsDummy);
