import React from 'react';
import { Switch, Route } from 'react-router-dom';
import LogIn from './LogIn';
import SignUp from './SignUp';
import { Button } from "react-bootstrap";

class LogInSignUp extends React.Component {

  constructor(props) {
      super(props);
      this.logIn = this.logIn.bind(this);
      this.signUp = this.signUp.bind(this);
      this.state = {
        loggingIn: false,
        signingUp: false
      };
  }

  logIn(e){
    this.setState({
      loggingIn: true,
      signingUp: false
    });
  }

  signUp(e){
    this.setState({
      loggingIn: false,
      signingUp: true
    });
  }

  render() {
    let content;
    if (this.state.loggingIn) {
      content =
        <LogIn updateLoggedIn={this.props.updateLoggedIn}/>
    } else if (this.state.signingUp) {
      content =
        <SignUp {...this.props}/>
    } else {
      content =
        <div>
          <Button variant="primary" onClick={this.logIn}>Log In</Button>
          <Button variant="primary" onClick={this.signUp}>Sign Up</Button>
        </div>
    }

    return (
      <div>
        {content}
      </div>
    )
  }
}

export default LogInSignUp;