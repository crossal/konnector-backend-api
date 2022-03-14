import React from 'react';
import ConnectionsList from './ConnectionsList';
import AddConnections from './addConnections/AddConnections';
import { Button } from "react-bootstrap";

class Connections extends React.Component {

  constructor(props) {
    super(props);
    this.addConnections = this.addConnections.bind(this);
    this.state = {
      addingConnections: false,
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

  render() {
    let content;
    if (this.state.addingConnections) {
      content =
        <div>
          <AddConnections {...this.state} {...this.props} />
        </div>
    } else {
      content =
        <div>
          <h2>Connections</h2>
          <ConnectionsList {...this.props} />
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
