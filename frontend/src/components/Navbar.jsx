import React, { useEffect, useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import api from '../api';

export default function Navbar() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const currentCatId = searchParams.get('category'); 

  const [categories, setCategories] = useState([]);
  const isLogin = !!localStorage.getItem('accessToken');
  const nickname = localStorage.getItem('nickname');

  useEffect(() => {
    api.get('/categories')
      .then(res => setCategories(res.data.result))
      .catch(err => console.error("카테고리 로딩 실패:", err));
  }, []);

  const handleLogout = () => {
    localStorage.clear();
    alert('로그아웃 되었습니다.');
    navigate('/login');
    window.location.reload(); 
  };

  return (
    <>
      {/* 1. 상단 로고 및 버튼 영역 */}
      <nav className="navbar">
        <div className="nav-content">
          <div className="logo" onClick={() => navigate('/')}>
            DGU 공구마켓
          </div>
          <div className="nav-links">
            {isLogin ? (
              <>
                <span style={{marginRight: '15px', fontWeight:'bold'}}>{nickname}님</span>
                <button onClick={() => navigate('/forms/new')}>공구 올리기</button>
                <button onClick={() => navigate('/mypage')}>마이페이지</button>
                <button onClick={handleLogout} className="btn-secondary">로그아웃</button>
              </>
            ) : (
              <>
                <button onClick={() => navigate('/login')} className="btn-primary">로그인</button>
                <button onClick={() => navigate('/signup')}>회원가입</button>
              </>
            )}
          </div>
        </div>
      </nav>

      {/* 2. 하단 카테고리 메뉴바 */}
      <div className="category-bar">
        <div className="category-content">
          <button 
            className={`category-item ${!currentCatId ? 'active' : ''}`} 
            onClick={() => navigate('/')}
          >
            전체보기
          </button>
          {categories.map(cat => (
            <button
              key={cat.categoryId}
              className={`category-item ${currentCatId == cat.categoryId ? 'active' : ''}`}
              onClick={() => navigate(`/?category=${cat.categoryId}`)}
            >
              {cat.categoryName}
            </button>
          ))}
        </div>
      </div>
    </>
  );
}