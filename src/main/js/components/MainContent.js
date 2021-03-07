import React from 'react';
import { Switch, Route } from 'react-router-dom';
import Home from './Home';
import Profile from './Profile';
import Connections from './Connections';
import LogIn from './LogIn';
import SignUp from './SignUp';

class MainContent extends React.Component {

  constructor(props) {
      super(props);
  }

  render() {
    let switchComponent;
    if (this.props.loggedIn) {
      switchComponent =
        <Switch>
          <Route exact path='/' component={Home}/>
          <Route path='/profile' render={() => <Profile {...this.props}/>}/>
          <Route path='/connections' component={Connections}/>
        </Switch>
    } else {
      switchComponent =
        <Switch>
          <Route path='/login' render={() => <LogIn updateLoggedIn={this.props.updateLoggedIn}/>}/>
          <Route path='/signup' component={SignUp}/>
        </Switch>
    }

    return (
      <main>
        {switchComponent}
      </main>
    )
  }
}

export default MainContent;
