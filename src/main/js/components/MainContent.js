import React from 'react';
import { Switch, Route } from 'react-router-dom';
import Home from './Home';
import Profile from './Profile';
import Connections from './Connections';
import LogIn from './LogIn';
import SignUp from './SignUp';
import { useStickyState } from './../functions/stickyState';

const MainContent = () => {

	const [loggedIn, setLoggedIn] = useStickyState(false, 'loggedIn');

	let switchComponent;
	if (loggedIn) {
		switchComponent =
			<Switch>
				<Route exact path='/' component={Home}/>
				<Route path='/profile' component={Profile}/>
				<Route path='/connections' component={Connections}/>
			</Switch>
	} else {
		switchComponent =
			<Switch>
				<Route path='/login' component={LogIn}/>
				<Route path='/signup' component={SignUp}/>
			</Switch>
	}

	return (
		<main>
			{switchComponent}
		</main>
	)
}

export default MainContent;
