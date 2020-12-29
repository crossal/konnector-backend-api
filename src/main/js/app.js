'use strict';

// tag::vars[]
import React from 'react';
import ReactDOM from 'react-dom';
import client from './client';
import { BrowserRouter } from 'react-router-dom';
//const React = require('react'); // <1>
//const ReactDOM = require('react-dom'); // <2>
//const client = require('./client'); // <3>
import Nav from './components/Nav';
import Main from './components/Main';
// end::vars[]

// tag::app[]
class App extends React.Component { // <1>

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
//			<EmployeeList employees={this.state.employees}/>
			<div>
				<Nav />
				<Main />
			</div>
		)
	}
}
// end::app[]

// tag::employee-list[]
//class EmployeeList extends React.Component{
//	render() {
//		const employees = this.props.employees.map(employee =>
//			<Employee key={employee._links.self.href} employee={employee}/>
//		);
//		return (
//			<table>
//				<tbody>
//					<tr>
//						<th>First Name</th>
//						<th>Last Name</th>
//						<th>Description</th>
//					</tr>
//					{employees}
//				</tbody>
//			</table>
//		)
//	}
//}
// end::employee-list[]

// tag::employee[]
//class Employee extends React.Component{
//	render() {
//		return (
//			<tr>
//				<td>{this.props.employee.firstName}</td>
//				<td>{this.props.employee.lastName}</td>
//				<td>{this.props.employee.description}</td>
//			</tr>
//		)
//	}
//}
// end::employee[]

// tag::render[]
//ReactDOM.render(
//	<App />,
//	document.getElementById('react')
//)
ReactDOM.render((
  <BrowserRouter>
    <App />
  </BrowserRouter>
), document.getElementById('react'));
// end::render[]