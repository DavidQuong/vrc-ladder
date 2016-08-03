import {createElement, Element} from 'react';
import {connect} from 'react-redux';
import {Panel, Well, Nav, NavItem} from 'react-bootstrap';
import {createAction} from 'redux-actions';

import styles from './admin.css';
import LadderOverride from './ladder/ladder-override';
import PlayerOverride from './players/player-override';
import TeamOverride from './teams/team-override';
import {requestPDF} from '../../action/ladder';

const updateView = createAction('ADMIN_VIEW');

const ToggleView = ({adminView}) => {
  switch (adminView) {
  case 'ladder': return <LadderOverride/>;
  case 'player': return <PlayerOverride/>;
  case 'team': return <TeamOverride/>;
  default: return null;
  }
};

const ViewSwitch = (props) => (dispatch) => {
  dispatch(updateView(props));
};

const EmailLadderPDF = ({requestPDF}) => {
  return requestPDF().then(() => {
    // some response
  }).catch(() => {
    // future code
  });
};

const Admin = ({
  adminView,
  ViewSwitch,
  requestPDF,
  EmailLadderPDF,
}) : Element => (
  <Well className={`${styles.ladderTableContainer} table-responsive`}>
    <Nav
      bsStyle='tabs'
      activeKey={adminView}
      onSelect={(eventKey) => ViewSwitch(eventKey)}
    >
      <NavItem eventKey='ladder'>Reorder Ladder</NavItem>
      <NavItem eventKey='player'>Delete Players</NavItem>
      <NavItem eventKey='team'>Update Teams</NavItem>
      <NavItem onClick={() => EmailLadderPDF({requestPDF})}>
        Request PDF Email
      </NavItem>
    </Nav>
    <Panel>
      <ToggleView adminView={adminView} />
    </Panel>
  </Well>
);

export default connect(
  (state) => ({
    adminView: state.app.adminView,
  }), {
    ViewSwitch,
    requestPDF,
    EmailLadderPDF,
  }
)(Admin);
