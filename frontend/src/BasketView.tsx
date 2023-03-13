import React, {useContext} from 'react';
import {BasketContext} from './ShoppingHandler';

export const GetItemBasket = () => {
	const {basket, removeFromBasket, basketAdder, basketSubber} = useContext(BasketContext);

	const [totalPrice, setTotal] = React.useState(0);

	React.useEffect(() => {
		setTotal(
			basket.reduce((acc, item) => acc + (item.itemData.price * item.amount), 0),
		);
	}, [basket]);

	function calculateTotalPrice(totalPrice: number): {price: number, savedAmount: number} {
		let price = totalPrice;
		let discount = 1;
		let savedAmount = 0;
		basket.forEach(item => {
			if (item.amount >= 3) {
				// apply 5% discount for each item that qualifies
				discount *= 0.95 ** Math.floor(item.amount / 3);
				// display message for the item
				item.discountMessage = "You have received your 5% discount";
			} else {
				// display message for the item
				item.discountMessage = `Buy ${3 - (item.amount % 3)} more to receive a 5% discount on this item`;
			}
		});
		if (price > 300) {
			savedAmount += price * 0.1; // calculate saved amount
			discount *= 0.9; // apply 10% discount
		}
		price *= discount;
		return {price, savedAmount}; // return both price and saved amount as an object
	}


	const {price, savedAmount} = calculateTotalPrice(totalPrice); // destructure price and savedAmount from the object returned by calculateTotalPrice()

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
			<div>Total: {price} DKK</div>
			<div>Total amount saved: {savedAmount} DKK</div> {/* display saved amount */}
		</div>
	);
}
