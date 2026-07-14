import { Navigate, Route, Routes } from 'react-router-dom';
import { AuthProvider } from './auth/AuthContext';
import ProtectedRoute from './auth/ProtectedRoute';
import AppLayout from './layout/AppLayout';
import LoginPage from './pages/LoginPage';
import EmpleadosPage from './pages/EmpleadosPage';
import UsuariosPage from './pages/UsuariosPage';

export default function App() {
  return (
    <AuthProvider>
      <Routes>
        <Route path="/login" element={<LoginPage />} />

        <Route element={<ProtectedRoute />}>
          <Route element={<AppLayout />}>
            <Route path="/empleados" element={<EmpleadosPage />} />
            <Route path="/usuarios" element={<UsuariosPage />} />
            <Route path="/" element={<Navigate to="/empleados" replace />} />
          </Route>
        </Route>

        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </AuthProvider>
  );
}
