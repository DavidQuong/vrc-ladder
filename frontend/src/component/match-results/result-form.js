import {createElement} from 'react';
import {Form, Panel, FormGroup, Grid, Col} from 'react-bootstrap';
import {reduxForm} from 'redux-form';
import styles from './result-form.css';
import classNames from 'classnames';
import {FormattedMessage} from 'react-intl';
import {SubmitBtn} from '../button/button';
import Heading from '../heading/heading';
import map from 'lodash/fp/map';
import isEmpty from 'lodash/fp/isEmpty';
import {AlertModal} from '../alert/alert-modal';

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

const getDefaultAttendanceOrValue = (team) => (
  team.attendanceStatus ?
    team.attendanceStatus :
    'PRESENT'
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

const mapInitialAttendanceStateToProps = (state, ownProps) => {
  return {
    initialValues: getInitialAttendanceStatuses(ownProps.matchTeams),
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
  <FormGroup>
    <Col md={3} sm={5}>
      <label className={classNames(styles.colXsTitle)}>
        {team.firstPlayer.name} & {team.secondPlayer.name}
      </label>
    </Col>
    <Col>
      <select className={classNames(styles.goodForm)} {...teamField}>
        <option value='PRESENT'>Present</option>
        <option value='LATE'>Late</option>
        <option value='NO_SHOW'>No Show</option>
      </select>
    </Col>
  </FormGroup>
);

const AttendanceStatusForm = reduxForm({
  fields: ['team1', 'team2', 'team3', 'team4'],
}, mapInitialAttendanceStateToProps)(({
    fields: {team1, team2, team3, team4},
    matchTeams,
    handleSubmit,
  }) => (
    <Form horizontal onSubmit={handleSubmit}>
      <Grid className={styles.grid}>
        {generateAttendanceSubmissionRow(matchTeams[0], team1)}
        {generateAttendanceSubmissionRow(matchTeams[1], team2)}
        {generateAttendanceSubmissionRow(matchTeams[2], team3)}
        {matchTeams.length === 4 ?
          generateAttendanceSubmissionRow(matchTeams[3], team4) :
          null}
      </Grid>
      <div className={classNames(styles.center)}>
        <SubmitBtn type='submit'>
          Submit Results
        </SubmitBtn>
      </div>
    </Form>
  ));

const onAttendanceSubmissionSuccess = () => {
  alert.open('Attendance Submission Success!');
  return Promise.resolve();
};

const assignGlobalReference = function(a) {
  global.alert = a;
};

const generateOnSubmitMatchResults =
(matchGroup, reportMatchResults) => (props) => {
  const errors = validate(props);
  if (!isEmpty(errors)) {
    return Promise.reject(errors);
  }
  return reportMatchResults({
    results: props,
    matchGroupId: matchGroup.matchGroupId,
  }).then(() => {
    alert.open('Success Submitting Results');
    return Promise.resolve();
  }).catch((errors) => {
    alert.open('Results Submission Failure');
    return Promise.reject(errors);
  });
};

const generateOnSubmitAttendance =
(matchGroupTeams, updateTeamAttendanceStatus) => (props) => {
  // TODO: create an api call for updating MatchGroup attendances!
  updateTeamAttendanceStatus(
    matchGroupTeams[0],
    props.team1).then(() =>
    updateTeamAttendanceStatus(
      matchGroupTeams[1],
      props.team2).then(() =>
      updateTeamAttendanceStatus(
        matchGroupTeams[2],
        props.team3).then(() =>
        matchGroupTeams.length === 4 ?
          updateTeamAttendanceStatus(
            matchGroupTeams[3],
            props.team4).then(() =>
            onAttendanceSubmissionSuccess()
          ) :
          onAttendanceSubmissionSuccess()
        )))
  .catch((errors) => {
    alert.open('Attendance submission failure');
    return Promise.reject(errors);
  });
};

export const ResultForm = (
  formName,
  matchGroup,
  matchGroupTeams,
  reportMatchResults,
  updateTeamAttendanceStatus) => {
  return (
    <div>
      <AlertModal
        ref={assignGlobalReference}
        body='Results submitted successfully'
      />
      <Panel header='Result Submission' bsStyle='primary'>
        <ResultFormRows
          form={`${formName}Results`}
          matchTeams={matchGroupTeams}
          onSubmit={
            generateOnSubmitMatchResults(
              matchGroup,
              reportMatchResults)
          }
        />
      </Panel>
      <Panel header='Attendance Status Submission' bsStyle='primary'>
        <AttendanceStatusForm
          form={`${formName}Attendance`}
          matchTeams={matchGroupTeams}
          onSubmit={
            generateOnSubmitAttendance(
              matchGroupTeams,
              updateTeamAttendanceStatus)
          }
        />
      </Panel>
    </div>
  );
};
