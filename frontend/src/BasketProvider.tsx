import React, {createContext, type ReactNode, useState} from 'react';
import {type Product} from './Item';

export type BasketItem = {
	products: Product;
	amount: number;
};

export const BasketContext = createContext({
	basket: [] as BasketItem[],
	addToBasket(item: Product) {
		// Ding
	},
	removeFromBasket(item: BasketItem) {
		// Dong
	},
	basketAdder(item: BasketItem) {
		// Deng
	},
	basketSubber(item: BasketItem) {
		// Dang
	},
});

type BasketProviderProps = {
	children: ReactNode;
};
export const BasketProvider: React.FC<BasketProviderProps> = ({children}) => {
	const [basket, setBasket] = useState<BasketItem[]>([]);

	const addToBasket = (item: Product) => {
		const itemExists = basket.some(element => element.products.id === item.id);

		if (itemExists) {
			setBasket(basket.map(element => {
				if (element.products.id === item.id) {
					return {
						...element,
						amount: element.amount + 1,
					};
				}

				return element;
			}));
		} else {
			setBasket([...basket, {products: item, amount: 1}]);
		}
	};

	const removeFromBasket = (item: BasketItem) => {
		setBasket(basket.filter(element => element.products.id !== item.products.id));
	};

	const basketAdder = (item: BasketItem) => {
		setBasket(basket.filter(element => element.products.id, item.amount += 1));
	};

	const basketSubber = (item: BasketItem) => {
		if (item.amount >= 1) {
			setBasket(basket.filter(element => element.products.id, item.amount -= 1));
		}
	};

	return (
		<BasketContext.Provider
			value={{
				basket,
				addToBasket,
				removeFromBasket,
				basketAdder,
				basketSubber,
			}}
		>
			{children}
		</BasketContext.Provider>
	);
};
