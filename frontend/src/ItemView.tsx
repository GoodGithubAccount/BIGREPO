import React, {useContext, useEffect, useState} from 'react';
import ItemCreate, {type Product, type ProductsResponse} from './Item';

const ItemView = () => {
	const [products, setProducts] = useState<Product[]>([]);

	useEffect(() => {
		const fetchProducts = async () => {
			const productsUrl = 'http://localhost:8080/products';
			const response = await fetch(productsUrl);
			const data: ProductsResponse = await response.json();
			setProducts(data._embedded.productList);
		};

		fetchProducts();
	}, []);

	return (
		<div className='itemVIEW'>
			<h1> ITEMS </h1>
			{products.map(item => (
				<ItemCreate
					key={item.id}
					id={item.id}
					name={item.name}
					price={item.price}
					currency={item.currency}
					rebateQuantity={item.rebateQuantity}
					rebatePercent={item.rebatePercent}
					upsellProduct={item.upsellProduct}
				/>
			))}
		</div>
	);
};

export default ItemView;
