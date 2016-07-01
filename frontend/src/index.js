import {createElement, Element} from 'react';
import {Provider} from 'react-redux';
import {IntlProvider} from 'react-intl';
import {Router, Route, IndexRoute, browserHistory} from 'react-router';
import SignUp from './component/signup/signup';
import Ladder from './component/ladder/ladder';
import CreateTeam from './component/create-team/create-team';
import {Nav, NavItem, Navbar} from 'react-bootstrap';
import {LinkContainer} from 'react-router-bootstrap';
import styles from './index.css';

const Layout = ({children}) => (
  <div>
    <div>
      <Navbar inverse fixedTop className={styles.navbarInverse}>
        <Navbar.Header>
          <Navbar.Brand>
            <LinkContainer to='/'>
              <img src={require('url?limit=10000!./../src/public/logo.png')} />
            </LinkContainer>
          </Navbar.Brand>
          <Navbar.Toggle />
        </Navbar.Header>
        <Navbar.Collapse>
          <Nav pullLeft>
            <p className={styles.navbarLogo}>Vancouver Racquets Club</p>
          </Nav>
          <Nav pullRight>
            <LinkContainer to='/ladder'>
              <NavItem bsStyle='link'>Ladder</NavItem>
            </LinkContainer>
            <LinkContainer to='/create-team'>
              <NavItem bsStyle='link'>Create team</NavItem>
            </LinkContainer>
            <LinkContainer to='/signup'>
              <NavItem bsStyle='link'>Sign up</NavItem>
            </LinkContainer>
          </Nav>
        </Navbar.Collapse>
      </Navbar>
    </div>
    <div>
      {children}
    </div>
  </div>
);

export default ({store}) : Element => (
  <Provider store={store}>
    <IntlProvider messages={{}} defaultLocale='en-US'>
      <Router history={browserHistory}>
        <Route path='/' component={Layout}>
          <IndexRoute component={Ladder}/>
          <Route path='/signup' component={SignUp}/>
          <Route path='/ladder' component={Ladder}/>
          <Route path='/create-team' component={CreateTeam}/>
        </Route>
      </Router>
    </IntlProvider>
  </Provider>
);
