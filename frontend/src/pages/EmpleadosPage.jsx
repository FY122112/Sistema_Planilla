import { useEffect, useMemo, useState } from 'react';
import { ActionIcon, Badge, Button, Group, Text, TextInput, Title, Tooltip } from '@mantine/core';
import { DataTable } from 'mantine-datatable';
import { IconEdit, IconPlus, IconSearch, IconTrash } from '@tabler/icons-react';
import { notifications } from '@mantine/notifications';
import { useNavigate } from 'react-router-dom';
import { deleteEmpleado, fetchEmpleados } from '../api/empleados';
import ConfirmModal from '../components/ConfirmModal';

const PAGE_SIZE = 10;

export default function EmpleadosPage() {
  const navigate = useNavigate();
  const [empleados, setEmpleados] = useState([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [page, setPage] = useState(1);
  const [toDelete, setToDelete] = useState(null);
  const [deleting, setDeleting] = useState(false);

  const loadData = () => {
    setLoading(true);
    return fetchEmpleados()
      .then(setEmpleados)
      .catch(() => {
        notifications.show({
          color: 'red',
          title: 'Error',
          message: 'No se pudo cargar la lista de empleados',
        });
      })
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    loadData();
  }, []);

  const filtered = useMemo(() => {
    const term = search.trim().toLowerCase();
    if (!term) return empleados;

    return empleados.filter((empleado) =>
      `${empleado.nombre} ${empleado.apellido} ${empleado.numeroDocumento}`
        .toLowerCase()
        .includes(term),
    );
  }, [empleados, search]);

  const paged = filtered.slice((page - 1) * PAGE_SIZE, page * PAGE_SIZE);

  const handleDelete = async () => {
    if (!toDelete) return;
    setDeleting(true);
    try {
      await deleteEmpleado(toDelete.idEmpleado);
      notifications.show({ color: 'green', title: 'Empleado eliminado', message: '' });
      setToDelete(null);
      loadData();
    } catch (error) {
      const message = error.response?.data?.message || 'No se pudo eliminar el empleado';
      notifications.show({ color: 'red', title: 'Error', message });
    } finally {
      setDeleting(false);
    }
  };

  return (
    <>
      <Group justify="space-between" mb="md">
        <Title order={2}>Empleados</Title>
        <Group>
          <TextInput
            placeholder="Buscar por nombre o DNI..."
            leftSection={<IconSearch size={16} />}
            value={search}
            onChange={(event) => {
              setSearch(event.currentTarget.value);
              setPage(1);
            }}
            w={280}
          />
          <Button leftSection={<IconPlus size={16} />} onClick={() => navigate('/empleados/nuevo')}>
            Nuevo empleado
          </Button>
        </Group>
      </Group>

      <DataTable
        withTableBorder
        withColumnBorders
        striped
        highlightOnHover
        fetching={loading}
        minHeight={200}
        noRecordsText="No hay empleados registrados"
        records={paged}
        columns={[
          { accessor: 'numeroDocumento', title: 'Documento' },
          {
            accessor: 'nombreCompleto',
            title: 'Nombres y Apellidos',
            render: (empleado) => `${empleado.nombre} ${empleado.apellido}`,
          },
          { accessor: 'nombrePuesto', title: 'Cargo' },
          {
            accessor: 'estado',
            title: 'Estado',
            render: (empleado) => (
              <Badge color={empleado.estado ? 'green' : 'gray'}>
                {empleado.estado ? 'Activo' : 'Cesado'}
              </Badge>
            ),
          },
          {
            accessor: 'acciones',
            title: 'Acciones',
            textAlign: 'right',
            render: (empleado) => (
              <Group justify="flex-end" gap="xs">
                <Tooltip label="Editar">
                  <ActionIcon
                    variant="subtle"
                    onClick={() => navigate(`/empleados/${empleado.idEmpleado}/editar`)}
                  >
                    <IconEdit size={18} />
                  </ActionIcon>
                </Tooltip>
                <Tooltip label="Eliminar">
                  <ActionIcon variant="subtle" color="red" onClick={() => setToDelete(empleado)}>
                    <IconTrash size={18} />
                  </ActionIcon>
                </Tooltip>
              </Group>
            ),
          },
        ]}
        totalRecords={filtered.length}
        recordsPerPage={PAGE_SIZE}
        page={page}
        onPageChange={setPage}
      />

      <Text size="xs" c="dimmed" mt="sm">
        {filtered.length} empleado(s)
      </Text>

      <ConfirmModal
        opened={Boolean(toDelete)}
        title="Eliminar empleado"
        message={
          toDelete
            ? `¿Seguro que quieres eliminar a ${toDelete.nombre} ${toDelete.apellido}? Esta acción es permanente y no se puede deshacer.`
            : ''
        }
        loading={deleting}
        onConfirm={handleDelete}
        onCancel={() => setToDelete(null)}
      />
    </>
  );
}
