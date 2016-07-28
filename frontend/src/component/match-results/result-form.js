import {createElement} from 'react';
import {Form, Panel, Well} from 'react-bootstrap';
import {reduxForm} from 'redux-form';
// import styles from './result-form.css';
import styles from './temp.css';
import classNames from 'classnames';
import {FormattedMessage} from 'react-intl';
import {SubmitBtn} from '../button';
import Heading from '../heading/heading';
import map from 'lodash/fp/map';

const generateRankingSubmissionRow = (teams, teamId, rankNumber) => (
  <div className={classNames(styles.formGroup)}>
    <label className={classNames(styles.colXsTitle)}>
      <FormattedMessage
        id={`teamId ${rankNumber}`}
        defaultMessage={`TeamId ${rankNumber}:`}
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
  form: 'resultForm',
  fields: ['teamId1', 'teamId2', 'teamId3', 'teamId4'],
})(({
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

export const ResultForm = (matchGroup, matchGroupTeams, onSubmitCallback) => {
  return (
    <Well>
      <Panel header='Result Submission' bsStyle='primary'>
        <ResultFormRows
          matchTeams={matchGroupTeams}
          onSubmit={onSubmitCallback}
        />
      </Panel>
    </Well>
  );
};
