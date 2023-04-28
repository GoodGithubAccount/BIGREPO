import * as React from 'react';
import {useState, useEffect} from 'react';
import Pages from '../Pages';

// This component is responsible for rendering the appropriate page based on the current step
const Route = () => {
	// Initialize the step state to 1
	const [step, setStep] = useState(1);

	// Define a component for displaying a 404 message
	const NotFound = () => <h2>404 Not Found</h2>;

	// Set up a listener for the popstate event to handle the user navigating back or forward in history
	useEffect(() => {
		const onPopState = (event: PopStateEvent) => {
			// If the event state contains a step, update the step state
			if (event.state?.step) {
				setStep(event.state.step);
			}
		};

		window.addEventListener('popstate', onPopState);

		// Handle page reloads
		const currentStep = parseInt(window.location.pathname.replace('/step', ''), 10);
		// If the current URL contains a valid step number, update the step state
		if (!isNaN(currentStep) && currentStep >= 1 && currentStep <= Object.keys(Pages).length) {
			setStep(currentStep);
		}

		// Clean up the event listener when the component unmounts
		return () => {
			window.removeEventListener('popstate', onPopState);
		};
	}, []);

	// Update the document title whenever the step changes
	useEffect(() => {
		document.title = `Step ${step} - Checkout Flow`;
	}, [step]);

	// Define handlers for advancing to the next step or going back to the previous step
	const handleNext = () => {
		const nextStep = step + 1;
		setStep(nextStep);
		// Update the URL with the new step number
		window.history.pushState({step: nextStep}, '', `/step${nextStep}`);
	};

	const handleBack = () => {
		const prevStep = step - 1;
		setStep(prevStep);
		// Update the URL with the new step number
		window.history.pushState({step: prevStep}, '', `/step${prevStep}`);
	};

	// Look up the component for the current step in the Pages object
	// If the step is not found, use the NotFound component
	const Page = Pages[step] || NotFound;

	// Render the current page along with the back and next buttons (if applicable)
	return (
		<div>
			<Page />
			{step > 1 && (
				<button onClick={handleBack}>
					Back
				</button>
			)}
			{step < Object.keys(Pages).length && (
				<button onClick={handleNext}>
					Next
				</button>
			)}
		</div>
	);
};

export default Route;
