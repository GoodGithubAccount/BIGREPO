import React from 'react'
import ItemCreate from './Item'
import { GetItemBasket, basketItem } from './ShoppingHandler';
import { useState } from 'react'

function BasketView() {
    const [, updateState] = React.useState();
    const forceUpdate = React.useCallback(() => updateState({}), []);

    var basket: basketItem[] = GetItemBasket()

    return (
        <div>
            <h1> BASKET </h1>

            { basket.map((item) => (
            <ItemCreate
                name= {item.itemData.name}
                price = {item.itemData.price}
                id = {item.itemData.id}
            />
            ))}
            
            <button onClick={forceUpdate}>Force basket update</button>
        </div>
        
    );

}

export default BasketView