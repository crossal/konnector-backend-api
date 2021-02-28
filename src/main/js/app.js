'use strict';

import React from 'react';
import ReactDOM from 'react-dom';
import client from './client';
import { BrowserRouter } from 'react-router-dom';
import { useStickyState } from './functions/stickyState';
import KNav from './components/KNav';
import Main from './components/Main';

const App = () => {
	const [loggedIn, setLoggedIn] = useStickyState(false, 'loggedIn');

	return (
		<div>
			<Main />
		</div>
	)
}

ReactDOM.render((
	<BrowserRouter>
		<App />
	</BrowserRouter>
), document.getElementById('react'));