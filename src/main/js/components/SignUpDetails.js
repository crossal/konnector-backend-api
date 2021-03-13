import React from 'react';
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
      if (response.status.code === 200) {
        signedUp()
      }
    })
  }

  return (
    <div className="ml-3">
      <h2>Sign Up</h2>
      <Form>
        <Form.Row>
          <Form.Group as={Col} controlId="formGridUsernameOrEmail">
            <Form.Control placeholder="Username or Email" name="usernameOrEmail" />
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
      </Form>
      <Button variant="primary" type="submit">Submit</Button>
    </div>
  )
}

export default SignUpDetails;
