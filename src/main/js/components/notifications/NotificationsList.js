import React from 'react';
import client from '../../client';
import { ListGroup, Pagination, PageItem, Button } from "react-bootstrap";
import { BiX, BiCheck, BiPlus, BiRefresh } from "react-icons/bi";

class NotificationsList extends React.Component {

  constructor(props) {
    super(props);
    this.pageSize = 10;
    this.totalCountHeader = 'Total-Count';
    this.getPage = this.getPage.bind(this);
    this.getPageCount = this.getPageCount.bind(this);
    this.handlePageNavigation = this.handlePageNavigation.bind(this);
    this.refresh = this.refresh.bind(this);
    this.acceptNotificationAction = this.acceptNotificationAction.bind(this);
    this.denyNotificationAction = this.denyNotificationAction.bind(this);
    this.clearNotification = this.clearNotification.bind(this);
    this.getNotificationMessage = this.getNotificationMessage.bind(this);
    this.state = {
      currentPage: 1,
      notifications: [],
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
    client({method: 'GET', path: '/api/notifications{?user-id,page-number,page-size}', params: { 'user-id': this.props.userId, 'page-number': pageNumber, 'page-size': this.pageSize }}).then(
      response => {
        if (response.status.code === 401) {
          this.props.updateLoggedIn(false, null);
          return;
        }

        this.setState({
          notifications: response.entity,
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

  clearNotification(notification, index) {
    client({method: 'DELETE', path: '/api/notifications/' + notification.id }).then(
      response => {
        if (response.status.code === 401) {
          this.props.updateLoggedIn(false, null);
          return;
        }

        let newNotifications = [...this.state.notifications];
        newNotifications.splice(index, 1);
        this.setState({
          notifications: newNotifications
        });
      }
    );
  }

  acceptNotificationAction(notification, index) {
    const connection = { id: notification.referenceId, requesterId: notification.senderId, requesteeId: notification.recipientId, status: 1 };
    if (notification.type == 0) {
      client({method: 'PUT', path: '/api/connections/' + notification.referenceId, entity: connection }).then(
        response => {
          if (response.status.code === 401) {
            this.props.updateLoggedIn(false, null);
            return;
          }

          this.clearNotification(notification, index);
        }
      );
    } else if (notification.type == 1) {
      this.clearNotification(notification, index);
    }
  }

  denyNotificationAction(notification, index) {
    const connection = { id: notification.referenceId, requesterId: notification.senderId, requesteeId: notification.recipientId, status: 1 };
    if (notification.type == 0) {
      client({method: 'DELETE', path: '/api/connections/' + notification.referenceId }).then(
        response => {
          if (response.status.code === 401) {
            this.props.updateLoggedIn(false, null);
            return;
          }

          this.clearNotification(notification, index);
        }
      );
    } else if (notification.type == 1) {
      this.clearNotification(notification, index);
    }
  }

  refresh() {
    this.getPage(1);
  }

  getNotificationMessage(notification) {
    switch(notification.type) {
      case 0: return notification.sender.username + ' wants to connect.';
      case 1: return notification.sender.username + ' accepted your connection request.';
      default: return 'Unknown notification type..';
    }
  }

  render() {
    return (
      <>
        <ListGroup>
          {this.state.notifications.map((notification, index) =>
            <ListGroup.Item key={index} style={{ display: "flex", flexDirection: "row", justifyContent: "center", alignItems: "center" }} className="notificationsListRow">
              <div style={{ flex: "1" }}>{this.getNotificationMessage(notification)}</div>
              { notification.type == 0 ? <Button variant="light" onClick={() => this.acceptNotificationAction(notification, index)} style={{ display: "flex", alignItems: "center" }}><BiCheck /></Button> : <div/> }
              <Button variant="light" onClick={() => this.denyNotificationAction(notification, index)} style={{ display: "flex", alignItems: "center" }}><BiX /></Button>
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

export default NotificationsList;
