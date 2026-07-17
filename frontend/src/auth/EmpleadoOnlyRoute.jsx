import { useEffect } from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { notifications } from '@mantine/notifications';
import { useAuth } from './AuthContext';

export default function EmpleadoOnlyRoute() {
  const { isAdmin } = useAuth();

  useEffect(() => {
    if (isAdmin) {
      notifications.show({
        color: 'red',
        title: 'Acceso restringido',
        message: 'El portal de autoservicio es solo para cuentas no administradoras',
      });
    }
  }, [isAdmin]);

  if (isAdmin) {
    return <Navigate to="/planillas" replace />;
  }

  return <Outlet />;
}
