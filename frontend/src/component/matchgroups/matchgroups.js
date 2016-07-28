import {MatchGroups as UserMatchGroupsPage} from './user/matchgroups';
import {MatchGroups as AdminMatchGroupsPage} from './admin/matchgroups-admin';
import React, {createElement} from 'react';
import {connect} from 'react-redux';


// {this.props.userInfo.userRole === 'ADMIN' ?
//   <AdminMatchGroupsPage/> :
//   <UserMatchGroupsPage/>}

const MatchGroupsDummy = React.createClass({
  render: function() {
    return (<div>
      <AdminMatchGroupsPage/>
    </div>);
  },
});

const mapStateToProps = (state) => {
  return {
    userInfo: state.app.userInfo,
  };
};

export const MatchGroups = connect(mapStateToProps)(MatchGroupsDummy);
