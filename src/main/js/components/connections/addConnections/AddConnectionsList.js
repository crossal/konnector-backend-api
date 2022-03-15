import React from 'react';
import client from '../../../client';
import { ListGroup, Pagination, PageItem, Button } from "react-bootstrap";
import { BiCheck, BiPlus, BiRefresh } from "react-icons/bi";

class AddConnectionsList extends React.Component {

  constructor(props) {
    super(props);
    this.pageSize = 10;
    this.totalCountHeader = 'Total-Count';
    this.getPage = this.getPage.bind(this);
    this.handlePageNavigation = this.handlePageNavigation.bind(this);
    this.refresh = this.refresh.bind(this);
    this.addConnection = this.addConnection.bind(this);
    this.state = {
      currentPage: 1,
      users: [],
      totalPages: 0,
      addedUserIds: new Set()
    };
  }

  componentDidMount() {
    var _this = this;
    this.getPage(1);
  }

  getPage(pageNumber) {
    client({method: 'GET', path: '/api/users{?page-number,page-size}', params: { 'page-number': pageNumber, 'page-size': this.pageSize }}).then(
      response => {
        this.setState({
          users: response.entity,
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

  addConnection(connectedUserId, index) {
    const newAddedUserIds = new Set(this.state.addedUserIds);
    newAddedUserIds.add(connectedUserId);
    this.setState({
      addedUserIds: newAddedUserIds
    });

    const connection = { requesterId: this.props.userId, requesteeId: connectedUserId, status: 0 };
    client({method: 'POST', path: '/api/connections', entity: connection}).then(
      response => {
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
          {this.state.users.map((user, index) =>
            <ListGroup.Item key={index} style={{ display: "flex", flexDirection: "row", justifyContent: "center", alignItems: "center" }} className="connectionsListRow">
              <div style={{ flex: "1" }}>{user.firstName} {user.lastName} - {user.username}</div>
              { this.state.addedUserIds.has(user.id) ?
                <Button variant="light" style={{ display: "flex", alignItems: "center" }}><BiCheck /></Button>
                : <Button variant="light" onClick={() => this.addConnection(user.id, index)} style={{ display: "flex", alignItems: "center" }}><BiPlus /></Button>
              }
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

export default AddConnectionsList;
