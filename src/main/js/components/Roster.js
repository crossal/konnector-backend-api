import React from 'react';
import { Switch, Route } from 'react-router-dom';

class Roster extends React.Component { // <1>

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
			<Switch>
				<Route exact path='/roster' component={FullRoster}/>
				<Route path='/roster/:number' component={Player}/>
			</Switch>
		)
	}
}

export default Roster;
