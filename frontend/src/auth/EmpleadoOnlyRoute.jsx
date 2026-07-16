import { useEffect } from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { notifications } from '@mantine/notifications';
import { useAuth } from './AuthContext';

export default function EmpleadoOnlyRoute() {
  const { isEmpleado } = useAuth();

  useEffect(() => {
    if (!isEmpleado) {
      notifications.show({
        color: 'red',
        title: 'Acceso restringido',
        message: 'Esta sección es solo para cuentas de empleado',
      });
    }
  }, [isEmpleado]);

  if (!isEmpleado) {
    return <Navigate to="/planillas" replace />;
  }

  return <Outlet />;
}
