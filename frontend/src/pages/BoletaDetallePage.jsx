import { useEffect, useState } from 'react';
import {
  Badge,
  Button,
  Card,
  Divider,
  Grid,
  Group,
  Loader,
  Stack,
  Table,
  Text,
  Title,
} from '@mantine/core';
import { notifications } from '@mantine/notifications';
import { useNavigate, useParams } from 'react-router-dom';
import { IconArrowLeft, IconPrinter, IconTrash } from '@tabler/icons-react';
import { deleteBoleta, fetchBoletaById, updateBoleta } from '../api/boletas';
import { fetchEmpresas } from '../api/empresa';
import { ESTADO_BOLETA_INFO, ORDEN_ESTADOS_BOLETA, siguienteEstado } from '../constants/boleta';
import { formatearMoneda, nombreMes } from '../constants/planilla';
import ConfirmModal from '../components/ConfirmModal';

const ACCION_POR_ESTADO = {
  FIRMADA: 'Marcar como firmada',
  ENVIADA: 'Marcar como enviada',
  PAGADA: 'Marcar como pagada',
};

export default function BoletaDetallePage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [boleta, setBoleta] = useState(null);
  const [empresa, setEmpresa] = useState(null);
  const [loading, setLoading] = useState(true);
  const [avanzando, setAvanzando] = useState(false);
  const [confirmDelete, setConfirmDelete] = useState(false);
  const [deleting, setDeleting] = useState(false);

  const loadData = () => {
    setLoading(true);
    return Promise.all([fetchBoletaById(id), fetchEmpresas()])
      .then(([boletaData, empresas]) => {
        setBoleta(boletaData);
        setEmpresa(empresas[0] ?? null);
      })
      .catch(() => {
        notifications.show({ color: 'red', title: 'Error', message: 'No se pudo cargar la boleta' });
        navigate('/boletas');
      })
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    loadData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [id]);

  const handleAvanzarEstado = async () => {
    const nuevoEstado = siguienteEstado(boleta.estadoBoleta);
    if (!nuevoEstado) return;

    setAvanzando(true);
    try {
      await updateBoleta(id, { estadoBoleta: nuevoEstado });
      notifications.show({ color: 'green', title: 'Estado actualizado', message: ACCION_POR_ESTADO[nuevoEstado] });
      loadData();
    } catch (error) {
      const message = error.response?.data?.message || 'No se pudo actualizar el estado';
      notifications.show({ color: 'red', title: 'Error', message });
    } finally {
      setAvanzando(false);
    }
  };

  const handleDelete = async () => {
    setDeleting(true);
    try {
      await deleteBoleta(id);
      notifications.show({ color: 'green', title: 'Boleta eliminada', message: '' });
      navigate('/boletas');
    } catch (error) {
      const message = error.response?.data?.message || 'No se pudo eliminar la boleta';
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

  if (!boleta) return null;

  const estado = ESTADO_BOLETA_INFO[boleta.estadoBoleta] ?? { label: boleta.estadoBoleta, color: 'gray' };
  const proximoEstado = siguienteEstado(boleta.estadoBoleta);
  const esUltimoEstado = ORDEN_ESTADOS_BOLETA.indexOf(boleta.estadoBoleta) === ORDEN_ESTADOS_BOLETA.length - 1;

  return (
    <Stack>
      <Group justify="space-between" className="no-print">
        <Group>
          <Button variant="subtle" leftSection={<IconArrowLeft size={16} />} onClick={() => navigate('/boletas')}>
            Volver
          </Button>
          <Title order={2}>Boleta de Pago</Title>
          <Badge color={estado.color} size="lg">
            {estado.label}
          </Badge>
        </Group>
        <Group>
          {proximoEstado && (
            <Button variant="light" loading={avanzando} onClick={handleAvanzarEstado}>
              {ACCION_POR_ESTADO[proximoEstado]}
            </Button>
          )}
          <Button variant="light" leftSection={<IconPrinter size={16} />} onClick={() => window.print()}>
            Imprimir
          </Button>
          <Button
            variant="light"
            color="red"
            leftSection={<IconTrash size={16} />}
            onClick={() => setConfirmDelete(true)}
          >
            Eliminar
          </Button>
        </Group>
      </Group>

      <Card withBorder padding="xl" id="boleta-imprimible">
        <Group justify="space-between" mb="lg">
          <div>
            <Text fw={700} size="lg">
              {empresa?.razonSocial ?? 'TextiLima SAC'}
            </Text>
            <Text size="sm" c="dimmed">
              RUC: {empresa?.ruc ?? '-'}
            </Text>
            <Text size="sm" c="dimmed">
              {empresa?.direccion ?? '-'}
            </Text>
          </div>
          <div style={{ textAlign: 'right' }}>
            <Text fw={700}>BOLETA DE PAGO</Text>
            <Text size="sm" c="dimmed">
              {nombreMes(boleta.periodoMes)} {boleta.periodoAnio}
            </Text>
            <Text size="sm" c="dimmed">
              Emitida: {boleta.fechaEmision}
            </Text>
          </div>
        </Group>

        <Divider mb="md" />

        <Grid mb="lg">
          <Grid.Col span={6}>
            <Text size="sm" c="dimmed">
              Trabajador
            </Text>
            <Text fw={600}>{boleta.empleado ?? '—'}</Text>
          </Grid.Col>
        </Grid>

        <Table withTableBorder verticalSpacing="sm">
          <Table.Thead>
            <Table.Tr>
              <Table.Th>Concepto</Table.Th>
              <Table.Th ta="right">Monto</Table.Th>
            </Table.Tr>
          </Table.Thead>
          <Table.Tbody>
            <Table.Tr>
              <Table.Td>Ingresos (sueldo bruto)</Table.Td>
              <Table.Td ta="right">{formatearMoneda(boleta.sueldoBruto)}</Table.Td>
            </Table.Tr>
            <Table.Tr>
              <Table.Td>Total descuentos</Table.Td>
              <Table.Td ta="right" c="red">
                -{formatearMoneda(boleta.totalDescuento)}
              </Table.Td>
            </Table.Tr>
          </Table.Tbody>
          <Table.Tfoot>
            <Table.Tr>
              <Table.Th>Neto a pagar</Table.Th>
              <Table.Th ta="right" fz="lg">
                {formatearMoneda(boleta.sueldoNeto)}
              </Table.Th>
            </Table.Tr>
          </Table.Tfoot>
        </Table>

        {esUltimoEstado && (
          <Text size="sm" c="green" mt="md" ta="center">
            ✓ Boleta pagada
          </Text>
        )}
      </Card>

      <ConfirmModal
        opened={confirmDelete}
        title="Eliminar boleta"
        message="¿Seguro que quieres eliminar esta boleta? Esta acción no se puede deshacer."
        loading={deleting}
        onConfirm={handleDelete}
        onCancel={() => setConfirmDelete(false)}
      />
    </Stack>
  );
}
