import { AppShell, Burger, Group, NavLink, Text, Button } from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import { IconUsers, IconUserCog, IconLogout } from '@tabler/icons-react';
import { NavLink as RouterNavLink, Outlet, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';

const NAV_ITEMS = [
  { label: 'Empleados', to: '/empleados', icon: IconUsers },
  { label: 'Usuarios', to: '/usuarios', icon: IconUserCog },
];

export default function AppLayout() {
  const [opened, { toggle }] = useDisclosure();
  const { username, logout } = useAuth();
  const location = useLocation();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login', { replace: true });
  };

  return (
    <AppShell
      header={{ height: 60 }}
      navbar={{ width: 240, breakpoint: 'sm', collapsed: { mobile: !opened } }}
      padding="md"
    >
      <AppShell.Header>
        <Group h="100%" px="md" justify="space-between">
          <Group>
            <Burger opened={opened} onClick={toggle} hiddenFrom="sm" size="sm" />
            <Text fw={700}>Sistema Planilla — TextiLima SAC</Text>
          </Group>
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
      </AppShell.Header>

      <AppShell.Navbar p="md">
        {NAV_ITEMS.map((item) => (
          <NavLink
            key={item.to}
            component={RouterNavLink}
            to={item.to}
            label={item.label}
            leftSection={<item.icon size={18} />}
            active={location.pathname.startsWith(item.to)}
          />
        ))}
      </AppShell.Navbar>

      <AppShell.Main>
        <Outlet />
      </AppShell.Main>
    </AppShell>
  );
}
