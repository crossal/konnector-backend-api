import React from 'react';
import { Link } from 'react-router-dom';
import Navbar from 'react-bootstrap/Navbar';
import Nav from 'react-bootstrap/Nav';
import LogIn from './LogIn';
import SignUp from './SignUp';
import { useStickyState } from './../functions/stickyState';

const KNav = () => {

	const [loggedIn, setLoggedIn] = useStickyState(false, 'loggedIn');

	let nav;
	if (loggedIn) {
		nav =
			<Nav className="mr-auto">
				<Nav.Link as={Link} to="/">Home</Nav.Link>
				<Nav.Link as={Link} to="/profile">Profile</Nav.Link>
				<Nav.Link as={Link} to="/connections">Connections</Nav.Link>
			</Nav>
	} else {
		nav =
			<Nav className="mr-auto">
				<Nav.Link as={Link} to="/login">Log In</Nav.Link>
				<Nav.Link as={Link} to="/signup">Sign Up</Nav.Link>
			</Nav>
	}

	return (
		<Navbar bg="dark" variant="dark">
			<Navbar.Brand href="#home">Konnector</Navbar.Brand>
			{nav}
		</Navbar>
	)
}

export default KNav;
