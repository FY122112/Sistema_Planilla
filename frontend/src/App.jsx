import { Navigate, Route, Routes } from 'react-router-dom';
import { AuthProvider } from './auth/AuthContext';
import ProtectedRoute from './auth/ProtectedRoute';
import AppLayout from './layout/AppLayout';
import LoginPage from './pages/LoginPage';
import EmpleadosPage from './pages/EmpleadosPage';
import EmpleadoFormPage from './pages/EmpleadoFormPage';
import UsuariosPage from './pages/UsuariosPage';
import PlanillasPage from './pages/PlanillasPage';
import PlanillaDetallePage from './pages/PlanillaDetallePage';
import BoletasPage from './pages/BoletasPage';
import BoletaDetallePage from './pages/BoletaDetallePage';

export default function App() {
  return (
    <AuthProvider>
      <Routes>
        <Route path="/login" element={<LoginPage />} />

        <Route element={<ProtectedRoute />}>
          <Route element={<AppLayout />}>
            <Route path="/empleados" element={<EmpleadosPage />} />
            <Route path="/empleados/nuevo" element={<EmpleadoFormPage />} />
            <Route path="/empleados/:id/editar" element={<EmpleadoFormPage />} />
            <Route path="/usuarios" element={<UsuariosPage />} />
            <Route path="/planillas" element={<PlanillasPage />} />
            <Route path="/planillas/:id" element={<PlanillaDetallePage />} />
            <Route path="/boletas" element={<BoletasPage />} />
            <Route path="/boletas/:id" element={<BoletaDetallePage />} />
            <Route path="/" element={<Navigate to="/planillas" replace />} />
          </Route>
        </Route>

        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </AuthProvider>
  );
}
