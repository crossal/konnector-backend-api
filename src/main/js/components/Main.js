import React from 'react';
import { Switch, Route } from 'react-router-dom';
import Home from './Home';
import Profile from './Profile';
import Connections from './Connections';

class Main extends React.Component {

	render() {
		return (
			<main>
				<Switch>
					<Route exact path='/' component={Home}/>
					<Route path='/profile' component={Profile}/>
					<Route path='/connections' component={Connections}/>
				</Switch>
			</main>
		)
	}
}

export default Main;
