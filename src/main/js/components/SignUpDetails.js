import React from 'react';
import client from '../client';
import { Col, Row, Form, Button } from "react-bootstrap";

const SignUpDetails = ({ signedUp }) => {

  const handleSubmit = (event) => {
    event.preventDefault();
    const formData = new FormData(event.target), formDataObj = Object.fromEntries(formData.entries())
    client({
      method: 'POST',
      path: '/api/users',
      entity: formDataObj,
      headers: {'Content-Type': 'application/json'}
    }).then(response => {
      if (response.status.code === 201) {
        signedUp()
      } else if (response.status.code === 422) {

      }
    })
  }

  return (
    <div className="ml-3">
      <h2>Sign Up</h2>
      <Form onSubmit={handleSubmit}>
        <Form.Row>
          <Form.Group as={Col} controlId="formGridEmail">
            <Form.Control type="email" placeholder="Email" name="email" />
          </Form.Group>
        </Form.Row>
        <Form.Row>
          <Form.Group as={Col} controlId="formGridUsername">
            <Form.Control placeholder="Username" name="username" />
          </Form.Group>
        </Form.Row>
        <Form.Row>
          <Form.Group as={Col} controlId="formGridFirstName">
            <Form.Control placeholder="First name" name="firstName" />
          </Form.Group>
        </Form.Row>
        <Form.Row>
          <Form.Group as={Col} controlId="formGridLastName">
            <Form.Control placeholder="Last name" name="lastName" />
          </Form.Group>
        </Form.Row>
        <Form.Row>
          <Form.Group as={Col} controlId="formGridPassword">
            <Form.Control type="password" placeholder="Password" name="password" />
          </Form.Group>
        </Form.Row>
        <Form.Row>
          <Form.Group as={Col} controlId="formGridPasswordConfirmation">
            <Form.Control type="password" placeholder="Confirm password" name="passwordConfirmation" />
          </Form.Group>
        </Form.Row>
        <Button variant="primary" type="submit">Submit</Button>
      </Form>
    </div>
  )
}

export default SignUpDetails;
