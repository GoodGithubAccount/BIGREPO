import React, {useContext} from 'react';
import {BasketContext} from '../basket/BasketProvider';
import './Item.css';

export type Product = {
	id: string;
	name: string;
	price: number;
	currency: string;
	rebateQuantity: number;
	rebatePercent: number;
	upsellProduct: string | null;
};

export type ProductsResponse = {
	_embedded: {
		productList: Product[];
	};
	_links: {
		self: {
			href: string;
		};
	};
};

const ItemCreate = (props: Product) => {
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
