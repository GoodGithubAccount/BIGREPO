import React from 'react'
import ItemCreate from './Item'

function ItemView() {
    const itemList = [
        { name: "Big Spoon", price: 5, id: 0 },
        { name: "Big Toe", price: 523, id: 1 },
        { name: "Big AY", price: 745, id: 2 },
        { name: "Big Nay", price: 52323, id: 3 },
        { name: "COMPUTER", price: 235, id: 4 },
        { name: "Teaspoon", price: 123, id: 5 }
      ];

    let divT = document.createElement("div");

    for (let i = 0; i < 3; i++) {
        let p = document.createElement("p");
        divT.append("TEST", p)
    }

    return (
        <div>
            {itemList.map((item) => (
            <ItemCreate
                name= {item.name}
                price = {item.price}
                id = {item.id}
            />
            ))}
        </div>
    );

}

export default ItemView