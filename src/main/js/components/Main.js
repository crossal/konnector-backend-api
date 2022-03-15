import React from 'react';
import KNav from './KNav';
import MainContent from './MainContent';
import client from '../client';

class Main extends React.Component {

  constructor(props) {
    super(props);
    var updateLoggedIn = this.updateLoggedIn.bind(this);
    var updatePasswordReset = this.updatePasswordReset.bind(this);
    this.state = {
      loggedIn: localStorage.getItem('loggedIn') == 'true',
      userId: localStorage.getItem('userId'),
      updateLoggedIn: updateLoggedIn,
      updatePasswordReset: updatePasswordReset,
      resetPasswordToken: sessionStorage.getItem('resetPasswordToken')
    };
    if (this.state.resetPasswordToken === true) {
      window.history.replaceState({}, "Konnector", "/")
    }
    sessionStorage.removeItem('resetPasswordToken')
  }

   updateLoggedIn(loggedIn, userId) {
     this.setState({
        loggedIn: loggedIn,
        userId: userId
     });
     localStorage.setItem('loggedIn', loggedIn);
     localStorage.setItem('userId', userId);
   }

   updatePasswordReset() {
     this.setState({
       resetPasswordToken: null
     });
   }

   componentDidMount() {
     // check if user session is still valid
     // if not, clean up state
     client({method: 'GET', path: '/api/auth-health'}).then(
       response => {
         if (response.status.code === 401) {
           this.setState({
             loggedIn: false,
             userId: null
           });
         }
       }
     );
   }

  render() {
    return (
      <>
        <KNav {...this.state} />
        <MainContent {...this.state} />
      </>
    )
  }
}

export default Main;
