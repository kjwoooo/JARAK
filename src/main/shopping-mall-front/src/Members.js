import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { ListGroup, Button, Pagination } from 'react-bootstrap/';
import './Members.css';

const ITEMS_PER_PAGE = 10;

function Members() {
  const [members, setMembers] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);

  useEffect(() => {
    const fetchMembers = async () => {
      try {
        const response = await axios.get('/admin/members');
        setMembers(response.data);
      } catch (error) {
        console.error("회원 불러오기 실패", error);
      }
    };
    fetchMembers();
  }, []);

  const handleDelete = async (id) => {
    try {
      await axios.delete(`/admin/members/${id}`);
      setMembers(members.filter(member => member.id !== id));
      alert('회원이 삭제되었습니다.');
    } catch (error) {
      console.error("회원삭제 실패", error);
      alert('회원 삭제에 실패했습니다.');
    }
  };

  const handlePageChange = (page) => {
    setCurrentPage(page);
  };

  const indexOfLastItem = currentPage * ITEMS_PER_PAGE;
  const indexOfFirstItem = indexOfLastItem - ITEMS_PER_PAGE;
  const currentMembers = members.slice(indexOfFirstItem, indexOfLastItem);

  return (
    <div>
      <ListGroup>
        <ListGroup.Item>
          <div className="row">
            <div className="col-md-2">아이디</div>
            <div className="col-md-3">이메일</div>
            <div className="col-md-2">전화번호</div>
            <div className="col-md-2">회원등급</div>
            <div className="col-md-3">작업</div>
          </div>
        </ListGroup.Item>
        {currentMembers.map((member) => (
          <ListGroup.Item key={member.id}>
            <div className="row">
              <div className="col-md-2">{member.username}</div>
              <div className="col-md-3">{member.email}</div>
              <div className="col-md-2">{member.phone}</div>
              <div className="col-md-2">{member.membership}</div>
              <div className="col-md-3">
                {member.authority !== 'ADMIN' && (
                  <Button
                    variant="danger"
                    size="sm"
                    onClick={() => handleDelete(member.id)}
                  >
                    삭제
                  </Button>
                )}
              </div>
            </div>
          </ListGroup.Item>
        ))}
      </ListGroup>
      <Pagination>
        {Array.from({ length: Math.ceil(members.length / ITEMS_PER_PAGE) }).map((_, index) => (
          <Pagination.Item
            key={index}
            active={index + 1 === currentPage}
            onClick={() => handlePageChange(index + 1)}
          >
            {index + 1}
          </Pagination.Item>
        ))}
      </Pagination>
    </div>
  );
}

export default Members;
