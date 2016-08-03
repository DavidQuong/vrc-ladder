import {createElement} from 'react';
import {Button} from 'react-bootstrap';

export default (props) => {
  return <SubmitBtn {...props} />;
};

export const SubmitBtn = (props) => {
  return <Button bsStyle='primary' type='submit' {...props} />;
};
