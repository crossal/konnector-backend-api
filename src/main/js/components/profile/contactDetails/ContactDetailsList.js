import React from 'react';
import client from '../../../client';
import { ListGroup, Pagination, PageItem, Button } from "react-bootstrap";
import { BiX, BiRefresh } from "react-icons/bi";

class ContactDetailsList extends React.Component {

  constructor(props) {
    super(props);
    this.pageSize = 10;
    this.totalCountHeader = 'Total-Count';
    this.getPage = this.getPage.bind(this);
    this.getPageCount = this.getPageCount.bind(this);
    this.handlePageNavigation = this.handlePageNavigation.bind(this);
    this.refresh = this.refresh.bind(this);
    this.deleteContactDetail = this.deleteContactDetail.bind(this);
    this.state = {
      currentPage: 1,
      contactDetails: [],
      pageCount: 0
    };
  }

  componentDidMount() {
    this.getPage(1);
  }

  getPageCount(entryCount) {
    if (entryCount == 0) {
      return 0;
    }

    let pageCount = Math.floor(entryCount / this.pageSize);
    const leftover = entryCount % this.pageSize;
    if (leftover > 0) {
      pageCount++;
    }

    return pageCount;
  }

  getPage(pageNumber) {
    client({method: 'GET', path: '/api/contact-details{?user-id,page-number,page-size}', params: { 'user-id': this.props.userId, 'page-number': pageNumber, 'page-size': this.pageSize }}).then(
      response => {
        this.setState({
          contactDetails: response.entity,
          currentPage: pageNumber,
          pageCount: this.getPageCount(response.headers[this.totalCountHeader])
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

  deleteContactDetail(contactDetail, index) {
    client({method: 'DELETE', path: '/api/contact-details/' + contactDetail.id}).then(
      response => {
        let newContactDetails = [...this.state.contactDetails];
        newContactDetails.splice(index, 1);
        this.setState({
          contactDetails: newContactDetails
        });
      },
      response => {
        if (response.status.code === 401) {
          this.props.updateLoggedIn(false, null)
        }
      }
    );
  }

  refresh() {
    this.getPage(1);
  }

  render() {
    return (
      <>
        <ListGroup>
          {this.state.contactDetails.map((contactDetail, index) =>
            <ListGroup.Item key={index} style={{ display: "flex", flexDirection: "row", justifyContent: "center", alignItems: "center" }} className="contactDetailsListRow">
              <div style={{ flex: "1" }}><span style={{fontWeight: 'bold'}}>{contactDetail.type}:</span> {contactDetail.value}</div>
              { this.props.disableDeleteButton ? <div/> : <Button variant="light" onClick={() => this.deleteContactDetail(contactDetail, index)} style={{ display: "flex", alignItems: "center" }}><BiX /></Button> }
            </ListGroup.Item>
          )}
        </ListGroup>

        <br />

        <Pagination>
          { this.state.currentPage == 1 ? <div/> : <Pagination.Prev onClick={() => this.handlePageNavigation(0)} /> }
          <Pagination.Item>{ this.state.currentPage }</Pagination.Item>
          { this.state.currentPage < this.state.pageCount ? <Pagination.Next onClick={() => this.handlePageNavigation(1)} /> : <div/>}
          <Button variant="light" onClick={this.refresh} style={{ display: "flex", alignItems: "center" }}><BiRefresh /></Button>
        </Pagination>
      </>
    )
  }
}

export default ContactDetailsList;
