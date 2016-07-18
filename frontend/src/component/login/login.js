import {createElement, Element} from 'react';
import {FormattedMessage} from 'react-intl';
import {connect} from 'react-redux';
import {reduxForm} from 'redux-form';
import {withRouter} from 'react-router';
import {logInUser} from '../../action/login';
import {getUserInfo, getTeamInfo} from '../../action/users';
import {createAction} from 'redux-actions';
import {
  Well, Col, ControlLabel, Button, FormControl, FormGroup, Form,
} from 'react-bootstrap';

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
  <Form horizontal onSubmit={handleSubmit}>
    <div>
      <FormGroup>
        <Col componentClass={ControlLabel} sm={3}>User ID</Col>
        <Col sm={6}>
          <FormControl type='userID' placeholder='Your ID #' {...userId} />
        </Col>
        <Col sm={3}><FormError {...userId}/></Col>
      </FormGroup>

      <FormGroup>
        <Col componentClass={ControlLabel} sm={3}>Password</Col>
        <Col sm={6}>
          <FormControl type='password' placeholder='Password' {...password} />
        </Col>
        <Col sm={3}><FormError {...password}/></Col>
      </FormGroup>

      <div className={styles.center}>
        <Button bsStyle='primary' bsSize='large' type='submit'>Log In</Button>
      </div>
    </div>
  </Form>
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
  getUserInfo,
  getTeamInfo,
  router,
}) : Element => (
  <div className={styles.center}>
    <Well>
      <Heading>
        <FormattedMessage
          id='login'
          defaultMessage='Login'
        />
      </Heading>
      <LogInForm
        onSubmit={(props) => {
          let errors = validate(props);
          if (!isEmpty(errors)) {
            return Promise.reject(errors);
          }
          return logInUser(props).then((response) => {
            const userInfo = logInBuilder(props, response);
            userLogIn({
              ...userInfo,
            });
            getUserInfo().then(() => {
              getTeamInfo().then(() => {
                router.push('/ladder');
              });
            });
          }).catch(() => {
            errors = {userId: 'Incorrect ID or Password'};
            return Promise.reject(errors);
          });
        }}
      />
    </Well>
  </div>
));

export default connect(
  (state) => ({
    loggedIn: state.app.loggedIn,
  }), {
    logInUser,
    userLogIn,
    getUserInfo,
    getTeamInfo}
)(LogIn);
