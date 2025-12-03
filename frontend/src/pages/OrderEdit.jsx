import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../api';

export default function OrderEdit() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [form, setForm] = useState({ buyerName: '', buyerContact: '', quantity: 1 });

    const handleUpdate = async () => {
        try {
            await api.patch(`/users/submissions/${id}`, form);
            alert('수정되었습니다.');
            navigate('/mypage');
        } catch(e) {
            alert('수정 실패 (입금대기 상태에서만 가능합니다)');
        }
    };

    return (
        <div className="container">
            <div className="form-container">
                <h3>주문 정보 수정</h3>
                <p style={{fontSize:'12px', color:'red'}}>* 입금 대기 상태일 때만 수정 가능합니다.</p>
                <div className="form-group">
                    <label>수량</label>
                    <input type="number" className="form-control" value={form.quantity} onChange={e=>setForm({...form, quantity: e.target.value})} />
                </div>
                <div className="form-group">
                    <label>수령인 이름</label>
                    <input className="form-control" value={form.buyerName} onChange={e=>setForm({...form, buyerName: e.target.value})} />
                </div>
                <div className="form-group">
                    <label>연락처</label>
                    <input className="form-control" value={form.buyerContact} onChange={e=>setForm({...form, buyerContact: e.target.value})} />
                </div>
                <button className="btn btn-primary btn-block" onClick={handleUpdate}>수정하기</button>
            </div>
        </div>
    );
}