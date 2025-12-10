import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../api';

export default function OrderEdit() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [form, setForm] = useState({ buyerName: '', buyerContact: '', quantity: 1 });

    useEffect(() => {
        const fetchSubmission = async () => {
            try {
                const res = await api.post(`/users/submissions/${id}`);
                const data = res.data.result;

                setForm({
                    buyerName: data.buyerName,
                    buyerContact: data.buyerContact,
                    quantity: data.quantity
                });
            } catch (e) {
                console.error(e);
                alert("주문 정보를 불러오는데 실패했습니다.");
                navigate('/mypage');
            }
        };
        fetchSubmission();
    }, [id, navigate]);

    const handleQuantityChange = (e) => {
        const val = e.target.value;
        if (val !== '' && !/^\d+$/.test(val)) return;
        setForm({ ...form, quantity: val });
    };

    const handleContactChange = (e) => {
        const val = e.target.value;
        if (val !== '' && !/^[0-9-]*$/.test(val)) return;
        setForm({ ...form, buyerContact: val });
    };

    const handleUpdate = async () => {
        const qtyNum = Number(form.quantity);
        if (!form.quantity || !Number.isInteger(qtyNum) || qtyNum < 1) {
            return alert('수량은 1개 이상의 정수만 입력 가능합니다.');
        }

        if (!form.buyerName.trim()) return alert('수령인을 입력해주세요.');
        if (!form.buyerContact.trim()) return alert('연락처를 입력해주세요.');

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
                    <input
                        type="number"
                        className="form-control"
                        value={form.quantity}
                        onChange={handleQuantityChange}
                        min="1"
                        onKeyDown={(e) => ["e", "E", "+", "-", "."].includes(e.key) && e.preventDefault()}
                    />
                </div>

                <div className="form-group">
                    <label>수령인 이름</label>
                    <input
                        className="form-control"
                        value={form.buyerName}
                        onChange={e=>setForm({...form, buyerName: e.target.value})}
                    />
                </div>

                <div className="form-group">
                    <label>연락처</label>
                    <input
                        className="form-control"
                        value={form.buyerContact}
                        onChange={handleContactChange}
                        placeholder="010-0000-0000"
                    />
                </div>

                <button className="btn btn-primary btn-block" onClick={handleUpdate}>수정하기</button>
            </div>
        </div>
    );
}