import React, {createElement} from 'react';
import {Nav, NavItem} from 'react-bootstrap';
import {LinkContainer} from 'react-router-bootstrap';
import {connect} from 'react-redux';
import styles from './nav-tabs.css';

export const NavTabsDummy = React.createClass({
  render() {
    return (this.props.loggedIn.authorizationToken) ?
      (<Nav pullRight>
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
      (<Nav pullRight className={styles.navBar}>
       <LinkContainer to='/'>
         <NavItem>Log in</NavItem>
       </LinkContainer>
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
  }
);

export const NavTabs = connect(mapStateToProps)(NavTabsDummy);
