import React from 'react';
import LogIn from './LogIn';
import SignUp from './SignUp';
import { Button } from "react-bootstrap";

class LogInSignUp extends React.Component {

  constructor(props) {
      super(props);
      this.logIn = this.logIn.bind(this);
      this.signUp = this.signUp.bind(this);
      this.updateLogInOrSignUpStatus = this.updateLogInOrSignUpStatus.bind(this);
      var updateLogInOrSignUpStatus = this.updateLogInOrSignUpStatus.bind(this);
      this.state = {
        loggingIn: false,
        signingUp: false,
        updateLogInOrSignUpStatus: updateLogInOrSignUpStatus
      };
  }

  logIn(e) {
    this.setState({
      loggingIn: true,
      signingUp: false
    });
  }

  signUp(e) {
    this.setState({
      loggingIn: false,
      signingUp: true
    });
  }

  updateLogInOrSignUpStatus(loggingIn, signingUp) {
    this.setState({
      loggingIn: loggingIn,
      signingUp: signingUp
    });
  }

  render() {
    let content;
    if (this.state.loggingIn) {
      content =
        <LogIn updateLoggedIn={this.props.updateLoggedIn} updateLogInOrSignUpStatus={this.updateLogInOrSignUpStatus}/>
    } else if (this.state.signingUp) {
      content =
        <SignUp {...this.state}/>
    } else {
      content =
        <div className="mx-auto text-center">
          <Button className="mr-2" variant="primary" onClick={this.logIn}>Log In</Button>
          <Button variant="primary" onClick={this.signUp}>Sign Up</Button>
        </div>
    }

    return (
      <>
        {content}
      </>
    )
  }
}

export default LogInSignUp;