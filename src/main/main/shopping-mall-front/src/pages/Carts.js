import React, { useState, useEffect } from 'react';
import './Carts.css';
import useUserStore from '../stores/useUserStore.js';
import { useNavigate } from 'react-router-dom';

function Carts() {
    const [cartItems, setCartItems] = useState([]);
    const user = useUserStore(state => state.user);
    const navigate = useNavigate();

    useEffect(() => {
        if (user) {
            const cartKey = `cart_${user.id}`;
            const storedCartItems = JSON.parse(localStorage.getItem(cartKey)) || [];
            setCartItems(storedCartItems);
        }
    }, [user]);

    const handleQuantityChange = (itemIndex, optionIndex, delta) => {
        const updatedCartItems = cartItems.map((item, i) => {
            if (i === itemIndex) {
                const updatedOptions = item.options.map((option, j) => 
                    j === optionIndex 
                        ? { ...option, quantity: Math.max(1, option.quantity + delta) } 
                        : option
                );
                return { ...item, options: updatedOptions };
            }
            return item;
        });
        setCartItems(updatedCartItems);
        localStorage.setItem(`cart_${user.id}`, JSON.stringify(updatedCartItems));
    };

    const handleRemoveItem = (itemIndex, optionIndex) => {
        const updatedCartItems = cartItems.map((item, i) => {
            if (i === itemIndex) {
                const updatedOptions = item.options.filter((_, j) => j !== optionIndex);
                return { ...item, options: updatedOptions };
            }
            return item;
        }).filter(item => item.options.length > 0);
        setCartItems(updatedCartItems);
        localStorage.setItem(`cart_${user.id}`, JSON.stringify(updatedCartItems));
    };

    const calculateItemTotal = (item) => {
        return item.options.reduce((total, option) => total + (item.price * option.quantity), 0);
    };

    const productTotal = cartItems.reduce((total, item) => total + calculateItemTotal(item), 0);
    const SHIPPING = 2500;
    const total = productTotal + SHIPPING;

    const handleOrder = () => {
        navigate('/orders', { state: { cartItems, productTotal, shipping: SHIPPING, total } });
    };

    return (
        <div className="cart-page">
            <h1 className="cart-title">CART</h1>
            <div className="cart-container">
                {cartItems.map((item, itemIndex) => (
                    item.options.map((option, optionIndex) => (
                        <div key={`${itemIndex}-${optionIndex}`} className="cart-item">
                            <div className="item-details">
                                <div className="item-info">
                                    <div className="item-name">
                                        {item.itemName} / {option.size} / {option.color}
                                    </div>
                                </div>
                                <div className="item-quantity">
                                    <button onClick={() => handleQuantityChange(itemIndex, optionIndex, -1)}>&lt;</button>
                                    <span>{option.quantity}</span>
                                    <button onClick={() => handleQuantityChange(itemIndex, optionIndex, 1)}>&gt;</button>
                                </div>
                                <div className="item-price">{(item.price * option.quantity).toLocaleString()} 원</div>
                                <button className="item-remove" onClick={() => handleRemoveItem(itemIndex, optionIndex)}>x</button>
                            </div>
                        </div>
                    ))
                ))}
            </div>
            <div className="order-sheet">
                <div className="order-details">
                    <div className="order-total">
                        <span>총 주문금액</span>
                        <span>{productTotal.toLocaleString()} 원</span>
                    </div>
                    <div className="shipping">
                        <span>배송비</span>
                        <span>{SHIPPING.toLocaleString()} 원</span>
                    </div>
                    <div className="total">
                        <span>합계</span>
                        <span>{total.toLocaleString()} 원</span>
                    </div>
                </div>
                <button className="order-button" onClick={handleOrder}>주문하기</button>
            </div>
        </div>
    );
}

export default Carts;
