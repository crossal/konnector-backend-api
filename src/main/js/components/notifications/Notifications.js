import React from 'react';
import NotificationsList from './NotificationsList';

class Notifications extends React.Component {

  constructor(props) {
    super(props);
  }

  render() {
    return (
      <div>
        <h2>Notifications</h2>
        <NotificationsList {...this.props} />
      </div>
    )
  }
}

export default Notifications;
