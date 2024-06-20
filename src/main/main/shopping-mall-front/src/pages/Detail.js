import React, { useState, useEffect } from 'react';
import { Button, Modal, Form, Table, Nav, DropdownButton, Dropdown } from 'react-bootstrap';
import { useParams, useLocation, useNavigate } from 'react-router-dom';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import useUserStore from '../stores/useUserStore.js';
import './Detail.css';

function Detail() {
    const { state } = useLocation();
    const { item } = state || {};
    const { itemId } = useParams();
    const navigate = useNavigate();
    const user = useUserStore(state => state.user);

    const [selectedOptions, setSelectedOptions] = useState([]);
    const [modal, setModal] = useState('detail');

    if (!item) {
        return <div>상품 정보를 불러오는 중입니다...</div>;
    }

    const handleBuyNow = () => {
        if (user) {
            const cartKey = `cart_${user.id}`;
            const storedCartItems = JSON.parse(localStorage.getItem(cartKey)) || [];
            const duplicateItem = storedCartItems.find(storedItem => storedItem.itemId === item.itemId && compareOptions(storedItem.options, selectedOptions));

            const navigateToOrders = () => {
                const orderItem = {
                    itemId: item.itemId,
                    itemName: item.itemName,
                    price: item.price,
                    options: selectedOptions,
                    quantity: selectedOptions.reduce((acc, option) => acc + option.quantity, 0)
                };
                navigate('/orders', {
                    state: {
                        cartItems: [orderItem],
                        productTotal: orderItem.price * orderItem.quantity,
                        shipping: 2500,
                        total: orderItem.price * orderItem.quantity + 2500
                    }
                });
            };

            if (duplicateItem) {
                if (window.confirm("장바구니에 이미 추가된 상품입니다. 장바구니로 이동하시겠습니까?")) {
                    navigate('/carts');
                } else {
                    navigateToOrders();
                }
            } else {
                navigateToOrders();
            }
        }
    };

    const handleAddToCart = () => {
        if (user) {
            if (selectedOptions.some(option => !option.size || !option.color)) {
                toast.error("옵션을 먼저 선택해주세요");
                return;
            }
            
            const cartKey = `cart_${user.id}`;
            const storedCartItems = JSON.parse(localStorage.getItem(cartKey)) || [];
            const duplicateItem = storedCartItems.find(storedItem => storedItem.itemId === item.itemId && compareOptions(storedItem.options, selectedOptions));

            if (duplicateItem) {
                if (window.confirm("이미 장바구니에 추가된 상품입니다. 추가로 담으시겠습니까?")) {
                    const updatedCartItems = storedCartItems.map(storedItem => 
                        storedItem.itemId === item.itemId && compareOptions(storedItem.options, selectedOptions)
                            ? { ...storedItem, quantity: storedItem.quantity + selectedOptions.reduce((acc, option) => acc + option.quantity, 0) }
                            : storedItem
                    );
                    localStorage.setItem(cartKey, JSON.stringify(updatedCartItems));
                    setSelectedOptions([]);
                    toast.success("장바구니에 추가되었습니다!");
                }
            } else {
                const newCartItem = {
                    itemId: item.itemId,
                    itemName: item.itemName,
                    price: item.price,
                    options: selectedOptions,
                    quantity: selectedOptions.reduce((acc, option) => acc + option.quantity, 0)
                };
                const updatedCartItems = [...storedCartItems, newCartItem];
                localStorage.setItem(cartKey, JSON.stringify(updatedCartItems));
                setSelectedOptions([]);
                toast.success("장바구니에 추가되었습니다!");
            }
        }
    };

    const compareOptions = (options1, options2) => {
        if (options1.length !== options2.length) return false;
        return options1.every((opt1, index) => 
            opt1.size === options2[index].size && 
            opt1.color === options2[index].color
        );
    };

    const availableSizes = Array.from(new Set(item.itemDetailDTOs.map(detail => detail.size)));

    const getAvailableColors = (size) => {
        return item.itemDetailDTOs.filter(detail => detail.size === size).map(detail => detail.color);
    };

    const handleOptionAdd = () => {
        setSelectedOptions([...selectedOptions, { size: null, color: null, quantity: 1, stockQuantity: null }]);
    };

    const handleSizeChange = (index, size) => {
        const newOptions = [...selectedOptions];
        newOptions[index].size = size;
        newOptions[index].color = null;
        updateStockQuantity(newOptions, index, size, newOptions[index].color);
        setSelectedOptions(newOptions);
    };

    const handleColorChange = (index, color) => {
        const newOptions = [...selectedOptions];
        newOptions[index].color = color;
        updateStockQuantity(newOptions, index, newOptions[index].size, color);
        setSelectedOptions(newOptions);
    };

    const updateStockQuantity = (options, index, size, color) => {
        const selectedDetail = item.itemDetailDTOs.find(detail => detail.size === size && detail.color === color);
        if (selectedDetail) {
            options[index].stockQuantity = selectedDetail.quantity;
            options[index].quantity = 1;
        } else {
            options[index].stockQuantity = null;
        }
    };

    const handleDecreaseQuantity = (index) => {
        const newOptions = [...selectedOptions];
        if (newOptions[index].quantity > 1) {
            newOptions[index].quantity -= 1;
        }
        setSelectedOptions(newOptions);
    };

    const handleIncreaseQuantity = (index) => {
        const newOptions = [...selectedOptions];
        if (newOptions[index].stockQuantity && newOptions[index].quantity < newOptions[index].stockQuantity) {
            newOptions[index].quantity += 1;
        }
        setSelectedOptions(newOptions);
    };

    const handleOptionRemove = (index) => {
        const newOptions = [...selectedOptions];
        newOptions.splice(index, 1);
        setSelectedOptions(newOptions);
    };

    return (
        <div className="container detail-container">
            <ToastContainer />
            <div className="row">
                <div className="col-md-6 detail-image">
                    <div className="image-placeholder"><img src={item.image} width="100%" alt={item.itemName} /></div>
                </div>
                <div className="col-md-6 detail-info">
                    <h2>{item.itemName}</h2>
                    <p className="price">{item.price.toLocaleString()} 원</p>
                    <Button variant="outline-secondary" onClick={handleOptionAdd}>옵션 추가</Button>

                    {Array.isArray(selectedOptions) && selectedOptions.map((option, index) => (
                        <div key={index} className="option-selector">
                            <div className="dropdown-container">
                                <DropdownButton id={`size-dropdown-${index}`} title={option.size || "사이즈"} className="dropdown-button">
                                    {availableSizes.map((size, idx) => (
                                        <Dropdown.Item key={idx} onClick={() => handleSizeChange(index, size)}>{size}</Dropdown.Item>
                                    ))}
                                </DropdownButton>

                                <DropdownButton id={`color-dropdown-${index}`} title={option.color || "색상"} className="dropdown-button">
                                    {option.size && getAvailableColors(option.size).map((color, idx) => (
                                        <Dropdown.Item key={idx} onClick={() => handleColorChange(index, color)}>{color}</Dropdown.Item>
                                    ))}
                                </DropdownButton>
                            </div>

                            {option.size && option.color && (
                                <>
                                    <p>현재 재고: {option.stockQuantity}</p>
                                    <p>{item.itemName} / {option.size} / {option.color}</p>
                                    <div className="quantity-selector">
                                        <Button variant="outline-secondary" onClick={() => handleDecreaseQuantity(index)}>{"<"}</Button>
                                        <span className="quantity">{option.quantity}</span>
                                        <Button variant="outline-secondary" onClick={() => handleIncreaseQuantity(index)}>{">"}</Button>
                                    </div>
                                    <Button variant="danger" className="remove-option" onClick={() => handleOptionRemove(index)}>x</Button>
                                </>
                            )}
                        </div>
                    ))}

                    <p className="total-price">총 상품 금액 {selectedOptions.reduce((acc, option) => acc + (item.price * option.quantity), 0).toLocaleString()} 원</p>

                    <div className="buttons">
                        <Button variant="outline-dark" className="add-to-cart" onClick={handleAddToCart}>Add to cart</Button>
                        <Button variant="outline-dark" className="buy-now" onClick={handleBuyNow}>Buy now</Button>
                    </div>
                </div>
            </div>
            <Nav justify variant="tabs" defaultActiveKey="detail">
                <Nav.Item>
                    <Nav.Link eventKey="detail" onClick={() => setModal('detail')}>DETAIL</Nav.Link>
                </Nav.Item>
                <Nav.Item>
                    <Nav.Link eventKey="review" onClick={() => setModal('review')}>REVIEW</Nav.Link>
                </Nav.Item>
                <Nav.Item>
                    <Nav.Link eventKey="qna" onClick={() => setModal('qna')}>Q&A</Nav.Link>
                </Nav.Item>
            </Nav>
            {modal === 'detail' && <DetailModal />}
            {modal === 'review' && <ReviewModal />}
            {modal === 'qna' && <QnaModal />}
        </div>
    );

    function DetailModal() {
        return (
            <div className="DetailModal">
                <div>상품 사진 및 설명</div>
                <p>{item.itemName}</p>
            </div>
        );
    }

    function ReviewModal() {
        return (
            <div className="ReviewModal">
                <div>리뷰페이지 모달</div>
            </div>
        );
    }

    function QnaModal() {
        return (
            <div className="QnaModal">
                <div>QnA 모달</div>
            </div>
        );
    }
}

export default Detail;
