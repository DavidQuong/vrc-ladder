import {createElement, Element} from 'react';
import {FormattedMessage} from 'react-intl';
import {connect} from 'react-redux';
import {reduxForm} from 'redux-form';
import {SubmitBtn} from '../button';
import {withRouter} from 'react-router';
import {logInUser} from '../../action/login';
import {createAction} from 'redux-actions';

import styles from './login.css';
import Heading from '../heading/heading';
import isEmpty from 'lodash/fp/isEmpty';

const validate = (values) => {
  const errors = {};
  if (!values.userId) {
    errors.userId = 'Required';
  }
  if (!values.password) {
    errors.password = 'Password Required';
  }
  return errors;
};

const FormError = ({touched, error}) => {
  if (touched && error) {
    return (
      <div>
        <Heading kind='error'>
          {error}
        </Heading>
      </div>
    );
  }
  return null;
};

const BaseLogInForm = ({
  fields: {
    userId,
    password},
  handleSubmit,
}) => (
  <form
    onSubmit={handleSubmit}
  >
    <div>
      <label>
        <FormattedMessage
          id='userID'
          defaultMessage='Your ID #'
        />
      </label>
      <input
        type='text'
        placeholder='Id #'
        {...userId}
      />
      <FormError {...userId}/>
    </div>
    <div>
      <label>
        <FormattedMessage
          id='password'
          defaultMessage='Password'
        />
      </label>
      <input
        type='password'
        placeholder='Password'
        {...password}
      />
      <FormError {...password}/>
    </div>
    <div>
      <SubmitBtn type='submit'>Log In</SubmitBtn>
    </div>
  </form>
);

const formEnhancer = reduxForm({
  form: 'logIn',
  fields: [
    'userId',
    'password'],
  validate,
});

const LogInForm = formEnhancer(BaseLogInForm);

const logInBuilder = (props, response) => {
  const user = props;
  delete user.password;
  return ({
    ...user,
    ...response,
  });
};

const userLogIn = createAction('USER_LOGIN');

const LogIn = withRouter(({
  userLogIn,
  logInUser,
  router,
}) : Element => (
  <div className={styles.signup}>
    <Heading kind='huge'>
      <FormattedMessage
        id='login'
        defaultMessage='Log In'
      />
    </Heading>
    <LogInForm
      onSubmit={(props) => {
        const errors = validate(props);
        if (!isEmpty(errors)) {
          return Promise.reject(errors);
        }
        return logInUser(props).then((response) => {
          const userInfo = logInBuilder(props, response);
          userLogIn({
            ...userInfo,
          });
          router.push('/ladder');
        }).catch((errors) => {
          // TODO: Error object to expected.
          return Promise.reject(errors);
        });
      }}
    />
  </div>
));

export default connect(
  (state) => ({
    loggedIn: state.app.loggedIn,
  }),
  {logInUser,
  userLogIn}
)(LogIn);
