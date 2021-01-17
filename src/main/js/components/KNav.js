import React from 'react';
import { Link } from 'react-router-dom';
import Navbar from 'react-bootstrap/Navbar';
import Nav from 'react-bootstrap/Nav';

class KNav extends React.Component {

	render() {
		return (
			<Navbar bg="dark" variant="dark">
				<Navbar.Brand href="#home">Konnector</Navbar.Brand>
				<Nav className="mr-auto">
					<Nav.Link as={Link} to="/">Home</Nav.Link>
					<Nav.Link as={Link} to="/profile">Profile</Nav.Link>
					<Nav.Link as={Link} to="/connections">Connections</Nav.Link>
				</Nav>
			</Navbar>
		)
	}
}

export default KNav;
