import React, { useContext } from 'react'
import { BasketContext } from './ShoppingHandler'
import { itemData } from './Item'

interface basketItem {
  itemData: itemData
  amount: number
}

export const GetItemBasket = () => {
  const { basket, removeFromBasket } = useContext(BasketContext)

  const [totalPrice, setTotal] = React.useState(0)

  React.useEffect(() => {
    setTotal(
      basket.reduce((acc, item) => {
        return acc + item.itemData.price * item.amount
      }, 0)
    )
  }, [basket])

  return (
    <div>
      <h1>Basket</h1>
      {basket.map((item) => (
        <div key={item.itemData.id}>
          {item.itemData.name} x {item.amount} : {item.itemData.price * item.amount} DKK
          <button onClick={() => removeFromBasket(item)}>Remove</button>
        </div>
      ))}
      <div>Total : {totalPrice} DKK</div>
    </div>
  )
}