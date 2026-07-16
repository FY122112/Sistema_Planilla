import { useEffect, useMemo, useState } from 'react';
import {
  ActionIcon,
  Alert,
  Badge,
  Button,
  FileInput,
  Group,
  Modal,
  Table,
  Text,
  TextInput,
  Title,
  Tooltip,
} from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import { DataTable } from 'mantine-datatable';
import { IconEdit, IconFileUpload, IconPlus, IconSearch, IconTrash } from '@tabler/icons-react';
import { notifications } from '@mantine/notifications';
import { useNavigate } from 'react-router-dom';
import { deleteEmpleado, fetchEmpleados, importarEmpleadosCsv } from '../api/empleados';
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
  const [importOpened, { open: openImport, close: closeImport }] = useDisclosure(false);
  const [archivoCsv, setArchivoCsv] = useState(null);
  const [importando, setImportando] = useState(false);
  const [resultadoImport, setResultadoImport] = useState(null);

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

  const handleImportar = async () => {
    if (!archivoCsv) return;
    setImportando(true);
    try {
      const resultado = await importarEmpleadosCsv(archivoCsv);
      setResultadoImport(resultado);
      if (resultado.exitosos > 0) {
        loadData();
      }
    } catch (error) {
      const message = error.response?.data?.message || 'No se pudo importar el archivo CSV';
      notifications.show({ color: 'red', title: 'Error', message });
    } finally {
      setImportando(false);
    }
  };

  const handleCloseImport = () => {
    closeImport();
    setArchivoCsv(null);
    setResultadoImport(null);
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
          <Button variant="default" leftSection={<IconFileUpload size={16} />} onClick={openImport}>
            Importar CSV
          </Button>
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

      <Modal opened={importOpened} onClose={handleCloseImport} title="Importar empleados por CSV" size="lg" centered>
        <Text size="sm" c="dimmed" mb="sm">
          Columnas esperadas (con encabezado): nombre, apellido, tipoDocumento, numeroDocumento,
          fechaNacimiento (AAAA-MM-DD), sexo, estadoCivil, nacionalidad, correo, telefono,
          fechaIngreso (AAAA-MM-DD), puesto, regimenLaboral, tieneHijosCalificados
        </Text>

        <FileInput
          label="Archivo CSV"
          placeholder="Selecciona un archivo .csv"
          accept=".csv,text/csv"
          value={archivoCsv}
          onChange={setArchivoCsv}
        />

        <Group justify="flex-end" mt="md">
          <Button variant="default" onClick={handleCloseImport}>
            {resultadoImport ? 'Cerrar' : 'Cancelar'}
          </Button>
          <Button onClick={handleImportar} loading={importando} disabled={!archivoCsv}>
            Importar
          </Button>
        </Group>

        {resultadoImport && (
          <>
            <Alert
              color={resultadoImport.fallidos > 0 ? 'yellow' : 'green'}
              title="Resultado de la importación"
              mt="md"
            >
              {resultadoImport.totalFilas} fila(s) procesadas: {resultadoImport.exitosos} registrada(s)
              correctamente, {resultadoImport.fallidos} con errores.
            </Alert>

            <Table striped withTableBorder mt="sm" fz="xs">
              <Table.Thead>
                <Table.Tr>
                  <Table.Th>Fila</Table.Th>
                  <Table.Th>Documento</Table.Th>
                  <Table.Th>Estado</Table.Th>
                  <Table.Th>Mensaje</Table.Th>
                </Table.Tr>
              </Table.Thead>
              <Table.Tbody>
                {resultadoImport.detalle.map((item) => (
                  <Table.Tr key={item.fila}>
                    <Table.Td>{item.fila}</Table.Td>
                    <Table.Td>{item.numeroDocumento}</Table.Td>
                    <Table.Td>
                      <Badge color={item.exitoso ? 'green' : 'red'} size="sm">
                        {item.exitoso ? 'OK' : 'Error'}
                      </Badge>
                    </Table.Td>
                    <Table.Td>{item.mensaje}</Table.Td>
                  </Table.Tr>
                ))}
              </Table.Tbody>
            </Table>
          </>
        )}
      </Modal>
    </>
  );
}
