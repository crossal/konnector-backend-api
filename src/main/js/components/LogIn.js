import React from 'react';
import client from '../client';
import { Col, Row, Form, Button } from "react-bootstrap";

const LogIn = ({ updateLoggedIn, updateLogInOrSignUpStatus }) => {

  const [validated, setValidated] = React.useState(false);

  const handleSubmit = (event) => {
    event.preventDefault();
    const formData = new FormData(event.target), formDataObj = Object.fromEntries(formData.entries())
    client({
      method: 'POST',
      path: '/api/authenticate',
      entity: formDataObj,
      headers: {'Content-Type': 'application/json'}
    }).then(response => {
      if (response.status.code === 200) {
        updateLoggedIn(true, response.entity.id)
      } else {
        setValidated(true);
      }
    })
  }

//  handle form typing { setValidated(true); to turn off validation (green or red) }

  const backButton = (event) => {
    updateLogInOrSignUpStatus(false, false)
  }

  return (
    <div>
      <Button className="mb-4" variant="secondary" onClick={backButton}>Back</Button>
      <h3>Login</h3>
      <Form noValidate validated={validated} onSubmit={handleSubmit}>
        <Form.Row>
          <Form.Group as={Col} controlId="formGridUsernameOrEmail">
            <Form.Control placeholder="Username or Email" name="usernameOrEmail" />
            <Form.Control.Feedback type="valid">Username/email or password incorrect.</Form.Control.Feedback>
          </Form.Group>
        </Form.Row>
        <Form.Row>
          <Form.Group as={Col} controlId="formGridPassword">
            <Form.Control type="password" placeholder="Password" name="password" />
            <Form.Control.Feedback type="invalid">Username/email or password incorrect.</Form.Control.Feedback>
          </Form.Group>
        </Form.Row>
        <Button variant="primary" type="submit">Submit</Button>
      </Form>
    </div>
  )
}

export default LogIn;
