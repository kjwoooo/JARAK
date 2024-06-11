import React, { useState, useEffect } from 'react';
import { Form, Button } from 'react-bootstrap';
import axios from 'axios';
import useUserStore from './stores/useUserStore';

function MemberEdit() {
  const user = useUserStore(state => state.user);
  
  const [formData, setFormData] = useState({
    username: user.username,
    displayName: user.displayName,
    password: '',
    modifyPassword: '',
    phone: user.phone
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await axios.post('/members', formData);
      alert('회원정보가 수정되었습니다.');
    } catch (error) {
      console.error('회원정보 수정 실패:', error);
      alert('회원정보 수정에 실패했습니다.');
    }
  };

  return (
    <div className="MemberEdit">
      <Form onSubmit={handleSubmit}>
        <Form.Group controlId="formUsername">
          <Form.Label>아이디</Form.Label>
          <Form.Control type="text" value={formData.username} readOnly />
        </Form.Group>
        <Form.Group controlId="formDisplayName">
          <Form.Label>이름</Form.Label>
          <Form.Control type="text" name="displayName" value={formData.displayName} onChange={handleChange} />
        </Form.Group>
        <Form.Group controlId="formPassword">
          <Form.Label>비밀번호</Form.Label>
          <Form.Control type="password" name="password" value={formData.password} onChange={handleChange} />
        </Form.Group>
        <Form.Group controlId="formModifyPassword">
          <Form.Label>비밀번호 확인</Form.Label>
          <Form.Control type="password" name="modifyPassword" value={formData.modifyPassword} onChange={handleChange} />
        </Form.Group>
        <Form.Group controlId="formPhone">
          <Form.Label>휴대폰번호</Form.Label>
          <Form.Control type="text" name="phone" value={formData.phone} onChange={handleChange} />
        </Form.Group>
        <Button variant="primary" type="submit">
          수정하기
        </Button>
      </Form>
    </div>
  );
}

export default MemberEdit;
