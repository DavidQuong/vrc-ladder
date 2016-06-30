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

import {addUser} from '../../action/users';

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
  console.log("Errors:");
  console.log(errors);
  return errors;
};

const parseUser = (props) => {
  console.log("Props:");
  console.log(props);
  return props;
};

const formEnhancer = reduxForm({
  form: 'signUp',
  fields: ['firstName', 'lastName', 'email', 'password', 'confirmPassword'],
  validate,
});

const FormError = ({touched, error}) => {
  if (touched && error) {
    return (
      <div className={classNames(styles.errorMsg)}>
        <Heading kind='error'>
          {error}
        </Heading>
      </div>
    );
  }
  return null;
};

const Input = (props) => {
  return (
    <input
      {...props}
      className={classNames(
        styles.goodForm, {
          [styles.errorForm]: props.error && props.touched,
        },
        props.className
      )}
    />
  );
};

// const Foo = (props) => <div>{props.message}</div>;
// const Foo = ({message}) => <div>{message}</div>;
// <Foo message='...'/>

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
      <Input
        type='text'
        placeholder='First Name'
        {...firstName}
      />
      <FormError {...firstName}/>
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
      <Input
        type='text'
        placeholder='Last Name'
        {...lastName}
      />
      <FormError {...lastName}/>
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
      <Input
        type='text'
        placeholder='Email'
        {...email}
      />
      <FormError {...email}/>
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
  addUser,
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
        const userInfo = parseUser(props);
        if (!isEmpty(errors)) {
          return Promise.reject(errors);
        }
        return addUser({
          ...userInfo,
        });
      }}
    />
  </div>
);

export default connect(
  (state) => ({
    players: state.app.players,
  }),
  {addUser}
)(SignUp);
