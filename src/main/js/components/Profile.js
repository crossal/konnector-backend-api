import React from 'react';
import client from '../client';

class Profile extends React.Component { // <1>

	constructor(props) {
		super(props);
		this.state = {user: {}};
	}

	componentDidMount() { // <2>
		client({method: 'GET', path: '/api/users/1'}).then(response => {
			this.setState({user: response.entity._embedded.user});
		});
	}

	render() { // <3>
		return (
			<div>
				<h1>User email is: {this.state.user.email}</h1>
			</div>
		)
	}
}

export default Profile;
