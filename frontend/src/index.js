import {createElement, Element} from 'react';
import {Provider} from 'react-redux';
import {IntlProvider} from 'react-intl';
import {Router, Route, IndexRoute, browserHistory} from 'react-router';
import SignUp from './component/signup/signup';
import Ladder from './component/ladder/ladder';
import CreateTeam from './component/create-team/create-team';
import {Button, ButtonToolbar} from 'react-bootstrap';
import {LinkContainer} from 'react-router-bootstrap';

const divStyle = {
  height: '100%',
  color: 'white',
  backgroundColor: '#dbb079',
  backgroundImage: 'url(' + require('file!./../src/public/wood-tile.png') + ')',
  backgroundRepeat: 'repeat',
  WebkitTransition: 'all',
  msTransition: 'all',
};

const Layout = ({children}) => (
  <div style={divStyle}>
    <div>
      <ButtonToolbar>
        <LinkContainer to='/signup'>
          <Button>Sign up</Button>
        </LinkContainer>
        <LinkContainer to='/ladder'>
          <Button>Ladder</Button>
        </LinkContainer>
        <LinkContainer to='/create-team'>
          <Button>Create team</Button>
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
          <IndexRoute component={Ladder}/>
          <Route path='/signup' component={SignUp}/>
          <Route path='/ladder' component={Ladder}/>
          <Route path='/create-team' component={CreateTeam}/>
        </Route>
      </Router>
    </IntlProvider>
  </Provider>
);
