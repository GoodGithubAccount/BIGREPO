import React, { useContext } from 'react'
import { BasketContext } from './ShoppingHandler'
import { itemData } from './Item'

interface basketItem {
  itemData: itemData
  amount: number
}

export const GetItemBasket = () => {
  const { basket, removeFromBasket } = useContext(BasketContext)

  return (
    <div>
      <h1>Basket</h1>
      {basket.map((item) => (
        <div key={item.itemData.id}>
          {item.itemData.name} x {item.amount}
          <button onClick={() => removeFromBasket(item)}>Remove</button>
        </div>
      ))}
    </div>
  )
}