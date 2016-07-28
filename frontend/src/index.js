import {createElement, Element} from 'react';
import {Provider} from 'react-redux';
import {IntlProvider} from 'react-intl';
import {Router, Route, IndexRoute, browserHistory} from 'react-router';
import {getPlayer} from './action/users';
import {getTeams} from './action/teams';
import SignUp from './component/signup/signup';
import Ladder from './component/ladder/ladder';
import MatchGroups from './component/match-groups/match-groups';
import CreateTeam from './component/profile/profile';
import UpdateProfile from './component/profile/update-profile';
import {Nav, Navbar, Grid} from 'react-bootstrap';
import {LinkContainer} from 'react-router-bootstrap';
import styles from './index.css';
import LogIn from './component/login/login';
import Logout from './component/logout/logout';
import {UserLabel} from './component/user-label/user-label';
import {NavTabs} from './component/nav-tabs/nav-tabs';

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
        <UserLabel/>
        <Navbar.Collapse>
          <NavTabs/>
        </Navbar.Collapse>
      </Navbar>
      <Navbar fixedTop className={styles.lowerNavbar}>
        <Nav className={styles.lowerNavContainer}>
          <Navbar.Text className={styles.lowerNavbarHeading}>
            {children.props.route.navbarTitle}
          </Navbar.Text>
        </Nav>
      </Navbar>
    </div>
    <Grid>
      {children}
    </Grid>
  </div>
);

export default ({store}) : Element => (
  <Provider store={store}>
    <IntlProvider messages={{}} defaultLocale='en-US'>
      <Router history={browserHistory}>
        <Route path='/' component={Layout}>
          <IndexRoute navbarTitle='Login' component={LogIn}/>
          <Route
            path='/signup'
            navbarTitle='Sign Up'
            component={SignUp}
          />
          <Route
            path='/login'
            navbarTitle='Login'
            component={LogIn}
          />
          <Route
            path='/logout'
            component={Logout}
          />
          <Route
            path='/updateProfile'
            component={UpdateProfile}
          />
          <Route
            path='/profile'
            navbarTitle='Profile'
            component={CreateTeam}
          />
          <Route
            path='/match-groups'
            navbarTitle='Match Schedule'
            component={MatchGroups}
          />
          <Route
            path='/ladder'
            navbarTitle='Weekly Doubles Leaderboard'
            component={Ladder}
            onEnter={(nextState, replace, callback) => {
              store.dispatch(getPlayer()).then(callback);
              store.dispatch(getTeams()).then(callback);
            }}
          />
        </Route>
      </Router>
    </IntlProvider>
  </Provider>
);
