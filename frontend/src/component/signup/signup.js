import {createElement, Element} from 'react';
import {FormattedMessage} from 'react-intl';
import {connect} from 'react-redux';
import {reduxForm} from 'redux-form';
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
            placeholder='eg. Jane'
            {...firstName}
          />
        </Col>
        <Col sm={3}><FormError {...firstName}/></Col>
      </FormGroup>

      <FormGroup>
        <Col componentClass={ControlLabel} sm={4}>Last Name</Col>
        <Col sm={4}>
          <FormControl type='lastName' placeholder='eg. Doe' {...lastName} />
        </Col>
        <Col sm={3}><FormError {...lastName}/></Col>
      </FormGroup>

      <FormGroup>
        <Col componentClass={ControlLabel} sm={4}>Email</Col>
        <Col sm={5}>
          <FormControl
            type='emailAddress'
            placeholder='eg. jane.doe@example.com'
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
            placeholder='eg. 6041234567'
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
            placeholder='Password confirmation'
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

const checkErrors = (responseErrors) => {
  const errors = {};

  if (responseErrors.userId === false) {
    errors.userId = 'A user with this ID already exists!';
  }
  if (responseErrors.emailAddress === false) {
    errors.emailAddress = 'A user with this email already exists!';
  }

  if (responseErrors.userId === 'invalid') {
    errors.userId = 'This userId is not valid';
  }
  if (responseErrors.emailAddress === 'invalid') {
    errors.emailAddress = 'This email Address is not valid';
  }
  if (responseErrors.phoneNumber === 'invalid') {
    errors.phoneNumber = 'This phone number is not valid';
  }

  return errors;
};

const SignUp = withRouter(({
  addUser,
  router,
}) : Element => (
  <div className={styles.center}>
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
            router.push('/login');
          }).catch((response) => {
            return response.then(function(bodyContent) {
              const errors = checkErrors(bodyContent);
              return Promise.reject(errors);
            });
          });
        }}
      />
    </Well>
  </div>
));

export default connect(
  (state) => ({
    players: state.app.players,
    userInfo: state.app.userInfo,
  }),
  {addUser,
  }
)(SignUp);
