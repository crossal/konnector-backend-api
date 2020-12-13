import React from 'react';

class Schedule extends React.Component { // <1>

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
			<div>
				<ul>
					<li>6/5 @ Evergreens</li>
					<li>6/8 vs Kickers</li>
					<li>6/14 @ United</li>
				</ul>
			</div>
		)
	}
}

export default Schedule;
