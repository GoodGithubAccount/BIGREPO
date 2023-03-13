import React, {useContext} from 'react';
import ItemCreate from './Item';

function ItemView() {
	const itemList = [
		{name: 'Cool Glasses', price: 10, id: 7, rebateQuantity: 0, rebatePercent: 0},
		{name: 'Big Spoon', price: 5, id: 1, rebateQuantity: 2, rebatePercent: 10},
		{name: 'Big Toe', price: 100, id: 2, rebateQuantity: 2, rebatePercent: 15},
		{name: 'Big AY', price: 72, id: 3, rebateQuantity: 3, rebatePercent: 20},
		{name: 'Big Nay', price: 23, id: 4, rebateQuantity: 4, rebatePercent: 80},
		{name: 'COMPUTER', price: 235, id: 5, rebateQuantity: 5, rebatePercent: 1},
		{name: 'Teaspoon', price: 1, id: 6, rebateQuantity: 7, rebatePercent: 50},
	];

	return (
		<div className='itemVIEW'>
			<h1> ITEMS </h1>
			{itemList.map(item => (
				<ItemCreate
					key={item.id}
					name={item.name}
					price={item.price}
					id={item.id}
					rebateQuantity={item.rebateQuantity}
					rebatePercent={item.rebatePercent}

				/>
			))}
		</div>
	);
}

export default ItemView;
