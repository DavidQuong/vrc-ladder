import {createElement, Element} from 'react';
import {Provider} from 'react-redux';
import {IntlProvider} from 'react-intl';
import App from './app';

export default ({store}) : Element => (
  <Provider store={store}>
    <IntlProvider messages={{}} defaultLocale='en-US'>
      <App/>
    </IntlProvider>
  </Provider>
);
