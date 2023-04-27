import React from 'react';
import './App.css';
import './index.css';
import {BasketProvider} from './BasketProvider';
import {GridContainer} from './GridContainer';

function App() {
	return (
		<div>
			<h2>SHOPshop</h2>
			<BasketProvider>
				<GridContainer/>
			</BasketProvider>
			<h4>SHOPshop</h4>
		</div>
	);
}

export default App;
