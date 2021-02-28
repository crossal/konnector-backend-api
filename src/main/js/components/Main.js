import React from 'react';
import KNav from './KNav';
import MainContent from './MainContent';

class Main extends React.Component {

	render() {
		return (
			<div>
				<KNav />
				<MainContent />
			</div>
		)
	}
}

export default Main;
