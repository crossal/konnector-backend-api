import React from 'react';
import { Switch, Route } from 'react-router-dom';
import Home from './Home';
import Profile from './Profile';
import Connections from './Connections';
import LogInSignUp from './LogInSignUp';

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
    } else {
      content =
        <LogInSignUp {...this.props}/>
    }

    return (
      <main>
        {content}
      </main>
    )
  }
}

export default MainContent;
