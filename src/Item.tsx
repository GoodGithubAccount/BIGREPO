import React, { useContext } from 'react'
import { BasketContext } from './ShoppingHandler'

export interface itemData {
  name: string
  id: number
  price: number
}

const ItemCreate = (props: itemData) => {
  const { addToBasket } = useContext(BasketContext)

  return (
    <div className="item">
      <h3>{props.name}</h3>
      <p className="price">Price: {props.price}</p>
      <button className="itemButton" onClick={() => addToBasket(props)}>
        ADD ITEM
      </button>
    </div>
  )
}

export default ItemCreate