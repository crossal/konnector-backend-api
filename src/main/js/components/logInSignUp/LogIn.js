import React from 'react';
import LogInDetails from './LogInDetails';
import AccountVerification from './AccountVerification';

class LogIn extends React.Component {

  constructor(props) {
    super(props);
    this.logInWithoutVerification = this.logInWithoutVerification.bind(this);
    this.verified = this.verified.bind(this);
    this.back = this.back.bind(this);
    this.state = {
      loggedInWithoutVerification: false
    };
  }

  logInWithoutVerification() {
    this.setState({
      loggedInWithoutVerification: true
    });
  }

  verified() {
    this.setState({
      loggedInWithoutVerification: false
    });
  }

  back() {
    if (this.state.loggedInWithoutVerification) {
      this.setState({
        loggedInWithoutVerification: false
      });
    } else {
      this.props.updateLogInOrSignUpStatus(false, false)
    }
  }

  render() {
    let content;
    if (this.state.loggedInWithoutVerification) {
      content =
        <AccountVerification verified={this.verified} back={this.back}/>
    } else {
      content =
        <LogInDetails updateLoggedIn={this.props.updateLoggedIn} logInWithoutVerification={this.logInWithoutVerification} back={this.back}/>
    }

    return (
      <>
        {content}
      </>
    )
  }
}

export default LogIn;
