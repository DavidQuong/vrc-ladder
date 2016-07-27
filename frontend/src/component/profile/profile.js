import {createElement, Element} from 'react';
import {FormattedMessage} from 'react-intl';
import {connect} from 'react-redux';
import {reduxForm} from 'redux-form';
import {addTeam, updateTeamStatus} from '../../action/teams';
import {SubmitBtn} from '../button/button';
import {Grid, Col, Row, ListGroup, ListGroupItem, Form, Panel, Well} from 'react-bootstrap';
import {withRouter} from 'react-router';
import {getTeamInfo} from '../../action/users';

import map from 'lodash/fp/map';
import styles from './profile.css';
import Heading from '../heading/heading';
import classNames from 'classnames';
import isEmpty from 'lodash/fp/isEmpty';
import sortBy from 'lodash/fp/sortBy';

const validate = (values, userInfo) => {
  const errors = {};
  if (!values.secondPlayerId) {
    errors.secondPlayerId = 'Required';
  }
  if (userInfo.userId === values.secondPlayerId) {
    errors.secondPlayerId = 'Cannot be same person';
  }
  return errors;
};
const validateUpdateStatus = (values) => {
  const errors = {};
  if (!values.playTime) {
    errors.playTime = 'Required';
  }
  if (!values.teamId) {
    errors.teamId = 'Required';
  }
  return errors;
};

const playTimes = [{
  time: '8:00 pm',
  value: 'TIME_SLOT_A',
}, {
  time: '9:30 pm',
  value: 'TIME_SLOT_B',
}, {
  time: 'Not Playing',
  value: 'NONE',
}];

const UpdateAttendanceForm = reduxForm({
  form: 'updateTeam',
  fields: ['teamId', 'playTime'],
})(({
  fields: {teamId, playTime},
  teams,
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
        id='selectTeam'
        defaultMessage='Select Team'
      />
    </label>
    <select
      className={classNames(styles.goodForm, {
        [styles.errorForm]: teamId.error &&
                            teamId.touched})}
      {...teamId}
    >
      <option value=''>Select a team...</option>
      {map((teams) => (
        <option value={teams.teamId}key={teams.teamId}>
          With:  {teams.secondPlayer.name}
        </option>
      ), teams)}
    </select>
    {teamId.touched && teamId.error &&
      <div className={classNames(styles.errorMsg)}>
      <Heading kind='error'>
            {teamId.error}
          </Heading>
      </div>}
  </div>
  <div className={classNames(styles.formGroup)}>
    <label
      className={classNames(styles.colXsTitle)}
    >
      <FormattedMessage
        id='Select a Time'
        defaultMessage='Time'
      />
    </label>
    <select
      className={classNames(styles.goodForm, {
        [styles.errorForm]: playTime.error &&
                            playTime.touched})}
      {...playTime}
    >
      <option value=''>Select a time...</option>
      {map((playTimes) => (
        <option value={playTimes.value}key={playTimes.value}>
          {playTimes.time}
        </option>
      ), playTimes)}
    </select>
    {playTime.touched && playTime.error &&
      <div className={classNames(styles.errorMsg)}>
      <Heading kind='error'>
            {playTime.error}
          </Heading>
      </div>}
  </div>
    <div className={classNames(styles.center)}>
      <SubmitBtn type='submit'>Update Attendance</SubmitBtn>
    </div>
  </form>
));

const CreateTeamForm = reduxForm({
  form: 'teamCreate',
  fields: ['secondPlayerId'],
  validate,
})(({
  fields: {secondPlayerId},
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
          id='secondPlayerId'
          defaultMessage="Select Team Member's Name"
        />
      </label>
      <select
        className={classNames(styles.goodForm, {
          [styles.errorForm]: secondPlayerId.error &&
                              secondPlayerId.touched})}
        {...secondPlayerId}
      >
        <option value=''>Select a player...</option>
        {map((player) => (
          <option value={player.userId}key={player.userId}>
            {player.name} {player.userId}
          </option>
        ), players)}
      </select>
      {secondPlayerId.touched && secondPlayerId.error &&
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

const displayMyInfo = (userInfo) => (
  <ListGroup fill>
    <ListGroupItem>
      <Grid>
        <Col sm={3} md={2}><strong>Name:</strong></Col>
        <Col sm={10} md={6}>{userInfo.name}</Col>
      </Grid>
    </ListGroupItem>

    <ListGroupItem>
      <Grid>
        <Col sm={3} md={2}><strong>Phone Number:</strong></Col>
        <Col sm={6} md={4}>{userInfo.phoneNumber}</Col>
      </Grid>
    </ListGroupItem>

    <ListGroupItem>
      <Grid>
        <Col sm={3} md={2}><strong>Email:</strong></Col>
        <Col sm={10} md={6}>{userInfo.emailAddress}</Col>
      </Grid>
    </ListGroupItem>

  </ListGroup>
);

const getTime = (time) => {
  if (time === 'TIME_SLOT_A') {
    return '8:30';
  } else if (time === 'TIME_SLOT_B') {
    return '9:30';
  }
  return 'NONE';
};

const displayTeamInfo = map((teamInfo) => (
  <ListGroup fill>
    <ListGroupItem>
      <Grid>
        <Row className={styles.row}>
          <Col sm={5} md={3}><strong>Team With:</strong></Col>
          <Col sm={8} md={5}>{teamInfo.secondPlayer.name}</Col>
        </Row>
        <Row>
          <Col sm={5} md={3}><strong>Preferred Play Time:</strong></Col>
          <Col sm={3} md={2}>{getTime(teamInfo.playTime)}</Col>
        </Row>
      </Grid>
    </ListGroupItem>
  </ListGroup>
));

const CreateTeam = withRouter(({
  addTeam,
  players,
  teams,
  login,
  userInfo,
  teamInfo,
  getTeamInfo,
  updateTeamStatus,
  router,
}) : Element => (
  <Well>
    <Panel header='My Profile' bsStyle='primary'>
      {displayMyInfo(userInfo)}
    </Panel>

    <Panel header='My Teams' bsStyle='primary'>
      {displayTeamInfo(teamInfo)}
    </Panel>

    <Panel header='Update Attendance' bsStyle='primary'>
      <UpdateAttendanceForm
        teams={teamInfo}
        userInfo={userInfo}
        onSubmit={(props) => {
          const errors = validateUpdateStatus(props);
          if (!isEmpty(errors)) {
            return Promise.reject(errors);
          }
          return updateTeamStatus({
            ...props,
            authorizationToken: login.authorizationToken,
          }).then(() => {
            getTeamInfo();
            router.replace('/profile');
            // TODO: Show a popup here!
          });
        }}
      />
    </Panel>

    <Panel header='Create Team' bsStyle='primary'>
      <CreateTeamForm
        players={players}
        teams={teams}
        userInfo={userInfo}
        onSubmit={(props) => {
          const errors = validate(props, userInfo);
          if (!isEmpty(errors)) {
            return Promise.reject(errors);
          }
          return addTeam({
            ...props,
            firstPlayerId: userInfo.userId,
          }, login).then(() => {
            getTeamInfo();
            router.replace('/profile');
            // TODO: Show a popup here!
          }).catch(() => {
            const errors = {secondPlayerId: 'team exists'};
            return Promise.reject(errors);
          });
        }}
      />
    </Panel>
  </Well>
));
export default connect(
  (state) => ({
    players: sortBy('name', state.app.players),
    teams: state.app.teams,
    login: state.app.loggedIn,
    userInfo: state.app.userInfo,
    teamInfo: state.app.teamInfo,
  }), {
    addTeam,
    displayMyInfo,
    getTeamInfo,
    updateTeamStatus}
)(CreateTeam);
