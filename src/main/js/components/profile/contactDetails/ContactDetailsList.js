import React from 'react';
import client from '../../../client';
import { ListGroup, Pagination, PageItem, Button } from "react-bootstrap";
import { BiRefresh } from "react-icons/bi";

class ContactDetailsList extends React.Component {

  constructor(props) {
    super(props);
    this.pageSize = 10;
    this.totalCountHeader = 'Total-Count';
    this.getPage = this.getPage.bind(this);
    this.handlePageNavigation = this.handlePageNavigation.bind(this);
    this.refresh = this.refresh.bind(this);
    this.state = {
      currentPage: 1,
      contactDetails: [],
      totalPages: 0
    };
  }

  componentDidMount() {
    var _this = this;
    this.getPage(1);
  }

  getPage(pageNumber) {
    client({method: 'GET', path: '/api/contact-details{?userId,pageNumber,pageSize}', params: { userId: this.props.userId, pageNumber: pageNumber, pageSize: this.pageSize }}).then(
      response => {
        this.setState({
          contactDetails: response.entity,
          currentPage: pageNumber,
          totalPages: Math.floor(response.headers[this.totalCountHeader] / this.pageSize) + 1
        });
      },
      response => {
        if (response.status.code === 401) {
          this.props.updateLoggedIn(false, null)
        }
      }
    );
  }

  handlePageNavigation(direction) {
    let nextPageNumber;
    if (direction == 0) {
      nextPageNumber = this.state.currentPage - 1;
    } else {
      nextPageNumber = this.state.currentPage + 1;
    }

    this.getPage(nextPageNumber);
  }

  refresh() {
    this.getPage(1);
  }

  render() {
    return (
      <>
        <ListGroup>
          {this.state.contactDetails.map(contactDetail =>
            <ListGroup.Item key={contactDetail.id} className="contactDetailsListRow">
              {contactDetail.name}: {contactDetail.value}
            </ListGroup.Item>
          )}
        </ListGroup>

        <br />

        <Pagination>
          { this.state.currentPage == 1 ? <div/> : <Pagination.Prev onClick={() => this.handlePageNavigation(0)} /> }
          <Pagination.Item>{ this.state.currentPage }</Pagination.Item>
          { this.state.currentPage < this.state.totalPages ? <Pagination.Next onClick={() => this.handlePageNavigation(1)} /> : <div/>}
          <Button variant="light" onClick={this.refresh} style={{ display: "flex", alignItems: "center" }}><BiRefresh /></Button>
        </Pagination>
      </>
    )
  }
}

export default ContactDetailsList;
