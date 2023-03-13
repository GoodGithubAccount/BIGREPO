import React, {useContext} from 'react';
import {BasketContext} from './ShoppingHandler';

export const GetItemBasket = () => {
	const {basket, removeFromBasket} = useContext(BasketContext);

	const [totalPrice, setTotal] = React.useState(0);

	React.useEffect(() => {
		setTotal(
			basket.reduce((acc, item) => acc + (item.itemData.price * item.amount), 0),
		);
	}, [basket]);
	function calculateTotalPrice(): number {
		let price = totalPrice;
		if (price > 300) {
			price *= 0.9;
		}
		return price;
	}
	return (
		<div>
			<h1>Basket</h1>
			{basket.map(item => (
				<div key={item.itemData.id}>
					{item.itemData.name} x {item.amount} : {item.itemData.price * item.amount} DKK
					<button onClick={() => {
						removeFromBasket(item);
					}}>Remove</button>
				</div>
			))}
			<div>Total: {calculateTotalPrice()} DKK</div>
		</div>
	);
};
