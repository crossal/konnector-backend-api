import React from 'react';
import { useStickyState } from './../functions/stickyState';
import { Col, Row, Form } from "react-bootstrap";

const SignUp = () => {

	const [loggedIn, setLoggedIn] = useStickyState(false, 'loggedIn');

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
						<Form.Control placeholder="Password" name="password" />
					</Form.Group>
				</Form.Row>
			</Form>
			<input type="submit" value="Submit" />
		</div>
	)
}

export default SignUp;
