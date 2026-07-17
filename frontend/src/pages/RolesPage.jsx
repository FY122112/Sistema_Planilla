import { useEffect, useState } from 'react';
import {
  Badge,
  Button,
  Group,
  Modal,
  MultiSelect,
  Table,
  Text,
  Textarea,
  TextInput,
  Title,
  ActionIcon,
  Tooltip,
} from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import { useForm } from '@mantine/form';
import { notifications } from '@mantine/notifications';
import { IconEdit, IconPlus, IconTrash } from '@tabler/icons-react';
import { createRole, deleteRole, fetchRoles, updateRole } from '../api/roles';
import { fetchPermissions } from '../api/permissions';
import ConfirmModal from '../components/ConfirmModal';

const initialValues = { name: '', description: '', permissionIds: [] };

export default function RolesPage() {
  const [roles, setRoles] = useState([]);
  const [permissions, setPermissions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [opened, { open, close }] = useDisclosure(false);
  const [editando, setEditando] = useState(null);
  const [toDelete, setToDelete] = useState(null);
  const [deleting, setDeleting] = useState(false);

  const form = useForm({
    initialValues,
    validate: {
      name: (value) => (value.trim().length === 0 ? 'El nombre es obligatorio' : null),
      permissionIds: (value) => (value.length === 0 ? 'Selecciona al menos un permiso' : null),
    },
  });

  const loadData = () => {
    setLoading(true);
    return Promise.all([fetchRoles(), fetchPermissions()])
      .then(([rolesData, permissionsData]) => {
        setRoles(rolesData);
        setPermissions(permissionsData);
      })
      .catch(() => {
        notifications.show({ color: 'red', title: 'Error', message: 'No se pudieron cargar los roles o permisos' });
      })
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    loadData();
  }, []);

  const permissionOptions = permissions.map((p) => ({ value: String(p.idPermission), label: p.name }));

  const handleAbrirCrear = () => {
    setEditando(null);
    form.setValues(initialValues);
    open();
  };

  const handleAbrirEditar = (role) => {
    setEditando(role);
    form.setValues({
      name: role.name,
      description: role.description ?? '',
      permissionIds: role.permissions.map((p) => String(p.idPermission)),
    });
    open();
  };

  const handleSubmit = async (values) => {
    setSaving(true);
    try {
      const payload = {
        name: values.name,
        description: values.description,
        permissionIds: values.permissionIds.map(Number),
      };

      if (editando) {
        await updateRole(editando.idRole, payload);
        notifications.show({ color: 'green', title: 'Rol actualizado', message: values.name });
      } else {
        await createRole(payload);
        notifications.show({ color: 'green', title: 'Rol creado', message: values.name });
      }

      form.reset();
      setEditando(null);
      close();
      loadData();
    } catch (error) {
      const message = error.response?.data?.message || 'No se pudo guardar el rol';
      notifications.show({ color: 'red', title: 'Error', message });
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async () => {
    if (!toDelete) return;
    setDeleting(true);
    try {
      await deleteRole(toDelete.idRole);
      notifications.show({ color: 'green', title: 'Rol eliminado', message: '' });
      setToDelete(null);
      loadData();
    } catch (error) {
      const message =
        error.response?.data?.message || 'No se pudo eliminar el rol (puede estar en uso por alguna cuenta)';
      notifications.show({ color: 'red', title: 'Error', message });
    } finally {
      setDeleting(false);
    }
  };

  return (
    <>
      <Group justify="space-between" mb="md">
        <Title order={2}>Roles</Title>
        <Button leftSection={<IconPlus size={16} />} onClick={handleAbrirCrear}>
          Nuevo rol
        </Button>
      </Group>

      <Table striped highlightOnHover withTableBorder>
        <Table.Thead>
          <Table.Tr>
            <Table.Th>Nombre</Table.Th>
            <Table.Th>Descripción</Table.Th>
            <Table.Th>Permisos</Table.Th>
            <Table.Th ta="right">Acciones</Table.Th>
          </Table.Tr>
        </Table.Thead>
        <Table.Tbody>
          {roles.map((role) => (
            <Table.Tr key={role.idRole}>
              <Table.Td>{role.name}</Table.Td>
              <Table.Td>
                <Text size="sm" c="dimmed" lineClamp={1}>
                  {role.description || '—'}
                </Text>
              </Table.Td>
              <Table.Td>
                <Group gap={4}>
                  {role.permissions.length === 0 && (
                    <Text size="xs" c="dimmed">
                      Sin permisos
                    </Text>
                  )}
                  {role.permissions.map((p) => (
                    <Badge key={p.idPermission} variant="light">
                      {p.name}
                    </Badge>
                  ))}
                </Group>
              </Table.Td>
              <Table.Td>
                <Group justify="flex-end">
                  <Tooltip label="Editar">
                    <ActionIcon variant="subtle" onClick={() => handleAbrirEditar(role)}>
                      <IconEdit size={18} />
                    </ActionIcon>
                  </Tooltip>
                  <Tooltip label="Eliminar">
                    <ActionIcon variant="subtle" color="red" onClick={() => setToDelete(role)}>
                      <IconTrash size={18} />
                    </ActionIcon>
                  </Tooltip>
                </Group>
              </Table.Td>
            </Table.Tr>
          ))}
        </Table.Tbody>
      </Table>

      {!loading && roles.length === 0 && (
        <Text size="sm" c="dimmed" mt="sm">
          No hay roles registrados
        </Text>
      )}

      <Modal opened={opened} onClose={close} title={editando ? 'Editar rol' : 'Nuevo rol'} centered>
        <form onSubmit={form.onSubmit(handleSubmit)}>
          <TextInput label="Nombre" placeholder="ADMINISTRADOR" required {...form.getInputProps('name')} />
          <Textarea
            label="Descripción"
            placeholder="Qué puede hacer este rol"
            mt="sm"
            autosize
            minRows={2}
            {...form.getInputProps('description')}
          />
          <MultiSelect
            label="Permisos"
            placeholder="Selecciona uno o más permisos"
            data={permissionOptions}
            required
            mt="sm"
            {...form.getInputProps('permissionIds')}
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

      <ConfirmModal
        opened={Boolean(toDelete)}
        title="Eliminar rol"
        message={
          toDelete
            ? `¿Seguro que quieres eliminar el rol "${toDelete.name}"? Esta acción no se puede deshacer.`
            : ''
        }
        loading={deleting}
        onConfirm={handleDelete}
        onCancel={() => setToDelete(null)}
      />
    </>
  );
}
