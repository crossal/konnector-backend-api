import React from 'react';
import { Switch, Route } from 'react-router-dom';
import Home from './Home';
import Roster from './Roster';
import Schedule from './Schedule';

class Main extends React.Component { // <1>

//	constructor(props) {
//		super(props);
//		this.state = {employees: []};
//	}
//
//	componentDidMount() { // <2>
//		client({method: 'GET', path: '/api/employees'}).then(response => {
//			this.setState({employees: response.entity._embedded.employees});
//		});
//	}

	render() { // <3>
		return (
			<main>
				<Switch>
					<Route exact path='/' component={Home}/>
					<Route path='/roster' component={Roster}/>
					<Route path='/schedule' component={Schedule}/>
				</Switch>
			</main>
		)
	}
}

export default Main;
