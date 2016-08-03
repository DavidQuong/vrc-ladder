import {createElement, Element} from 'react';
import {connect} from 'react-redux';
import {reduxForm} from 'redux-form';
import {Button, Form, FormControl, Well} from 'react-bootstrap';
import {createAction} from 'redux-actions';
import {FormattedMessage} from 'react-intl';
import {updateLadder} from '../../../action/ladder';

import SubmitBtn from '../../button/button';
import classNames from 'classnames';
import styles from '../admin.css';
import sortBy from 'lodash/fp/sortBy';
import findIndex from 'lodash/fp/findIndex';
import isEmpty from 'lodash/fp/isEmpty';
import Heading from '../../heading/heading';

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
      <div className={classNames(styles.errorMsg)}>
        <Heading kind='error'>
          {error}
        </Heading>
      </div>
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
  <Form horizontal onSubmit={handleSubmit}>
    <label
      className={classNames(styles.colXsTitle)}
    >
    </label>
    <div>
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
        <SubmitBtn type='submit'>Update</SubmitBtn>
      </td>
      <div>
        <FormError {...rank}/>
      </div>
    </div>
  </Form>
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

const LadderOverride = ({
  teams,
  SquashGaps,
  UpdateTeamPositions,
  updateLadder,
}) : Element => (
  <div>
    <div>
      <Button
        bsStyle='warning'
        onClick={() => SquashGaps(teams)}
      >SquashGaps</Button>
      {teams.map((team) => (
        <TeamLadderUpdate
          key={team.teamId}
          team={team}
          teams={teams}
          UpdateTeamPositions={UpdateTeamPositions}
        />
      ))}
      <div>
        <Button
          bsStyle='warning'
          onClick={() => GetTeamIds({teams, updateLadder})}
        >Send Update</Button>
      </div>
    </div>
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
