import React, {createElement} from 'react';
import {connect} from 'react-redux';
import {getMatchSchedule, generateMatchGroups}
  from '../../../action/matchgroups';
// import {withRouter} from 'react-router';
import map from 'lodash/fp/map';
import {Panel} from 'react-bootstrap';

const getMatchGroupTeams = (matchGroup, allTeams) => {
  const teamIds = matchGroup.teamid4 ? [
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

const displayMatchGroup = (matchGroup, allTeams) => {
  const matchGroupTeams = getMatchGroupTeams(matchGroup, allTeams);
  return map((team) => (<div>
    {team.firstPlayer.name} & {team.secondPlayer.name}
  </div>), matchGroupTeams);
};

const displayCourt = (courtNumber, court, allTeams) => {
  return (<Panel header={`Court ${courtNumber}`}>
    <Panel header='Time Slot A'>
      {court.TIME_SLOT_A ?
        displayMatchGroup(court.TIME_SLOT_A, allTeams) :
        'Nothing Scheduled'}
    </Panel>
    <Panel header='Time Slot B'>
      {court.TIME_SLOT_B ?
        displayMatchGroup(court.TIME_SLOT_B, allTeams) :
        'Nothing scheduled'}
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

export const MatchGroups = React.createClass({
  render: function() {
    return (<div>
      Test text
      <button onClick={() => this.props.generateMatchGroups()}>GENERATE</button>
      <button onClick={() => this.props.getMatchSchedule()}>FETCH</button>
      <div>{this.props.matchSchedule.length === 0 ?
        'No Match Schedule. Try fetching!' :
        displayCourts(this.props.matchSchedule, this.props.teams)}
      </div>
    </div>);
  },
});

const mapStateToProps = (state) => (
  {
    login: state.app.loggedIn,
    matchSchedule: state.app.matchSchedule,
    teams: state.app.teams,
  }
);

export default
  connect(mapStateToProps,
    {generateMatchGroups, getMatchSchedule})(MatchGroups);
