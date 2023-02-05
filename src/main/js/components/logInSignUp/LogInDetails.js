import React from 'react';
import client from '../../client';
import { Col, Row, Form, Button, Alert } from "react-bootstrap";

const LogInDetails = ({ updateLoggedIn, logInWithoutVerification, requestResetPassword, back }) => {

  const [validated, setValidated] = React.useState(false);
  const [serverError, setServerError] = React.useState(null);
  const [loading, setLoading] = React.useState(false);

  const handleSubmit = (event) => {
    event.preventDefault();
    event.stopPropagation();

    setServerError(null);

    const form = event.currentTarget;
    if (form.checkValidity() === true) {
      setLoading(true);
      const formData = new FormData(event.target), formDataObj = Object.fromEntries(formData.entries())
      client({
        method: 'POST',
        path: '/api/authenticate',
        entity: formDataObj
      }).then(response => {
        setLoading(false);
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

  return (
    <div>
      <Button className="mb-4" variant="secondary" onClick={back}>Back</Button>
      <h3 className="mb-4">Login</h3>
      <Form noValidate validated={validated} onSubmit={handleSubmit}>
        <Row className="mb-2">
          <Form.Group as={Col} controlId="formGridUsernameOrEmail">
            <Form.Control required placeholder="Username or Email" name="usernameOrEmail" />
            <Form.Control.Feedback type="invalid">Please add a username or email.</Form.Control.Feedback>
          </Form.Group>
        </Row>
        <Row className="mb-2">
          <Form.Group as={Col} controlId="formGridPassword">
            <Form.Control required type="password" placeholder="Password" name="password" />
            <Form.Control.Feedback type="invalid">Please add a password.</Form.Control.Feedback>
          </Form.Group>
        </Row>
        { serverError ? <Alert variant="danger">{serverError}</Alert> : <div/> }
        <div><Button className="mb-2" variant="link" onClick={requestResetPassword}>Reset password</Button></div>
        <Button variant="primary" type="submit" disabled={loading}>{loading ? 'Loading...' : 'Submit'}</Button>
      </Form>
    </div>
  )
}

export default LogInDetails;