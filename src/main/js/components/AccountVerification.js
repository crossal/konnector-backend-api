import React from 'react';
import client from '../client';
import { Col, Row, Form, Button } from "react-bootstrap";

const AccountVerification = ({ verified }) => {

  const handleSubmit = (event) => {
    event.preventDefault();
    const formData = new FormData(event.target), formDataObj = Object.fromEntries(formData.entries())
    client({
      method: 'POST',
      path: '/api/verifications/verify?type=0',
      entity: formDataObj,
      headers: {'Content-Type': 'application/json'}
    }).then(response => {
      if (response.status.code === 200) {
        verified()
      }
    })
  }

  return (
    <div className="ml-3">
      <h2>Verification</h2>
      <Form onSubmit={handleSubmit}>
        <Form.Row>
          <Form.Group as={Col} controlId="formGridUsernameOrEmail">
            <Form.Control placeholder="Username or email" name="usernameOrEmail" />
          </Form.Group>
        </Form.Row>
        <Form.Row>
          <Form.Group as={Col} controlId="formGridCode">
            <Form.Control placeholder="Code" name="code" />
          </Form.Group>
        </Form.Row>
        <Button variant="primary" type="submit">Submit</Button>
      </Form>
    </div>
  )
}

export default AccountVerification;
