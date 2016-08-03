import {createElement, Element} from 'react';
import {connect} from 'react-redux';
import {reduxForm} from 'redux-form';
import {addTeam, updateTeamPlayTime} from '../../action/teams';
import {SubmitBtn} from '../button/button';
import {withRouter} from 'react-router';
import {getTeamInfo} from '../../action/users';
import {
  ListGroup, ListGroupItem, Row, Col, Grid,
  FormControl, FormGroup, Form, HelpBlock, Panel, Well,
} from 'react-bootstrap';
import map from 'lodash/fp/map';
import styles from './profile.css';
import classNames from 'classnames';
import isEmpty from 'lodash/fp/isEmpty';
import sortBy from 'lodash/fp/sortBy';
import {AlertModal} from '../alert/alert-modal';

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

const getValidationState = (item) => {
  if (item.touched) {
    if (item.error) {
      return {validationState: 'error'};
    }
    return {validationState: 'success'};
  }
  return null;
};

const mapInitialTimefromProps = (state, props) => {
  return {
    initialValues: props.team,
  };
};

const UpdateAttendanceForm = reduxForm({
  fields: ['playTime'],
}, mapInitialTimefromProps)(({
  fields: {playTime},
  handleSubmit,
}) => (
  <Form horizontal onSubmit={handleSubmit}>
    <Grid className={styles.grid}>
      <FormGroup {...getValidationState(playTime)}>
        <Col md={6}>
          <FormControl componentClass='select' {...playTime}>
            <option value=''>Select a time...</option>
            {map((playTimes) => (
              <option value={playTimes.value} key={playTimes.value}>
                {playTimes.time}
              </option>
            ), playTimes)}
          </FormControl>
          {playTime.touched && playTime.error &&
            <HelpBlock>{playTime.error}</HelpBlock>}
        </Col>
        <Col sm={1}>
          <SubmitBtn type='submit'>Update</SubmitBtn>
        </Col>
      </FormGroup>
    </Grid>
  </Form>
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
  <Form horizontal onSubmit={handleSubmit}>
    <Grid className={styles.grid}>
      <FormGroup {...getValidationState(secondPlayerId)}>
        <Col md={3} sm={5}><label>{"Select Your Partner"}</label></Col>
        <Col md={8} sm={13}>
          <FormControl componentClass='select' {...secondPlayerId}>
            <option value='' disabled>Select a player...</option>
            {map((player) => (
              <option value={player.userId}key={player.userId}>
                {player.name} {player.userId}
              </option>
            ), players)}
          </FormControl>
          {secondPlayerId.touched && secondPlayerId.error &&
            <HelpBlock>{secondPlayerId.error}</HelpBlock>}
        </Col>
      </FormGroup>
      <div className={classNames(styles.center)}>
        <SubmitBtn type='submit'>Create Team</SubmitBtn>
      </div>
    </Grid>
  </Form>
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
        <Col sm={10} md={6}>{userInfo.phoneNumber}</Col>
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

const DisplayTeamInfo = ({team, updateTeamPlayTime}) => {
  return (
    <ListGroupItem key={team.teamId}>
      <Grid className={styles.grid}>
        <Row>
          <Col sm={1}><strong>Rank:</strong></Col>
          <Col sm={1}>{team.ladderPosition}</Col>
          <Col sm={2}>{team.firstPlayer.name}</Col>
          <Col sm={2}>{team.secondPlayer.name}</Col>
          <Col sm={2} md={2}><strong>Preferred Play Time:</strong></Col>
          <Col sm={4} md={4}>
            {<UpdateAttendanceForm
              team={team}
              form={`${team.teamId}updateTeam`}
              onSubmit={(props) => {
                props.teamId = team.teamId;
                const errors = validateUpdateStatus(props);
                if (!isEmpty(errors)) {
                  return Promise.reject(errors);
                }
                return updateTeamPlayTime({
                  ...props,
                }).then(() => {
                  getTeamInfo();
                  alert.open('Play Time Updated');
                }).catch(() => {
                  alert.open('You\'re already attending in another team');
                });
              }}
             />
            }
          </Col>

        </Row>
      </Grid>
    </ListGroupItem>
  );
};

const assignGlobalReference = function(a) {
  global.alert = a;
};

const CreateTeam = withRouter(({
  addTeam,
  players,
  teams,
  login,
  userInfo,
  teamInfo,
  getTeamInfo,
  updateTeamPlayTime,
  router,
}) : Element => (
  <Well>
    <AlertModal
      ref={assignGlobalReference}
    />
    <Panel header='My Profile' bsStyle='primary'>
      {displayMyInfo(userInfo)}
    </Panel>
    <Panel header='My Teams' bsStyle='primary'>
      <ListGroup fill>
      {teamInfo.map((team) => (
        <DisplayTeamInfo
          key={team.teamId}
          team={team}
          updateTeamPlayTime={updateTeamPlayTime}
        />
      ))}
      </ListGroup>
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
            alert.open('Team Created!');
          }).catch(() => {
            alert.open('Team Exists');
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
    teamInfo: sortBy('ladderPostate', state.app.teamInfo),
  }), {
    addTeam,
    displayMyInfo,
    getTeamInfo,
    updateTeamPlayTime}
)(CreateTeam);
