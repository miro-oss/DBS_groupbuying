import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../api';

const STATUS_MAP = {
    WAITING: '입금대기',
    CONFIRMED: '입금확인',
    SHIPPING: '배송중',
    COMPLETED: '거래완료',
    CANCELED: '거래취소'
};

export default function SellerManage() {
    const { formId } = useParams();
    const navigate = useNavigate();
    const [submissions, setSubmissions] = useState([]);
    const [stats, setStats] = useState(null);

    const [selectedIds, setSelectedIds] = useState([]);

    useEffect(() => {
        fetchData();
    }, [formId]);

    const fetchData = async () => {
        try {
            const resSub = await api.get(`/users/forms/${formId}/submissions`);
            setSubmissions(resSub.data.result);

            const resStats = await api.get(`/users/forms/${formId}/statistics`);
            setStats(resStats.data.result);
        } catch(e) { alert('데이터 로드 실패'); }
    };

    const handleStatusChange = async (submissionId, newStatus) => {
        try {
            await api.patch(`/users/forms/${formId}/submissions/${submissionId}/status`, { paymentStatus: newStatus });
            fetchData();
        } catch(e) {
            alert(e.response?.data?.message || '변경 실패');
        }
    };

    const handleBulkUpdate = async (status) => {
        if(selectedIds.length === 0) return;

        let statusText = status;
        if(status === 'CONFIRMED') statusText = '입금확인';
        if(status === 'SHIPPING') statusText = '배송중';
        if(status === 'CANCELED') statusText = '취소';

        if(!window.confirm(`선택한 ${selectedIds.length}건을 [${statusText}] 상태로 변경하시겠습니까?`)) return;

        try {
            await api.patch(`/users/forms/${formId}/submissions/status/bulk`, {
                submissionIds: selectedIds,
                paymentStatus: status
            });
            alert('일괄 변경 완료');
            setSelectedIds([]);
            fetchData();
        } catch(e) {
            alert(e.response?.data?.message || '실패');
        }
    };

    const toggleSelect = (id) => {
        if(selectedIds.includes(id)) setSelectedIds(selectedIds.filter(sid => sid !== id));
        else setSelectedIds([...selectedIds, id]);
    };

    const handleSelectAll = (e) => {
        if(e.target.checked) {
            const availableIds = submissions
                .filter(s => s.paymentStatus !== 'CANCELED')
                .map(s => s.submissionId);
            setSelectedIds(availableIds);
        } else {
            setSelectedIds([]);
        }
    };

    return (
        <div className="container">
            <h2 style={{marginTop:'10px'}}>공구 관리 대시보드</h2>

            {/* 상단 통계 카드 */}
            {stats && (
                <div style={{
                    background:'white',
                    padding:'25px',
                    borderRadius:'12px',
                    marginBottom:'30px',
                    boxShadow:'0 2px 8px rgba(0,0,0,0.05)',
                    border:'1px solid #eee'
                }}>
                    <h4 style={{marginTop:0, marginBottom:'15px', color:'#333'}}>📊 요약 통계</h4>
                    <div style={{fontSize:'16px', marginBottom:'20px'}}>
                        총 주문: <strong>{stats.totalSubmissions}건</strong> <span style={{color:'#ddd', margin:'0 10px'}}>|</span>
                        총 수량: <strong>{stats.totalQuantity}개</strong>
                    </div>
                    <div style={{display:'flex', gap:'12px', flexWrap:'wrap'}}>
                        {stats.statusStats.map(stat => (
                            <div key={stat.status} style={{
                                background: stat.status === 'WAITING' ? '#fff3cd' : '#f8f9fa',
                                padding:'10px 15px',
                                borderRadius:'8px',
                                border: stat.status === 'WAITING' ? '1px solid #ffeeba' : '1px solid #eee',
                                minWidth: '120px'
                            }}>
                                <div style={{fontSize:'12px', color:'#666', marginBottom:'5px'}}>
                                    {STATUS_MAP[stat.status] || stat.status}
                                </div>
                                <div style={{fontSize:'18px', fontWeight:'bold', color: stat.status === 'WAITING' ? '#856404' : '#333'}}>
                                    {stat.count}명 <small style={{fontSize:'12px', fontWeight:'normal'}}>({stat.totalQuantity}개)</small>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>
            )}

            {/* 일괄 처리 */}
            <div style={{
                display:'flex',
                justifyContent:'space-between',
                alignItems:'center',
                background:'white',
                padding:'15px 20px',
                borderRadius:'8px 8px 0 0',
                border:'1px solid #ddd',
                borderBottom:'none'
            }}>
                <div style={{fontSize:'15px', fontWeight:'500'}}>
                    {selectedIds.length > 0 ? (
                        <span style={{color:'#ff6b00'}}>✅ {selectedIds.length}개 선택됨</span>
                    ) : (
                        <span style={{color:'#888'}}>주문을 선택하여 일괄 처리하세요</span>
                    )}
                </div>

                <div style={{display:'flex', gap:'8px'}}>
                    <button className="btn btn-sm btn-primary" onClick={()=>handleBulkUpdate('CONFIRMED')} disabled={selectedIds.length===0} style={{opacity: selectedIds.length===0 ? 0.5 : 1}}>입금확인</button>
                    <button className="btn btn-sm btn-secondary" onClick={()=>handleBulkUpdate('SHIPPING')} disabled={selectedIds.length===0} style={{opacity: selectedIds.length===0 ? 0.5 : 1}}>배송중</button>
                    <button className="btn btn-sm btn-danger" onClick={()=>handleBulkUpdate('CANCELED')} disabled={selectedIds.length===0} style={{opacity: selectedIds.length===0 ? 0.5 : 1}}>일괄 취소</button>
                </div>
            </div>

            {/* 리스트 테이블 */}
            <div style={{overflowX:'auto', border:'1px solid #ddd', borderTop:'none', borderRadius:'0 0 8px 8px'}}>
                <table className="table" style={{background:'white', margin:0, minWidth:'800px'}}>
                    <thead style={{background:'#f8f9fa'}}>
                    <tr>
                        <th style={{width:'50px', textAlign:'center'}}>
                            <input type="checkbox" onChange={handleSelectAll} checked={submissions.length > 0 && selectedIds.length === submissions.filter(s=>s.paymentStatus!=='CANCELED').length} />
                        </th>
                        <th>입금자명</th>
                        <th>연락처</th>
                        <th>수량</th>
                        <th>현재 상태</th>
                        <th>신청일</th>
                        <th>개별 변경</th>
                    </tr>
                    </thead>
                    <tbody>
                    {submissions.length === 0 ? (
                        <tr>
                            <td colSpan="7" style={{textAlign:'center', padding:'30px', color:'#888'}}>
                                아직 신청 내역이 없습니다.
                            </td>
                        </tr>
                    ) : (
                        submissions.map(sub => {
                            const isCanceled = sub.paymentStatus === 'CANCELED'; // 여기서 변수 선언!

                            return (
                                <tr key={sub.submissionId} style={{
                                    background: isCanceled ? '#f9f9f9' : (selectedIds.includes(sub.submissionId) ? '#fff8f0' : 'white'),
                                    color: isCanceled ? '#aaa' : '#333'
                                }}>
                                    <td style={{textAlign:'center'}}>
                                        <input
                                            type="checkbox"
                                            checked={selectedIds.includes(sub.submissionId)}
                                            onChange={()=>toggleSelect(sub.submissionId)}
                                            disabled={isCanceled}
                                        />
                                    </td>
                                    <td style={{fontWeight: isCanceled?'normal':'bold', textDecoration: isCanceled?'line-through':'none'}}>
                                        {sub.buyerName}
                                    </td>
                                    <td>{sub.buyerContact}</td>
                                    <td>{sub.quantity}개</td>
                                    <td>
                                    <span style={{
                                        padding:'4px 8px',
                                        borderRadius:'12px',
                                        fontSize:'12px',
                                        background: sub.paymentStatus === 'WAITING' ? '#fff3cd' : (isCanceled ? '#eee' : '#e2e3e5'),
                                        color: sub.paymentStatus === 'WAITING' ? '#856404' : (isCanceled ? '#999' : '#333'),
                                        fontWeight:'bold'
                                    }}>
                                        {STATUS_MAP[sub.paymentStatus] || sub.paymentStatus}
                                    </span>
                                    </td>
                                    <td style={{fontSize:'13px', color:'#888'}}>{new Date(sub.submittedAt).toLocaleDateString()}</td>
                                    <td>
                                        {isCanceled ? (
                                            <span style={{fontSize:'12px', color:'#dc3545', fontWeight:'bold'}}>변경 불가</span>
                                        ) : (
                                            <select
                                                value={sub.paymentStatus}
                                                onChange={(e) => handleStatusChange(sub.submissionId, e.target.value)}
                                                style={{padding:'5px', borderRadius:'4px', border:'1px solid #ddd', fontSize:'13px'}}
                                            >
                                                <option value="WAITING">입금대기</option>
                                                <option value="CONFIRMED">입금확인</option>
                                                <option value="SHIPPING">배송중</option>
                                                <option value="CANCELED">취소</option>
                                            </select>
                                        )}
                                    </td>
                                </tr>
                            );
                        })
                    )}
                    </tbody>
                </table>
            </div>
        </div>
    );
}