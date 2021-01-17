'use strict';

import React from 'react';
import ReactDOM from 'react-dom';
import client from './client';
import { BrowserRouter } from 'react-router-dom';
import KNav from './components/KNav';
import Main from './components/Main';

class App extends React.Component {

	render() {
		return (
			<div>
				<KNav />
				<Main />
			</div>
		)
	}
}

ReactDOM.render((
  <BrowserRouter>
    <App />
  </BrowserRouter>
), document.getElementById('react'));