import * as React from 'react';
import {useState, useEffect} from 'react';

const CheckoutFlow = () => {
	const [step, setStep] = useState(1);

	useEffect(() => {
		const onPopState = (event: PopStateEvent) => {
			if (event.state && event.state.step) {
				setStep(event.state.step);
			}
		};

		window.addEventListener('popstate', onPopState);
		return () => {
			window.removeEventListener('popstate', onPopState);
		};
	}, []);

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

	return (
		<div>
			{step === 1 && <h2>Home</h2>}
			{step === 2 && <h2>Step 1</h2>}
			{step === 3 && <h2>Step 2</h2>}
			{step === 4 && <h2>Step 3</h2>}
			{step > 1 && (
				<button onClick={handleBack}>
                    Back
				</button>
			)}
			{step < 4 && (
				<button onClick={handleNext}>
                    Next
				</button>
			)}
		</div>
	);
};

export default CheckoutFlow;
