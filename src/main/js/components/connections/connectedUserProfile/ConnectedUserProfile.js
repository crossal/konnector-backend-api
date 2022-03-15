import React from 'react';
import client from '../../../client';
import { Col, Row, Form, Button, Alert } from "react-bootstrap";
import ContactDetailsList from '../../profile/contactDetails/ContactDetailsList';

class ConnectedUserProfile extends React.Component {

  constructor(props) {
    super(props);
    var updateLoggedIn = this.props.updateLoggedIn;
    this.state = {
      user: {},
      serverError: null,
      loading: false
    };
  }

  componentDidMount() {
    var _this = this;
    client({method: 'GET', path: '/api/users/' + this.props.connectedUserId}).then(
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
        <Button className="mb-4" variant="secondary" onClick={this.props.exitConnectedUserProfile}>Back</Button>
        <h2>{this.state.user.firstName} {this.state.user.lastName}</h2>
        <h3>{this.state.user.username}</h3>
        <h4>Contact Details</h4>
        <ContactDetailsList disableDeleteButton={true} userId={this.props.connectedUserId} />
      </div>
    )
  }
}

export default ConnectedUserProfile;
