import React from 'react';
import { Switch, Route } from 'react-router-dom';
import Home from './Home';
import Profile from './profile/Profile';
import Connections from './connections/Connections';
import LogInSignUp from './logInSignUp/LogInSignUp';
import ResetPassword from './logInSignUp/ResetPassword';
import { Container, Row, Col } from "react-bootstrap";

class MainContent extends React.Component {

  constructor(props) {
      super(props);
  }

  render() {
    let content;
    if (this.props.loggedIn) {
      content =
        <Switch>
          <Route exact path='/' component={Home}/>
          <Route path='/profile' render={() => <Profile {...this.props}/>}/>
          <Route path='/connections' component={Connections}/>
        </Switch>
    } else if (this.props.resetPasswordToken) {
      content =
        <ResetPassword token={this.props.resetPasswordToken} updatePasswordReset={this.props.updatePasswordReset}/>
    } else {
      content =
        <LogInSignUp {...this.props}/>
    }

    return (
      <main>
        <div className="m-4">
          <Container>
            <Row>
              <Col xs={0} sm={0} md={1} lg={2} xl={3}/>
              <Col xs={12} sm={12} md={10} lg={8} xl={6}>
                {content}
              </Col>
              <Col xs={0} sm={0} md={1} lg={2} xl={3}/>
            </Row>
          </Container>
        </div>
      </main>
    )
  }
}

export default MainContent;
