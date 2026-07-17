import { AppShell, Group, Text, Button } from '@mantine/core';
import { IconLogout } from '@tabler/icons-react';
import { NavLink as RouterNavLink, Outlet, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';

const NAV_ITEMS = [
  { label: 'Mis boletas', to: '/mi-portal' },
  { label: 'Mi asistencia', to: '/mi-portal/asistencia' },
];

// Chrome deliberadamente simple: sin sidebar administrativo, pensado para un operario que
// solo necesita moverse entre sus propias secciones de autoservicio (boletas, asistencia),
// no todos los módulos administrativos.
export default function EmpleadoLayout() {
  const { username, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = () => {
    logout();
    navigate('/login', { replace: true });
  };

  return (
    <AppShell header={{ height: 100 }} padding="md">
      <AppShell.Header>
        <Group h={60} px="md" justify="space-between">
          <Text fw={700}>Sistema Planilla — Mi Portal</Text>
          <Group>
            <Text size="sm" c="dimmed">
              {username}
            </Text>
            <Button
              variant="subtle"
              color="red"
              size="xs"
              leftSection={<IconLogout size={16} />}
              onClick={handleLogout}
            >
              Salir
            </Button>
          </Group>
        </Group>
        <Group h={40} px="md" gap="lg">
          {NAV_ITEMS.map((item) => (
            <Text
              key={item.to}
              component={RouterNavLink}
              to={item.to}
              size="sm"
              fw={location.pathname === item.to ? 700 : 400}
              c={location.pathname === item.to ? 'blue' : 'dimmed'}
            >
              {item.label}
            </Text>
          ))}
        </Group>
      </AppShell.Header>

      <AppShell.Main>
        <Outlet />
      </AppShell.Main>
    </AppShell>
  );
}
