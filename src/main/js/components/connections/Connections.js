import React from 'react';
import ConnectionsList from './ConnectionsList';
import AddConnections from './addConnections/AddConnections';
import ConnectedUserProfile from './connectedUserProfile/ConnectedUserProfile';
import { Button } from "react-bootstrap";

class Connections extends React.Component {

  constructor(props) {
    super(props);
    this.addConnections = this.addConnections.bind(this);
    this.exitAddConnections = this.exitAddConnections.bind(this);
    this.viewConnectedUser = this.viewConnectedUser.bind(this);
    this.exitViewConnectedUser = this.exitViewConnectedUser.bind(this);
    this.state = {
      addingConnections: false,
      connectedUserIdToView: null
    };
  }

  addConnections(e) {
    this.setState({
      addingConnections: true
    });
  }

  exitAddConnections(e) {
    this.setState({
      addingConnections: false
    });
  }

  viewConnectedUser(userId) {
    this.setState({
      connectedUserIdToView: userId
    });
  }

  exitViewConnectedUser() {
    this.setState({
      connectedUserIdToView: null
    });
  }

  render() {
    let content;
    if (this.state.addingConnections) {
      content =
        <div>
          <AddConnections exitAddConnections={this.exitAddConnections} {...this.props} />
        </div>
    } else if (this.state.connectedUserIdToView) {
      content =
        <div>
          <ConnectedUserProfile exitConnectedUserProfile={this.exitViewConnectedUser} connectedUserId={this.state.connectedUserIdToView} />
        </div>
    } else {
      content =
        <div>
          <h2>Connections</h2>
          <ConnectionsList viewConnectedUser={this.viewConnectedUser} {...this.props} />
          <br />
          <Button variant="primary" onClick={this.addConnections}>Add</Button>
        </div>
    }

    return (
      <>
        {content}
      </>
    )
  }
}

export default Connections;
