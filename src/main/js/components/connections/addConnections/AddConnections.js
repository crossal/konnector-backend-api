import React from 'react';
import AddConnectionsList from './AddConnectionsList';
import Search from './../../search/Search';
import { Button } from "react-bootstrap";

class AddConnections extends React.Component {

  constructor(props) {
    super(props);
    this.search = this.search.bind(this);
    this.state = {
      searchString: ''
    };
  }

  search(newSearchString) {
    this.setState({
      searchString: newSearchString
    });
  }

  render() {
    return (
      <div>
        <h2>Add Connections</h2>
        <Button className="mb-4" variant="secondary" onClick={this.props.exitAddConnections}>Back</Button>
        <Search {...this.props} placeholder="Search" search={this.search} />
        <AddConnectionsList {...this.props} searchString={this.state.searchString} />
      </div>
    )
  }
}

export default AddConnections;
