import { useEffect } from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { notifications } from '@mantine/notifications';
import { useAuth } from './AuthContext';

// Bloquea la interfaz administrativa completa a cualquier cuenta que no sea
// ADMINISTRADOR, aunque naveguen la URL a mano — la restricción real está en el backend
// (@PreAuthorize en cada controlador administrativo, que hoy solo acepta ROLE_ADMINISTRADOR);
// esto evita además que la app intente renderizar pantallas cuyos endpoints le van a
// devolver 403.
export default function NoEmpleadoRoute() {
  const { isAdmin } = useAuth();
  const bloqueado = !isAdmin;

  useEffect(() => {
    if (bloqueado) {
      notifications.show({
        color: 'red',
        title: 'Acceso restringido',
        message: 'Tu cuenta solo tiene acceso al portal de autoservicio',
      });
    }
  }, [bloqueado]);

  if (bloqueado) {
    return <Navigate to="/mi-portal" replace />;
  }

  return <Outlet />;
}
