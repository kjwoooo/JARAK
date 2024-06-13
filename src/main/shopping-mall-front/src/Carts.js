import React, { useState, useEffect } from 'react';
import './Carts.css';
import axios from 'axios';

function Carts() {
    const [cartItems, setCartItems] = useState([]);

    useEffect(() => {
        // 카트 아이템을 서버에서 가져오는 로직 (예시)
        axios.get('/api/cart')
            .then(response => setCartItems(response.data))
            .catch(error => console.error('Error fetching cart items:', error));
    }, []);

    const handleQuantityChange = (index, delta) => {
        setCartItems(prevItems => 
            prevItems.map((item, i) => 
                i === index 
                    ? { ...item, quantity: Math.max(1, item.quantity + delta) } 
                    : item
            )
        );
    };

    const handleRemoveItem = index => {
        setCartItems(prevItems => prevItems.filter((_, i) => i !== index));
    };

    const productTotal = cartItems.reduce((total, item) => total + item.price * item.quantity, 0);
    const shipping = 2500;
    const total = productTotal + shipping;

    return (
        <div className="cart-page">
            <h1 className="cart-title">CART</h1>
            <div className="cart-container">
                {cartItems.map((item, index) => (
                    <div key={index} className="cart-item">
                        <div className="item-image">상품 이미지</div>
                        <div className="item-details">
                            <div className="item-name">{item.name}</div>
                            <div className="item-options">{item.size} / {item.color}</div>
                            <div className="item-quantity">
                                <button onClick={() => handleQuantityChange(index, -1)}>&lt;</button>
                                <span>{item.quantity}</span>
                                <button onClick={() => handleQuantityChange(index, 1)}>&gt;</button>
                            </div>
                            <div className="item-price">{item.price.toLocaleString()} 원</div>
                            <button className="item-remove" onClick={() => handleRemoveItem(index)}>x</button>
                        </div>
                    </div>
                ))}
            </div>
            <div className="order-sheet">
                <div className="order-details">
                    <div className="order-total">
                        <span>product total</span>
                        <span>{productTotal.toLocaleString()} 원</span>
                    </div>
                    <div className="shipping">
                        <span>shipping</span>
                        <span>{shipping.toLocaleString()} 원</span>
                    </div>
                    <div className="total">
                        <span>total</span>
                        <span>{total.toLocaleString()} 원</span>
                    </div>
                </div>
                <button className="order-button">주문하기</button>
            </div>
        </div>
    );
}

export default Carts;
