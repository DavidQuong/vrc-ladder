import {createElement, Element} from 'react';
import {Provider} from 'react-redux';
import {IntlProvider} from 'react-intl';
import {Router, Route, IndexRoute, browserHistory} from 'react-router';
import {getPlayer} from './action/users';
import {getTeams} from './action/teams';
import {getMatchSchedule, getMatchGroups, getMatchResults}
  from './action/matchgroups';
import {Row, Col, Navbar, Grid} from 'react-bootstrap';
import {LinkContainer} from 'react-router-bootstrap';
import {UserLabel} from './component/user-label/user-label';
import {NavTabs} from './component/nav-tabs/nav-tabs';
import Admin from './component/admin/admin';
import SignUp from './component/signup/signup';
import Ladder from './component/ladder/ladder';
import {MatchGroups} from './component/matchgroups/matchgroups';
import CreateTeam from './component/profile/profile';
import UpdateProfile from './component/profile/update-profile';
import styles from './index.css';
import LogIn from './component/login/login';
import Logout from './component/logout/logout';
import {MatchResults}
  from './component/match-results/match-results';
import {findUserMatchGroupFromAllUserTeams}
  from './util/matchgroup-util';

const Layout = ({children}) => (
  <div>
     <div>
      <Navbar fixedTop className={styles.upperNavbar}>
        <Navbar.Header>
          <Navbar.Brand>
            <LinkContainer to='/'>
              <img src={require('url?limit=10000!./../src/public/logo.png')} />
            </LinkContainer>
          </Navbar.Brand>
          <Navbar.Text className={styles.navbarLogo}>
          Vancouver Racquets Club
          </Navbar.Text>
          <Navbar.Toggle />
        </Navbar.Header>
        <Navbar.Collapse>
          <NavTabs/>
        </Navbar.Collapse>
      </Navbar>
      <Navbar fixedTop className={styles.lowerNavbar}>
        <Navbar.Collapse>
          <Grid className={styles.lowerNavContainer}>
            <Row>
            <Col sm={8} md={5} className={styles.lowerNavbarHeading}>
              {children.props.route.navbarTitle}
            </Col>
            <UserLabel/>
            </Row>
          </Grid>
        </Navbar.Collapse>
      </Navbar>
    </div>
    <Grid>
      {children}
    </Grid>
  </div>
);

const syncMatchGroupsAndResults = (store) => (nextState, replace, callback) => {
  store.dispatch(getTeams()).then(() =>
    store.dispatch(getMatchGroups()).then(() => {
      const state = store.getState();
      const allMatchGroups = state.app.matchGroups;
      const allTeams = state.app.teams;
      const userTeams = state.app.teamInfo;
      const userMatchGroup = findUserMatchGroupFromAllUserTeams(
        allMatchGroups,
        allTeams,
        userTeams
      );
      userMatchGroup ?
        store.dispatch(getMatchResults(userMatchGroup))
          .catch(() => (callback())).then(callback) :
        callback();
    }));
};

export default ({store}) : Element => (
  <Provider store={store}>
    <IntlProvider messages={{}} defaultLocale='en-US'>
      <Router history={browserHistory}>
        <Route path='/' component={Layout}>
          <IndexRoute navbarTitle='' component={LogIn}/>
          <Route
            path='/signup'
            navbarTitle=''
            component={SignUp}
          />
          <Route
            path='/login'
            navbarTitle=''
            component={LogIn}
          />
          <Route
            path='/logout'
            component={Logout}
          />
          <Route
            path='/update-profile'
            navbarTitle='Change Personal Information'
            component={UpdateProfile}
          />
          <Route
            path='/profile'
            navbarTitle='Personal and Team Information'
            component={CreateTeam}
          />
          <Route
            path='/match-schedule'
            navbarTitle='Match Schedule'
            component={MatchGroups}
            onEnter={(nextState, replace, callback) =>
              store.dispatch(getTeams()).then(() =>
                store.dispatch(getMatchSchedule()).then(callback)
              )
            }
          />
          <Route
            path='/match-results'
            navbarTitle='Match Results'
            component={MatchResults}
            onEnter={syncMatchGroupsAndResults(store)}
          />
          <Route
            path='/admin'
            navbarTitle='Admin Override Panel'
            component={Admin}
            onEnter={(nextState, replace, callback) => {
              store.dispatch(getPlayer()).then(callback);
              store.dispatch(getTeams()).then(callback);
            }}
          />
          <Route
            path='/ladder'
            navbarTitle='Weekly Doubles Leaderboard'
            component={Ladder}
            onEnter={(nextState, replace, callback) => {
              store.dispatch(getTeams()).then(callback);
            }}
          />
        </Route>
      </Router>
    </IntlProvider>
  </Provider>
);
