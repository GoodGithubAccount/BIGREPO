import React, {useContext} from 'react';
import {BasketContext} from './BasketProvider';
import {OrderForm} from './OrderForm';
import {BasketItem} from './BasketItem';

export const GetItemBasket = () => {
	const {basket, removeFromBasket, basketAdder, basketSubber} = useContext(BasketContext);

	const [totalPrice, setTotal] = React.useState(0);

	const [totalRebate, setRebate] = React.useState(0);

	React.useEffect(() => {
		setTotal(
			basket.reduce((acc, item) => acc + (item.products.price * item.amount), 0),
		);
		setRebate(
			basket.reduce((acc, item) => acc + findTotal(item), 0),
		);
	}, [basket]);

	return (
		<div>
			<h1>Basket</h1>
			{basket.map(item => (
				<BasketItem
					key={item.products.id}
					item={item}
					basketAdder={basketAdder}
					basketSubber={basketSubber}
					removeFromBasket={removeFromBasket}
				/>
			))}

			<div>
				Total: {totalPrice} DKK
				<br/>Your Total : {toFixedIfNecessary(totalRebate, 2)} DKK
				<br/>Money saved: {toFixedIfNecessary(totalPrice - totalRebate, 2)} DKK
			</div>

			<OrderForm basket={basket} />
		</div>
	);
};

// Function finds rebate if any and calculates new total with rebate
function findTotal(item: any) {
	let total;
	// Calculate total price with rebate
	if (item.amount >= item.products.rebateQuantity) {
		total = item.products.price * item.amount * (1 - (item.products.rebatePercent / 100));
	} else {
		total = item.products.price * item.amount;
	}

	return total;
}

// To get two decimals when showing values from: https://stackoverflow.com/questions/32229667/have-max-2-decimal-places
function toFixedIfNecessary(value: any, dp: number) {
	return Number(parseFloat(value).toFixed(dp));
}
