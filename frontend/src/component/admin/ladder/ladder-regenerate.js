import {regenerateLadder} from '../../../action/ladder';
import {connect} from 'react-redux';
import React, {createElement} from 'react';
import SubmitBtn from '../../button/button';
import {Label} from 'react-bootstrap';
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
    this.props.regenerateLadder().then(() => {
      alert.open('Ladder Regenerated Successfully');
    }).catch(() => {
      alert.open('Ladder failed to regenerate');
    });
  },

  render: function() {
    return (
      <div>
        <AlertModal
          ref={assignGlobalReference}
        />
        <Label>
          Pressing this button will regenerate the ladder rankings
          based on the current MatchGroups, their submitted scores, and
          Attendance Statuses
        </Label>
        <SubmitBtn onClick={this.handleSubmit} type='submit'>
          Regenerate Ladder
        </SubmitBtn>
      </div>
    );
  },
});

export default connect(mapStateToProps, {
  regenerateLadder,
})(LadderRegen);
