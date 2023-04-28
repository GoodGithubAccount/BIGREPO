import * as React from 'react';
import {useState, useEffect} from 'react';
import Pages from '../Pages';

const Route = () => {
	const [step, setStep] = useState(1);
	const NotFound = () => <h2>404 Not Found</h2>;

	useEffect(() => {
		const onPopState = (event: PopStateEvent) => {
			if (event.state?.step) {
				setStep(event.state.step);
			}
		};

		window.addEventListener('popstate', onPopState);

		// Handle page reloads
		const currentStep = parseInt(window.location.pathname.replace('/step', ''), 10);
		if (!isNaN(currentStep) && currentStep >= 1 && currentStep <= Object.keys(Pages).length) {
			setStep(currentStep);
		}

		return () => {
			window.removeEventListener('popstate', onPopState);
		};
	}, []);

	useEffect(() => {
		document.title = `Step ${step} - Checkout Flow`;
	}, [step]);

	const handleNext = () => {
		const nextStep = step + 1;
		setStep(nextStep);
		window.history.pushState({step: nextStep}, '', `/step${nextStep}`);
	};

	const handleBack = () => {
		const prevStep = step - 1;
		setStep(prevStep);
		window.history.pushState({step: prevStep}, '', `/step${prevStep}`);
	};

	const Page = Pages[step] || NotFound;

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
