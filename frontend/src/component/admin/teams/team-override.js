import {createElement, Element} from 'react';
import {connect} from 'react-redux';
import {reduxForm} from 'redux-form';
import {FormattedMessage} from 'react-intl';
import {Well, FormControl} from 'react-bootstrap';
import {
  removeTeam,
  updateTeamStatus,
  getTeams} from '../../../action/teams';

import classNames from 'classnames';
import styles from '../admin.css';
import sortBy from 'lodash/fp/sortBy';
import map from 'lodash/fp/map';
import Heading from '../../heading/heading';

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
      <button type='submit'>Update Attendance</button>
    </div>
  </form>
));

const TeamUpdate = ({team, updateTeamStatus, getTeams}) => {
  return (
    <TeamUpdateForm
      team={team}
      form={`teamOverride-${team.teamId}`}
      onSubmit={(props) => {
        props.teamId = team.teamId;
        return updateTeamStatus({
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
  updateTeamStatus,
  getTeams,
}) : Element => (
  <Well className={`${styles.ladderTableContainer} table-responsive`}>
    <div>
    <Heading>
      <FormattedMessage
        id='updateTeams'
        defaultMessage='Team Override:'
      />
    </Heading>
      {teams.map((team) => (
        <TeamUpdate
          key={team.teamId}
          team={team}
          updateTeamStatus={updateTeamStatus}
          getTeams={getTeams}
        />
      ))}
    </div>
    <div>
      <button
        onClick={() => DeleteTeams({teams, removeTeam})}
      >Delete Selected Teams</button>
    </div>
  </Well>
);
export default connect(
  (state) => ({
    teams: sortBy('ladderPosition', state.app.teams),
  }), {
    removeTeam,
    updateTeamStatus,
    getTeams,
  }
)(TeamOverride);
