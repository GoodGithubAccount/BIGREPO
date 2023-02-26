import React from 'react'
import { useState } from 'react'
import './Item.css'
import AddItemToBasket from './ShoppingHandler'


interface itemData {
    name: string;
    id: number;
    price: number;
}

function ItemCreate(props: itemData) {
    return (
        <div className="item">
          <h3>{props.name}</h3>
          <p className = "price"> Price: {props.price} </p>
            <button className= "itemButton" onClick={() => AddItemToBasket(props)}>
            ADD ITEM
            </button>
          </div>
      )
}

export default ItemCreate
export type { itemData }