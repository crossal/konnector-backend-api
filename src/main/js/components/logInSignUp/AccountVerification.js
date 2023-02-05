import React from 'react';
import client from '../../client';
import { Col, Row, Form, Button, Alert } from "react-bootstrap";

const AccountVerification = ({ verified, back }) => {

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
        path: '/api/verifications/verify?type=0',
        entity: formDataObj
      }).then(response => {
        setLoading(false);
        if (response.status.code === 200) {
          verified()
        } else if (response.status.code === 422) {
          setServerError(response.entity.error)
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
      <h3>Verification</h3>
      <Form noValidate validated={validated}  onSubmit={handleSubmit}>
        <Row className="mb-2">
          <Form.Group required as={Col} controlId="formGridUsernameOrEmail">
            <Form.Control placeholder="Username or email" name="usernameOrEmail" />
            <Form.Control.Feedback type="invalid">Please add a username or email.</Form.Control.Feedback>
          </Form.Group>
        </Row>
        <Row className="mb-2">
          <Form.Group as={Col} controlId="formGridCode">
            <Form.Control required placeholder="Code" name="code" />
            <Form.Control.Feedback type="invalid">Please add a code.</Form.Control.Feedback>
          </Form.Group>
        </Row>
        { serverError ? <Alert variant="danger">{serverError}</Alert> : <div/> }
        <Button variant="primary" type="submit" disabled={loading}>{loading ? 'Loading...' : 'Submit'}</Button>
      </Form>
    </div>
  )
}

export default AccountVerification;
