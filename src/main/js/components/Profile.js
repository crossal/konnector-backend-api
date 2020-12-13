import React from 'react';

class Profile extends React.Component { // <1>

	constructor(props) {
		super(props);
		this.state = {employees: []};
	}

	componentDidMount() { // <2>
		client({method: 'GET', path: '/api/employees'}).then(response => {
			this.setState({employees: response.entity._embedded.employees});
		});
	}

	render() { // <3>
		return (
			<div>
				<h1>Welcome to the Tornadoes Website!</h1>
			</div>
		)
	}
}

export default Profile;
