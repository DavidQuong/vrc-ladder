import React, {createElement} from 'react';
import {connect} from 'react-redux';
import {getMatchSchedule, generateMatchGroups, reportMatchResults}
  from '../../../action/matchgroups';
import {withRouter} from 'react-router';
import map from 'lodash/fp/map';
import {Panel} from 'react-bootstrap';

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
  return (<Panel header={`Court ${courtNumber}`}>
    <Panel header='8:00PM'>
      {court.TIME_SLOT_A ?
        displayMatchGroup(court.TIME_SLOT_A, allTeams) :
        'Nothing Scheduled'}
    </Panel>
    <Panel header='9:30PM'>
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

const MatchGroupsDummy = React.createClass({
  render: function() {
    return (<div>
      <button onClick={() => this.props.router.push('/match-results')}>
        Goto Match Results Submission
      </button>
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

export const MatchGroups =
  connect(mapStateToProps,
    {generateMatchGroups,
      getMatchSchedule,
      reportMatchResults})(withRouter(MatchGroupsDummy));
