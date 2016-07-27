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

const generateRankingSubmissionRow = (teams, rank, rankNumber) => (
  <div className={classNames(styles.formGroup)}>
    <label className={classNames(styles.colXsTitle)}>
      <FormattedMessage
        id={`rank ${rankNumber}`}
        defaultMessage={`Rank ${rankNumber}:`}
      />
    </label>
    <select
      className={classNames(styles.goodForm, {
        [styles.errorForm]: rank.error &&
                            rank.touched})}
      {...rank}
    >
      <option value=''>Select a team...</option>
      {map((team) => (
        <option value={team.teamId}key={team.teamId}>
          {team.firstPlayer.name} & {team.secondPlayer.name}
        </option>
      ), teams)}
    </select>
    {rank.touched && rank.error &&
      <div className={classNames(styles.errorMsg)}>
      <Heading kind='error'>
            {rank.error}
          </Heading>
      </div>}
  </div>);

const ResultFormThree = reduxForm({
  form: 'resultFour',
  fields: ['rank1', 'rank2', 'rank3', 'rank4'],
})(({
  fields: {rank1, rank2, rank3, rank4},
  matchTeams,
  handleSubmit,
}) => (
  <Form horizontal onSubmit={handleSubmit}>
  {generateRankingSubmissionRow(matchTeams, rank1, 1)}
  {generateRankingSubmissionRow(matchTeams, rank2, 2)}
  {generateRankingSubmissionRow(matchTeams, rank3, 3)}
  {matchTeams.length === 4 ?
    generateRankingSubmissionRow(matchTeams, rank4, 4) :
    console.log("nay")}
  <div className={classNames(styles.center)}>
    <SubmitBtn type='submit'>Submit Results</SubmitBtn>
  </div>
  </Form>
));

export const ResultForm = (matchGroupTeams) => {
  return (
    <Well>
      <Panel header='Result Submission' bsStyle='primary'>
        <ResultFormThree
          matchTeams={matchGroupTeams}
          onSubmit={(props) => {
            // below code is a placeholder for more functional code later
            const temp = props;
            temp.rank1 = props.rank1;
            // console.log('submitted: ', props);
          }}
        />
      </Panel>
    </Well>
  );
};
