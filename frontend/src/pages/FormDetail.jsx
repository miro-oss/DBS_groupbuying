import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../api';

export default function FormDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [form, setForm] = useState(null);
  
  const [inputs, setInputs] = useState({
    buyerName: '',
    buyerContact: '',
    quantity: 1 // 기본값 1
  });
  
  const myId = Number(localStorage.getItem('userId'));

  useEffect(() => {
    fetchDetail();
    const storedNickname = localStorage.getItem('nickname');
    if(storedNickname) {
        setInputs(prev => ({ ...prev, buyerName: storedNickname }));
    }
  }, [id]);

  const fetchDetail = async () => {
      try {
        const res = await api.get(`/forms/${id}`);
        setForm(res.data.result);
      } catch(e) { console.error(e); }
  };

  const handleChange = (e) => {
      setInputs({ ...inputs, [e.target.name]: e.target.value });
  };

  const handleQuantityChange = (e) => {
      const val = e.target.value;
      if (val !== '' && !/^\d+$/.test(val)) return;
      
      setInputs({ ...inputs, quantity: val });
  };

  const handlePurchase = async () => {
    if (!inputs.buyerName.trim()) return alert('입금자명을 입력해주세요.');
    if (!inputs.buyerContact.trim()) return alert('전화번호를 입력해주세요.');
    
    const qtyNum = Number(inputs.quantity);
    if (!inputs.quantity || !Number.isInteger(qtyNum) || qtyNum < 1) {
        return alert('수량은 1개 이상의 정수만 입력 가능합니다.');
    }

    if (!window.confirm(`${form.title} 공구를 신청하시겠습니까?`)) return;

    try {
      const res = await api.post(`/forms/${id}/submissions`, {
        buyerName: inputs.buyerName,
        buyerContact: inputs.buyerContact,
        quantity: qtyNum
      });

      const { accountBank, accountNumber, accountName } = res.data.result;
      
      alert(
        `✅ 신청이 완료되었습니다!\n\n` +
        `[입금하실 계좌]\n` +
        `----------------------------\n` +
        `은행: ${accountBank}\n` +
        `계좌: ${accountNumber}\n` +
        `예금주: ${accountName}\n` +
        `----------------------------\n\n` +
        `마이페이지에서 내역을 확인할 수 있습니다.`
      );

      navigate('/mypage');
    } catch(err) {
      alert('신청 실패: ' + (err.response?.data?.message || '오류가 발생했습니다.'));
    }
  };

  const handleClose = async () => {
    if(!window.confirm('정말 마감하시겠습니까?')) return;
    try {
      await api.patch(`/forms/${id}/close`);
      alert('마감되었습니다.');
      window.location.reload();
    } catch(err) { alert('실패'); }
  };

  const handleDelete = async () => {
    if(!window.confirm('정말 삭제하시겠습니까?')) return;
    try {
      await api.delete(`/forms/${id}`);
      alert('삭제되었습니다.');
      navigate('/');
    } catch(err) { alert('실패'); }
  };

  if(!form) return <div className="container">로딩중...</div>;

  const isSeller = form.sellerId === myId;

  const totalPrice = (Number(inputs.quantity) || 0) * form.pricePerUnit;

  return (
    <div className="container">
      <div className="detail-container">
        <div style={{display:'flex', gap:'30px', flexWrap:'wrap'}}>
            <div style={{flexShrink:0}}>
                <img 
                    src={form.imageUrl || 'https://via.placeholder.com/400'} 
                    style={{width:'400px', height:'400px', objectFit:'cover', borderRadius:'8px', border:'1px solid #eee'}} 
                    alt="img"
                />
            </div>
            
            <div style={{flex:1, minWidth:'300px'}}>
                <span className={`status-badge ${form.status === 'OPEN' ? 'bg-open' : 'bg-closed'}`}>
                    {form.status === 'OPEN' ? '모집중' : '마감'}
                </span>
                <h2 style={{marginTop:'10px'}}>{form.title}</h2>
                <h3 style={{color:'#ff6b00', borderBottom:'1px solid #ddd', paddingBottom:'20px'}}>
                    {form.pricePerUnit.toLocaleString()}원 <small style={{color:'#888', fontSize:'14px'}}>(1개당)</small>
                </h3>
                
                <div style={{marginTop:'20px'}}>
                    <div className="info-row"><span className="info-label">판매자</span> {form.sellerNickname}</div>
                    <div className="info-row"><span className="info-label">카테고리</span> {form.categoryName}</div>
                    <div className="info-row"><span className="info-label">마감일</span> {new Date(form.deadline).toLocaleString()}</div>
                    <div className="info-row"><span className="info-label">주문예정일</span> {new Date(form.orderDate).toLocaleString()}</div>
                    <div className="info-row"><span className="info-label">수령장소</span> {form.location}</div>
                    <div className="info-row"><span className="info-label">수령시간</span> {new Date(form.tradeTime).toLocaleString()}</div>
                </div>
                
                <div style={{marginTop:'20px', padding:'15px', background:'#f8f9fa', borderRadius:'8px', minHeight:'80px', fontSize:'14px'}}>
                    <p style={{whiteSpace:'pre-wrap', margin:0}}>{form.description}</p>
                </div>

                {/* --- 신청 입력 폼 영역 --- */}
                <div style={{marginTop:'30px', borderTop:'2px solid #333', paddingTop:'20px'}}>
                    {isSeller ? (
                        <div style={{display:'flex', gap:'10px'}}>
                            <button className="btn btn-primary" onClick={() => navigate(`/manage/${id}`)}>신청자 관리</button>
                            <button className="btn btn-secondary" onClick={() => navigate(`/forms/edit/${id}`, {state: form})}>수정</button>
                            {form.status === 'OPEN' && <button className="btn btn-secondary" onClick={handleClose}>마감하기</button>}
                            <button className="btn btn-danger" onClick={handleDelete}>삭제</button>
                        </div>
                    ) : (
                        form.status === 'OPEN' ? (
                            <div style={{background:'#fff', border:'1px solid #ddd', padding:'20px', borderRadius:'8px'}}>
                                <h4 style={{marginTop:0, marginBottom:'15px'}}>공구 참여 신청</h4>
                                
                                <div className="form-group">
                                    <label>입금자명 (실명)</label>
                                    <input 
                                        name="buyerName" 
                                        className="form-control" 
                                        value={inputs.buyerName} 
                                        onChange={handleChange} 
                                        placeholder="예: 홍길동"
                                    />
                                </div>
                                <div className="form-group">
                                    <label>전화번호</label>
                                    <input 
                                        name="buyerContact" 
                                        className="form-control" 
                                        value={inputs.buyerContact} 
                                        onChange={handleChange} 
                                        placeholder="010-0000-0000"
                                    />
                                </div>
                                <div className="form-group">
                                    <label>수량</label>
                                    <input 
                                        type="number" 
                                        name="quantity"
                                        className="form-control" 
                                        min="1"
                                        step="1"
                                        value={inputs.quantity} 
                                        onChange={handleQuantityChange}
                                        onKeyDown={(e) => ["e", "E", "+", "-", "."].includes(e.key) && e.preventDefault()}
                                    />
                                </div>
                                <div style={{textAlign:'right', marginTop:'10px', fontWeight:'bold', fontSize:'18px', color:'#ff6b00'}}>
                                    총 금액: {totalPrice.toLocaleString()}원
                                </div>
                                
                                <button className="btn btn-primary btn-block" onClick={handlePurchase} style={{marginTop:'15px', padding:'15px', fontSize:'16px'}}>
                                    신청하기
                                </button>
                            </div>
                        ) : (
                            <button className="btn btn-secondary btn-block" disabled style={{padding:'15px'}}>
                                마감된 공구입니다
                            </button>
                        )
                    )}
                </div>
            </div>
        </div>
      </div>
    </div>
  );
}