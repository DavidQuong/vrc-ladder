import {createElement, Element} from 'react';
import {Provider} from 'react-redux';
import {IntlProvider} from 'react-intl';
import {Router, Route, IndexRoute, browserHistory, Link} from 'react-router';
import SignUp from './component/signup/signup';
import Ladder from './component/ladder/ladder';
import CreateTeam from './component/create-team/create-team';

const Layout = ({children}) => (
  <div>
    <div>
      <Link to='/signup'>Sign Up</Link>
    </div>
    <div>
      <Link to='/ladder'>Ladder</Link>
    </div>
    <div>
      <Link to='/create-team'>Create team</Link>
    </div>
    {children}
  </div>
);

export default ({store}) : Element => (
  <Provider store={store}>
    <IntlProvider messages={{}} defaultLocale='en-US'>
      <Router history={browserHistory}>
        <Route path='/' component={Layout}>
          <IndexRoute component={() => <div>INDEX</div>}/>
          <Route path='/signup' component={SignUp}/>
          <Route path='/ladder' component={Ladder}/>
          <Route path='/create-team' component={CreateTeam}/>
        </Route>
      </Router>
    </IntlProvider>
  </Provider>
);
