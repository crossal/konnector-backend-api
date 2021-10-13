import React from 'react';
import client from '../../../client';
import { ListGroup } from "react-bootstrap";

class ContactDetailsList extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      pageNumber: 1,
      contactDetails: []
    };
  }

  componentDidMount() {
    var _this = this;
    client({method: 'GET', path: '/api/contactDetails{?userId,pageNumber,pageSize}', params: { userId: this.props.userId, pageNumber: 1, pageSize: 10 }}).then(
      response => {
        this.setState({contactDetails: response.entity});
      },
      response => {
        if (response.status.code === 401) {
          this.props.updateLoggedIn(false, null)
        }
      }
    );
  }

  handlePageNavigation() {

  }

  render() {
    return (
      <ListGroup>
        {this.state.contactDetails.map(contactDetail =>
          <ListGroup.Item className="contactDetailsListRow">
            {contactDetail.name}: {contactDetail.value}
          </ListGroup.Item>
        )}
      </ListGroup>
    )
  }
}

export default ContactDetailsList;
