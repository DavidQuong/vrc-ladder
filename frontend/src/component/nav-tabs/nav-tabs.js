import React, {createElement} from 'react';
import {Nav, NavItem} from 'react-bootstrap';
import {LinkContainer, IndexLinkContainer} from 'react-router-bootstrap';
import {connect} from 'react-redux';
import styles from './nav-tabs.css';

const volunteerTabs = (<Nav pullRight className={styles.navTab}>
  <LinkContainer to='/logout'>
    <NavItem>Log out</NavItem>
  </LinkContainer>
  <LinkContainer to='/profile'>
    <NavItem>Profile</NavItem>
  </LinkContainer>
  <LinkContainer to='/ladder'>
    <NavItem>Ladder</NavItem>
  </LinkContainer>
  <LinkContainer to='/match-schedule'>
    <NavItem>Match Groups</NavItem>
  </LinkContainer>
</Nav>);

const playerTabs = (<Nav pullRight className={styles.navTab}>
  <LinkContainer to='/logout'>
    <NavItem>Log out</NavItem>
  </LinkContainer>
  <LinkContainer to='/profile'>
    <NavItem>Profile</NavItem>
  </LinkContainer>
  <LinkContainer to='/ladder'>
    <NavItem>Ladder</NavItem>
  </LinkContainer>
  <LinkContainer to='/match-schedule'>
    <NavItem>Match Schedule</NavItem>
  </LinkContainer>
  <LinkContainer to='/match-results'>
    <NavItem>Match Results</NavItem>
  </LinkContainer>
</Nav>);

const signedOutTabs = (<Nav pullRight className={styles.navTab}>
 <IndexLinkContainer to='/'>
   <NavItem>Log in</NavItem>
 </IndexLinkContainer>
 <LinkContainer to='/ladder'>
   <NavItem>Ladder</NavItem>
 </LinkContainer>
 <LinkContainer to='/signup'>
   <NavItem>Sign up</NavItem>
 </LinkContainer>
</Nav>);

const NavTabsDummy = React.createClass({
  render() {
    if (this.props.loggedIn.authorizationToken) {
      return this.props.userInfo.userRole === 'VOLUNTEER' ?
        volunteerTabs :
        playerTabs;
    }
    return signedOutTabs;
  },
});

const mapStateToProps = (state) => (
  {
    loggedIn: state.app.loggedIn,
    userInfo: state.app.userInfo,
  }
);

export const NavTabs = connect(
  mapStateToProps, null, null, {pure: false}
)(NavTabsDummy);
