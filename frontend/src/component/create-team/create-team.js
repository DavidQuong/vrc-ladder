import {createElement, Element} from 'react';
import {FormattedMessage} from 'react-intl';
import {connect} from 'react-redux';
import {reduxForm} from 'redux-form';
import {addTeam} from '../../action/teams';
import {SubmitBtn} from '../button';
import {withRouter} from 'react-router';

import map from 'lodash/fp/map';
import styles from './create-team.css';
import Heading from '../heading/heading';
import classNames from 'classnames';
// import findIndex from 'lodash/fp/findIndex';
import sortBy from 'lodash/fp/sortBy';

const validate = (values, {teams}) => {
  const errors = {};
  let index = -1;
  if (!values.firstPlayerId) {
    errors.firstPlayerId = 'Required';
  }
  if (!values.secondPlayerId) {
    errors.secondPlayerId = 'Required';
  }
  if (values.firstPlayerId === values.secondPlayerId) {
    errors.secondPlayerId = 'Cannot be same person';
  }
  for (let i = 0; i < teams.length; i++) {
    if (teams[i].firstPlayer.userId === values.firstPlayerId) {
      index = i;
      if (teams[index].secondPlayer.userId === values.secondPlayerId) {
        errors.secondPlayerId = 'Team Exists';
      }
    }
  }
  index = -1;
  for (let i = 0; i < teams.length; i++) {
    if (teams[i].firstPlayer.userId === values.secondPlayerId) {
      index = i;
      if (teams[index].secondPlayer.userId === values.firstPlayerId) {
        errors.secondPlayerId = 'Team Exists';
      }
    }
  }
  return errors;
};

const CreateTeamForm = reduxForm({
  form: 'teamCreate',
  fields: ['firstPlayerId', 'secondPlayerId'],
  validate,
})(({
  fields: {firstPlayerId, secondPlayerId},
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
          id='firstPlayerId'
          defaultMessage='Select Team Members Name'
        />
      </label>
      <select
        className={classNames(styles.goodForm, {
          [styles.errorForm]: secondPlayerId.error &&
                              firstPlayerId.touched &&
                              secondPlayerId.touched})}
        {...firstPlayerId}
      >
        <option value=''>Select a player...</option>
        {map((player) => (
          <option value={player.userId}key={player.userId}>
            {player.firstName} {player.userId}
          </option>
        ), players)}
      </select>
    </div>
    <div className={classNames(styles.formGroup)}>
      <label
        className={classNames(styles.colXsTitle)}
      >
        <FormattedMessage
          id='secondPlayerId'
          defaultMessage='Select Team Members Name'
        />
      </label>
      <select
        className={classNames(styles.goodForm, {
          [styles.errorForm]: secondPlayerId.error &&
                              firstPlayerId.touched &&
                              secondPlayerId.touched})}
        {...secondPlayerId}
      >
        <option value=''>Select a player...</option>
        {map((player) => (
          <option value={player.userId}key={player.userId}>
            {player.firstName} {player.userId}
          </option>
        ), players)}
      </select>
      {firstPlayerId.touched && secondPlayerId.touched
        && secondPlayerId.error &&
        <div className={classNames(styles.errorMsg)}>
          <Heading kind='error'>
            {secondPlayerId.error}
          </Heading>
        </div>}
    </div>
    <div className={classNames(styles.center)}>
      <SubmitBtn type='submit'>Create Team</SubmitBtn>
    </div>
  </form>
));

const CreateTeam = withRouter(({
  addTeam,
  players,
  teams,
  router,
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
        return addTeam({
          ...props,
        }).then(() => {
          router.push('/');
        }).catch((errors) => {
          // TODO: Error object handling
          return Promise.reject(errors);
        });
      }}
    />
  </div>
));

export default connect(
  (state) => ({
    players: sortBy('firstName', state.app.players),
    teams: state.app.teams,
  }),
  {addTeam}
)(CreateTeam);
