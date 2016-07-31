import React, {createElement} from 'react';
import {connect} from 'react-redux';
import {getMatchGroups, reportMatchResults} from '../../action/matchgroups';
import {ResultForm} from './result-form';
import {Well} from 'react-bootstrap';
import {getMatchGroupTeams, findUserMatchGroup}
  from '../../util/matchgroup-util';

const displayMatchGroup = (matchGroup, allTeams, reportMatchResults) => {
  return (
    <Well>
      {matchGroup ?
        ResultForm(
          'playerResultForm',
          matchGroup,
          getMatchGroupTeams(matchGroup, allTeams),
          reportMatchResults) :
        'You\'re not in any MatchGroups!'}
    </Well>);
};

const MatchResultsDummy = React.createClass({
  render: function() {
    return (<div>
      {this.props.teamInfo.teamId ?
        displayMatchGroup(findUserMatchGroup(
          this.props.matchGroups,
          this.props.teams,
          this.props.teamInfo.teamId),
            this.props.teams,
            this.props.reportMatchResults) :
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
    {getMatchGroups, reportMatchResults})(MatchResultsDummy);
