import React from 'react';
import SignUpDetails from './SignUpDetails';

class SignUp extends React.Component {

  constructor(props) {
      super(props);
      this.signedUp = this.signedUp.bind(this);
      this.state = {
        signedUp: false
      };
  }

  signedUp(){
    this.setState({
      signedUp: true
    });
  }

  render() {
    let content;
    if (this.state.signedUp) {
      content =
        <AccountVerification updateLoggedIn={this.props.updateLoggedIn}/>
    } else {
      content =
        <SignUpDetails signedUp={this.signedUp}/>
    }

    return (
      <div>
        {content}
      </div>
    )
  }
}

export default SignUp;
