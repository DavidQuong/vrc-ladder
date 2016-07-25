import React, {createElement} from 'react';
import {connect} from 'react-redux';
import {Navbar} from 'react-bootstrap';
import styles from './user-label.css';

const UserLabelDummy = React.createClass({
  render: function() {
    return (<Navbar.Text className={styles.userLabel}>
      {this.props.userInfo.name}
    </Navbar.Text>);
  },
});

const mapStateToProps = (state) => (
  {
    userInfo: state.app.userInfo,
  }
);

export const UserLabel = connect(mapStateToProps)(UserLabelDummy);
