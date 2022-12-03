import React from 'react';
import { InputGroup, Form } from "react-bootstrap";

class Search extends React.Component {

  constructor(props) {
    super(props);
    var updateLoggedIn = this.props.updateLoggedIn;
    this.search = this.search.bind(this);
    this.state = {
      searchString: '',
      typing: false,
      typingTimeout: 0
    };
  }

  search(event) {
    if (this.state.typingTimeout) {
      clearTimeout(this.state.typingTimeout);
    }

    this.setState({
      searchString: event.target.value,
      typingTimeout: setTimeout(() => {
        this.props.search(this.state.searchString);
       }, 1000)
    });
  }

  render() {
    return (
      <InputGroup className="mb-3">
        <Form.Control placeholder={this.props.placeholder} onChange={this.search}/>
      </InputGroup>
    )
  }
}

export default Search;
