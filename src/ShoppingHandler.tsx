import React from 'react'
import { itemData } from './Item'

let i = 0

interface basketItem {
    itemData: itemData,
    amount: number
}

var basket: basketItem[] = []

function AddItemToBasket(props: itemData){
    var item: basketItem = { itemData: props, amount: 1 };

    var itemExists: boolean = false

    basket.forEach( (element) => {
        if(element.itemData.id == item.itemData.id){
            element.amount = element.amount + 1;
            itemExists = true
        }
    });

    if(!itemExists){
        basket.push(item)
    }

    console.log(basket)
}

export function GetItemBasket(): basketItem[] { return basket }

export default AddItemToBasket

export type { basketItem }