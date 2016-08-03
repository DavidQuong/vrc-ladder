import {createElement, Element} from 'react';
import {connect} from 'react-redux';
import {reduxForm} from 'redux-form';
import {Checkbox, Table, FormControl} from 'react-bootstrap';
import {
  removeTeam, updateTeamPlayTime, getTeams,
} from '../../../action/teams';

import SubmitBtn from '../../button/button';
import styles from '../admin.css';
import sortBy from 'lodash/fp/sortBy';
import map from 'lodash/fp/map';

const validate = (values, {team}) => {
  team.check = values.check;
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

const getActivePlaytime = ({playTime}) => {
  if (playTime === 'TIME_SLOT_A') {
    return '8:00 pm';
  } else if (playTime === 'TIME_SLOT_B') {
    return '9:30 pm';
  }
  return 'NONE';
};

const TeamUpdateForm = reduxForm({
  fields: ['check', 'playTime'],
  validate,
})(({
  fields: {check, playTime},
  team,
  handleSubmit,
}) => (
  <tr>
    <td>{team.firstPlayer.name}</td>
    <td>{team.secondPlayer.name}</td>
    <td>
      <FormControl componentClass='select' {...playTime}>
        <option value='' disabled>{getActivePlaytime(team)}</option>
        {map((playTimes) => (
          <option value={playTimes.value} key={playTimes.value}>
            {playTimes.time}
          </option>
        ), playTimes)}
      </FormControl>
    </td>
    <td>
      <SubmitBtn
        className={styles.largeButton}
        onClick={handleSubmit}
        type='submit'
      >Update</SubmitBtn>
    </td>
    <td><Checkbox {...check} /></td>
  </tr>
));

const TeamUpdate = ({team, updateTeamPlayTime, getTeams}) => {
  return (
    <TeamUpdateForm
      team={team}
      form={`teamOverride-${team.teamId}`}
      onSubmit={(props) => {
        props.teamId = team.teamId;
        return updateTeamPlayTime({
          ...props,
        }).then(() => {
          getTeams();
        });
      }}
    />
  );
};

const DeleteTeams = ({teams, removeTeam}) => {
  teams.map((team) => {
    if (team.check) {
      removeTeam(team);
    }
  });
};

const TeamOverride = ({
  teams,
  removeTeam,
  updateTeamPlayTime,
  getTeams,
}) : Element => (
  <div>
    <Table responsive fill>
      <thead>
        <tr>
          <th><strong>First Player</strong></th>
          <th><strong>Second Player</strong></th>
          <th><strong>New Attendance</strong></th>
          <th><strong>Update Attendance</strong></th>
          <th><strong>Delete?</strong></th>
        </tr>
      </thead>
      <tbody>
        {teams.map((team) => (
          <TeamUpdate
            key={team.teamId}
            team={team}
            updateTeamPlayTime={updateTeamPlayTime}
            getTeams={getTeams}
          />
        ))}
      </tbody>
    </Table>
    <SubmitBtn
      bsStyle='danger'
      onClick={() => DeleteTeams({teams, removeTeam})}
    >Delete Selected Teams</SubmitBtn>
  </div>
);
export default connect(
  (state) => ({
    teams: sortBy('ladderPosition', state.app.teams),
  }), {
    removeTeam,
    updateTeamPlayTime,
    getTeams,
  }
)(TeamOverride);
