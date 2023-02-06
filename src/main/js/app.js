'use strict';

import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';
import Main from './components/Main';

const App = () => {
	return (
		<Main />
	)
}

ReactDOM.createRoot(document.getElementById("react"))
.render(
	<BrowserRouter>
		<App />
	</BrowserRouter>
);