import {createElement, Element} from 'react';
import {FormattedMessage} from 'react-intl';
import {connect} from 'react-redux';
import {createAction} from 'redux-actions';
import {reduxForm} from 'redux-form';
import map from 'lodash/fp/map';
import styles from './create-team.css';
import {SubmitBtn} from '../button';
import Heading from '../heading/heading';
import classNames from 'classnames';
import findIndex from 'lodash/fp/findIndex';
import sortBy from 'lodash/fp/sortBy';

const validate = (values, {teams}) => {
  const errors = {};
  let i = 0;
  if (!values.firstPlayer) {
    errors.firstPlayer = 'Required';
  }
  if (!values.secondPlayer) {
    errors.secondPlayer = 'Required';
  }
  if (values.firstPlayer === values.secondPlayer) {
    errors.secondPlayer = 'Cannot be same person';
  }
  i = findIndex(['firstPlayer', values.firstPlayer], teams);
  if (i !== -1) {
    if (teams[i].secondPlayer === values.secondPlayer) {
      errors.secondPlayer = 'Team Exists';
    }
  }
  i = findIndex(['firstPlayer', values.secondPlayer], teams);
  if (i !== -1) {
    if (teams[i].secondPlayer === values.firstPlayer) {
      errors.secondPlayer = 'Team Exists';
    }
  }

  return errors;
};

const CreateTeamForm = reduxForm({
  form: 'teamCreate',
  fields: ['firstPlayer', 'secondPlayer', 'teamName'],
  validate,
})(({
  fields: {firstPlayer, secondPlayer, teamName},
  players,
  handleSubmit,
}) => (
  <form
    className={styles.formHorizontal}
    onSubmit={handleSubmit}
  >
  <div className={classNames(styles.formGroup)}>
    <label
      className={classNames(styles.colXsTitle)}
    >
      <FormattedMessage
        id='teamName'
        defaultMessage='Team Name'
      />
    </label>
    <input
      className={classNames(styles.goodForm)}
      type='text'
      placeholder='Team Name'
      {...teamName}
    />
  </div>
  <div className={classNames(styles.formGroup)}>
    <label
      className={classNames(styles.colXsTitle)}
    >
        <FormattedMessage
          id='firstPlayer'
          defaultMessage='Select Team Members Name'
        />
      </label>
      <select
        className={classNames(styles.goodForm, {
          [styles.errorForm]: secondPlayer.error &&
                              firstPlayer.touched &&
                              secondPlayer.touched})}
        {...firstPlayer}
      >
        <option value=''>Select a player...</option>
        {map((player) => (
          <option value={player.firstName}key={player.firstName}>
            {player.firstName}
          </option>
        ), players)}
      </select>
    </div>
    <div className={classNames(styles.formGroup)}>
      <label
        className={classNames(styles.colXsTitle)}
      >
        <FormattedMessage
          id='secondPlayer'
          defaultMessage='Select Team Members Name'
        />
      </label>
      <select
        className={classNames(styles.goodForm, {
          [styles.errorForm]: secondPlayer.error &&
                              firstPlayer.touched &&
                              secondPlayer.touched})}
        {...secondPlayer}
      >
        <option value=''>Select a player...</option>
        {map((player) => (
          <option value={player.firstName}key={player.firstName}>
            {player.firstName}
          </option>
        ), players)}
      </select>
      {firstPlayer.touched && secondPlayer.touched && secondPlayer.error &&
        <div className={classNames(styles.errorMsg)}>
          <Heading kind='error'>
            {secondPlayer.error}
          </Heading>
        </div>}
    </div>
    <div className={classNames(styles.center)}>
      <SubmitBtn type='submit'>Create Team</SubmitBtn>
    </div>
  </form>
));

const addTeam = createAction('TEAM_ADD');

const CreateTeam = ({
  addTeam,
  players,
  teams,
}) : Element => (
  <div className={styles.createTeam}>
    <Heading kind='huge'>
      <FormattedMessage
        id='createTeam'
        defaultMessage='Create Team'
      />
    </Heading>
    <CreateTeamForm
      players={players}
      teams={teams}
      onSubmit={(props) => {
        addTeam({
          ...props,
          rank: teams.length + 1,
        });
      }}
    />
  </div>
);

export default connect(
  (state) => ({
    players: sortBy('firstName', state.app.players),
    teams: state.app.teams,
  }),
  {addTeam}
)(CreateTeam);
