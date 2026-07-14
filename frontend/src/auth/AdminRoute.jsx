import { useEffect } from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { notifications } from '@mantine/notifications';
import { useAuth } from './AuthContext';

export default function AdminRoute() {
  const { isAdmin } = useAuth();

  useEffect(() => {
    if (!isAdmin) {
      notifications.show({
        color: 'red',
        title: 'Acceso restringido',
        message: 'Solo un administrador puede gestionar usuarios y roles',
      });
    }
  }, [isAdmin]);

  if (!isAdmin) {
    return <Navigate to="/planillas" replace />;
  }

  return <Outlet />;
}
