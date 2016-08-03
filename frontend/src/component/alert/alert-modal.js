import React, {createElement} from 'react';
import {Modal, Button} from 'react-bootstrap';

export const AlertModal = React.createClass({

  getInitialState: function() {
    return {
      showModal: false,
      body: '',
    };
  },

  close: function() {
    this.setState({showModal: false});
  },

  open: function(body) {
    this.setState({body: body});
    this.setState({showModal: true});
  },

  render: function() {
    return (<div>
        <Modal show={this.state.showModal} onHide={this.close}>
          <Modal.Header>
            <Modal.Title>Alert</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <p>{this.state.body}</p>
          </Modal.Body>
          <Modal.Footer>
            <Button onClick={this.close} bsStyle='primary'>Close</Button>
          </Modal.Footer>
        </Modal>
      </div>
    );
  },
});
