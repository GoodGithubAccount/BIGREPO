import React from 'react';
import ShoppingHandler from './ShoppingHandler';
import TestPage from './Pages/TestPage';
// Import other page components here

const Pages = {
	1: () => <ShoppingHandler />,
	2: () => <TestPage />,
	// Add other pages here
};

export default Pages;
