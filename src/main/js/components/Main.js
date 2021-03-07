import React from 'react';
import KNav from './KNav';
import MainContent from './MainContent';

class Main extends React.Component {

  constructor(props) {
    super(props);
    var updateLoggedIn  = this.updateLoggedIn.bind(this);
    this.state = {
      loggedIn: localStorage.getItem('loggedIn'),
      userId: localStorage.getItem('userId'),
      updateLoggedIn: updateLoggedIn
    };
  }

   updateLoggedIn(loggedIn, userId){
     this.setState({
        loggedIn: loggedIn,
        userId, userId
     });
     localStorage.setItem('loggedIn', loggedIn);
     localStorage.setItem('userId', userId);
   }

  render() {
    var updateLoggedIn = this.updateLoggedIn;
    return (
      <div>
        <KNav {...this.state} />
        <MainContent {...this.state} />
      </div>
    )
  }
}

export default Main;
