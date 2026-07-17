import { Navigate, Route, Routes } from 'react-router-dom';
import { AuthProvider } from './auth/AuthContext';
import ProtectedRoute from './auth/ProtectedRoute';
import AdminRoute from './auth/AdminRoute';
import NoEmpleadoRoute from './auth/NoEmpleadoRoute';
import EmpleadoOnlyRoute from './auth/EmpleadoOnlyRoute';
import AppLayout from './layout/AppLayout';
import EmpleadoLayout from './layout/EmpleadoLayout';
import LoginPage from './pages/LoginPage';
import EmpleadosPage from './pages/EmpleadosPage';
import EmpleadoFormPage from './pages/EmpleadoFormPage';
import UsuariosPage from './pages/UsuariosPage';
import PuestosPage from './pages/PuestosPage';
import RolesPage from './pages/RolesPage';
import AsistenciaPage from './pages/AsistenciaPage';
import PlanillasPage from './pages/PlanillasPage';
import PlanillaDetallePage from './pages/PlanillaDetallePage';
import PlanillaDetalleEditarPage from './pages/PlanillaDetalleEditarPage';
import BoletasPage from './pages/BoletasPage';
import BoletaDetallePage from './pages/BoletaDetallePage';
import MiPortalPage from './pages/MiPortalPage';
import MiBoletaDetallePage from './pages/MiBoletaDetallePage';
import SolicitudesAjustePage from './pages/SolicitudesAjustePage';

export default function App() {
  return (
    <AuthProvider>
      <Routes>
        <Route path="/login" element={<LoginPage />} />

        <Route element={<ProtectedRoute />}>
          <Route element={<NoEmpleadoRoute />}>
            <Route element={<AppLayout />}>
              <Route path="/empleados" element={<EmpleadosPage />} />
              <Route path="/empleados/nuevo" element={<EmpleadoFormPage />} />
              <Route path="/empleados/:id/editar" element={<EmpleadoFormPage />} />

              <Route element={<AdminRoute />}>
                <Route path="/usuarios" element={<UsuariosPage />} />
                <Route path="/solicitudes-ajuste" element={<SolicitudesAjustePage />} />
                <Route path="/puestos" element={<PuestosPage />} />
                <Route path="/roles" element={<RolesPage />} />
                <Route path="/asistencia" element={<AsistenciaPage />} />
              </Route>

              <Route path="/planillas" element={<PlanillasPage />} />
              <Route path="/planillas/:id" element={<PlanillaDetallePage />} />
              <Route path="/planillas/:id/detalles/:idDetalle/editar" element={<PlanillaDetalleEditarPage />} />
              <Route path="/boletas" element={<BoletasPage />} />
              <Route path="/boletas/:id" element={<BoletaDetallePage />} />
              <Route path="/" element={<Navigate to="/planillas" replace />} />
            </Route>
          </Route>

          <Route element={<EmpleadoOnlyRoute />}>
            <Route element={<EmpleadoLayout />}>
              <Route path="/mi-portal" element={<MiPortalPage />} />
              <Route path="/mi-portal/boletas/:id" element={<MiBoletaDetallePage />} />
            </Route>
          </Route>
        </Route>

        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </AuthProvider>
  );
}
