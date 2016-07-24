// import {Component, PropTypes} from 'react';
import {connect} from 'react-redux';
import {withRouter} from 'react-router';
import React from 'react';

const logoutAction = {type: 'USER_LOGOUT'};

const Logout = React.createClass({
  render() {
    // probably a bad idea to change the state inside a component :D
    this.props.dispatch(logoutAction);
    // don't use router.push(), because then the browser's back Button
    // will go back to this 'page', which will just instantly
    // redirect to the login page, preventing the user from going back
    this.props.router.replace('/');
    return null;
  },
});

export default connect()(withRouter(Logout), {logoutAction});
