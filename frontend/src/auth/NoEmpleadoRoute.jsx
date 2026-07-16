import { useEffect } from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { notifications } from '@mantine/notifications';
import { useAuth } from './AuthContext';

// Bloquea la interfaz administrativa completa a cuentas de solo-empleado, aunque
// naveguen la URL a mano — la restricción real está en el backend (@PreAuthorize en cada
// controlador administrativo); esto evita además que la app intente renderizar pantallas
// cuyos endpoints le van a devolver 403.
export default function NoEmpleadoRoute() {
  const { isEmpleado, isAdmin } = useAuth();
  const bloqueado = isEmpleado && !isAdmin;

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
