import React, { useEffect, useState } from "react";

// Define the Product interface to describe the shape of our product data
interface Product {
    id: string;
    name: string;
    price: number;
    currency: string;
    rebateQuantity: number;
    rebatePercent: number;
    upsellProductId: string | null;
}

// Define the Checkout component as a functional component
const Checkout: React.FC = () => {
    // Define a state variable to hold the product data
    const [product, setProduct] = useState<Product | null>(null);

    // Use the useEffect hook to fetch the product data from the server
    useEffect(() => {
        fetch("product.json")
            .then((response) => response.json())
            .then((data: Product) => {
                setProduct(data);
            });
    }, []);

    // Render the product data, or a loading message if the data is still being fetched
    return (
        <div>
            {product ? (
                <div>
                    <h2>{product.name}</h2>
                    <p>
                        Price: {product.price} {product.currency}
                    </p>
                    <p>
                        Rebate: {product.rebatePercent}% off when you buy {product.rebateQuantity} or more
                    </p>
                </div>
            ) : (
                <p>Loading product data...</p>
            )}
        </div>
    );
};

export default Checkout;
