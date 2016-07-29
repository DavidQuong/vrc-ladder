import {createElement, Element} from 'react';
import {connect} from 'react-redux';
import {
  getMatchGroups,
  generateMatchGroups,
  reportMatchResults,
  regenerateMatchGroups} from '../../../action/matchgroups';
import {withRouter} from 'react-router';
import map from 'lodash/fp/map';
import reduce from 'lodash/fp/reduce';
import find from 'lodash/fp/find';
import classNames from 'classnames';
import styles from './matchgroups-admin.css';
import {ResultForm} from '../../match-results/result-form';
import {Well, Panel} from 'react-bootstrap';

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

const MatchGroupForms = ({formName, matchGroup, teams, reportMatchResults}) => {
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
      <Panel header='Result Submission' bsStyle='primary'>
        {ResultForm(formName, matchGroup, matchTeams, reportMatchResults)}
      </Panel>
    </Well>
  );
};

const MatchGroupsDummy = withRouter(({
  getMatchGroups,
  generateMatchGroups,
  teams,
  matchGroups,
  regenerateMatchGroups,
  reportMatchResults,
}) : Element => {
  let formCount = 0;
  return (<div className={styles.matchGroupPage}>
    <div>
      <button onClick={() => generateMatchGroups()}>GENERATE</button>
      <button onClick={() => regenerateMatchGroups()}>REGENERATE</button>
      <button onClick={() => getMatchGroups()}>FETCH</button>
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
        />);
    })}
    </div>
  </div>);
});

export const MatchGroups = connect(
  (state) => ({
    login: state.app.loggedIn,
    matchGroups: state.app.matchGroups,
    teams: getTeams(state),
  }), {
    getMatchGroups,
    generateMatchGroups,
    reportMatchResults,
    regenerateMatchGroups}
)(MatchGroupsDummy);
