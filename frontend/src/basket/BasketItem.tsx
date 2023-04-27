import React from 'react';

type BasketItemProps = {
	item: any;
	basketAdder: (item: any) => void;
	basketSubber: (item: any) => void;
	removeFromBasket: (item: any) => void;
};

export const BasketItem: React.FC<BasketItemProps> = ({
	item,
	basketAdder,
	basketSubber,
	removeFromBasket,
}) => (
	<div key={item.products.id}>
		{item.products.name} x {item.amount} : {item.products.price * item.amount} DKK

		<button onClick={() => basketAdder(item)}>
			+
		</button>

		<button onClick={() => basketSubber(item)}>
			-
		</button>

		<button onClick={() => removeFromBasket(item)}>
			Remove
		</button>
	</div>
);
