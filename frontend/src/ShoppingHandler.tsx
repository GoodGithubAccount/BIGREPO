import React, {createContext, useState} from 'react';
import ItemCreate, {type ItemData} from './Item';
import {GetItemBasket} from './BasketView';
import ItemView from './ItemView';

type BasketItem = {
	itemData: ItemData;
	amount: number;
};

export const BasketContext = createContext({
	basket: [] as BasketItem[],
	addToBasket(item: ItemData) {
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

const ShoppingHandler = () => {
	const [basket, setBasket] = useState<BasketItem[]>([]);

	const addToBasket = (item: ItemData) => {
		const itemExists = basket.some(element => element.itemData.id === item.id);

		if (itemExists) {
			setBasket(basket.map(element => {
				if (element.itemData.id === item.id) {
					return {
						...element,
						amount: element.amount + 1,
					};
				}

				return element;
			}));
		} else {
			setBasket([...basket, {itemData: item, amount: 1}]);
		}
	};

	const removeFromBasket = (item: BasketItem) => {
		setBasket(basket.filter(element => element.itemData.id !== item.itemData.id));
	};

	const basketAdder = (item: BasketItem) => {
		setBasket(basket.filter(element => element.itemData.id, item.amount += 1));
	};

	const basketSubber = (item: BasketItem) => {
		if (item.amount >= 1) {
			setBasket(basket.filter(element => element.itemData.id, item.amount -= 1));
		}
	};

	return (
		<BasketContext.Provider value={{basket, addToBasket, removeFromBasket, basketAdder, basketSubber}}>
			<div className='grid-container'>

				<div className='items'>
					<ItemView/>
				</div>

				<div className='basket'>
					<GetItemBasket/>
				</div>

			</div>

		</BasketContext.Provider>
	);
};

export default ShoppingHandler;
