import React, { useState } from 'react';
import api from '../api';
import { useNavigate } from 'react-router-dom';

export default function Signup() {
  // [수정됨] passwordConfirm 상태 추가
  const [form, setForm] = useState({
    email: '', 
    password: '', 
    passwordConfirm: '', 
    nickname: '', 
    phone: ''
  });
  const navigate = useNavigate();

  const handleChange = (e) => setForm({...form, [e.target.name]: e.target.value});

  const handleSignup = async (e) => {
    e.preventDefault();

    // [추가됨] 비밀번호 일치 확인 로직
    if (form.password !== form.passwordConfirm) {
        return alert("⛔ 비밀번호가 일치하지 않습니다.\n다시 확인해주세요!");
    }

    try {
      // 백엔드에는 passwordConfirm을 보낼 필요가 없으므로 제외하고 전송
      const { passwordConfirm, ...requestData } = form;

      await api.post('/users/signup', requestData);
      alert('회원가입 성공! 로그인해주세요.');
      navigate('/login');
    } catch (err) {
      alert('가입 실패: ' + (err.response?.data?.message || '오류'));
    }
  };

  return (
    <div className="container">
      <div className="form-container">
        <h2 style={{textAlign:'center'}}>회원가입</h2>
        <form onSubmit={handleSignup}>
          <div className="form-group">
            <label>이메일 (학교 이메일)</label>
            <input 
                name="email" 
                type="email" 
                className="form-control" 
                onChange={handleChange} 
                placeholder="example@dgu.ac.kr"
                required 
            />
          </div>
          
          <div className="form-group">
            <label>비밀번호</label>
            <input 
                name="password" 
                type="password" 
                className="form-control" 
                onChange={handleChange} 
                placeholder="8자 이상 입력"
                required 
            />
          </div>

          {/* [추가됨] 비밀번호 확인 입력창 */}
          <div className="form-group">
            <label>비밀번호 확인</label>
            <input 
                name="passwordConfirm" 
                type="password" 
                className="form-control" 
                onChange={handleChange} 
                placeholder="비밀번호를 한 번 더 입력하세요"
                required 
            />
            {/* 실시간 안내 문구 (선택사항) */}
            {form.password && form.passwordConfirm && (
                <p style={{
                    fontSize:'12px', 
                    marginTop:'5px', 
                    color: form.password === form.passwordConfirm ? 'green' : 'red'
                }}>
                    {form.password === form.passwordConfirm ? '✅ 비밀번호가 일치합니다.' : '❌ 비밀번호가 일치하지 않습니다.'}
                </p>
            )}
          </div>

          <div className="form-group">
            <label>닉네임</label>
            <input name="nickname" className="form-control" onChange={handleChange} required />
          </div>
          
          <div className="form-group">
            <label>전화번호</label>
            <input 
                name="phone" 
                className="form-control" 
                onChange={handleChange} 
                placeholder="010-0000-0000"
                required 
            />
          </div>
          
          <button type="submit" className="btn btn-primary btn-block" style={{marginTop:'20px'}}>
            가입완료
          </button>
        </form>
      </div>
    </div>
  );
}