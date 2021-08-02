import React from 'react';
import client from '../../client';
import { Col, Row, Form, Button, Alert } from "react-bootstrap";

const SignUpDetails = ({ signedUp, back }) => {

  const [validated, setValidated] = React.useState(false);
  const [serverError, setServerError] = React.useState(null);
  const [loading, setLoading] = React.useState(false);
  const password = React.useRef(null);
  const passwordConfirmation = React.useRef(null);

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
        path: '/api/users',
        entity: formDataObj
      }).then(response => {
        setLoading(false);
        if (response.status.code === 201) {
          signedUp()
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

  const checkPasswordMatching = () => {
    if (password.current.value != passwordConfirmation.current.value) {
      passwordConfirmation.current.setCustomValidity('Passwords must match.');
    } else {
      passwordConfirmation.current.setCustomValidity('');
    }
  }

  return (
    <div>
      <Button className="mb-4" variant="secondary" onClick={backButton}>Back</Button>
      <h3 className="mb-4">Sign Up</h3>
      <Form noValidate validated={validated} onSubmit={handleSubmit}>
        <Form.Row>
          <Form.Group as={Col} controlId="formGridEmail">
            <Form.Control required type="email" placeholder="Email" name="email" />
            <Form.Control.Feedback type="invalid">Please add a valid email.</Form.Control.Feedback>
          </Form.Group>
        </Form.Row>
        <Form.Row>
          <Form.Group as={Col} controlId="formGridUsername">
            <Form.Control required pattern="[a-zA-Z0-9._]+" placeholder="Username" name="username" />
            <Form.Control.Feedback type="invalid">Please add a valid username.</Form.Control.Feedback>
          </Form.Group>
        </Form.Row>
        <Form.Row>
          <Form.Group as={Col} controlId="formGridFirstName">
            <Form.Control required placeholder="First name" name="firstName" />
            <Form.Control.Feedback type="invalid">Please add a first name.</Form.Control.Feedback>
          </Form.Group>
        </Form.Row>
        <Form.Row>
          <Form.Group as={Col} controlId="formGridLastName">
            <Form.Control required placeholder="Last name" name="lastName" />
            <Form.Control.Feedback type="invalid">Please add a last name.</Form.Control.Feedback>
          </Form.Group>
        </Form.Row>
        <Form.Row>
          <Form.Group as={Col} controlId="formGridPassword">
            <Form.Control ref={password} required minlength="7" type="password" placeholder="Password" name="password" />
            <Form.Control.Feedback type="invalid">Please add a password greater than 7 characters.</Form.Control.Feedback>
          </Form.Group>
        </Form.Row>
        <Form.Row>
          <Form.Group as={Col} controlId="formGridPasswordConfirmation">
            <Form.Control ref={passwordConfirmation} required type="password" placeholder="Confirm password" name="passwordConfirmation" onChange={checkPasswordMatching} />
            <Form.Control.Feedback type="invalid">Please add a password confirmation.</Form.Control.Feedback>
          </Form.Group>
        </Form.Row>
        { serverError ? <Alert variant="danger">{serverError}</Alert> : <div/> }
        <Button variant="primary" type="submit" disabled={loading}>{loading ? 'Loading...' : 'Submit'}</Button>
      </Form>
    </div>
  )
}

export default SignUpDetails;
