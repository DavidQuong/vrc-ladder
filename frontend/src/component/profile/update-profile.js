import {createElement, Element} from 'react';
import {FormattedMessage} from 'react-intl';
import {connect} from 'react-redux';
import {reduxForm} from 'redux-form';
import {SubmitBtn} from '../button';
import {withRouter} from 'react-router';
import {getTeamInfo} from '../../action/users';
import map from 'lodash/fp/map';
import styles from './profile.css';
import Heading from '../heading/heading';
import classNames from 'classnames';
// import isEmpty from 'lodash/fp/isEmpty';
// import sortBy from 'lodash/fp/sortBy';
import {
  Col, ControlLabel, Button, FormControl, FormGroup, Form,
} from 'react-bootstrap';

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

const updateProfileInfo = ({
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

const updateProfileForm = formEnhancer(updateProfileInfo);
