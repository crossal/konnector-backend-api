import React from 'react';
import client from '../../client';
import { ListGroup, Pagination, PageItem, Button } from "react-bootstrap";
import { BiX, BiRefresh } from "react-icons/bi";

class ConnectionsList extends React.Component {

  constructor(props) {
    super(props);
    this.pageSize = 10;
    this.totalCountHeader = 'Total-Count';
    this.getPage = this.getPage.bind(this);
    this.handlePageNavigation = this.handlePageNavigation.bind(this);
    this.refresh = this.refresh.bind(this);
    this.deleteConnection = this.deleteConnection.bind(this);
    this.state = {
      currentPage: 1,
      connections: [],
      totalPages: 0
    };
  }

  componentDidMount() {
    var _this = this;
    this.getPage(1);
  }

  getPage(pageNumber) {
    client({method: 'GET', path: '/api/users{?connections-of-user-id,page-number,page-size}', params: { 'connections-of-user-id': this.props.userId, 'page-number': pageNumber, 'page-size': this.pageSize }}).then(
      response => {
        this.setState({
          connections: response.entity,
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

  deleteConnection(connection, index) {
    client({method: 'DELETE', path: '/api/connections{?connected-user-id}', params: { 'connected-user-id': connection.id }}).then(
      response => {
        let newConnections = [...this.state.connections];
        newConnections.splice(index, 1);
        this.setState({
          connections: newConnections
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
          {this.state.connections.map((connection, index) =>
            <ListGroup.Item onClick={() => this.props.viewConnectedUser(connection.id)} key={index} style={{ display: "flex", flexDirection: "row", justifyContent: "center", alignItems: "center" }} className="connectionsListRow">
              <div style={{ flex: "1" }}>{connection.firstName} {connection.lastName} - {connection.username}</div>
              <Button variant="light" onClick={() => this.deleteConnection(connection, index)} style={{ display: "flex", alignItems: "center" }}><BiX /></Button>
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

export default ConnectionsList;
