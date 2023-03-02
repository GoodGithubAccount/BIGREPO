import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import ItemView from './ItemView';
import './index.css';
import {GetItemBasket} from './BasketView';

ReactDOM.createRoot(document.getElementById('root')!).render(
	<React.StrictMode>
		<App />
	</React.StrictMode>,
);
