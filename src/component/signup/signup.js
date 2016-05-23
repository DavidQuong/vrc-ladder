import {createElement, Element} from 'react';
import {FormattedMessage} from 'react-intl';
import {connect} from 'react-redux';
import {createAction} from 'redux-actions';
import {reduxForm} from 'redux-form';
import {SubmitBtn} from '../button';
import styles from './signup.css';
import Heading from '../heading/heading';
import findIndex from 'lodash/fp/findIndex';
import isEmpty from 'lodash/fp/isEmpty';
import classNames from 'classnames';

const validate = (values, {players} ) => {
  const errors = {};
  if (!values.firstName) {
    errors.firstName = 'Required';
  }
  if (!values.lastName) {
    errors.lastName = 'Required';
  }
  if (!values.email) {
    errors.email = 'Required';
  } else if (!/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i.test(values.email)) {
    errors.email = 'Invalid email address';
  } else if (findIndex(['email', values.email], players) !== -1) {
    errors.email = 'Email already exist';
  }
  if (!values.password) {
    errors.password = 'Password Required';
  } else if (values.password !== values.confirmPassword) {
    errors.password = 'Password does not match';
  }
  return errors;
};

const formEnhancer = reduxForm({
  form: 'signUp',
  fields: ['firstName', 'lastName', 'email', 'password', 'confirmPassword'],
  validate,
});

const BaseSignUpForm = ({
  fields: {firstName, lastName, email, password, confirmPassword},
  handleSubmit,
}) => (
  <form
    className={styles.formHorizontal}
    onSubmit={handleSubmit}
  >
    <div className={classNames(styles.formGroup)}>
      <label
        className={classNames(styles.colXsTitle)}
      >
        <FormattedMessage
          id='firstName'
          defaultMessage='First Name'
        />
      </label>
      <input
        className={classNames(styles.goodForm, {
          [styles.errorForm]: firstName.error && firstName.touched})}
        type='text'
        placeholder='First Name'
        {...firstName}
      />
      {firstName.touched && firstName.error &&
        <div className={classNames(styles.errorMsg)}>
          <Heading kind='error'>
            {firstName.error}
          </Heading>
        </div>}
    </div>
    <div className={classNames(styles.formGroup)}>
      <label
        className={classNames(styles.colXsTitle)}
      >
        <FormattedMessage
          id='lastName'
          defaultMessage='Last name'
        />
      </label>
      <input
        className={classNames(styles.goodForm, {
          [styles.errorForm]: lastName.error && lastName.touched})}
        type='text'
        placeholder='Last Name'
        {...lastName}
      />
      {lastName.touched && lastName.error &&
        <div className={classNames(styles.errorMsg)}>
          <Heading kind='error'>
            {lastName.error}
          </Heading>
        </div>}
    </div>
    <div className={classNames(styles.formGroup)}>
      <label
        className={classNames(styles.colXsTitle)}
      >
        <FormattedMessage
          id='email'
          defaultMessage='Email'
        />
      </label>
      <input
        className={classNames(styles.goodForm, {
          [styles.errorForm]: email.error && email.touched})}
        type='text'
        placeholder='Email'
        {...email}
      />
      {email.touched && email.error &&
        <div className={classNames(styles.errorMsg)}>
          <Heading kind='error'>
            {email.error}
          </Heading>
        </div>}
    </div>
    <div className={classNames(styles.formGroup)}>
      <label
        className={classNames(styles.colXsTitle)}
      >
        <FormattedMessage
          id='password'
          defaultMessage='Password'
        />
      </label>
      <input
        className={classNames(styles.goodForm, {
          [styles.errorForm]: password.error &&
                              password.touched &&
                              confirmPassword.touched})}
        type='password'
        placeholder='Password'
        {...password}
      />
    </div>
    <div className={classNames(styles.formGroup)}>
      <label
        className={classNames(styles.colXsTitle)}
      >
        <FormattedMessage
          id='password'
          defaultMessage='Confirm Password'
        />
      </label>
      <input
        className={classNames(styles.goodForm, {
          [styles.errorForm]: password.error &&
                              password.touched &&
                              confirmPassword.touched})}
        type='password'
        placeholder='Password'
        {...confirmPassword}
      />
      {password.touched && confirmPassword.touched && password.error &&
        <div className={classNames(styles.errorMsg)}>
          <Heading kind='error'>
          {password.error}
          </Heading>
        </div>}
    </div>
    <div className={classNames(styles.center)}>
      <SubmitBtn type='submit'>Sign Up</SubmitBtn>
    </div>
  </form>
);

const SignUpForm = formEnhancer(BaseSignUpForm);

const addPlayer = createAction('PLAYER_ADD');

const SignUp = ({
  addPlayer,
  players,
}) : Element => (
  <div className={styles.signup}>
    <Heading kind='huge'>
      <FormattedMessage
        id='signup'
        defaultMessage='Sign Up'
      />
    </Heading>
    <SignUpForm
      onSubmit={(props) => {
        const errors = validate(props, {players});
        if (!isEmpty(errors)) {
          return Promise.reject(errors);
        }
        addPlayer({
          ...props,
        });
        return Promise.resolve();
      }}
    />
  </div>
);

export default connect(
  (state) => ({
    players: state.app.players,
  }),
  {addPlayer}
)(SignUp);
