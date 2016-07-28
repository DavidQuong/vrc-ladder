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
import isEmpty from 'lodash/fp/isEmpty';

const validate = (values) => {
  const errors = {};
  if (values.teamId1 === values.teamId2) {
    errors.teamId1 = 'Cannot be same team';
    errors.teamId2 = 'Cannot be same team';
  } else if (values.teamId1 === values.teamId3) {
    errors.teamId1 = 'Cannot be same team';
    errors.teamId3 = 'Cannot be same team';
  } else if (values.teamId2 === values.teamId3) {
    errors.teamId2 = 'Cannot be same team';
    errors.teamId3 = 'Cannot be same team';
  }
  return errors;
};

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

const MatchGroupForms = ({matchGroup, teams}) => {
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
        {ResultForm(matchGroup, matchTeams, (props) => {
          const errors = validate(props);
          if (!isEmpty(errors)) {
            return Promise.reject(errors);
          }
          console.log('submitted: ', props);
          console.log('matchgroup: ', matchGroup.matchGroupId);
          console.log(reportMatchResults);
          return reportMatchResults({
            ...props,
            ...matchGroup.matchGroupId,
          }).then((response) => {
            console.log("Submitted: ", response);
            return Promise.resolve();
          }).catch((errors) => {
            console.log('errors: ', errors);
            return Promise.reject(errors);
          });
        })}
      </Panel>
    </Well>
  );
};

const MatchGroups = withRouter(({
  getMatchGroups,
  generateMatchGroups,
  teams,
  matchGroups,
  regenerateMatchGroups,
}) : Element => (
  <div className={styles.matchGroupPage}>
    <div>
      <button onClick={() => generateMatchGroups()}>GENERATE</button>
      <button onClick={() => regenerateMatchGroups()}>REGENERATE</button>
      <button onClick={() => getMatchGroups()}>FETCH</button>
    </div>
    <div>
    {matchGroups.map((matchGroup) => (
      <MatchGroupForms
        key={matchGroup.matchGroupId}
        matchGroup={matchGroup}
        teams={teams}
      />
    ))}
    </div>
  </div>
));

export default connect(
  (state) => ({
    login: state.app.loggedIn,
    matchGroups: state.app.matchGroups,
    teams: getTeams(state),
  }), {
    getMatchGroups,
    generateMatchGroups,
    reportMatchResults,
    regenerateMatchGroups}
)(MatchGroups);
