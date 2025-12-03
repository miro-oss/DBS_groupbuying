import React, { useEffect, useState } from 'react';
import api from '../api';
import { useNavigate, useSearchParams } from 'react-router-dom';

export default function Home() {
  const [forms, setForms] = useState([]);
  const [categories, setCategories] = useState([]);
  const [searchParams] = useSearchParams();
  const categoryId = searchParams.get('category'); 

  const [search, setSearch] = useState({ keyword: '', status: '' });
  const navigate = useNavigate();

  useEffect(() => {
    fetchCategories();
    fetchForms();
  }, [categoryId]); 

  const fetchCategories = async () => {
    try {
      const res = await api.get('/categories');
      setCategories(res.data.result);
    } catch(e) {}
  };

  const fetchForms = async () => {
    try {
      const params = {};
      if (categoryId) params.categoryId = categoryId;
      if (search.status) params.status = search.status;
      if (search.keyword) params.keyword = search.keyword;

      const res = await api.get('/forms', { params });
      
      // [수정됨] 데이터를 받자마자 정렬 (모집중 우선 -> 최신순)
      const sortedList = res.data.result.sort((a, b) => {
          // 1. 상태 정렬: OPEN이 먼저 오도록 (-1)
          if (a.status === 'OPEN' && b.status === 'CLOSED') return -1;
          if (a.status === 'CLOSED' && b.status === 'OPEN') return 1;
          
          // 2. 같은 상태끼리는 최신순(ID 큰게 위로)
          return b.formId - a.formId;
      });

      setForms(sortedList);
    } catch (error) {
      console.error(error);
    }
  };

  const handleSearch = () => fetchForms();

  return (
    <div className="container">
      {/* 검색 필터 영역 */}
      <div style={{ background: 'white', padding: '20px', borderRadius: '8px', marginBottom: '20px', display:'flex', gap:'10px', alignItems:'center' }}>
        
        <select className="form-control" style={{width:'120px'}}
          value={search.status} onChange={e=>setSearch({...search, status: e.target.value})}>
          <option value="">전체 상태</option>
          <option value="OPEN">모집중</option>
          <option value="CLOSED">마감</option>
        </select>

        <input className="form-control" style={{flex:1}} placeholder="상품명 검색..."
          value={search.keyword} onChange={e=>setSearch({...search, keyword: e.target.value})}
          onKeyDown={e => e.key === 'Enter' && handleSearch()}
        />
        <button className="btn btn-primary" onClick={handleSearch}>검색</button>
      </div>

      {/* 리스트 결과 */}
      {forms.length === 0 ? (
        <div style={{textAlign:'center', padding:'50px', color:'#888'}}>
          등록된 공구가 없습니다.
        </div>
      ) : (
        <div className="grid-container">
          {forms.map(form => (
            <div key={form.formId} className="card" onClick={() => navigate(`/forms/${form.formId}`)}>
              <img src={form.imageUrl || 'https://via.placeholder.com/300?text=No+Image'} alt="product" className="card-img" />
              <div className="card-body">
                {/* 뱃지 색상 구분을 위한 클래스 지정 */}
                <span className={`status-badge ${form.status === 'OPEN' ? 'bg-open' : 'bg-closed'}`}>
                  {form.status === 'OPEN' ? '모집중' : '마감'}
                </span>
                <h3 className="card-title">{form.title}</h3>
                <div className="card-price">{form.pricePerUnit.toLocaleString()}원</div>
                <div className="card-meta">
                  {form.categoryName} | 마감: {new Date(form.deadline).toLocaleDateString()}
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}