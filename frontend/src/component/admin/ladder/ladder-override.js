import {createElement, Element} from 'react';
import {connect} from 'react-redux';
import {reduxForm} from 'redux-form';
import {Button, ButtonToolbar, Table, FormControl} from 'react-bootstrap';
import {createAction} from 'redux-actions';
import {updateLadder} from '../../../action/ladder';

import SubmitBtn from '../../button/button';
import styles from '../admin.css';
import sortBy from 'lodash/fp/sortBy';
import findIndex from 'lodash/fp/findIndex';
import isEmpty from 'lodash/fp/isEmpty';
import Heading from '../../heading/heading';
import {AlertModal} from '../../alert/alert-modal';

const syncTeams = createAction('TEAM_SYNC');

const validateLadderPosition = (values, teams) => {
  const errors = {};
  if (!values.rank) {
    errors.rank = 'required';
  }
  if (isNaN(values.rank)) {
    errors.rank = 'number required';
  }
  if (values.rank > teams.length || values.rank < 1) {
    errors.rank = `out of bound 1 - ${teams.length} valid`;
  }
  return errors;
};

const FormError = ({touched, error}) => {
  if (touched && error) {
    return (
        <Heading kind='error'>
          {error}
        </Heading>
    );
  }
  return null;
};

const UpdatePositionForm = reduxForm({
  fields: ['rank'],
})(({
  fields: {rank},
  team,
  handleSubmit,
}) => (
  <tr>
    <td className={styles.ladderTeamPlace}>
      <span>{team.ladderPosition}</span>
    </td>
    <td className={styles.ladderTeamPlayer}>
      <span>{team.firstPlayer.name}</span>
    </td>
    <td className={styles.ladderTeamPlayer}>
      <span>{team.secondPlayer.name}</span>
    </td>
    <td className={styles.ladderTeamPlayer}>
      <FormControl type='rank' placeholder='new Rank' {...rank} />
    </td>
    <td className={styles.ladderTeamPlayer}>
      <SubmitBtn onClick={handleSubmit} type='submit'>Move</SubmitBtn>
    </td>
    <td>
      <FormError {...rank}/>
    </td>
  </tr>
));

const UpdateTeamPositions = ({teamId, ladderPosition, rank}, teams) =>
  (dispatch) => {
    const index = findIndex({teamId: teamId}, teams);
    let i;
    if (ladderPosition === rank) {
      // Same position dont move
    } else if (ladderPosition > rank) {
      for (i = index - 1; i >= rank - 1; i--) {
        teams[i].ladderPosition = (teams[i].ladderPosition + 1);
      }
      teams[index].ladderPosition = rank;
      dispatch(syncTeams(teams));
    } else if (ladderPosition < rank) {
      for (i = index + 1; i < rank; i++) {
        teams[i].ladderPosition = (teams[i].ladderPosition - 1);
      }
      teams[index].ladderPosition = rank;
      dispatch(syncTeams(teams));
    }
  };

const TeamLadderUpdate = ({team, teams, UpdateTeamPositions}) => {
  return (
      <UpdatePositionForm
        team={team}
        form={`updatePosition-${team.teamId}`}
        onSubmit={(props) => {
          const errors = validateLadderPosition(props, teams);
          if (!isEmpty(errors)) {
            return Promise.reject(errors);
          }
          return UpdateTeamPositions({
            ...props,
            ...team,
          }, teams);
        }}
      />
    );
};

const SquashGaps = (teams) => (dispatch) => {
  const newTeamsList = [];
  teams.map((team) => {
    team.ladderPosition = findIndex({teamId: team.teamId}, teams) + 1;
    newTeamsList.push(team);
  });
  dispatch(syncTeams(newTeamsList));
};

const GetTeamIds = ({teams, updateLadder}) => {
  const teamIds = [];
  teams.map((team) => {
    teamIds.push(team.teamId);
  });
  return updateLadder({teamIds});
};

const assignGlobalReference = function(a) {
  global.alert = a;
};

const LadderOverride = ({
  teams,
  SquashGaps,
  UpdateTeamPositions,
  updateLadder,
}) : Element => (
  <div>
    <AlertModal
      ref={assignGlobalReference}
    />
    <Table responsive fill className={styles.ladderTable}>
      <thead>
        <tr>
          <th><strong>Position</strong></th>
          <th><strong>First Player</strong></th>
          <th><strong>Second Player</strong></th>
          <th><strong>New Position</strong></th>
          <th><strong>Update?</strong></th>
        </tr>
      </thead>
      <tbody>
        {teams.map((team) => (
          <TeamLadderUpdate
            key={team.teamId}
            team={team}
            teams={teams}
            UpdateTeamPositions={UpdateTeamPositions}
          />
        ))}
      </tbody>
    </Table>
    <ButtonToolbar>
      <Button
        bsStyle='danger'
        onClick={() => SquashGaps(teams)}
      >Squash Gaps in Ladder</Button>
      <Button
        bsStyle='danger'
        onClick={() => GetTeamIds({teams, updateLadder}).then(() =>
          alert.open('Ladder Update Successful!')
        ).catch(() =>
          alert.open('Ladder Update Failure')
        )}
      >Confirm Changes</Button>
    </ButtonToolbar>
  </div>
);

export default connect(
  (state) => ({
    teams: sortBy('ladderPosition', state.app.teams),
  }), {
    SquashGaps,
    UpdateTeamPositions,
    updateLadder,
  }
)(LadderOverride);
