import {regenerateLadder} from '../../../action/ladder';
import {connect} from 'react-redux';
import React, {createElement} from 'react';
import {Button} from 'react-bootstrap';
import {AlertModal} from '../../alert/alert-modal';

const mapStateToProps = () => (
  {
    dmmy: 1,
  }
);

const assignGlobalReference = function(a) {
  global.alert = a;
};

const LadderRegen = React.createClass({

  handleSubmit: function() {
    this.props.regenerateLadder().then(() =>
      alert.open('Ladder Regenerated Successfully')
    ).catch(() =>
      alert.open('Ladder Regeneration Failure. Make sure all' +
      ' MatchGroups have submitted their scores')
    );
  },

  render: function() {
    return (
      <div>
        <AlertModal
          ref={assignGlobalReference}
        />
        <p>
          Pressing this button will regenerate the ladder rankings
          based on the current MatchGroups, their submitted scores, and
          Attendance Statuses.
        </p>
        <p>
          All current MatchGroups, scores, playtimes, and
           attendance statuses will be reset. Proceed with caution.
        </p>
        <Button bsStyle='danger' onClick={this.handleSubmit} type='submit'>
          Regenerate Ladder
        </Button>
      </div>
    );
  },
});

export default connect(mapStateToProps, {
  regenerateLadder,
})(LadderRegen);
