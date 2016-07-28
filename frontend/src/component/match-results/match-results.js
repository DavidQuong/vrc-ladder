import React, {createElement} from 'react';
import {connect} from 'react-redux';
import map from 'lodash/fp/map';
import {getMatchGroups} from '../../action/matchgroups';
import {ResultForm} from './result-form';

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

const findUserMatchGroup = (matchGroups, allTeams, teamId) => {
  const userMatchGroup = matchGroups.find((matchGroup) => {
    const matchGroupTeams = getMatchGroupTeams(matchGroup, allTeams);
    const userTeam = matchGroupTeams.find((team) => {
      return team.teamId === teamId;
    });
    return userTeam ? true : false;
  });
  return userMatchGroup;
};

const displayTeams = (teams) => {
  return ResultForm(teams);
};

const displayMatchGroup = (matchGroup, allTeams) => {
  return matchGroup ?
    displayTeams(getMatchGroupTeams(matchGroup, allTeams)) :
    'Try fetching MatchGroups';
};

const MatchResultsDummy = React.createClass({
  render: function() {
    return (<div>
      {this.props.teamInfo.teamId ?
        displayMatchGroup(findUserMatchGroup(
          this.props.matchGroups,
          this.props.teams,
          this.props.teamInfo.teamId),
          this.props.teams) :
        'You\'re not in any MatchGroups!'}
      </div>);
  },
});

const mapStateToProps = (state) => {
  return {
    matchGroups: state.app.matchGroups,
    teams: state.app.teams,
    teamInfo: state.app.teamInfo[0],
  };
};

export const MatchResults =
  connect(mapStateToProps,
    {getMatchGroups})(MatchResultsDummy);
