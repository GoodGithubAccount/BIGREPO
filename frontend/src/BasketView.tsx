import React, {useContext, useState, type FormEvent} from 'react';
import {BasketContext} from './ShoppingHandler';

export const GetItemBasket = () => {
	const {basket, removeFromBasket, basketAdder, basketSubber} = useContext(BasketContext);

	const [totalPrice, setTotal] = React.useState(0);

	const [totalRebate, setRebate] = React.useState(0);

	async function handleSubmit(e: FormEvent) {
		e.preventDefault();
		const form = e.target as HTMLFormElement;
		const formElements = form.elements as typeof form.elements & {
			country: HTMLInputElement;
			zipCode: HTMLInputElement;
			city: HTMLInputElement;
			address: HTMLInputElement;
			name: HTMLInputElement;
			phone: HTMLInputElement;
			email: HTMLInputElement;
		};
		const country = encodeURIComponent(formElements.country.value);
		const zipCode = encodeURIComponent(formElements.zipCode.value);
		const city = encodeURIComponent(formElements.city.value);
		const address = encodeURIComponent(formElements.address.value);
		const name = encodeURIComponent(formElements.name.value);
		const phone = encodeURIComponent(formElements.phone.value);
		const email = encodeURIComponent(formElements.email.value);
	}



	React.useEffect(() => {
		setTotal(
			basket.reduce((acc, item) => acc + (item.itemData.price * item.amount), 0),
		);
		setRebate(
			basket.reduce((acc, item) => acc + findTotal(item), 0),
		);
	}, [basket]);

	return (
		<div>
			<h1>Basket</h1>
			{basket.map(item => (
				<div key={item.itemData.id}>
					{item.itemData.name} x {item.amount} : {item.itemData.price * item.amount} DKK

					<button onClick={() => {
						basketAdder(item);
					}}>+</button>

					<button onClick={() => {
						basketSubber(item);
					}}>-</button>

					<button onClick={() => {
						removeFromBasket(item);
					}}>Remove</button>
				</div>
			))}
			<div>
				Total: {totalPrice} DKK
				<br />Your Total : {toFixedIfNecessary(totalRebate, 2)} DKK
				<br />Money saved: {toFixedIfNecessary(totalPrice - totalRebate, 2)} DKK</div>

			<form action='orderDetails'>
				<label>Country: <input name='country' value='Denmark' readOnly /></label>
				<br />
				<label htmlFor='zipCode'>Zip Code: </label>
				<input id='zipCode' name='zipCode' onBlur={handleZipCodeBlur} />
				<br />
				<label htmlFor='city'>City: </label>
				<input id='city' name='city' readOnly />
				<br />
				<label>Address: <input name='address' /></label>
				<br />
				<label>Name: <input name='name' /></label>
				<br />
				<label htmlFor='phone'>Phone: </label>
				<input id='phone' name='phone' />
				<br />
				<label htmlFor='email'>Email: </label>
				<input id='email' name='email' />
				<br />
				<label>
					<input type='checkbox' name='terms' />
					Accept the terms and conditions <a href='https://www.shop.dtu.dk/en/terms-and-conditions-for-sales/'> (view here) </a>
				</label>
				<br />
				<label>
					<input type='checkbox' name='marketing' />
					I would like to receive good offers
				</label>
				<br />
				<label>
					Optional order comment:
					<textarea
						ref={input => {
							orderComment = input;
						}}
					></textarea>
				</label>
				<br />
				<button onClick={() => {
					const emailInput = document.getElementById('email') as HTMLInputElement;
					const phoneInput = document.getElementsByName('phone')[0] as HTMLInputElement;
					checkForm(emailInput, phoneInput);
				}}> Place order </button>
			</form>
		</div>
	);
};

// Function finds rebate if any and calculates new total with rebate
function findTotal(item: any) {
	let total;
	// Calculate total price with rebate
	if (item.amount >= item.itemData.rebateQuantity) {
		total = item.itemData.price * item.amount * (1 - (item.itemData.rebatePercent / 100));
	} else {
		total = item.itemData.price * item.amount;
	}

	return total;
}

// To get two decimals when showing values from: https://stackoverflow.com/questions/32229667/have-max-2-decimal-places
function toFixedIfNecessary(value: any, dp: number) {
	return Number(parseFloat(value).toFixed(dp));
}

// Help from good friend Chattersbot
function handleZipCodeBlur(event: React.FocusEvent<HTMLInputElement>) {
	const zipCodeInput = event.target;
	const cityInput = document.getElementById('city') as HTMLInputElement;

	// Make a request to the postnumre API endpoint with the entered zip code
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
		})
		.catch(error => {
			console.log('Invalid ZIP code');
			console.error(error);
			cityInput.value = 'Please enter valid zipcode';
		});
}

function checkForm(emailInput: HTMLInputElement, phoneInput: HTMLInputElement) {
	const isPhoneValid = /^\d{8}$/.test(phoneInput.value);
	const isEmailValid = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(emailInput.value);

	console.log('Optional order comment:', orderComment.value);

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
}
let orderComment = null;

/*
const checkForm = (emailInput, phoneInput) => {
	// simulate form validation and submission
	console.log('Email:', emailInput.value);
	console.log('Phone:', phoneInput.value);
	console.log('Terms and conditions accepted:', termsAccepted.checked);
	console.log('Marketing emails accepted:', marketingAccepted.checked);
	console.log('Optional order comment:', orderComment.value);
};

let termsAccepted = null;
let marketingAccepted = null;
let orderComment = null;

 */