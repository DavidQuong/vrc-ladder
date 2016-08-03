import React, {createElement} from 'react';
import {Nav, NavItem} from 'react-bootstrap';
import {LinkContainer, IndexLinkContainer} from 'react-router-bootstrap';
import {connect} from 'react-redux';
import styles from './nav-tabs.css';

const NavTabsDummy = React.createClass({
  render() {
    if (this.props.userInfo.userRole === 'VOLUNTEER') {
      return (<Nav pullRight className={styles.navTab}>
        <LinkContainer to='/logout'>
          <NavItem>Log out</NavItem>
        </LinkContainer>
        <LinkContainer to='/profile'>
          <NavItem>Profile</NavItem>
        </LinkContainer>
        <LinkContainer to='/ladder'>
          <NavItem>Ladder</NavItem>
        </LinkContainer>
        <LinkContainer to='/match-groups'>
          <NavItem>Match Groups</NavItem>
        </LinkContainer>
        <LinkContainer to='/admin'>
          <NavItem>Admin</NavItem>
        </LinkContainer>
      </Nav>);
    }
    return (this.props.loggedIn.authorizationToken) ?
      (<Nav pullRight className={styles.navTab}>
        <LinkContainer to='/logout'>
          <NavItem>Log out</NavItem>
        </LinkContainer>
        <LinkContainer to='/profile'>
          <NavItem>Profile</NavItem>
        </LinkContainer>
        <LinkContainer to='/ladder'>
          <NavItem>Ladder</NavItem>
        </LinkContainer>
        <LinkContainer to='/match-groups'>
          <NavItem>Match Groups</NavItem>
        </LinkContainer>
      </Nav>) :
      (<Nav pullRight className={styles.navTab}>
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
