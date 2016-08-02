import {createElement} from 'react';
import {Form, Panel} from 'react-bootstrap';
import {reduxForm} from 'redux-form';
import styles from './result-form.css';
import classNames from 'classnames';
import {FormattedMessage} from 'react-intl';
import {SubmitBtn} from '../button/button';
import Heading from '../heading/heading';
import map from 'lodash/fp/map';
import isEmpty from 'lodash/fp/isEmpty';

// TODO: make this work for 4 team matchgroup forms
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

const mapInitialResultsStateToProps = (state) => {
  return {
    initialValues: state.app.matchResults,
  };
};

const defaultInitialAttendanceStatus = 'PRESENT';

const getDefaultAttendanceOrValue = (team) => (
  team.attendanceStatus ?
    team.attendanceStatus :
    defaultInitialAttendanceStatus
);

const getInitialAttendanceStatuses = (matchGroupTeams) => (
  {
    team1: getDefaultAttendanceOrValue(matchGroupTeams[0]),
    team2: getDefaultAttendanceOrValue(matchGroupTeams[1]),
    team3: getDefaultAttendanceOrValue(matchGroupTeams[2]),
    team4: matchGroupTeams.length === 4 ?
            getDefaultAttendanceOrValue(matchGroupTeams[3]) :
            null,
  }
);

// this doesn't really map the redux state,
// but was the only way I could find to initialize redux form field values
const getMapInitialAttendanceStateToProps = (matchGroupTeams) => (state) => {
  return {
    initialValues: getInitialAttendanceStatuses(matchGroupTeams),
  };
};

const generateRankingSubmissionRow = (teams, teamId, rankNumber) => (
  <div className={classNames(styles.formGroup)}>
    <label className={classNames(styles.colXsTitle)}>
      <FormattedMessage
        id={`teamId ${rankNumber}`}
        defaultMessage={`Team ${rankNumber}:`}
      />
    </label>
    <select
      className={classNames(styles.goodForm, {
        [styles.errorForm]: teamId.error &&
                            teamId.touched})}
      {...teamId}
    >
      <option value=''>Select a team...</option>
      {map((team) => (
        <option value={team.teamId}key={team.teamId}>
          {team.firstPlayer.name} & {team.secondPlayer.name}
        </option>
      ), teams)}
    </select>
    {teamId.touched && teamId.error &&
      <div className={classNames(styles.errorMsg)}>
      <Heading kind='error'>
            {teamId.error}
          </Heading>
      </div>}
  </div>);

const ResultFormRows = reduxForm({
  fields: ['teamId1', 'teamId2', 'teamId3', 'teamId4'],
}, mapInitialResultsStateToProps)(({
  fields: {teamId1, teamId2, teamId3, teamId4},
  matchTeams,
  handleSubmit,
}) => (
  <Form horizontal onSubmit={handleSubmit}>
    {generateRankingSubmissionRow(matchTeams, teamId1, 1)}
    {generateRankingSubmissionRow(matchTeams, teamId2, 2)}
    {generateRankingSubmissionRow(matchTeams, teamId3, 3)}
    {matchTeams.length === 4 ?
      generateRankingSubmissionRow(matchTeams, teamId4, 4) :
      null}
    <div className={classNames(styles.center)}>
      <SubmitBtn type='submit'>Submit Results</SubmitBtn>
    </div>
  </Form>
));

const generateAttendanceSubmissionRow = (team, teamField) => (
  <div>
    <label className={classNames(styles.colXsTitle)}>
      {team.firstPlayer.name} & {team.secondPlayer.name}
    </label>
    <select className={classNames(styles.goodForm)} {...teamField}>
      <option value='PRESENT'>Present</option>
      <option value='LATE'>Late</option>
      <option value='NO_SHOW'>No Show</option>
    </select>
  </div>
);

const getAttendanceStatusForm = (matchGroupTeams) => reduxForm({
  fields: ['team1', 'team2', 'team3', 'team4'],
}, getMapInitialAttendanceStateToProps(matchGroupTeams))(({
  fields: {team1, team2, team3, team4},
  matchTeams,
  handleSubmit,
}) => (
  <Form horizontal onSubmit={handleSubmit}>
    <div>
      {generateAttendanceSubmissionRow(matchTeams[0], team1)}
      {generateAttendanceSubmissionRow(matchTeams[1], team2)}
      {generateAttendanceSubmissionRow(matchTeams[2], team3)}
      {matchTeams.length === 4 ?
        generateAttendanceSubmissionRow(matchTeams[3], team4) :
        null}
      <SubmitBtn type='submit'>Submit Results</SubmitBtn>
    </div>
  </Form>
));

const onAttendanceSubmissionSuccess = () => {
  alert('Attendance Submission Success!');
  return Promise.resolve();
}

// note this is a function that returns a react component
// generated based on the parameters
export const ResultForm = (
  formName,
  matchGroup,
  matchGroupTeams,
  reportMatchResults,
  updateTeamAttendanceStatus) => {
  const AttendanceStatusForm = getAttendanceStatusForm(matchGroupTeams);
  return (
    <div>
      <Panel header='Result Submission' bsStyle='primary'>
        <ResultFormRows
          form={`${formName}Results`}
          matchTeams={matchGroupTeams}
          onSubmit={
            (props) => {
              const errors = validate(props);
              if (!isEmpty(errors)) {
                return Promise.reject(errors);
              }
              return reportMatchResults({
                results: props,
                matchGroupId: matchGroup.matchGroupId,
              }).then(() => {
                alert('Results submitted successfully');
                return Promise.resolve();
              }).catch((errors) => {
                alert('Results submission failure');
                return Promise.reject(errors);
              });
            }
          }
        />
      </Panel>
      <Panel header='Attendance Status Submission' bsStyle='primary'>
        <AttendanceStatusForm
          form={`${formName}Attendance`}
          matchTeams={matchGroupTeams}
          onSubmit={
            (props) => {
              // TODO: create an api call for updating MatchGroup attendances!
              updateTeamAttendanceStatus(matchGroupTeams[0], props.team1).then(() =>
                updateTeamAttendanceStatus(matchGroupTeams[1], props.team2).then(() =>
                  updateTeamAttendanceStatus(matchGroupTeams[2], props.team3).then(() =>
                    matchGroupTeams.length === 4 ?
                      updateTeamAttendanceStatus(matchGroupTeams[3], props.team4).then(() =>
                        onAttendanceSubmissionSuccess()
                      ) :
                      onAttendanceSubmissionSuccess()
                    )))
              .catch((errors) => {
                alert('Attendance submission failure');
                return Promise.reject(errors);
              });
            }
          }
        />
      </Panel>
    </div>
  );
};
