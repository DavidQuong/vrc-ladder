import React, {createElement} from 'react';
import {connect} from 'react-redux';
import {getMatchGroups, reportMatchResults} from '../../action/matchgroups';
import {updateTeamAttendanceStatus} from '../../action/teams';
import {ResultForm} from './result-form';
import {Well} from 'react-bootstrap';
import {getMatchGroupTeams, findUserMatchGroupFromAllUserTeams}
  from '../../util/matchgroup-util';
import Heading from '../heading/heading';

const showNoMatchGroupMessage = () => {
  return (
    <Heading>You&#39;re not in any match groups!</Heading>
  );
};

const displayMatchGroup = (
  matchGroup,
  allTeams,
  reportMatchResults,
  updateTeamAttendanceStatus) => {
  return (
    <div>
      {matchGroup ?
        ResultForm(
          'playerResultForm',
          matchGroup,
          getMatchGroupTeams(matchGroup, allTeams),
          reportMatchResults,
          updateTeamAttendanceStatus) :
          showNoMatchGroupMessage()
      }
    </div>);
};

const MatchResultsDummy = React.createClass({
  render: function() {
    const userMatchGroup = findUserMatchGroupFromAllUserTeams(
      this.props.matchGroups,
      this.props.teams,
      this.props.teamInfo);
    return (<Well>
      {userMatchGroup ?
        displayMatchGroup(userMatchGroup,
            this.props.teams,
            this.props.reportMatchResults,
            this.props.updateTeamAttendanceStatus) :
        showNoMatchGroupMessage()}
      </Well>);
  },
});

const mapStateToProps = (state) => {
  return {
    matchGroups: state.app.matchGroups,
    teams: state.app.teams,
    teamInfo: state.app.teamInfo,
  };
};

export const MatchResults =
  connect(mapStateToProps,
    {
      getMatchGroups,
      reportMatchResults,
      updateTeamAttendanceStatus,
    })(MatchResultsDummy);
