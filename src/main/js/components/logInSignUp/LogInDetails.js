import React from 'react';
import client from '../../client';
import { Col, Row, Form, Button, Alert } from "react-bootstrap";

const LogInDetails = ({ updateLoggedIn, logInWithoutVerification, back }) => {

  const [validated, setValidated] = React.useState(false);
  const [serverError, setServerError] = React.useState(null);

  const handleSubmit = (event) => {
    event.preventDefault();
    event.stopPropagation();

    setServerError(null);

    const form = event.currentTarget;
    if (form.checkValidity() === true) {
      const formData = new FormData(event.target), formDataObj = Object.fromEntries(formData.entries())
      client({
        method: 'POST',
        path: '/api/authenticate',
        entity: formDataObj
      }).then(response => {
        if (response.status.code === 200) {
          updateLoggedIn(true, response.entity.id)
        } else if (response.status.code === 401) {
          setServerError("Username/email or password incorrect.")
        } else if (response.status.code === 422 && response.entity.code === 1) {
          logInWithoutVerification()
        }
      })
    } else {
      setValidated(true);
    }
  }

  const backButton = (event) => {
    back()
  }

  return (
    <div>
      <Button className="mb-4" variant="secondary" onClick={backButton}>Back</Button>
      <h3>Login</h3>
      <Form noValidate validated={validated} onSubmit={handleSubmit}>
        <Form.Row>
          <Form.Group as={Col} controlId="formGridUsernameOrEmail">
            <Form.Control required placeholder="Username or Email" name="usernameOrEmail" />
            <Form.Control.Feedback type="invalid">Please add a username or email.</Form.Control.Feedback>
          </Form.Group>
        </Form.Row>
        <Form.Row>
          <Form.Group as={Col} controlId="formGridPassword">
            <Form.Control required type="password" placeholder="Password" name="password" />
            <Form.Control.Feedback type="invalid">Please add a password.</Form.Control.Feedback>
          </Form.Group>
        </Form.Row>
        { serverError ? <Alert variant="danger">{serverError}</Alert> : <div/> }
        <Button variant="primary" type="submit">Submit</Button>
      </Form>
    </div>
  )
}

export default LogInDetails;