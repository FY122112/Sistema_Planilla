import { useEffect, useMemo, useState } from 'react';
import { Badge, Group, Text, TextInput, Title } from '@mantine/core';
import { DataTable } from 'mantine-datatable';
import { IconSearch } from '@tabler/icons-react';
import { notifications } from '@mantine/notifications';
import { fetchEmpleados } from '../api/empleados';

const PAGE_SIZE = 10;

export default function EmpleadosPage() {
  const [empleados, setEmpleados] = useState([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [page, setPage] = useState(1);

  useEffect(() => {
    fetchEmpleados()
      .then(setEmpleados)
      .catch(() => {
        notifications.show({
          color: 'red',
          title: 'Error',
          message: 'No se pudo cargar la lista de empleados',
        });
      })
      .finally(() => setLoading(false));
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

  return (
    <>
      <Group justify="space-between" mb="md">
        <Title order={2}>Empleados</Title>
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
        ]}
        totalRecords={filtered.length}
        recordsPerPage={PAGE_SIZE}
        page={page}
        onPageChange={setPage}
      />

      <Text size="xs" c="dimmed" mt="sm">
        {filtered.length} empleado(s)
      </Text>
    </>
  );
}
