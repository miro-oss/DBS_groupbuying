import React, { useState } from 'react';
import api from '../api';
import { useNavigate } from 'react-router-dom';

export default function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const res = await api.post('/users/login', { email, password });
      const { accessToken, userId, nickname } = res.data.result;
      localStorage.setItem('accessToken', accessToken);
      localStorage.setItem('userId', userId);
      localStorage.setItem('nickname', nickname);
      navigate('/');
    } catch (err) {
      alert('로그인 실패: ' + (err.response?.data?.message || '오류 발생'));
    }
  };

  return (
    <div className="container">
      <div className="form-container">
        <h2 style={{textAlign:'center'}}>로그인</h2>
        <form onSubmit={handleLogin}>
          <div className="form-group">
            <label>이메일</label>
            <input type="email" className="form-control" placeholder="user@dgu.ac.kr"
              value={email} onChange={e => setEmail(e.target.value)} required />
          </div>
          <div className="form-group">
            <label>비밀번호</label>
            <input type="password" className="form-control"
              value={password} onChange={e => setPassword(e.target.value)} required />
          </div>
          <button type="submit" className="btn btn-primary btn-block">로그인</button>
        </form>
        <button className="btn btn-secondary btn-block" onClick={()=>navigate('/signup')}>회원가입</button>
      </div>
    </div>
  );
}