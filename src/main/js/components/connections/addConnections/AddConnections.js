import React from 'react';
import AddConnectionsList from './AddConnectionsList';
import { Button } from "react-bootstrap";

class AddConnections extends React.Component {

  constructor(props) {
    super(props);
  }

  render() {
    return (
      <div>
        <h2>Add Connections</h2>
        <Button className="mb-4" variant="secondary" onClick={this.props.exitAddConnections}>Back</Button>
        <AddConnectionsList {...this.props} />
      </div>
    )
  }
}

export default AddConnections;
