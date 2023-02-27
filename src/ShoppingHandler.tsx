import React, { createContext, useState } from 'react'
import ItemCreate, { itemData } from './Item'
import { GetItemBasket } from './BasketView'
import ItemView from './ItemView'

interface basketItem {
  itemData: itemData,
  amount: number
}

export const BasketContext = createContext({
  basket: [] as basketItem[],
  addToBasket: (item: itemData) => {},
  removeFromBasket: (item: basketItem) => {}
})

const ShoppingHandler = () => {
  const [basket, setBasket] = useState<basketItem[]>([])

  const addToBasket = (item: itemData) => {
    const itemExists = basket.some((element) => element.itemData.id === item.id)

    if (itemExists) {
      setBasket(basket.map((element) => {
        if (element.itemData.id === item.id) {
          return {
            ...element,
            amount: element.amount + 1
          }
        }
        return element
      }))
    } else {
      setBasket([...basket, { itemData: item, amount: 1 }])
    }
  }

  const removeFromBasket = (item: basketItem) => {
    setBasket(basket.filter((element) => element.itemData.id !== item.itemData.id))
  }

  return (
    <BasketContext.Provider value={{ basket, addToBasket, removeFromBasket }}>
      <ItemView />
      <GetItemBasket />
    </BasketContext.Provider>
  )
}

export default ShoppingHandler