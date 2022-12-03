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
    this.getPageCount = this.getPageCount.bind(this);
    this.handlePageNavigation = this.handlePageNavigation.bind(this);
    this.refresh = this.refresh.bind(this);
    this.deleteConnection = this.deleteConnection.bind(this);
    this.state = {
      currentPage: 1,
      connections: [],
      pageCount: 0
    };
  }

  componentDidMount() {
    this.getPage(1);
  }

  componentDidUpdate(prevProps, prevState) {
    if (this.props.searchString !== prevProps.searchString) {
      this.refresh();
    }
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
    client({method: 'GET', path: '/api/users{?connections-of-user-id,page-number,page-size,username}', params: { 'connections-of-user-id': this.props.userId, 'page-number': pageNumber, 'page-size': this.pageSize, 'username': this.props.searchString }}).then(
      response => {
        if (response.status.code === 401) {
          this.props.updateLoggedIn(false, null);
          return;
        }

        this.setState({
          connections: response.entity,
          currentPage: pageNumber,
          pageCount: this.getPageCount(response.headers[this.totalCountHeader])
        });
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

  deleteConnection(e, connection, index) {
    e.stopPropagation();
    client({method: 'DELETE', path: '/api/connections{?connected-user-id}', params: { 'connected-user-id': connection.id }}).then(
      response => {
        if (response.status.code === 401) {
          this.props.updateLoggedIn(false, null);
          return;
        }

        let newConnections = [...this.state.connections];
        newConnections.splice(index, 1);
        this.setState({
          connections: newConnections
        });
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
            <ListGroup.Item onClick={() => this.props.viewConnectedUser(connection.id)} key={index} style={{ display: "flex", flexDirection: "row", justifyContent: "center", alignItems: "center", cursor: "pointer" }} className="connectionsListRow">
              <div style={{ flex: "1" }}><span style={{fontWeight: 'bold'}}>{connection.username}</span> - {connection.firstName} {connection.lastName}</div>
              <Button variant="light" onClick={(e) => this.deleteConnection(e, connection, index)} style={{ display: "flex", alignItems: "center" }}><BiX /></Button>
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

export default ConnectionsList;
