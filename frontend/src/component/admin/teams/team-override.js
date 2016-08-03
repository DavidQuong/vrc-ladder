import {createElement, Element} from 'react';
import {connect} from 'react-redux';
import {reduxForm} from 'redux-form';
import {FormControl} from 'react-bootstrap';
import {
  removeTeam,
  updateTeamPlayTime,
  getTeams} from '../../../action/teams';

import SubmitBtn from '../../button/button';
import classNames from 'classnames';
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
  <form onSubmit={handleSubmit}>
    <label
      className={classNames(styles.colXsTitle)}
    >
    </label>
    <div>
      {team.firstPlayer.name} & {team.secondPlayer.name}
      <input
        type='checkbox'
        {...check}
      />
      <FormControl componentClass='select' {...playTime}>
        <option value='' disabled>{getActivePlaytime(team)}</option>
        {map((playTimes) => (
          <option value={playTimes.value} key={playTimes.value}>
            {playTimes.time}
          </option>
        ), playTimes)}
      </FormControl>
      <SubmitBtn type='submit'>Update Attendance</SubmitBtn>
    </div>
  </form>
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
    <div>
      {teams.map((team) => (
        <TeamUpdate
          key={team.teamId}
          team={team}
          updateTeamPlayTime={updateTeamPlayTime}
          getTeams={getTeams}
        />
      ))}
    </div>
    <div>
      <SubmitBtn
        bsStyle='danger'
        onClick={() => DeleteTeams({teams, removeTeam})}
      >Delete Selected Teams</SubmitBtn>
    </div>
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
