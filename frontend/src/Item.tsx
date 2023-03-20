import React, {useContext} from 'react';
import {BasketContext} from './ShoppingHandler';
import './Item.css';

export type ItemData = {
	id: number;
	name: string;
	price: number;
	currency: string;
	rebateQuantity: number;
	rebatePercent: number;
	upsellProduct: string;
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
