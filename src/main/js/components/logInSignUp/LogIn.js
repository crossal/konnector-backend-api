import React from 'react';
import LogInDetails from './LogInDetails';
import AccountVerification from './AccountVerification';
import RequestPasswordReset from './RequestPasswordReset';

class LogIn extends React.Component {

  constructor(props) {
    super(props);
    this.logInWithoutVerification = this.logInWithoutVerification.bind(this);
    this.requestResetPassword = this.requestResetPassword.bind(this);
    this.verified = this.verified.bind(this);
    this.back = this.back.bind(this);
    this.state = {
      loggedInWithoutVerification: false,
      requestedResetPassword: false
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

  requestResetPassword() {
    this.setState({
      requestedResetPassword: true
    })
  }

  back() {
    if (this.state.loggedInWithoutVerification) {
      this.setState({
        loggedInWithoutVerification: false
      });
    } else if (this.state.requestedResetPassword) {
      this.setState({
        requestedResetPassword: false
      })
    } else {
      this.props.updateLogInOrSignUpStatus(false, false)
    }
  }

  render() {
    let content;
    if (this.state.loggedInWithoutVerification) {
      content =
        <AccountVerification verified={this.verified} back={this.back}/>
    } else if (this.state.requestedResetPassword) {
      content =
        <RequestPasswordReset back={this.back}/>
    } else {
      content =
        <LogInDetails updateLoggedIn={this.props.updateLoggedIn} logInWithoutVerification={this.logInWithoutVerification} requestResetPassword={this.requestResetPassword} back={this.back}/>
    }

    return (
      <>
        {content}
      </>
    )
  }
}

export default LogIn;
