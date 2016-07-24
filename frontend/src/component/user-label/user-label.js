import {createElement} from 'react';
import {connect} from 'react-redux';
import {Navbar} from 'react-bootstrap';
import map from 'lodash/fp/map';
import styles from './user-label.css';

const UserLabelDummy = (userInfo) => {
  return map((userInfo) => (
  <Navbar.Text className={styles.userLabel}>
    {userInfo.name}
  </Navbar.Text>), userInfo)[0];
};

const mapStateToProps = (state) => (
  {
    userInfo: state.app.userInfo,
  }
);

export const UserLabel = connect(mapStateToProps)(UserLabelDummy);
