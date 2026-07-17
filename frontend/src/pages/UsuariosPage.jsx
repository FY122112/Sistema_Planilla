import { useEffect, useState } from 'react';
import {
  Badge,
  Button,
  Group,
  Modal,
  MultiSelect,
  PasswordInput,
  Select,
  Switch,
  Table,
  Text,
  TextInput,
  Title,
  ActionIcon,
  Tooltip,
} from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import { useForm } from '@mantine/form';
import { notifications } from '@mantine/notifications';
import { IconEdit, IconPlus, IconTrash } from '@tabler/icons-react';
import { createUsuario, deleteUsuario, fetchUsuarios, updateUsuario } from '../api/usuarios';
import { fetchRoles } from '../api/roles';
import { fetchEmpleados } from '../api/empleados';
import ConfirmModal from '../components/ConfirmModal';

export default function UsuariosPage() {
  const [usuarios, setUsuarios] = useState([]);
  const [roles, setRoles] = useState([]);
  const [empleados, setEmpleados] = useState([]);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [opened, { open, close }] = useDisclosure(false);
  const [editando, setEditando] = useState(null);
  const [editOpened, { open: openEdit, close: closeEdit }] = useDisclosure(false);
  const [savingEdit, setSavingEdit] = useState(false);
  const [toDelete, setToDelete] = useState(null);
  const [deleting, setDeleting] = useState(false);

  const form = useForm({
    initialValues: { username: '', password: '', email: '', roleIds: [], empleadoId: '' },
    validate: {
      username: (value) => (value.trim().length === 0 ? 'El usuario es obligatorio' : null),
      password: (value) =>
        value.length < 8 ? 'La contraseña debe tener al menos 8 caracteres' : null,
      email: (value) => (/^\S+@\S+\.\S+$/.test(value) ? null : 'Correo inválido'),
    },
  });

  const editForm = useForm({
    initialValues: { username: '', email: '', password: '', empleadoId: '', enabled: true },
    validate: {
      username: (value) => (value.trim().length === 0 ? 'El usuario es obligatorio' : null),
      email: (value) => (/^\S+@\S+\.\S+$/.test(value) ? null : 'Correo inválido'),
      password: (value) => (value && value.length < 8 ? 'La contraseña debe tener al menos 8 caracteres' : null),
    },
  });

  const loadData = () => {
    setLoading(true);
    return Promise.all([fetchUsuarios(), fetchRoles(), fetchEmpleados()])
      .then(([usuariosData, rolesData, empleadosData]) => {
        setUsuarios(usuariosData);
        setRoles(rolesData);
        setEmpleados(empleadosData);
      })
      .catch(() => {
        notifications.show({
          color: 'red',
          title: 'Error',
          message: 'No se pudo cargar usuarios, roles o empleados',
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

  const empleadoOptions = empleados.map((empleado) => ({
    value: String(empleado.idEmpleado),
    label: `${empleado.nombre} ${empleado.apellido} (${empleado.numeroDocumento})`,
  }));

  const nombreEmpleado = (empleadoId) => {
    const empleado = empleados.find((e) => e.idEmpleado === empleadoId);
    return empleado ? `${empleado.nombre} ${empleado.apellido}` : null;
  };

  const handleSubmit = async (values) => {
    setSaving(true);
    try {
      await createUsuario({
        username: values.username,
        password: values.password,
        email: values.email,
        roleIds: values.roleIds.map(Number),
        empleadoId: values.empleadoId ? Number(values.empleadoId) : null,
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

  const handleAbrirEdicion = (usuario) => {
    setEditando(usuario);
    editForm.setValues({
      username: usuario.username,
      email: usuario.email,
      password: '',
      empleadoId: usuario.empleadoId ? String(usuario.empleadoId) : '',
      enabled: usuario.enabled,
    });
    openEdit();
  };

  const handleSubmitEdit = async (values) => {
    setSavingEdit(true);
    try {
      await updateUsuario(editando.idUsuario, {
        username: values.username,
        email: values.email,
        password: values.password || undefined,
        empleadoId: values.empleadoId ? Number(values.empleadoId) : null,
        enabled: values.enabled,
      });

      notifications.show({ color: 'green', title: 'Usuario actualizado', message: '' });

      closeEdit();
      setEditando(null);
      loadData();
    } catch (error) {
      const message = error.response?.data?.message || 'No se pudo actualizar el usuario';
      notifications.show({ color: 'red', title: 'Error', message });
    } finally {
      setSavingEdit(false);
    }
  };

  const handleDelete = async () => {
    if (!toDelete) return;
    setDeleting(true);
    try {
      await deleteUsuario(toDelete.idUsuario);
      notifications.show({ color: 'green', title: 'Usuario eliminado', message: '' });
      setToDelete(null);
      loadData();
    } catch (error) {
      const message = error.response?.data?.message || 'No se pudo eliminar el usuario';
      notifications.show({ color: 'red', title: 'Error', message });
    } finally {
      setDeleting(false);
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
            <Table.Th>Empleado vinculado</Table.Th>
            <Table.Th>Roles</Table.Th>
            <Table.Th>Estado</Table.Th>
            <Table.Th ta="right">Acciones</Table.Th>
          </Table.Tr>
        </Table.Thead>
        <Table.Tbody>
          {usuarios.map((usuario) => (
            <Table.Tr key={usuario.idUsuario}>
              <Table.Td>{usuario.username}</Table.Td>
              <Table.Td>{usuario.email}</Table.Td>
              <Table.Td>
                {nombreEmpleado(usuario.empleadoId) ?? (
                  <Text size="xs" c="dimmed">
                    Sin vincular
                  </Text>
                )}
              </Table.Td>
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
              <Table.Td>
                <Group justify="flex-end">
                  <Tooltip label="Editar">
                    <ActionIcon variant="subtle" onClick={() => handleAbrirEdicion(usuario)}>
                      <IconEdit size={18} />
                    </ActionIcon>
                  </Tooltip>
                  <Tooltip label="Eliminar">
                    <ActionIcon variant="subtle" color="red" onClick={() => setToDelete(usuario)}>
                      <IconTrash size={18} />
                    </ActionIcon>
                  </Tooltip>
                </Group>
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
          <Select
            label="Empleado vinculado"
            description="Obligatorio si el usuario va a entrar al portal de autoservicio (rol EMPLEADO)"
            placeholder="Selecciona un empleado (opcional)"
            data={empleadoOptions}
            searchable
            clearable
            mt="sm"
            {...form.getInputProps('empleadoId')}
            onChange={(value) => {
              form.setFieldValue('empleadoId', value);
              const empleado = empleados.find((e) => String(e.idEmpleado) === value);
              if (empleado?.correo) {
                form.setFieldValue('email', empleado.correo);
              }
            }}
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

      <Modal opened={editOpened} onClose={closeEdit} title={`Editar usuario — ${editando?.username ?? ''}`} centered>
        <form onSubmit={editForm.onSubmit(handleSubmitEdit)}>
          <TextInput label="Usuario" required {...editForm.getInputProps('username')} />
          <TextInput label="Correo" required mt="sm" {...editForm.getInputProps('email')} />
          <PasswordInput
            label="Nueva contraseña"
            placeholder="Dejar en blanco para no cambiarla"
            mt="sm"
            {...editForm.getInputProps('password')}
          />
          <Select
            label="Empleado vinculado"
            description="Solo se puede cambiar a otro empleado, no se puede desvincular desde aquí"
            placeholder="Selecciona un empleado"
            data={empleadoOptions}
            searchable
            mt="sm"
            {...editForm.getInputProps('empleadoId')}
            onChange={(value) => {
              editForm.setFieldValue('empleadoId', value);
              const empleado = empleados.find((e) => String(e.idEmpleado) === value);
              if (empleado?.correo) {
                editForm.setFieldValue('email', empleado.correo);
              }
            }}
          />
          <Switch
            label="Usuario activo"
            description="Un usuario inactivo no puede iniciar sesión"
            mt="md"
            checked={editForm.values.enabled}
            onChange={(event) => editForm.setFieldValue('enabled', event.currentTarget.checked)}
          />
          <Group justify="flex-end" mt="lg">
            <Button variant="default" onClick={closeEdit}>
              Cancelar
            </Button>
            <Button type="submit" loading={savingEdit}>
              Guardar
            </Button>
          </Group>
        </form>
      </Modal>

      <ConfirmModal
        opened={Boolean(toDelete)}
        title="Eliminar usuario"
        message={
          toDelete
            ? `¿Seguro que quieres eliminar la cuenta "${toDelete.username}"? Esta acción no se puede deshacer.`
            : ''
        }
        loading={deleting}
        onConfirm={handleDelete}
        onCancel={() => setToDelete(null)}
      />
    </>
  );
}
