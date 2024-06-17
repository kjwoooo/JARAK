import { useState } from "react";
import { useParams, useLocation, useNavigate } from "react-router-dom";
import { Nav, Button, DropdownButton, Dropdown } from 'react-bootstrap/';
import useUserStore from '../stores/useUserStore.js';
import './Detail.css';

function Detail() {
  const { state } = useLocation();
  const { item } = state || {};
  const { itemId } = useParams();
  const navigate = useNavigate();
  const user = useUserStore(state => state.user); 

  const [modal, setModal] = useState('detail');

  if (!item) {
    return <div>상품 정보를 불러오는 중입니다...</div>;
  }

  const handleBuyNow = () => {
    if (user) {
      const cartKey = `cart_${user.id}`;
      const storedCartItems = JSON.parse(localStorage.getItem(cartKey)) || [];
      const existingItemIndex = storedCartItems.findIndex(cartItem => cartItem.id === item.id);

      if (storedCartItems.length > 0) {
        if (existingItemIndex >= 0) {
          const confirmAddToCart = window.confirm("이미 장바구니에 담은 상품입니다 추가로 구매하시겠습니까?");
          if (confirmAddToCart) {
            storedCartItems[existingItemIndex].quantity += 1;
            localStorage.setItem(cartKey, JSON.stringify(storedCartItems));
          } else {
            const confirmGoToCart = window.confirm("취소를 누르면 장바구니로 이동하시겠습니까?");
            if (confirmGoToCart) {
              navigate('/cart');
              return;
            }
          }
        } else {
          const confirmAddToCart = window.confirm("장바구니에 있는 상품을 함께 구매하시겠습니까?");
          if (confirmAddToCart) {
            storedCartItems.push({ ...item, quantity: 1 });
            localStorage.setItem(cartKey, JSON.stringify(storedCartItems));
            navigate('/order', { state: { cartItems: storedCartItems, productTotal: calculateTotal(storedCartItems), shipping: 2500, total: calculateTotal(storedCartItems) + 2500 } });
            return;
          } else {
            navigate('/order', { state: { cartItems: [{ ...item, quantity: 1 }], productTotal: item.price, shipping: 2500, total: item.price + 2500 } });
            return;
          }
        }
      } else {
        navigate('/order', { state: { cartItems: [{ ...item, quantity: 1 }], productTotal: item.price, shipping: 2500, total: item.price + 2500 } });
      }
    } else {
      window.alert("지금 당장 로그인하고 쌈@뽕하게 주문하세요!");
    }
  };

  const handleAddToCart = () => {
    if (user) {
      const cartKey = `cart_${user.id}`;
      const storedCartItems = JSON.parse(localStorage.getItem(cartKey)) || [];
      const existingItemIndex = storedCartItems.findIndex(cartItem => cartItem.id === item.id);

      if (existingItemIndex >= 0) {
        const confirmIncreaseQuantity = window.confirm("이미 장바구니에 있는 상품 입니다. 추가로 담으시겠습니까?");
        if (confirmIncreaseQuantity) {
          storedCartItems[existingItemIndex].quantity += 1;
        }
      } else {
        storedCartItems.push({ ...item, quantity: 1 });
      }

      localStorage.setItem(cartKey, JSON.stringify(storedCartItems));
      window.alert("장바구니에 추가되었습니다.");
    } else {
      window.alert("지금 당장 쌈@뽕하게 로그인하고 장바구니에 담으세요!");
    }
  };

  const calculateTotal = (items) => {
    return items.reduce((total, item) => total + item.price * item.quantity, 0);
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

          <DropdownButton id="size-dropdown" title="사이즈">
            <Dropdown.Item>Small</Dropdown.Item>
            <Dropdown.Item>Medium</Dropdown.Item>
            <Dropdown.Item>Large</Dropdown.Item>
          </DropdownButton>
          <DropdownButton id="color-dropdown" title="색상">
            <Dropdown.Item>Red</Dropdown.Item>
            <Dropdown.Item>Blue</Dropdown.Item>
            <Dropdown.Item>Green</Dropdown.Item>
          </DropdownButton>
          <p className="total-price">총 상품 금액 <span>0</span></p>
          <div className="buttons">
            <Button variant="outline-dark" className="add-to-cart" onClick={handleAddToCart}>Add to cart</Button>
            <Button variant="outline-dark" className="buy-now" onClick={handleBuyNow}>Buy now</Button>
          </div>
        </div>
      </div>
      <Nav justify variant="tabs" defaultActiveKey="detail">
        <Nav.Item>
          <Nav.Link eventKey="detail" onClick={() => setModal('detail')}>상품 상세</Nav.Link>
        </Nav.Item>
        <Nav.Item>
          <Nav.Link eventKey="review" onClick={() => setModal('review')}>리뷰</Nav.Link>
        </Nav.Item>
        <Nav.Item>
          <Nav.Link eventKey="qna" onClick={() => setModal('qna')}>문의사항</Nav.Link>
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
        <div>상세페이지 모달</div>
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
