import { useEffect, useState } from 'react';
import {
  Badge,
  Button,
  Card,
  Group,
  Loader,
  Stack,
  Table,
  Text,
  Title,
} from '@mantine/core';
import { notifications } from '@mantine/notifications';
import { useNavigate, useParams } from 'react-router-dom';
import { IconArrowLeft, IconLock, IconLockOpen, IconTrash } from '@tabler/icons-react';
import {
  abrirPlanilla,
  cerrarPlanilla,
  deletePlanilla,
  fetchPlanillaById,
} from '../api/planillas';
import { formatearMoneda, nombreMes, nombreTipoPlanilla } from '../constants/planilla';
import ConfirmModal from '../components/ConfirmModal';

export default function PlanillaDetallePage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [planilla, setPlanilla] = useState(null);
  const [loading, setLoading] = useState(true);
  const [toggling, setToggling] = useState(false);
  const [confirmDelete, setConfirmDelete] = useState(false);
  const [deleting, setDeleting] = useState(false);

  const loadData = () => {
    setLoading(true);
    return fetchPlanillaById(id)
      .then(setPlanilla)
      .catch(() => {
        notifications.show({
          color: 'red',
          title: 'Error',
          message: 'No se pudo cargar la planilla',
        });
        navigate('/planillas');
      })
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    loadData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [id]);

  const handleToggleEstado = async () => {
    setToggling(true);
    try {
      if (planilla.cerrada) {
        await abrirPlanilla(id);
      } else {
        await cerrarPlanilla(id);
      }
      loadData();
    } catch (error) {
      const message = error.response?.data?.message || 'No se pudo cambiar el estado';
      notifications.show({ color: 'red', title: 'Error', message });
    } finally {
      setToggling(false);
    }
  };

  const handleDelete = async () => {
    setDeleting(true);
    try {
      await deletePlanilla(id);
      notifications.show({ color: 'green', title: 'Planilla eliminada', message: '' });
      navigate('/planillas');
    } catch (error) {
      const message = error.response?.data?.message || 'No se pudo eliminar la planilla';
      notifications.show({ color: 'red', title: 'Error', message });
      setConfirmDelete(false);
    } finally {
      setDeleting(false);
    }
  };

  if (loading) {
    return (
      <Group justify="center" mt="xl">
        <Loader />
      </Group>
    );
  }

  if (!planilla) {
    return null;
  }

  const totales = planilla.detalles.reduce(
    (acc, detalle) => ({
      sueldoBase: acc.sueldoBase + Number(detalle.sueldoBase ?? 0),
      asignacionFamiliar: acc.asignacionFamiliar + Number(detalle.asignacionFamiliar ?? 0),
      sueldoBruto: acc.sueldoBruto + Number(detalle.sueldoBruto ?? 0),
      totalDescuento: acc.totalDescuento + Number(detalle.totalDescuento ?? 0),
      sueldoNeto: acc.sueldoNeto + Number(detalle.sueldoNeto ?? 0),
    }),
    { sueldoBase: 0, asignacionFamiliar: 0, sueldoBruto: 0, totalDescuento: 0, sueldoNeto: 0 },
  );

  return (
    <Stack>
      <Group justify="space-between">
        <Group>
          <Button
            variant="subtle"
            leftSection={<IconArrowLeft size={16} />}
            onClick={() => navigate('/planillas')}
          >
            Volver
          </Button>
          <Title order={2}>
            Planilla {nombreMes(planilla.mes)} {planilla.anio}
          </Title>
          <Badge color={planilla.cerrada ? 'green' : 'yellow'} size="lg">
            {planilla.cerrada ? 'Cerrada' : 'Abierta'}
          </Badge>
        </Group>
        <Group>
          <Button
            variant="light"
            color={planilla.cerrada ? 'blue' : 'orange'}
            leftSection={planilla.cerrada ? <IconLockOpen size={16} /> : <IconLock size={16} />}
            loading={toggling}
            onClick={handleToggleEstado}
          >
            {planilla.cerrada ? 'Reabrir' : 'Cerrar'}
          </Button>
          <Button
            variant="light"
            color="red"
            leftSection={<IconTrash size={16} />}
            disabled={planilla.cerrada}
            onClick={() => setConfirmDelete(true)}
          >
            Eliminar
          </Button>
        </Group>
      </Group>

      <Card withBorder padding="md">
        <Group gap="xl">
          <Text size="sm">
            <Text span fw={600}>
              Tipo:
            </Text>{' '}
            {nombreTipoPlanilla(planilla.tipoPlanilla)}
          </Text>
          <Text size="sm">
            <Text span fw={600}>
              Fecha generada:
            </Text>{' '}
            {planilla.fechaGenerada}
          </Text>
          <Text size="sm">
            <Text span fw={600}>
              Empleados:
            </Text>{' '}
            {planilla.cantidadDetalles}
          </Text>
        </Group>
      </Card>

      <Table striped highlightOnHover withTableBorder verticalSpacing="sm">
        <Table.Thead>
          <Table.Tr>
            <Table.Th>Empleado</Table.Th>
            <Table.Th ta="right">Sueldo base</Table.Th>
            <Table.Th ta="right">Asig. familiar</Table.Th>
            <Table.Th ta="right">Bruto</Table.Th>
            <Table.Th ta="right">Descuentos</Table.Th>
            <Table.Th ta="right">Neto</Table.Th>
          </Table.Tr>
        </Table.Thead>
        <Table.Tbody>
          {planilla.detalles.map((detalle) => (
            <Table.Tr key={detalle.idDetalle}>
              <Table.Td>{detalle.empleado}</Table.Td>
              <Table.Td ta="right">{formatearMoneda(detalle.sueldoBase)}</Table.Td>
              <Table.Td ta="right">{formatearMoneda(detalle.asignacionFamiliar)}</Table.Td>
              <Table.Td ta="right">{formatearMoneda(detalle.sueldoBruto)}</Table.Td>
              <Table.Td ta="right" c="red">
                -{formatearMoneda(detalle.totalDescuento)}
              </Table.Td>
              <Table.Td ta="right" fw={700}>
                {formatearMoneda(detalle.sueldoNeto)}
              </Table.Td>
            </Table.Tr>
          ))}
        </Table.Tbody>
        <Table.Tfoot>
          <Table.Tr>
            <Table.Th>Total</Table.Th>
            <Table.Th ta="right">{formatearMoneda(totales.sueldoBase)}</Table.Th>
            <Table.Th ta="right">{formatearMoneda(totales.asignacionFamiliar)}</Table.Th>
            <Table.Th ta="right">{formatearMoneda(totales.sueldoBruto)}</Table.Th>
            <Table.Th ta="right" c="red">
              -{formatearMoneda(totales.totalDescuento)}
            </Table.Th>
            <Table.Th ta="right">{formatearMoneda(totales.sueldoNeto)}</Table.Th>
          </Table.Tr>
        </Table.Tfoot>
      </Table>

      {planilla.detalles.length === 0 && (
        <Text size="sm" c="dimmed">
          Esta planilla no tiene detalles
        </Text>
      )}

      <ConfirmModal
        opened={confirmDelete}
        title="Eliminar planilla"
        message="¿Seguro que quieres eliminar esta planilla? Esta acción no se puede deshacer."
        loading={deleting}
        onConfirm={handleDelete}
        onCancel={() => setConfirmDelete(false)}
      />
    </Stack>
  );
}
