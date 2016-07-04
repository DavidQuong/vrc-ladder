import {createElement, Element} from 'react';
import {FormattedMessage} from 'react-intl';
import {connect} from 'react-redux';
import {reduxForm} from 'redux-form';
// import {SubmitBtn} from '../button';
import {withRouter} from 'react-router';
import {addUser} from '../../action/users';

import styles from './signup.css';
import Heading from '../heading/heading';
import isEmpty from 'lodash/fp/isEmpty';
import classNames from 'classnames';

import {
  Well, Col, ControlLabel, Button, FormControl, FormGroup, Form,
} from 'react-bootstrap';

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
  <Form horizontal onSubmit={handleSubmit}>
    <div>
      <FormGroup>
        <Col componentClass={ControlLabel} sm={4}>User ID</Col>
        <Col sm={4}>
          <FormControl type='userID' placeholder='Your ID #' {...userId} />
        </Col>
        <Col sm={3}><FormError {...userId}/></Col>
      </FormGroup>

      <FormGroup>
        <Col componentClass={ControlLabel} sm={4}>First Name</Col>
        <Col sm={4}>
          <FormControl
            type='firstName'
            placeholder='First Name'
            {...firstName}
          />
        </Col>
        <Col sm={3}><FormError {...firstName}/></Col>
      </FormGroup>

      <FormGroup>
        <Col componentClass={ControlLabel} sm={4}>Last Name</Col>
        <Col sm={4}>
          <FormControl type='lastName' placeholder='Last Name' {...lastName} />
        </Col>
        <Col sm={3}><FormError {...lastName}/></Col>
      </FormGroup>

      <FormGroup>
        <Col componentClass={ControlLabel} sm={4}>Email</Col>
        <Col sm={5}>
          <FormControl
            type='emailAddress'
            placeholder='Email'
            {...emailAddress}
          />
        </Col>
        <Col sm={3}><FormError {...emailAddress}/></Col>
      </FormGroup>

      <FormGroup>
        <Col componentClass={ControlLabel} sm={4}>Phone Number</Col>
        <Col sm={4}>
          <FormControl
            type='phoneNumber'
            placeholder='6041234567'
            {...phoneNumber}
          />
        </Col>
        <Col sm={3}><FormError {...phoneNumber}/></Col>
      </FormGroup>

      <FormGroup>
        <Col componentClass={ControlLabel} sm={4}>Password</Col>
        <Col sm={5}>
          <FormControl type='password' placeholder='Password' {...password} />
        </Col>
        <Col sm={3}><FormError {...password}/></Col>
      </FormGroup>

      <FormGroup>
        <Col componentClass={ControlLabel} sm={4}>Confirm Password</Col>
        <Col sm={5}>
          <FormControl
            type='password'
            placeholder='Password'
            {...confirmPassword}
          />
        </Col>
        <Col sm={3}><FormError {...confirmPassword}/></Col>
      </FormGroup>

      <div className={classNames(styles.center)}>
        <Button bsStyle='primary' bsSize='large' type='submit'>Sign Up</Button>
      </div>
    </div>
  </Form>
);

const SignUpForm = formEnhancer(BaseSignUpForm);

const SignUp = withRouter(({
  addUser,
  router,
}) : Element => (
  <Well>
      <Heading>
        <FormattedMessage
          id='signup'
          defaultMessage='Create Your Account:'
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
          router.push('/ladder');
        }).catch((errors) => {
          // TODO: Error object to expected.
          return Promise.reject(errors);
        });
      }}
    />
  </Well>
));

export default connect(
  (state) => ({
    players: state.app.players,
  }),
  {addUser}
)(SignUp);
