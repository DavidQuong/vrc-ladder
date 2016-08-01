import React, {createElement} from 'react';
import {Modal, Button} from 'react-bootstrap';

export const AlertModal = React.createClass({

  getInitialState: function() {
    return {showModal: false};
  },

  close: function() {
    this.setState({showModal: false});
  },

  open: function() {
    this.setState({showModal: true});
  },

  render: function() {
    return (<div>
        <Modal show={this.state.showModal} onHide={this.close}>
          <Modal.Header>
            <Modal.Title>Alert!</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <p>{this.props.body}</p>
          </Modal.Body>
          <Modal.Footer>
            <Button onClick={this.close}>Close</Button>
          </Modal.Footer>
        </Modal>
      </div>
    );
  },
});
