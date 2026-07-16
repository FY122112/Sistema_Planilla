import { useEffect, useState } from 'react';
import {
  ActionIcon,
  Alert,
  Badge,
  Button,
  Card,
  Group,
  Loader,
  Stack,
  Table,
  Text,
  Title,
  Tooltip,
} from '@mantine/core';
import { notifications } from '@mantine/notifications';
import { useNavigate, useParams } from 'react-router-dom';
import {
  IconAlertTriangle,
  IconArrowLeft,
  IconArrowsSort,
  IconFileSpreadsheet,
  IconFileText,
  IconFileZip,
  IconLock,
  IconLockOpen,
  IconPencil,
  IconSortAscending,
  IconSortDescending,
  IconTrash,
} from '@tabler/icons-react';
import {
  abrirPlanilla,
  cerrarPlanilla,
  deletePlanilla,
  descargarPlanillaExcel,
  fetchPlanillaById,
} from '../api/planillas';
import { createBoleta, descargarBoletasZip, fetchBoletas } from '../api/boletas';
import { fetchIdsEmpleadosSinAsistencia } from '../api/asistencia';
import { formatearMoneda, nombreMes, nombreTipoPlanilla } from '../constants/planilla';
import ConfirmModal from '../components/ConfirmModal';

export default function PlanillaDetallePage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [planilla, setPlanilla] = useState(null);
  // idDetalle -> idBoleta, para saber en qué filas ya existe boleta generada
  const [boletasPorDetalle, setBoletasPorDetalle] = useState({});
  const [loading, setLoading] = useState(true);
  const [toggling, setToggling] = useState(false);
  const [generandoBoletaId, setGenerandoBoletaId] = useState(null);
  const [confirmDelete, setConfirmDelete] = useState(false);
  const [deleting, setDeleting] = useState(false);
  const [exportando, setExportando] = useState(false);
  const [descargandoZip, setDescargandoZip] = useState(false);
  const [ordenPor, setOrdenPor] = useState(null); // 'empleado' | 'neto' | null
  const [ordenAsc, setOrdenAsc] = useState(true);
  const [empleadosSinAsistencia, setEmpleadosSinAsistencia] = useState([]);

  const loadData = () => {
    setLoading(true);
    return Promise.all([fetchPlanillaById(id), fetchBoletas()])
      .then(([planillaData, boletas]) => {
        setPlanilla(planillaData);

        const mapa = {};
        boletas
          .filter((b) => planillaData.detalles.some((d) => d.idDetalle === b.idDetalle))
          .forEach((b) => {
            mapa[b.idDetalle] = b.idBoleta;
          });
        setBoletasPorDetalle(mapa);

        const idsEmpleados = planillaData.detalles.map((d) => d.idEmpleado).filter(Boolean);
        if (idsEmpleados.length > 0) {
          fetchIdsEmpleadosSinAsistencia(idsEmpleados, planillaData.mes, planillaData.anio)
            .then(setEmpleadosSinAsistencia)
            .catch(() => setEmpleadosSinAsistencia([]));
        }
      })
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

  const handleGenerarBoleta = async (detalle) => {
    setGenerandoBoletaId(detalle.idDetalle);
    try {
      const boleta = await createBoleta({
        idDetalle: detalle.idDetalle,
        fechaEmision: new Date().toISOString().slice(0, 10),
        periodoMes: planilla.mes,
        periodoAnio: planilla.anio,
        sueldoBruto: detalle.sueldoBruto,
        totalDescuento: detalle.totalDescuento,
      });

      notifications.show({
        color: 'green',
        title: 'Boleta generada',
        message: 'Si el empleado tiene correo registrado, se le envió una notificación.',
      });
      navigate(`/boletas/${boleta.idBoleta}`);
    } catch (error) {
      const message = error.response?.data?.message || 'No se pudo generar la boleta';
      notifications.show({ color: 'red', title: 'Error', message });
    } finally {
      setGenerandoBoletaId(null);
    }
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

  const handleExportar = async () => {
    setExportando(true);
    try {
      const blob = await descargarPlanillaExcel(id);
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = `planilla-${planilla.mes}-${planilla.anio}.xlsx`;
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);
    } catch (error) {
      notifications.show({ color: 'red', title: 'Error', message: 'No se pudo exportar la planilla' });
    } finally {
      setExportando(false);
    }
  };

  const handleDescargarZip = async () => {
    setDescargandoZip(true);
    try {
      const blob = await descargarBoletasZip(planilla.mes, planilla.anio);
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = `boletas-${planilla.mes}-${planilla.anio}.zip`;
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);
    } catch (error) {
      notifications.show({ color: 'red', title: 'Error', message: 'No se pudo descargar el ZIP de boletas' });
    } finally {
      setDescargandoZip(false);
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

  const handleOrdenar = (campo) => {
    if (ordenPor === campo) {
      setOrdenAsc((asc) => !asc);
    } else {
      setOrdenPor(campo);
      setOrdenAsc(true);
    }
  };

  const iconoOrden = (campo) => {
    if (ordenPor !== campo) return <IconArrowsSort size={14} />;
    return ordenAsc ? <IconSortAscending size={14} /> : <IconSortDescending size={14} />;
  };

  const detallesOrdenados = [...planilla.detalles].sort((a, b) => {
    if (!ordenPor) return 0;
    const factor = ordenAsc ? 1 : -1;
    if (ordenPor === 'empleado') {
      return a.empleado.localeCompare(b.empleado) * factor;
    }
    return (Number(a.sueldoNeto) - Number(b.sueldoNeto)) * factor;
  });

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
            leftSection={<IconFileSpreadsheet size={16} />}
            loading={exportando}
            onClick={handleExportar}
          >
            Exportar
          </Button>
          <Button
            variant="light"
            leftSection={<IconFileZip size={16} />}
            loading={descargandoZip}
            onClick={handleDescargarZip}
          >
            Boletas (.zip)
          </Button>
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

      {empleadosSinAsistencia.length > 0 && (
        <Alert
          color="yellow"
          icon={<IconAlertTriangle size={18} />}
          title="Registros de asistencia incompletos"
        >
          {empleadosSinAsistencia.length} empleado(s) de esta planilla no tienen ninguna marca de
          asistencia registrada en {nombreMes(planilla.mes)} {planilla.anio} — revisa sus filas
          (resaltadas abajo) antes de procesar los pagos.
        </Alert>
      )}

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
            <Table.Th style={{ cursor: 'pointer' }} onClick={() => handleOrdenar('empleado')}>
              <Group gap={4} wrap="nowrap">
                Empleado {iconoOrden('empleado')}
              </Group>
            </Table.Th>
            <Table.Th ta="right">Sueldo base</Table.Th>
            <Table.Th ta="right">Asig. familiar</Table.Th>
            <Table.Th ta="right">Bruto</Table.Th>
            <Table.Th ta="right">Descuentos</Table.Th>
            <Table.Th ta="right" style={{ cursor: 'pointer' }} onClick={() => handleOrdenar('neto')}>
              <Group gap={4} wrap="nowrap" justify="flex-end">
                Neto {iconoOrden('neto')}
              </Group>
            </Table.Th>
            <Table.Th ta="right">Editar</Table.Th>
            <Table.Th ta="right">Boleta</Table.Th>
          </Table.Tr>
        </Table.Thead>
        <Table.Tbody>
          {detallesOrdenados.map((detalle) => {
            const idBoletaExistente = boletasPorDetalle[detalle.idDetalle];
            const sinAsistencia = empleadosSinAsistencia.includes(detalle.idEmpleado);
            return (
              <Table.Tr key={detalle.idDetalle} bg={sinAsistencia ? 'yellow.0' : undefined}>
                <Table.Td>
                  {detalle.empleado}
                  {sinAsistencia && (
                    <Tooltip label="Sin marcas de asistencia este mes">
                      <IconAlertTriangle size={14} color="var(--mantine-color-yellow-7)" style={{ marginLeft: 6, verticalAlign: 'middle' }} />
                    </Tooltip>
                  )}
                </Table.Td>
                <Table.Td ta="right">{formatearMoneda(detalle.sueldoBase)}</Table.Td>
                <Table.Td ta="right">{formatearMoneda(detalle.asignacionFamiliar)}</Table.Td>
                <Table.Td ta="right">{formatearMoneda(detalle.sueldoBruto)}</Table.Td>
                <Table.Td ta="right" c="red">
                  -{formatearMoneda(detalle.totalDescuento)}
                </Table.Td>
                <Table.Td ta="right" fw={700}>
                  {formatearMoneda(detalle.sueldoNeto)}
                </Table.Td>
                <Table.Td ta="right">
                  <Tooltip label={planilla.cerrada ? 'La planilla está cerrada' : 'Editar nómina individual'}>
                    <ActionIcon
                      variant="light"
                      disabled={planilla.cerrada}
                      onClick={() => navigate(`/planillas/${id}/detalles/${detalle.idDetalle}/editar`)}
                    >
                      <IconPencil size={16} />
                    </ActionIcon>
                  </Tooltip>
                </Table.Td>
                <Table.Td ta="right">
                  {idBoletaExistente ? (
                    <Button
                      size="xs"
                      variant="light"
                      leftSection={<IconFileText size={14} />}
                      onClick={() => navigate(`/boletas/${idBoletaExistente}`)}
                    >
                      Ver boleta
                    </Button>
                  ) : (
                    <Button
                      size="xs"
                      variant="outline"
                      loading={generandoBoletaId === detalle.idDetalle}
                      onClick={() => handleGenerarBoleta(detalle)}
                    >
                      Generar boleta
                    </Button>
                  )}
                </Table.Td>
              </Table.Tr>
            );
          })}
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
            <Table.Th></Table.Th>
            <Table.Th></Table.Th>
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
