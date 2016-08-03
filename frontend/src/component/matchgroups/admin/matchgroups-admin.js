import {createElement, Element} from 'react';
import {connect} from 'react-redux';
import {
  getMatchGroups,
  generateMatchGroups,
  reportMatchResults,
} from '../../../action/matchgroups';
import map from 'lodash/fp/map';
import reduce from 'lodash/fp/reduce';
import find from 'lodash/fp/find';
import classNames from 'classnames';
import styles from './matchgroups-admin.css';
import {ResultForm} from '../../match-results/result-form';
import {Well, Panel, Button} from 'react-bootstrap';
import {updateTeamAttendanceStatus} from '../../../action/teams';
import {AlertModal} from '../../alert/alert-modal';

const getTeams = (state) => {
  const matchGroups = state.app.matchGroups;
  const teams = state.app.teams;

  const teamIds = reduce((output, {teamId1, teamId2, teamId3, teamId4}) => {
    return teamId4 ? [
      ...output,
      teamId1,
      teamId2,
      teamId3,
      teamId4,
    ] : [
      ...output,
      teamId1,
      teamId2,
      teamId3,
    ];
  }, [], matchGroups);

  return map(
    (teamId) => find({teamId}, teams),
    teamIds
  );
};

const matchGroupTeams = ({matchGroup, teams}) => {
  const teamIds = matchGroup.teamId4 ? [
    matchGroup.teamId1,
    matchGroup.teamId2,
    matchGroup.teamId3,
    matchGroup.teamId4] : [
      matchGroup.teamId1,
      matchGroup.teamId2,
      matchGroup.teamId3];
  return map(
    (teamId) => find({teamId}, teams),
    teamIds
  );
};

const MatchGroupForms = (
  {formName,
  matchGroup,
  teams,
  reportMatchResults,
  updateTeamAttendanceStatus}) => {
  const matchTeams = matchGroupTeams({matchGroup, teams});
  return (
    <Well>
      <Panel header={`Group ID: ${matchGroup.matchGroupId}`} bsStyle='primary'>
        <div className={classNames(styles.teamNames)}>
          {matchTeams.map((team) => (
            <div key={team.teamId}>
              {team.firstPlayer.name} & {team.secondPlayer.name}
            </div>
          ))}
        </div>
      </Panel>
      {ResultForm(
        formName,
        matchGroup,
        matchTeams,
        reportMatchResults,
        updateTeamAttendanceStatus)}
    </Well>
  );
};

const assignGlobalReference = function(a) {
  global.alert = a;
};

const MatchGroupsDummy = ({
  getMatchGroups,
  generateMatchGroups,
  teams,
  matchGroups,
  reportMatchResults,
  updateTeamAttendanceStatus,
}) : Element => {
  let formCount = 0;
  return (
    <div className={styles.matchGroupPage}>
      <div>
        <AlertModal
          ref={assignGlobalReference}
        />
        <Button
          bsStyle='danger'
          onClick={() =>
            generateMatchGroups().then(() =>
              alert.open('Match Groups Generated Successfully!')
            ).catch(() =>
              alert.open(
                'MatchGroup Generation Failure. ' +
                'Make sure there are at least 3 teams attending.')
            )
          }
        >
          GENERATE
        </Button>
          <Button
            bsStyle='danger'
            onClick={() =>
              getMatchGroups()
            }
          >
            FETCH
            </Button>
      </div>
      <div>
        {matchGroups.map((matchGroup) => {
          formCount++;
          return (
            <MatchGroupForms
              key={matchGroup.matchGroupId}
              formName={`adminResultForm${formCount}`}
              matchGroup={matchGroup}
              reportMatchResults={reportMatchResults}
              teams={teams}
              updateTeamAttendanceStatus={updateTeamAttendanceStatus}
            />);
        })}
      </div>
    </div>);
};

export const MatchGroups = connect(
  (state) => ({
    login: state.app.loggedIn,
    matchGroups: state.app.matchGroups,
    teams: getTeams(state),
  }), {
    getMatchGroups,
    generateMatchGroups,
    reportMatchResults,
    updateTeamAttendanceStatus,
  }
)(MatchGroupsDummy);
