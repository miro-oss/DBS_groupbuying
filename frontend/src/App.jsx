import { BrowserRouter, Routes, Route } from 'react-router-dom';
import './App.css';
import Navbar from './components/Navbar';
import Home from './pages/Home';
import Login from './pages/Login';
import Signup from './pages/Signup';
import FormDetail from './pages/FormDetail';
import FormCreate from './pages/FormCreate';
import MyPage from './pages/MyPage';
import SellerManage from './pages/SellerManage';
import OrderEdit from './pages/OrderEdit';
import PrivateRoute from './components/PrivateRoute';

function App() {
    return (
        <BrowserRouter>
            <Navbar />
            <div style={{minHeight: '80vh'}}>
                <Routes>
                    {/* 누구나 들어갈 수 있는 페이지 */}
                    <Route path="/" element={<Home />} />
                    <Route path="/login" element={<Login />} />
                    <Route path="/signup" element={<Signup />} />
                    <Route path="/forms/:id" element={<FormDetail />} />

                    {/* 로그인해야만 들어갈 수 있는 페이지들 */}
                    <Route element={<PrivateRoute />}>
                        <Route path="/forms/new" element={<FormCreate />} />
                        <Route path="/forms/edit/:id" element={<FormCreate />} />
                        <Route path="/mypage" element={<MyPage />} />
                        <Route path="/manage/:formId" element={<SellerManage />} />
                        <Route path="/submissions/edit/:id" element={<OrderEdit />} />
                    </Route>

                </Routes>
            </div>
            <footer style={{textAlign:'center', padding:'20px', color:'#888', borderTop:'1px solid #ddd'}}>
                &copy; 2025 Dongguk Group Buying Project.
            </footer>
        </BrowserRouter>
    );
}

export default App;