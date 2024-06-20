import { useState, useEffect } from 'react';
import { Col } from 'react-bootstrap/';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { apiInstance } from '../util/api';

function Products() {
  const [items, setItems] = useState([]);
  const [displayItems, setDisplayItems] = useState([]);
  const [itemsOffset, setItemsOffset] = useState(0);
  const itemsPerPage = 9;
  const navigate = useNavigate();
  const { categoryId } = useParams();

  useEffect(() => {
    apiInstance.get('/items')
      .then((result) => {
        console.log(result.data);
        setItems(result.data);
        setDisplayItems(result.data.slice(0, itemsPerPage));
      })
      .catch((error) => {
        console.error("상품데이터 못가져왔음요: ", error);
      });
  }, []);

  useEffect(() => {
    if (categoryId) {
      const filteredItems = items.filter(item => item.categoryId === parseInt(categoryId));
      setDisplayItems(filteredItems.slice(0, itemsPerPage));
      setItemsOffset(0);
    } else {
      setDisplayItems(items.slice(0, itemsPerPage));
    }
  }, [categoryId, items]);

  const loadMoreItems = () => {
    const newOffset = itemsOffset + itemsPerPage;
    setItemsOffset(newOffset);
    setDisplayItems(displayItems.concat(items.slice(newOffset, newOffset + itemsPerPage)));
  };

  const chunkedItems = (items, size) => {
    const result = [];
    for (let i = 0; i < items.length; i += size) {
      result.push(items.slice(i, i + size));
    }
    return result;
  };

  const itemsInRows = Array.isArray(displayItems) ? chunkedItems(displayItems, 3) : [];

  const getMainImageSrc = (itemImageDTOs) => {
    const mainImage = itemImageDTOs.find(image => image.isMain);
    return mainImage ? mainImage.filePath : '';
  };

  return (
    <div className="Products">
      {Array.isArray(itemsInRows) && itemsInRows.map((row, rowIndex) => (
        <div className="row" key={rowIndex}>
          {Array.isArray(row) && row.map((item, index) => (
            <Col xs key={index}>
              <img src={getMainImageSrc(item.itemImageDTOs)} width="150px" height="150px" alt={item.itemName}></img>
              <Link style={{ textDecoration: "none" }} to={`/detail/${item.id}`} state={{ item }}>
                <h4>{item.itemName}</h4>
              </Link>
              <p style={{ color: "black" }}>{item.content}</p>
              <p>가격 : {item.price}</p>
            </Col>
          ))}
        </div>
      ))}
      {displayItems.length < items.length && (
        <button onClick={loadMoreItems}>상품 더보기</button>
      )}
    </div>
  );
}

export default Products;
