import React, { useEffect, useState } from 'react';
import { NavDropdown, Navbar, Nav, Form, Container, Button } from 'react-bootstrap/';
import { Link, useNavigate } from 'react-router-dom';
import { apiInstance } from '../util/api.js';
import LINKS from '../links/links.js';
import useUserStore from '../stores/useUserStore.js';
import MainLogo from '../images/main-logo.png';
import './MainNavbar.css'

function NavigationBar() {
  const user = useUserStore(state => state.user);
  const logout = useUserStore(state => state.logout);
  const navigate = useNavigate();
  const [categories, setCategories] = useState([]);
  const [searchQuery, setSearchQuery] = useState(''); // 검색어 상태 추가

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const response = await apiInstance.get('/categories');
        setCategories(Array.isArray(response.data) ? response.data : []);
      } catch (error) {
        console.error("Failed to fetch categories", error);
        setCategories([]);
      }
    };
    fetchCategories();
  }, []);

  const handleLogout = async () => {
    try {
      await apiInstance.post('/members-logout');
      logout();
      navigate(LINKS.HOME.path);
    } catch (error) {
      console.error("로그아웃 실패:", error);
    }
  };

  const handleSearchChange = (e) => {
    setSearchQuery(e.target.value);
  };

  const handleSearchSubmit = (e) => {
    e.preventDefault();
    navigate(`/search?query=${searchQuery}`);
  };

  const renderSubCategories = (parentId) => {
    const subCategories = categories.filter(category => category.parentId === parentId);
    return subCategories.map(subCategory => (
        <NavDropdown.Item
            key={subCategory.id}
            onClick={() => navigate(`/categories/${subCategory.id}`)}
        >
          {subCategory.name}
        </NavDropdown.Item>
    ));
  };

  const renderCategories = () => {
    const parentCategories = categories.filter(category => category.parentId === null);
    return parentCategories.map(category => (
        <NavDropdown title={category.name} id={`navbarScrollingDropdown-${category.id}`} key={category.id}>
          {renderSubCategories(category.id)}
        </NavDropdown>
    ));
  };

  return (
      <Navbar expand="lg">
        <Container fluid>
          <Navbar.Brand href={LINKS.HOME.path}><img src={MainLogo} alt='main-logo' height='85' width='300'/>
          </Navbar.Brand>
          <Navbar.Toggle aria-controls="navbarScroll" />
          <Navbar.Collapse id="navbarScroll">
            <Nav className="me-auto my-2 my-lg-5 font-size20" style={{ maxHeight: '100px', marginLeft: '10px'}} navbarScroll>
              {renderCategories()}
            </Nav>
            <Nav>
              {!user && (
                  <Navbar.Text>
                    <Link to={LINKS.LOGIN.path}>
                      <Button className='btn-custom' variant="outline-dark">LOGIN</Button>
                    </Link>
                  </Navbar.Text>
              )}
              {user && (
                  <Navbar.Text>
                    <Link to={LINKS.LOGOUT.path}>
                      <Button className='btn-custom' variant="outline-dark" onClick={handleLogout}>LOGOUT</Button>
                    </Link>
                  </Navbar.Text>
              )}
              {!user && (
                  <Navbar.Text>
                    <Link to={LINKS.REGISTER.path}>
                      <Button className='btn-custom' variant="outline-dark">SIGN-UP</Button>
                    </Link>
                  </Navbar.Text>
              )}
              {user && (
                  <Navbar.Text>
                    <Link to={LINKS.MYPAGE.path}>
                      <Button className='btn-custom' variant="outline-dark">MY</Button>
                    </Link>
                  </Navbar.Text>
              )}
              {user && user.authority === 'ADMIN' && (
                  <Navbar.Text>
                    <Link to={LINKS.ADMIN_PAGE.path}>
                      <Button className='btn-custom' variant="outline-dark">ADMIN</Button>
                    </Link>
                  </Navbar.Text>
              )}
              {user && (
                  <Navbar.Text>
                    <Link to={LINKS.CART.path}>
                      <Button className='btn-custom' variant="outline-dark">CART</Button>
                    </Link>
                  </Navbar.Text>
              )}
            </Nav>
            <Form className="d-flex" onSubmit={handleSearchSubmit}>
              <Form.Control 
                type="search" 
                placeholder="상품 검색하기" 
                className="me-2" 
                aria-label="Search" 
                value={searchQuery} 
                onChange={handleSearchChange} // 검색어 변경 핸들러 추가
              />
              <Button variant="outline-dark" type="submit">Search</Button>
            </Form>
          </Navbar.Collapse>
        </Container>
      </Navbar>
  );
}

export default NavigationBar;
