import React, {createElement} from 'react';
import {connect} from 'react-redux';
import {Col} from 'react-bootstrap';
import styles from './user-label.css';

const UserLabelDummy = React.createClass({
  render: function() {
    return (<Col sm={6} md={4} className={styles.userLabel}>
       {this.props.userInfo.name ?
         (`Welcome ${this.props.userInfo.name}!`) : null}
       </Col>);
  },
});

const mapStateToProps = (state) => (
  {
    userInfo: state.app.userInfo,
  }
);

export const UserLabel = connect(mapStateToProps)(UserLabelDummy);
