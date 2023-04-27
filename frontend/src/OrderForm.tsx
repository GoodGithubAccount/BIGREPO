import React, { useState } from 'react';
import {type BasketItem} from './BasketProvider';

type OrderFormProps = {
	basket: any[];
};

export const OrderForm: React.FC<OrderFormProps> = ({basket }) => {
	const [isFormValid, setIsFormValid] = useState(false);

	const handleFormSubmit = (event: {preventDefault: () => void}) => {
		event.preventDefault();
		const emailInput = document.getElementById('email') as HTMLInputElement;
		const phoneInput = document.getElementsByName('phone')[0] as HTMLInputElement;
		checkForm(emailInput, phoneInput);
		createNewOrder(basket);
	};

	const handleInputChange = () => {
		const emailInput = document.getElementById('email') as HTMLInputElement;
		const phoneInput = document.getElementsByName('phone')[0] as HTMLInputElement;
		setIsFormValid(checkForm(emailInput, phoneInput));
	};

	return (
		<form action='orderDetails'>
			<label>
				Country: <input name='country' value='Denmark' readOnly/>
			</label>
			<br/>
			<label htmlFor='zipCode'>Zip Code: </label>
			<input id='zipCode' name='zipCode' onBlur={handleZipCodeBlur}/>
			<br/>
			<label htmlFor='city'>City: </label>
			<input id='city' name='city' readOnly/>
			<br/>
			<label>
				Address: <input name='address' onChange={handleInputChange}/>
			</label>
			<br/>
			<label>
				Name: <input name='name' onChange={handleInputChange}/>
			</label>
			<br/>
			<label htmlFor='phone'>Phone: </label>
			<input id='phone' name='phone' onChange={handleInputChange}/>
			<br/>
			<label htmlFor='email'>Email: </label>
			<input id='email' name='email' onChange={handleInputChange}/>
			<br/>
			<label>
				<input type='checkbox' name='terms' onChange={handleInputChange}/>
				Accept the terms and conditions{' '}
				<a href='https://www.shop.dtu.dk/en/terms-and-conditions-for-sales/'>
					(view here)
				</a>
			</label>
			<br/>
			<label>
				<input type='checkbox' name='marketing' onChange={handleInputChange}/>
				I would like to receive good offers
			</label>
			<br/>
			<label>
				Optional order comment:
				<textarea
					ref={input => {
						if (input === null) {
							orderComment = '';
						} else {
							orderComment = input;
						}
					}}
				></textarea>
			</label>
			<br/>
			<button disabled={!isFormValid} onClick={handleFormSubmit}>
				Place order
			</button>
		</form>
	);
};

let orderComment: HTMLTextAreaElement | '';
function checkForm(emailInput: HTMLInputElement, phoneInput: HTMLInputElement) {
	const isPhoneValid = /^\d{8}$/.test(phoneInput.value);
	const isEmailValid = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(emailInput.value);

	if (isPhoneValid) {
		phoneInput.setCustomValidity('');
	} else {
		phoneInput.setCustomValidity('Please enter a valid phone number');
	}

	if (isEmailValid) {
		emailInput.setCustomValidity('');
	} else {
		emailInput.setCustomValidity('Please enter a valid email');
	}

	return (isPhoneValid && isEmailValid);
}

function handleZipCodeBlur(event: React.FocusEvent<HTMLInputElement>) {
	const zipCodeInput = event.target;
	const cityInput = document.getElementById('city') as HTMLInputElement;

	// Make a request to the postnumre API endpoint with the entered zip code
	if (zipCodeInput.value === '') {
		return;
	}

	fetch(`https://api.dataforsyningen.dk/postnumre/${zipCodeInput.value}`)
		.then(async response => {
			if (!response.ok) {
				throw new Error('Network response was not ok');
			}

			return response.json();
		})
		.then(data => {
			console.log('Valid ZIP code');
			console.log('City:', data.navn);
			cityInput.value = data.navn;
			cityInput.setCustomValidity('');
		})
		.catch(error => {
			console.log('Invalid ZIP code');
			console.error(error);
			cityInput.value = '';
			cityInput.setCustomValidity('Please enter valid zipcode');
		});
}

async function createNewOrder(basket: BasketItem[]) {
	const url = 'https://localhost/api/orders';

	const basketItems = basket.flatMap(({products, amount}) => Array.from({length: amount}, () => ({
		id: products.id,
		name: products.name,
		price: products.price,
		currency: products.currency,
		rebateQuantity: products.rebateQuantity,
		rebatePercent: products.rebatePercent,
		upsellProduct: products.upsellProduct,
	})));

	const response = await fetch(url, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
			Accept: 'application/json',
		},
		body: JSON.stringify({
			products: basketItems,
		}),
	});

	if (!response.ok) {
		throw new Error(`HTTP error! status: ${response.status}`);
	}
}