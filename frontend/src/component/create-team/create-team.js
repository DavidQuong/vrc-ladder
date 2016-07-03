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

const validate = (values) => {
  const errors = {};
  if (!values.firstPlayerId) {
    errors.firstPlayerId = 'Required';
  }
  if (!values.secondPlayerId) {
    errors.secondPlayerId = 'Required';
  }
  if (values.firstPlayerId === values.secondPlayerId) {
    errors.secondPlayerId = 'Cannot be same person';
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
  login,
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
        }, login).then(() => {
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
    login: state.app.loggedIn,
  }),
  {addTeam}
)(CreateTeam);
