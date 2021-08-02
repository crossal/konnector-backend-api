import React from 'react';
import LogInDetails from './LogInDetails';
import AccountVerification from './AccountVerification';
import RequestPasswordReset from './RequestPasswordReset';
import ResetPasswordWithCode from './ResetPasswordWithCode';

class LogIn extends React.Component {

  constructor(props) {
    super(props);
    this.logInWithoutVerification = this.logInWithoutVerification.bind(this);
    this.handleRequestResetPassword = this.handleRequestResetPassword.bind(this);
    this.handleRequestResetPasswordFollowUp = this.handleRequestResetPasswordFollowUp.bind(this);
    this.handlePasswordReset = this.handlePasswordReset.bind(this);
    this.verified = this.verified.bind(this);
    this.back = this.back.bind(this);
    this.state = {
      loggedInWithoutVerification: false,
      requestResetPassword: false,
      requestResetPasswordFollowUp: false
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

  handleRequestResetPassword() {
    this.setState({
      requestResetPassword: true
    })
  }

  handleRequestResetPasswordFollowUp() {
    this.setState({
      requestResetPassword: false,
      requestResetPasswordFollowUp: true
    })
  }

  handlePasswordReset() {
    this.setState({
      requestResetPasswordFollowUp: false
    })
  }

  back() {
    if (this.state.loggedInWithoutVerification) {
      this.setState({
        loggedInWithoutVerification: false
      });
    } else if (this.state.requestResetPassword) {
      this.setState({
        requestResetPassword: false
      })
    } else if (this.state.requestResetPasswordFollowUp) {
      this.setState({
        requestResetPassword: true,
        requestResetPasswordFollowUp: false
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
    } else if (this.state.requestResetPassword) {
      content =
        <RequestPasswordReset back={this.back} passwordResetRequested={this.handleRequestResetPasswordFollowUp}/>
    } else if (this.state.requestResetPasswordFollowUp) {
      content =
        <ResetPasswordWithCode updatePasswordReset={this.handlePasswordReset}/>
    } else {
      content =
        <LogInDetails updateLoggedIn={this.props.updateLoggedIn} logInWithoutVerification={this.logInWithoutVerification} requestResetPassword={this.handleRequestResetPassword} back={this.back}/>
    }

    return (
      <>
        {content}
      </>
    )
  }
}

export default LogIn;
