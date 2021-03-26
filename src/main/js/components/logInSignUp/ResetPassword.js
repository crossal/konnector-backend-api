import React from 'react';
import client from '../../client';
import { Col, Row, Form, Button, Alert } from "react-bootstrap";

const ResetPassword = ({ token, updatePasswordReset }) => {

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
        path: '/api/verifications/verify?type=1&passwordResetToken=' + token,
        entity: formDataObj
      }).then(response => {
        if (response.status.code === 200) {
          updatePasswordReset()
        } else if (response.status.code === 422) {
          setServerError(response.entity.error)
        }
        setLoading(false);
      })
    } else {
      setValidated(true);
    }
  }

  const cancelButton = (event) => {
    updatePasswordReset()
  }

  const checkPasswordMatching = () => {
    if (password.current.value != passwordConfirmation.current.value) {
      password.current.setCustomValidity('Passwords must match.');
      passwordConfirmation.current.setCustomValidity('Passwords must match.');
    } else {
      password.current.setCustomValidity('');
      passwordConfirmation.current.setCustomValidity('');
    }
  }

  return (
    <div>
      <Button className="mb-4" variant="secondary" onClick={cancelButton}>Cancel</Button>
      <h3 className="mb-4">Password Reset</h3>
      <Form noValidate validated={validated} onSubmit={handleSubmit}>
        <Form.Row>
          <Form.Group as={Col} controlId="formGridPassword">
            <Form.Control ref={password} required placeholder="Password" name="userPassword" onChange={checkPasswordMatching} />
            <Form.Control.Feedback type="invalid">Please add a password.</Form.Control.Feedback>
          </Form.Group>
        </Form.Row>
        <Form.Row>
          <Form.Group as={Col} controlId="formGridPasswordConfirmation">
            <Form.Control ref={passwordConfirmation} required type="password" placeholder="Confirm password " name="passwordConfirmation" onChange={checkPasswordMatching} />
            <Form.Control.Feedback type="invalid">Please add a matching password confirmation.</Form.Control.Feedback>
          </Form.Group>
        </Form.Row>
        { serverError ? <Alert variant="danger">{serverError}</Alert> : <div/> }
        <Button variant="primary" type="submit" disabled={loading}>{loading ? 'Loading...' : 'Submit'}</Button>
      </Form>
    </div>
  )
}

export default ResetPassword;