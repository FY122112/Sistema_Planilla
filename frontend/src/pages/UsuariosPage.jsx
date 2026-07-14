import { useEffect, useState } from 'react';
import {
  Badge,
  Button,
  Group,
  Modal,
  MultiSelect,
  PasswordInput,
  Table,
  Text,
  TextInput,
  Title,
} from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import { useForm } from '@mantine/form';
import { notifications } from '@mantine/notifications';
import { IconPlus } from '@tabler/icons-react';
import { createUsuario, fetchUsuarios } from '../api/usuarios';
import { fetchRoles } from '../api/roles';

export default function UsuariosPage() {
  const [usuarios, setUsuarios] = useState([]);
  const [roles, setRoles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [opened, { open, close }] = useDisclosure(false);

  const form = useForm({
    initialValues: { username: '', password: '', email: '', roleIds: [] },
    validate: {
      username: (value) => (value.trim().length === 0 ? 'El usuario es obligatorio' : null),
      password: (value) =>
        value.length < 8 ? 'La contraseña debe tener al menos 8 caracteres' : null,
      email: (value) => (/^\S+@\S+\.\S+$/.test(value) ? null : 'Correo inválido'),
    },
  });

  const loadData = () => {
    setLoading(true);
    return Promise.all([fetchUsuarios(), fetchRoles()])
      .then(([usuariosData, rolesData]) => {
        setUsuarios(usuariosData);
        setRoles(rolesData);
      })
      .catch(() => {
        notifications.show({
          color: 'red',
          title: 'Error',
          message: 'No se pudo cargar usuarios o roles',
        });
      })
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    loadData();
  }, []);

  const roleOptions = roles.map((role) => ({
    value: String(role.idRole),
    label: role.name,
  }));

  const handleSubmit = async (values) => {
    setSaving(true);
    try {
      await createUsuario({
        username: values.username,
        password: values.password,
        email: values.email,
        roleIds: values.roleIds.map(Number),
      });

      notifications.show({
        color: 'green',
        title: 'Usuario creado',
        message: `${values.username} se creó correctamente`,
      });

      form.reset();
      close();
      loadData();
    } catch (error) {
      const message = error.response?.data?.message || 'No se pudo crear el usuario';
      notifications.show({ color: 'red', title: 'Error', message });
    } finally {
      setSaving(false);
    }
  };

  return (
    <>
      <Group justify="space-between" mb="md">
        <Title order={2}>Usuarios</Title>
        <Button leftSection={<IconPlus size={16} />} onClick={open}>
          Nuevo usuario
        </Button>
      </Group>

      <Table striped highlightOnHover withTableBorder>
        <Table.Thead>
          <Table.Tr>
            <Table.Th>Usuario</Table.Th>
            <Table.Th>Correo</Table.Th>
            <Table.Th>Roles</Table.Th>
            <Table.Th>Estado</Table.Th>
          </Table.Tr>
        </Table.Thead>
        <Table.Tbody>
          {usuarios.map((usuario) => (
            <Table.Tr key={usuario.idUsuario}>
              <Table.Td>{usuario.username}</Table.Td>
              <Table.Td>{usuario.email}</Table.Td>
              <Table.Td>
                <Group gap={4}>
                  {usuario.roles.length === 0 && (
                    <Text size="xs" c="dimmed">
                      Sin rol
                    </Text>
                  )}
                  {usuario.roles.map((roleName) => (
                    <Badge key={roleName} variant="light">
                      {roleName}
                    </Badge>
                  ))}
                </Group>
              </Table.Td>
              <Table.Td>
                <Badge color={usuario.enabled ? 'green' : 'gray'}>
                  {usuario.enabled ? 'Activo' : 'Inactivo'}
                </Badge>
              </Table.Td>
            </Table.Tr>
          ))}
        </Table.Tbody>
      </Table>

      {!loading && usuarios.length === 0 && (
        <Text size="sm" c="dimmed" mt="sm">
          No hay usuarios registrados
        </Text>
      )}

      <Modal opened={opened} onClose={close} title="Nuevo usuario" centered>
        <form onSubmit={form.onSubmit(handleSubmit)}>
          <TextInput
            label="Usuario"
            placeholder="nombre.apellido"
            required
            {...form.getInputProps('username')}
          />
          <TextInput
            label="Correo"
            placeholder="correo@empresa.com"
            required
            mt="sm"
            {...form.getInputProps('email')}
          />
          <PasswordInput
            label="Contraseña"
            placeholder="Mínimo 8 caracteres"
            required
            mt="sm"
            {...form.getInputProps('password')}
          />
          <MultiSelect
            label="Roles"
            placeholder="Selecciona uno o más roles"
            data={roleOptions}
            mt="sm"
            {...form.getInputProps('roleIds')}
          />
          <Group justify="flex-end" mt="lg">
            <Button variant="default" onClick={close}>
              Cancelar
            </Button>
            <Button type="submit" loading={saving}>
              Guardar
            </Button>
          </Group>
        </form>
      </Modal>
    </>
  );
}
