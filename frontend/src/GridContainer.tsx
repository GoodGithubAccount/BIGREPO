import React from 'react';
import ItemView from './products/ItemView';
import {GetItemBasket} from './basket/BasketView';

export const GridContainer: React.FC = () => {
	return (
		<div className='grid-container'>
			<div className='items'>
				<ItemView/>
			</div>
			<div className='basket'>
				<GetItemBasket/>
			</div>
		</div>
	);
};
