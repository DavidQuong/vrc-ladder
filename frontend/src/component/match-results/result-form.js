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

const ResultFormThree = reduxForm({
  form: 'resultFour',
  fields: ['rank1', 'rank2', 'rank3'],
})(({
  fields: {rank1, rank2, rank3},
  matchTeams,
  handleSubmit,
}) => (
  <Form horizontal onSubmit={handleSubmit}>
  <div className={classNames(styles.formGroup)}>
    <label
      className={classNames(styles.colXsTitle)}
    >
      <FormattedMessage
        id='rank1'
        defaultMessage='Rank 1:'
      />
    </label>
    <select
      className={classNames(styles.goodForm, {
        [styles.errorForm]: rank1.error &&
                            rank1.touched})}
      {...rank1}
    >
      <option value=''>Select a team...</option>
      {map((teams) => (
        <option value={teams.teamId}key={teams.teamId}>
          {teams.firstPlayer.name} & {teams.secondPlayer.name}
        </option>
      ), matchTeams)}
    </select>
    {rank1.touched && rank1.error &&
      <div className={classNames(styles.errorMsg)}>
      <Heading kind='error'>
            {rank1.error}
          </Heading>
      </div>}
  </div>
  <div className={classNames(styles.formGroup)}>
    <label
      className={classNames(styles.colXsTitle)}
    >
      <FormattedMessage
        id='rank2'
        defaultMessage='Rank 2:'
      />
    </label>
    <select
      className={classNames(styles.goodForm, {
        [styles.errorForm]: rank2.error &&
                            rank2.touched})}
      {...rank2}
    >
      <option value=''>Select a team...</option>
      {map((teams) => (
        <option value={teams.teamId}key={teams.teamId}>
          {teams.firstPlayer.name} & {teams.secondPlayer.name}
        </option>
      ), matchTeams)}
    </select>
    {rank2.touched && rank2.error &&
      <div className={classNames(styles.errorMsg)}>
      <Heading kind='error'>
            {rank2.error}
          </Heading>
      </div>}
  </div>
  <div className={classNames(styles.formGroup)}>
    <label
      className={classNames(styles.colXsTitle)}
    >
      <FormattedMessage
        id='rank3'
        defaultMessage='Rank 3:'
      />
    </label>
    <select
      className={classNames(styles.goodForm, {
        [styles.errorForm]: rank3.error &&
                            rank3.touched})}
      {...rank3}
    >
      <option value=''>Select a team...</option>
      {map((teams) => (
        <option value={teams.teamId}key={teams.teamId}>
          {teams.firstPlayer.name} & {teams.secondPlayer.name}
        </option>
      ), matchTeams)}
    </select>
    {rank3.touched && rank3.error &&
      <div className={classNames(styles.errorMsg)}>
      <Heading kind='error'>
            {rank3.error}
          </Heading>
      </div>}
  </div>
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
