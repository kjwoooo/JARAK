import React, { useState, useEffect } from 'react';
import { Button, Modal, Form, Table } from 'react-bootstrap';
import axios from 'axios';
import './AdminItemPage.css';

function AdminItemPage() {
    const [products, setProducts] = useState([]);
    const [categories, setCategories] = useState([]);
    const [subCategories, setSubCategories] = useState([]);
    const [showAddModal, setShowAddModal] = useState(false);
    const [newProductName, setNewProductName] = useState('');
    const [newProductPrice, setNewProductPrice] = useState('');
    const [selectedMainCategory, setSelectedMainCategory] = useState(null);
    const [selectedSubCategory, setSelectedSubCategory] = useState(null);
    const [mainFile, setMainFile] = useState(null);
    const [subFile, setSubFile] = useState(null);

    useEffect(() => {
        fetchProducts();
        fetchCategories();
    }, []);

    const fetchProducts = async () => {
        try {
            const response = await axios.get('/items');
            setProducts(response.data);
        } catch (error) {
            console.error("상품 불러오기 실패:", error);
        }
    };

    const fetchCategories = async () => {
        try {
            const response = await axios.get('/categories');
            const categoryData = response.data;
            setCategories(categoryData.filter(category => category.parentId === null));
            setSubCategories(categoryData.filter(category => category.parentId !== null));
        } catch (error) {
            console.error("카테고리 불러오기 실패:", error);
        }
    };

    const fetchSubCategories = async (parentId) => {
        try {
            const response = await axios.get(`/categories/${parentId}/subcategories`);
            setSubCategories(response.data);
        } catch (error) {
            console.error("서브 카테고리 불러오기 실패:", error);
        }
    };

    const getCategoryNameById = (id) => {
        const category = categories.find(cat => cat.id === id) || subCategories.find(cat => cat.id === id);
        return category ? category.name : '카테고리 없음';
    };

    const handleAddProduct = async () => {
        const itemDTO = {
            itemName: newProductName,
            price: newProductPrice,
            categoryId: selectedSubCategory
        };

        const formData = new FormData();
        formData.append('itemDTO', new Blob([JSON.stringify(itemDTO)], { type: 'application/json' }));
        formData.append('mainFile', mainFile);
        formData.append('subFile', subFile);

        try {
            await axios.post('/items', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });
            setShowAddModal(false);
            setNewProductName('');
            setNewProductPrice('');
            setSelectedMainCategory(null);
            setSelectedSubCategory(null);
            setMainFile(null);
            setSubFile(null);
            fetchProducts();
        } catch (error) {
            console.error("상품 추가 실패:", error);
        }
    };

    const handleMainCategoryChange = (e) => {
        const mainCategoryId = e.target.value;
        setSelectedMainCategory(mainCategoryId);
        fetchSubCategories(mainCategoryId);
    };

    const handleDeleteProduct = async (id) => {
        if (window.confirm("정말 상품을 삭제하시겠습니까?")) {
            try {
                await axios.delete(`/items/${id}`);
                fetchProducts();
            } catch (error) {
                console.error("상품 삭제 실패:", error);
            }
        }
    };

    return (
        <div className="admin-item-container">
            <h2>상품 관리</h2>
            <Table striped bordered hover>
                <thead>
                    <tr>
                        <th>썸네일</th>
                        <th>상품명</th>
                        <th>가격</th>
                        <th>카테고리</th>
                        <th>작업</th>
                    </tr>
                </thead>
                <tbody>
                    {products.map((product) => (
                        <tr key={product.id}>
                            <td><img src={product.thumbnailUrl} alt="thumbnail" className="thumbnail-img" /></td>
                            <td>{product.itemName}</td>
                            <td>{product.price}원</td>
                            <td>{getCategoryNameById(product.categoryId)}</td>
                            <td>
                                <Button size="sm" variant="secondary" onClick={() => {/* 수정 로직 추가 */}}>수정</Button>
                                <Button size="sm" variant="danger" onClick={() => handleDeleteProduct(product.id)}>삭제</Button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </Table>
            <Button variant="primary" className="add-product-button" onClick={() => setShowAddModal(true)}>상품 등록</Button>

            <Modal show={showAddModal} onHide={() => setShowAddModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>상품 등록하기</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form.Group>
                        <Form.Label>상품 이름</Form.Label>
                        <Form.Control type="text" value={newProductName} onChange={(e) => setNewProductName(e.target.value)} />
                    </Form.Group>
                    <Form.Group>
                        <Form.Label>상품 가격</Form.Label>
                        <Form.Control type="number" value={newProductPrice} onChange={(e) => setNewProductPrice(e.target.value)} />
                    </Form.Group>
                    <Form.Group>
                        <Form.Label>상위 카테고리 선택</Form.Label>
                        <Form.Control as="select" onChange={handleMainCategoryChange}>
                            <option value={null}>선택</option>
                            {categories.map(cat => (
                                <option key={cat.id} value={cat.id}>{cat.name}</option>
                            ))}
                        </Form.Control>
                    </Form.Group>
                    <Form.Group>
                        <Form.Label>하위 카테고리 선택</Form.Label>
                        <Form.Control as="select" value={selectedSubCategory} onChange={(e) => setSelectedSubCategory(e.target.value)} disabled={!selectedMainCategory}>
                            <option value={null}>선택</option>
                            {subCategories.map(subCat => (
                                <option key={subCat.id} value={subCat.id}>{subCat.name}</option>
                            ))}
                        </Form.Control>
                    </Form.Group>
                    <Form.Group>
                        <Form.Label>대표 이미지 등록</Form.Label>
                        <Form.Control type="file" onChange={(e) => setMainFile(e.target.files[0])} />
                    </Form.Group>
                    <Form.Group>
                        <Form.Label>상세 이미지 등록</Form.Label>
                        <Form.Control type="file" onChange={(e) => setSubFile(e.target.files[0])} />
                    </Form.Group>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="primary" onClick={handleAddProduct}>등록하기</Button>
                </Modal.Footer>
            </Modal>
        </div>
    );
}

export default AdminItemPage;
