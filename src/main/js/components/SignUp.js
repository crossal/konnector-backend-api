import React from 'react';
import SignUpDetails from './SignUpDetails';
import AccountVerification from './AccountVerification';

class SignUp extends React.Component {

  constructor(props) {
      super(props);
      this.signedUp = this.signedUp.bind(this);
      this.verified = this.verified.bind(this);
      this.state = {
        signedUp: false
      };
  }

  signedUp() {
    this.setState({
      signedUp: true
    });
  }

  verified() {
    this.props.updateLogInOrSignUpStatus(false, false)
  }

  render() {
    let content;
    if (this.state.signedUp) {
      content =
        <AccountVerification verified={this.verified}/>
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
