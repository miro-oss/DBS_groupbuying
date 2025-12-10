import React, { useState, useEffect } from 'react';
import api from '../api';
import { useNavigate, useLocation, useParams } from 'react-router-dom';

export default function FormCreate() {
  const navigate = useNavigate();
  const { state } = useLocation();
  const { id } = useParams();
  const isEdit = !!id;

  const [categories, setCategories] = useState([]);
  const [file, setFile] = useState(null);
  const [preview, setPreview] = useState('');

  const [form, setForm] = useState({
    categoryId: '', title: '', description: '', pricePerUnit: '',
    location: '', accountBank: '', accountNumber: '', accountName: '',
    orderDate: '', tradeTime: '', deadline: ''
  });

  useEffect(() => {
    api.get('/categories').then(res => setCategories(res.data.result));

    if (isEdit && state) {
      setForm({
        ...state,
        categoryId: state.categoryId,
        orderDate: state.orderDate,
        tradeTime: state.tradeTime,
        deadline: state.deadline
      });
      if(state.imageUrl) setPreview(state.imageUrl);
    }
  }, [isEdit, state]);

  const handleFileChange = (e) => {
    const selectedFile = e.target.files[0];
    if(selectedFile) {
      setFile(selectedFile);
      setPreview(URL.createObjectURL(selectedFile));
    }
  };

  const handlePriceChange = (e) => {
    const val = e.target.value;
    if (val !== '' && !/^\d+$/.test(val)) return;
    setForm({...form, pricePerUnit: val});
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const deadline = new Date(form.deadline);
    const orderDate = new Date(form.orderDate);
    const tradeTime = new Date(form.tradeTime);

    if (orderDate <= deadline) {
      return alert(" 구매 날짜는 공구 마감일보다 늦어야 합니다.\n(마감 후 주문 가능)");
    }
    if (tradeTime <= orderDate) {
      return alert(" 수령 시간은 구매 날짜보다 늦어야 합니다.\n(주문 후 수령 가능)");
    }

    const priceNum = Number(form.pricePerUnit);
    if (!form.pricePerUnit || !Number.isInteger(priceNum) || priceNum < 0) {
      return alert('가격은 0원 이상의 정수만 입력 가능합니다.');
    }

    try {
      // 1. FormData 생성 (파일 전송용)
      const formData = new FormData();
      const jsonBlob = new Blob([JSON.stringify(form)], { type: "application/json" });
      formData.append("request", jsonBlob);

      if (file) {
        formData.append("image", file);
      }

      const config = { headers: { "Content-Type": "multipart/form-data" } };

      if (isEdit) {
        await api.patch(`/forms/${id}`, formData, config);
        alert('수정되었습니다.');
      } else {
        await api.post('/forms', formData, config);
        alert('등록되었습니다.');
      }
      navigate('/mypage');
    } catch (err) {
      alert('처리 실패: ' + (err.response?.data?.message || '오류'));
    }
  };

  const handleChange = (e) => setForm({...form, [e.target.name]: e.target.value});

  return (
      <div className="container">
        <div className="form-container" style={{maxWidth:'800px'}}>
          <h2>{isEdit ? '공구 수정하기' : '새 공구 등록하기'}</h2>
          <form onSubmit={handleSubmit}>

            <div className="form-group">
              <label>카테고리</label>
              <select name="categoryId" className="form-control" value={form.categoryId} onChange={handleChange} required>
                <option value="">선택하세요</option>
                {categories.map(c => <option key={c.categoryId} value={c.categoryId}>{c.categoryName}</option>)}
              </select>
            </div>

            <div className="form-group">
              <label>제목</label>
              <input name="title" className="form-control" value={form.title} onChange={handleChange} required />
            </div>

            <div className="form-group">
              <label>가격 (1개당)</label>
              <input
                  type="number"
                  name="pricePerUnit"
                  className="form-control"
                  value={form.pricePerUnit}
                  onChange={handlePriceChange}
                  min="0"
                  onKeyDown={(e) => ["e", "E", "+", "-", "."].includes(e.key) && e.preventDefault()}
                  required
              />
            </div>

            <div className="form-group">
              <label style={{color: '#ff6b00'}}>상품 이미지 (파일 업로드)</label>
              <input type="file" accept="image/*" className="form-control" onChange={handleFileChange} />
              {preview && (
                  <div style={{marginTop:'10px'}}>
                    <p style={{fontSize:'12px', color:'#888'}}>미리보기:</p>
                    <img
                        src={preview}
                        alt="미리보기"
                        style={{width:'200px', height:'200px', objectFit:'cover', borderRadius:'8px', border:'1px solid #ddd'}}
                        onError={(e) => {
                          e.target.onerror = null;
                          e.target.src = 'https://via.placeholder.com/200?text=No+Image';
                        }}
                    />
                  </div>
              )}
            </div>

            <div className="form-group">
              <label>설명</label>
              <textarea name="description" className="form-control" rows="5" value={form.description} onChange={handleChange} required />
            </div>

            <h4 style={{marginTop:'20px'}}>일정 및 장소</h4>

            <div className="form-group">
              <label>1. 공구 마감일 (이때까지 사람을 모읍니다)</label>
              <input type="datetime-local" name="deadline" className="form-control" value={form.deadline} onChange={handleChange} required />
            </div>

            <div className="form-group">
              <label>2. 구매 날짜 (마감 후 판매자가 주문하는 날)</label>
              <input type="datetime-local" name="orderDate" className="form-control" value={form.orderDate} onChange={handleChange} required />
            </div>

            <div className="form-group">
              <label>3. 수령 시간 (물건을 나눠주는 시간)</label>
              <input type="datetime-local" name="tradeTime" className="form-control" value={form.tradeTime} onChange={handleChange} required />
            </div>

            <div className="form-group">
              <label>수령 장소</label>
              <input name="location" className="form-control" value={form.location} onChange={handleChange} required />
            </div>

            <h4 style={{marginTop:'20px'}}>계좌 정보</h4>
            <div style={{display:'flex', gap:'10px'}}>
              <input name="accountBank" placeholder="은행명" className="form-control" value={form.accountBank} onChange={handleChange} required />
              <input
                  name="accountNumber"
                  placeholder="계좌번호 (숫자, -)"
                  className="form-control"
                  value={form.accountNumber}
                  onChange={(e) => {
                    const val = e.target.value;
                    if (val === '' || /^[0-9-]*$/.test(val)) {
                      setForm({...form, accountNumber: val});
                    }
                  }}
                  required
              />
              <input name="accountName" placeholder="예금주" className="form-control" value={form.accountName} onChange={handleChange} required />
            </div>

            <button type="submit" className="btn btn-primary btn-block" style={{marginTop:'30px'}}>
              {isEdit ? '수정 완료' : '등록하기'}
            </button>
          </form>
        </div>
      </div>
  );
}