import {createElement, Element} from 'react';
import {FormattedMessage} from 'react-intl';
import {connect} from 'react-redux';
import {reduxForm} from 'redux-form';
import {SubmitBtn} from '../button';
import {withRouter} from 'react-router';
import {addUser} from '../../action/users';

import styles from './signup.css';
import Heading from '../heading/heading';
import isEmpty from 'lodash/fp/isEmpty';
import classNames from 'classnames';

const validate = (values) => {
  const errors = {};
  if (!values.userId) {
    errors.userId = 'Required';
  }
  if (!values.firstName) {
    errors.firstName = 'Required';
  }
  if (!values.lastName) {
    errors.lastName = 'Required';
  }
  if (!values.emailAddress) {
    errors.emailAddress = 'Required';
  } else if
  (!/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i.test(values.emailAddress)) {
    errors.emailAddress = 'Invalid email address';
  }
  if (!values.phoneNumber) {
    errors.phoneNumber = 'Required';
  }
  if (!values.password) {
    errors.password = 'Password Required';
  } else if (values.password !== values.confirmPassword) {
    errors.password = 'Password does not match';
  }
  return errors;
};

const parseUser = (props) => {
  const user = props;
  delete user.confirmPassword;
  return ({
    ...user,
    userRole: 'PLAYER',
    middleName: '',
  });
};

const formEnhancer = reduxForm({
  form: 'signUp',
  fields: [
    'userId',
    'firstName',
    'lastName',
    'emailAddress',
    'phoneNumber',
    'password',
    'confirmPassword'],
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
  fields: {
    userId,
    firstName,
    lastName,
    emailAddress,
    phoneNumber,
    password,
    confirmPassword},
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
          id='userID'
          defaultMessage='Your ID #'
        />
      </label>
      <Input
        type='text'
        placeholder='Id #'
        {...userId}
      />
      <FormError {...userId}/>
    </div>
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
          id='emailAddress'
          defaultMessage='Email'
        />
      </label>
      <Input
        type='text'
        placeholder='Email'
        {...emailAddress}
      />
      <FormError {...emailAddress}/>
    </div>
    <div className={classNames(styles.formGroup)}>
      <label
        className={classNames(styles.colXsTitle)}
      >
        <FormattedMessage
          id='phoneNumber'
          defaultMessage='Your Phone Number'
        />
      </label>
      <Input
        type='text'
        placeholder='6041234567'
        {...phoneNumber}
      />
      <FormError {...phoneNumber}/>
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

const SignUp = withRouter(({
  addUser,
  router,
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
        const errors = validate(props);
        const userInfo = parseUser(props);
        if (!isEmpty(errors)) {
          return Promise.reject(errors);
        }
        return addUser(userInfo).then(() => {
          router.push('/');
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
    players: state.app.players,
  }),
  {addUser}
)(SignUp);
