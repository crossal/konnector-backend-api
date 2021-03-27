import React from 'react';
import client from '../../client';
import { Col, Row, Form, Button, Alert } from "react-bootstrap";

const RequestPasswordReset = ({ back }) => {

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
      client({
        method: 'POST',
        path: '/api/verifications?type=1&usernameOrEmail=' + event.target.usernameOrEmail.value
      }).then(response => {
        setLoading(false);
        if (response.status.code === 200) {
          back()
        } else if (response.status.code === 422) {
          setServerError(response.entity.error)
        }
      })
    } else {
      setValidated(true);
    }
  }

  return (
    <div>
      <Button className="mb-4" variant="secondary" onClick={back}>Back</Button>
      <h3 className="mb-4">Request Password Reset</h3>
      <Form noValidate validated={validated} onSubmit={handleSubmit}>
        <Form.Row>
          <Form.Group as={Col} controlId="formGridUsernameOrEmail">
            <Form.Control required placeholder="Username or Email" name="usernameOrEmail" />
            <Form.Control.Feedback type="invalid">Please add a username or email.</Form.Control.Feedback>
          </Form.Group>
        </Form.Row>
        { serverError ? <Alert variant="danger">{serverError}</Alert> : <div/> }
        <Button variant="primary" type="submit" disabled={loading}>{loading ? 'Loading...' : 'Submit'}</Button>
      </Form>
    </div>
  )
}

export default RequestPasswordReset;