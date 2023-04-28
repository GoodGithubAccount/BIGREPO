import React from 'react';
import ShoppingHandler from './ShoppingHandler';
import TestPage from './Pages/TestPage';
// Import other page components here

// `Pages` is an object that contains functions for rendering each step of the checkout flow.
// Each function returns a React component that represents the content for that step.
const Pages = {
	1: () => <ShoppingHandler />, // Step 1 shows the shopping cart and item details
	2: () => <TestPage />, // Step 2 is a test page for demonstration purposes
	// Add other page components here
};

// Export the `Pages` object for use in other components.
export default Pages;
