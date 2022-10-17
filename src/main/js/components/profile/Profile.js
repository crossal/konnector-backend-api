import React from 'react';
import client from '../../client';
import { Col, Row, Form, Button, Alert } from "react-bootstrap";
import ContactDetails from './contactDetails/ContactDetails';

class Profile extends React.Component {

  constructor(props) {
    super(props);
    var updateLoggedIn = this.props.updateLoggedIn;
    this.handleSubmit = this.handleSubmit.bind(this);
    this.validatePasswords = this.validatePasswords.bind(this);
    this.state = {
      user: {},
      validated: false,
      serverError: null,
      loading: false,
      oldPasswordValid: true
    };
    this.oldPassword = React.createRef();
    this.password = React.createRef();
  }

  componentDidMount() {
    client({method: 'GET', path: '/api/users/' + this.props.userId + '{?view-type}', params: { 'view-type': 1 }}).then(
      response => {
        this.setState({user: response.entity});
      },
      response => {
        if (response.status.code === 401) {
          this.props.updateLoggedIn(false, null)
        }
      }
    );
  }

  validatePasswords() {
    if (this.password.current.value != '' && this.oldPassword.current.value == '') {
      this.oldPassword.current.setCustomValidity('Something.');
      this.setState({oldPasswordValid: false})
    } else {
      this.oldPassword.current.setCustomValidity('');
      this.setState({oldPasswordValid: true})
    }
  }

  handleSubmit(event) {
    event.preventDefault();
    event.stopPropagation();

    this.setState({serverError: null});

    this.validatePasswords();

    const form = event.currentTarget;
    if (form.checkValidity() === true && this.state.oldPasswordValid === true) {
      this.setState({loading: true});
      const formData = new FormData(event.target), formDataObj = Object.fromEntries(formData.entries())
      formDataObj.emailVerified = formDataObj.emailVerified === 'true'; // TODO: required?
      client({
        method: 'PUT',
        path: '/api/users/' + this.props.userId,
        entity: formDataObj
      }).then(response => {
        this.setState({loading: false});
        if (response.status.code === 200) {
          // TODO: show some success popup
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

  render() {
    return (
      <div>
        <h2>Account Details</h2>
        <Form noValidate validated={this.state.validated} onSubmit={this.handleSubmit}>
          <input hidden name="id" defaultValue={this.props.userId} />
          <input hidden name="emailVerified" defaultValue={this.state.user.emailVerified} />
          <Form.Row>
            <Form.Group as={Col} controlId="formGridEmail">
              <Form.Label>Email</Form.Label>
              <Form.Control readOnly type="email" placeholder="Email" defaultValue={this.state.user.email || ''} name="email" />
            </Form.Group>
          </Form.Row>
          <Form.Row>
            <Form.Group as={Col} controlId="formGridUsername">
              <Form.Label>Username</Form.Label>
              <Form.Control readOnly placeholder="Username" defaultValue={this.state.user.username || ''} name="username" />
            </Form.Group>
          </Form.Row>
          <Form.Row>
            <Form.Group as={Col} controlId="formGridPassword">
              <Form.Label>Password</Form.Label>
              <Form.Control isInvalid={!this.state.oldPasswordValid && this.state.validated} ref={this.oldPassword}  type="password" placeholder="Password" name="oldPassword" onChange={this.validatePasswords} />
              <Form.Control.Feedback type="invalid">Password cannot be empty when setting new password.</Form.Control.Feedback>
            </Form.Group>
            <Form.Group as={Col} controlId="formGridNewPassword">
              <Form.Label>Password</Form.Label>
              <Form.Control ref={this.password} minLength="7" type="password" placeholder="New password" name="password" onChange={this.validatePasswords} />
              <Form.Control.Feedback type="invalid">Please add a password greater than 7 characters.</Form.Control.Feedback>
            </Form.Group>
          </Form.Row>
          <Form.Row>
            <Form.Group as={Col} controlId="formGridFirstName">
              <Form.Label>First Name</Form.Label>
              <Form.Control required placeholder="First name" defaultValue={this.state.user.firstName || ''} name="firstName" />
            </Form.Group>
            <Form.Group as={Col} controlId="formGridLastName">
              <Form.Label>Last Name</Form.Label>
              <Form.Control required placeholder="Last name" defaultValue={this.state.user.lastName || ''} name="lastName" />
            </Form.Group>
          </Form.Row>
          { this.state.serverError ? <Alert variant="danger">{this.state.serverError}</Alert> : <div/> }
          <Button variant="primary" type="submit" disabled={this.state.loading}>{this.state.loading ? 'Loading...' : 'Save'}</Button>
        </Form>

        <br />

        <ContactDetails {...this.props} />
      </div>
    )
  }
}

export default Profile;
