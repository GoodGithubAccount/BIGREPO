import React, {useContext} from 'react';
import {BasketContext} from './ShoppingHandler';
import './Item.css';

export type ItemData = {
	name: string;
	id: number;
	price: number;
	rebateQuantity: number;
	rebatePercent: number;
};

const ItemCreate = (props: ItemData) => {
	const {addToBasket} = useContext(BasketContext);

	return (
		<div className='item'>
			<h3>{props.name}</h3>

			<p className='price'>Price: {props.price}
				<br/> Buy: {props.rebateQuantity} get {props.rebatePercent}% off! </p>

			<button className='itemButton' onClick={() => {
				addToBasket(props);
			}}>
				ADD ITEM
			</button>
		</div>
	);
};

export default ItemCreate;
