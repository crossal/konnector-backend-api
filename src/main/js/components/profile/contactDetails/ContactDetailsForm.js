import React from 'react';
import client from '../../../client';
import { Col, Row, Form, Button, Alert } from "react-bootstrap";

class ContactDetailsForm extends React.Component {

  constructor(props) {
    super(props);
    this.type = React.createRef();
    this.value = React.createRef();
    this.handleSubmit = this.handleSubmit.bind(this);
    this.state = {
      serverError: null,
      loading: false
    };
  }

  handleSubmit(event) {
    event.preventDefault();
    event.stopPropagation();

    this.setState({serverError: null});

    const form = event.currentTarget;
    if (form.checkValidity() === true) {
      this.setState({loading: true});
      const formData = new FormData(event.target), formDataObj = Object.fromEntries(formData.entries())
      client({
        method: 'POST',
        path: '/api/contact-details',
        entity: formDataObj
      }).then(response => {
        this.setState({loading: false});
        if (response.status.code === 201) {
          this.clearForm();
        } else if (response.status.code === 401) {
          this.props.updateLoggedIn(false, null)
        } else if (response.status.code === 422) {
          this.setState({serverError: response.entity.error});
        }
      })
    } else {
      this.setState({validated: true});
    }
  }

  clearForm() {
    this.type.current.value = '';
    this.value.current.value = '';
  }

  render() {
    return (
      <Form noValidate validated={this.state.validated} onSubmit={this.handleSubmit}>
        <Form.Row>
          <input hidden name="userId" defaultValue={this.props.userId} />
          <Form.Group as={Col} controlId="formGridContactType">
            <Form.Control ref={this.type} required placeholder="Type" name="type" />
            <Form.Control.Feedback type="invalid">Cannot be empty.</Form.Control.Feedback>
          </Form.Group>
          <Form.Group as={Col} controlId="formGridContactValue">
            <Form.Control ref={this.value} required placeholder="Value" name="value" />
            <Form.Control.Feedback type="invalid">Cannot be empty.</Form.Control.Feedback>
          </Form.Group>
        </Form.Row>
        { this.state.serverError ? <Alert variant="danger">{this.state.serverError}</Alert> : <div/> }
        <Button variant="primary" type="submit" disabled={this.state.loading}>{this.state.loading ? 'Loading...' : 'Add'}</Button>
      </Form>
    )
  }
}

export default ContactDetailsForm;
