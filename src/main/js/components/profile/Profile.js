import React from 'react';
import client from '../../client';
import { Col, Row, Form } from "react-bootstrap";

class Profile extends React.Component {

  constructor(props) {
    super(props);
    var updateLoggedIn = this.props.updateLoggedIn;
    this.state = {user: {}};
  }

  componentDidMount() {
    var _this = this;
    client({method: 'GET', path: '/api/users/' + this.props.userId}).then(
      response => {
        this.setState({user: response.entity});
      },
      response => {
        if (response.status.code === 401) {
          this.props.updateLoggedIn(false, null)
        }
      }
    );
  }

  render() {
      return (
        <div>
          <h2>Account Details</h2>
          <Form>
            <Form.Row>
              <Form.Group as={Col} controlId="formGridEmail">
                <Form.Label>Email</Form.Label>
                <Form.Control readOnly type="email" placeholder="Email" defaultValue={this.state.user.email || ''} />
              </Form.Group>
            </Form.Row>
            <Form.Row>
              <Form.Group as={Col} controlId="formGridUsername">
                <Form.Label>Username</Form.Label>
                <Form.Control readOnly placeholder="Username" defaultValue={this.state.user.username || ''} />
              </Form.Group>
            </Form.Row>
            <Form.Row>
              <Form.Group as={Col} controlId="formGridPassword">
                <Form.Label>Password</Form.Label>
                <Form.Control type="password" placeholder="Password" />
              </Form.Group>
              <Form.Group as={Col} controlId="formGridNewPassword">
                <Form.Label>Password</Form.Label>
                <Form.Control type="password" placeholder="New password" />
              </Form.Group>
            </Form.Row>
            <Form.Row>
              <Form.Group as={Col} controlId="formGridFirstName">
                <Form.Label>First Name</Form.Label>
                <Form.Control placeholder="First name" defaultValue={this.state.user.firstName || ''} />
              </Form.Group>
              <Form.Group as={Col} controlId="formGridLastName">
                <Form.Label>Last Name</Form.Label>
                <Form.Control placeholder="Last name" defaultValue={this.state.user.lastName || ''} />
              </Form.Group>
            </Form.Row>
          </Form>

          <br />

          <h2>Contact Details</h2>
          <Form>
            <Form.Row>
              <Form.Group as={Col} controlId="formGridContactType">
                <Form.Control placeholder="Type" />
              </Form.Group>
              <Form.Group as={Col} controlId="formGridLastContactValue">
                <Form.Control placeholder="Value" />
              </Form.Group>
            </Form.Row>
          </Form>
        </div>
      )
    }
}

export default Profile;
