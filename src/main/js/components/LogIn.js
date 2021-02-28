import React from 'react';
import client from '../client';
import { useStickyState } from './../functions/stickyState';
import { Col, Row, Form } from "react-bootstrap";

const LogIn = () => {

	const [loggedIn, setLoggedIn] = useStickyState(false, 'loggedIn');

	const handleSubmit = (event) => {
		event.preventDefault();
		const data = new FormData(event.target);
		client({
			method: 'POST',
			path: '/api/authenticate',
			entity: data,
			headers: {'Content-Type': 'application/json'}
		}).then(response => {
			if (response.status.code === 200) {
				setLoggedIn(true)
			}
		})
	}

	return (
		<div className="ml-3">
			<h2>Login</h2>
			<Form onSubmit={handleSubmit}>
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

export default LogIn;
