import React from 'react';
import { Link } from 'react-router-dom';

class Nav extends React.Component { // <1>

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
			<nav>
				<ul>
					<li><Link to='/'>Home</Link></li>
					<li><Link to='/roster'>Roster</Link></li>
					<li><Link to='/schedule'>Schedule</Link></li>
				</ul>
			</nav>
		)
	}
}

export default Nav;
