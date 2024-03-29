import React from 'react';
import { Link } from 'react-router-dom';
import Container from 'react-bootstrap/Container';
import Navbar from 'react-bootstrap/Navbar';
import Nav from 'react-bootstrap/Nav';
import { Button } from "react-bootstrap";
import client from '../client';

class KNav extends React.Component {

  constructor(props) {
    super(props);
    this.updateLoggedIn = this.updateLoggedIn.bind(this);
  }

  updateLoggedIn(e){
    client({method: 'POST', path: '/api/deauthenticate/'}).then(
      response => {
      }
    );
    this.props.updateLoggedIn(false, null)
  }

  render() {
    let content;
    if (this.props.loggedIn === true) {
      content =
        <Navbar bg="dark" variant="dark" expand="md">
          <Container>
            <Navbar.Brand href="#home">Konnector</Navbar.Brand>
            <Navbar.Toggle aria-controls="basic-navbar-nav" />
            <Navbar.Collapse id="basic-navbar-nav" className="justify-content-end">
              <Nav>
                <Nav.Link as={Link} to="/">Home</Nav.Link>
                <Nav.Link as={Link} to="/profile">Profile</Nav.Link>
                <Nav.Link as={Link} to="/connections">Connections</Nav.Link>
                <Nav.Link as={Link} to="/notifications">Notifications</Nav.Link>
                <a href="https://www.buymeacoffee.com/crossal" target="_blank">
                  <img src="https://cdn.buymeacoffee.com/buttons/v2/default-yellow.png" alt="Buy Me A Coffee" style={{ height: "40px", width: "143px" }}/>
                </a>
                <Button variant="link" onClick={this.updateLoggedIn}>Log Out</Button>
              </Nav>
            </Navbar.Collapse>
          </Container>
        </Navbar>
    } else {
      content =
        <Navbar bg="dark" variant="dark" expand="md">
          <Container>
            <Navbar.Brand href="#home">Konnector</Navbar.Brand>
            <Nav className="justify-content-end">
              <a href="https://www.buymeacoffee.com/crossal" target="_blank">
                <img src="https://cdn.buymeacoffee.com/buttons/v2/default-yellow.png" alt="Buy Me A Coffee" style={{ height: "40px", width: "143px" }}/>
              </a>
            </Nav>
          </Container>
        </Navbar>
    }

    return (
      <>
        {content}
      </>
    )
  }
}

export default KNav;
