import React, { useEffect, useState } from 'react';
import api from '../api';
import { useNavigate } from 'react-router-dom';

const STATUS_MAP = {
    WAITING: '입금대기',
    CONFIRMED: '입금확인',
    SHIPPING: '배송중',
    COMPLETED: '거래완료',
    CANCELED: '거래취소'
};

export default function MyPage() {
    const navigate = useNavigate();
    const [profile, setProfile] = useState({});
    const [mySubmissions, setMySubmissions] = useState([]);
    const [myForms, setMyForms] = useState([]);

    const [editMode, setEditMode] = useState(false);
    const [editData, setEditData] = useState({ nickname: '', phone: '' });

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            const resProfile = await api.get('/users/profile');
            setProfile(resProfile.data.result);
            setEditData({ nickname: resProfile.data.result.nickname, phone: resProfile.data.result.phone });

            const resSub = await api.get('/users/submissions');
            setMySubmissions(resSub.data.result);

            const resForms = await api.get('/users/forms');

            const sortedForms = resForms.data.result.sort((a, b) => {
                if (a.status === 'OPEN' && b.status !== 'OPEN') return -1;
                if (a.status !== 'OPEN' && b.status === 'OPEN') return 1;

                return b.formId - a.formId;
            });

            setMyForms(sortedForms);

        } catch (e) { console.error(e); }
    };

    const handlePhoneEdit = (e) => {
        const val = e.target.value;
        if (val !== '' && !/^[0-9-]*$/.test(val)) return;
        setEditData({...editData, phone: val});
    };

    const handleProfileUpdate = async () => {
        try {
            await api.patch('/users/profile', editData);
            alert('수정 완료');
            setEditMode(false);
            fetchData();
        } catch(e) { alert('수정 실패'); }
    };

    const handleUserDelete = async () => {
        if(!window.confirm('정말 탈퇴하시겠습니까?')) return;
        try {
            await api.delete('/users');
            localStorage.clear();
            alert('탈퇴되었습니다.');
            navigate('/login');
        } catch(e) { alert('실패'); }
    };

    const handleConfirmTransaction = async (e, submissionId) => {
        e.stopPropagation();
        if (!window.confirm("상품을 수령하셨나요? 거래를 확정하시겠습니까?\n(확정 후에는 취소할 수 없습니다)")) return;

        try {
            await api.patch(`/users/submissions/${submissionId}/confirm`);
            alert("거래가 확정되었습니다! 🎉");
            fetchData();
        } catch(err) {
            alert(err.response?.data?.message || "실패");
        }
    };

    return (
        <div className="container">
            <h2 style={{borderBottom:'2px solid #333', paddingBottom:'10px'}}>마이페이지</h2>

            {/* 1. 내 정보 */}
            <section style={{background:'white', padding:'20px', borderRadius:'8px', marginBottom:'30px'}}>
                <div style={{display:'flex', justifyContent:'space-between'}}>
                    <h3>내 정보</h3>
                    <div>
                        {!editMode ? (
                            <button className="btn btn-sm btn-secondary" onClick={()=>setEditMode(true)}>수정</button>
                        ) : (
                            <button className="btn btn-sm btn-primary" onClick={handleProfileUpdate}>저장</button>
                        )}
                        <button className="btn btn-sm btn-danger" style={{marginLeft:'5px'}} onClick={handleUserDelete}>탈퇴</button>
                    </div>
                </div>
                {editMode ? (
                    <div>
                        <label style={{display:'block', marginTop:'10px', fontWeight:'bold'}}>닉네임</label>
                        <input
                            value={editData.nickname}
                            onChange={e=>setEditData({...editData, nickname:e.target.value})}
                            className="form-control"
                            style={{marginBottom:'5px'}}
                        />

                        <label style={{display:'block', marginTop:'10px', fontWeight:'bold'}}>전화번호</label>
                        <input
                            value={editData.phone}
                            onChange={handlePhoneEdit}
                            className="form-control"
                            placeholder="숫자와 하이픈(-)만 입력"
                        />
                    </div>
                ) : (
                    <div>
                        <p><strong>이메일:</strong> {profile.email}</p>
                        <p><strong>닉네임:</strong> {profile.nickname}</p>
                        <p><strong>전화번호:</strong> {profile.phone}</p>
                    </div>
                )}
            </section>

            {/* 2. 내가 신청한 공구 */}
            <section style={{marginBottom:'30px'}}>
                <h3>내가 신청한 공구 내역</h3>
                {mySubmissions.length === 0 ? <p>신청 내역이 없습니다.</p> : (
                    <div className="grid-container">
                        {mySubmissions.map(sub => (
                            <div
                                key={sub.submissionId}
                                className="card"
                                onClick={() => navigate(`/forms/${sub.formId}`)}
                                style={{cursor: 'pointer'}}
                            >
                                <div className="card-body">
                                    <div style={{display:'flex', alignItems:'center', marginBottom:'10px'}}>
                                        <img
                                            src={sub.formImageUrl || 'https://via.placeholder.com/100'}
                                            style={{width:'50px', height:'50px', borderRadius:'4px', objectFit:'cover', marginRight:'10px'}}
                                            alt="img"
                                            onError={(e) => {
                                                e.target.onerror = null;
                                                e.target.src = 'https://via.placeholder.com/100?text=No+Image';
                                            }}
                                        />
                                        <div>
                                            <h4 style={{margin:0, fontSize:'16px'}}>{sub.formTitle}</h4>
                                            <span style={{fontSize:'12px', color:'#888'}}>
                                                {new Date(sub.submittedAt).toLocaleDateString()} 신청
                                            </span>
                                        </div>
                                    </div>

                                    <p style={{margin:'5px 0'}}>수량: <strong>{sub.quantity}개</strong> / 총 {sub.pricePerUnit * sub.quantity}원</p>

                                    <p>상태: <span style={{fontWeight:'bold', color:'blue'}}>
                                        {STATUS_MAP[sub.paymentStatus] || sub.paymentStatus}
                                    </span></p>

                                    {/* 수령 정보 및 판매자 연락처 */}
                                    <div style={{marginTop:'10px', fontSize:'13px', color:'#555'}}>
                                        <p style={{margin:'2px 0'}}>📍 수령 장소: <strong>{sub.location}</strong></p>
                                        <p style={{margin:'2px 0'}}>⏰ 수령 시간: <strong>{new Date(sub.tradeTime).toLocaleString()}</strong></p>
                                        <p style={{margin:'2px 0', color:'#ff6b00'}}>📞 판매자 연락처: <strong>{sub.sellerPhone}</strong></p>
                                    </div>

                                    {/* 계좌 정보 박스 */}
                                    <div style={{background:'#f1f3f5', padding:'10px', borderRadius:'4px', fontSize:'13px', marginTop:'10px'}}>
                                        <strong>[입금 계좌 정보]</strong><br/>
                                        {sub.accountBank} {sub.accountNumber}<br/>
                                        예금주: {sub.accountName}
                                    </div>

                                    {/* 배송중 상태일 때만 '거래 확정' 버튼 표시 */}
                                    {sub.paymentStatus === 'SHIPPING' && (
                                        <button
                                            className="btn btn-sm"
                                            style={{
                                                width:'100%', marginTop:'10px',
                                                backgroundColor:'#28a745', color:'white', border:'none', padding:'10px',
                                                cursor: 'pointer'
                                            }}
                                            onClick={(e) => handleConfirmTransaction(e, sub.submissionId)}
                                        >
                                            📦 수령 완료 (거래 확정)
                                        </button>
                                    )}

                                    <div style={{marginTop:'10px'}}>
                                        {sub.paymentStatus === 'WAITING' && (
                                            <button className="btn btn-sm btn-secondary"
                                                    onClick={(e) => {
                                                        e.stopPropagation();
                                                        navigate(`/submissions/edit/${sub.submissionId}`);
                                                    }}>
                                                주문 정보 수정
                                            </button>
                                        )}
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </section>

            {/* 3. 내가 올린 공구 */}
            <section>
                <h3>내가 올린 공구 관리</h3>
                {myForms.length === 0 ? <p>올린 공구가 없습니다.</p> : (
                    <div className="grid-container">
                        {myForms.map(form => (
                            <div key={form.formId} className="card" onClick={() => navigate(`/forms/${form.formId}`)}>
                                <div className="card-body">
                                    <span className={`status-badge ${form.status === 'OPEN' ? 'bg-open' : 'bg-closed'}`}>
                                        {form.status === 'OPEN' ? '모집중' : '마감'}
                                    </span>
                                    <h4>{form.title}</h4>
                                    <p>{form.pricePerUnit}원</p>
                                    <button className="btn btn-sm btn-primary"
                                            onClick={(e) => { e.stopPropagation(); navigate(`/manage/${form.formId}`); }}>
                                        신청자 관리
                                    </button>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </section>
        </div>
    );
}