import React from 'react';
import SignUpDetails from './SignUpDetails';
import AccountVerification from './AccountVerification';

class SignUp extends React.Component {

  constructor(props) {
    super(props);
    this.signedUp = this.signedUp.bind(this);
    this.verified = this.verified.bind(this);
    this.back = this.back.bind(this);
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

  back() {
    if (this.state.signedUp) {
      this.setState({
        signedUp: false
      });
    } else {
      this.props.updateLogInOrSignUpStatus(false, false)
    }
  }

  render() {
    let content;
    if (this.state.signedUp === true) {
      content =
        <AccountVerification verified={this.verified} back={this.back}/>
    } else {
      content =
        <SignUpDetails signedUp={this.signedUp} back={this.back}/>
    }

    return (
      <>
        {content}
      </>
    )
  }
}

export default SignUp;
