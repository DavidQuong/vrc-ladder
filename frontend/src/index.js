import {createElement, Element} from 'react';
import {Provider} from 'react-redux';
import {IntlProvider} from 'react-intl';
import {Router, Route, IndexRoute, browserHistory} from 'react-router';
import SignUp from './component/signup/signup';
import Ladder from './component/ladder/ladder';
import CreateTeam from './component/create-team/create-team';
import LogIn from './component/login/login';
import {Button, ButtonToolbar} from 'react-bootstrap';
import {LinkContainer} from 'react-router-bootstrap';
import {getUser} from './action/users';

const Layout = ({children}) => (
  <div>
    <div>
      <ButtonToolbar>
        <LinkContainer to='/signup'>
          <Button>Sign up</Button>
        </LinkContainer>
        <LinkContainer to='/'>
          <Button>Ladder</Button>
        </LinkContainer>
        <LinkContainer to='/create-team'>
          <Button>Create team</Button>
        </LinkContainer>
        <LinkContainer to='/login'>
          <Button>Log In</Button>
        </LinkContainer>
      </ButtonToolbar>
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
          <IndexRoute
            component={Ladder}
            onEnter={(nextState, replace, callback) => {
              store.dispatch(getUser()).then(callback);
            }}
          />
          <Route path='/signup' component={SignUp}/>
          <Route path='/login' component={LogIn}/>
          <Route path='/create-team' component={CreateTeam}/>
        </Route>
      </Router>
    </IntlProvider>
  </Provider>
);
