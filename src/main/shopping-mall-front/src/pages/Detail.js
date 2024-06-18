import React, { useState, useEffect } from 'react';
import { Button, Modal, Form, Table, Nav, DropdownButton, Dropdown } from 'react-bootstrap';
import { useParams, useLocation, useNavigate } from 'react-router-dom';
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
        // handle buy now logic
    };

    const handleAddToCart = () => {
        // handle add to cart logic
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
        newOptions[index].color = null; // Reset color when size changes
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