import { useEffect, useState } from 'react';
import {
  Badge,
  Button,
  Card,
  Divider,
  Grid,
  Group,
  Loader,
  Modal,
  Stack,
  Table,
  Text,
  Textarea,
  Title,
} from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import { notifications } from '@mantine/notifications';
import { useNavigate, useParams } from 'react-router-dom';
import { IconArrowLeft, IconCheck, IconFileTypePdf, IconMessageReport, IconPrinter } from '@tabler/icons-react';
import { descargarBoletaPdf, fetchBoletaById, firmarBoleta } from '../api/boletas';
import { fetchEmpresas } from '../api/empresa';
import { crearSolicitudAjuste } from '../api/solicitudesAjuste';
import { ESTADO_BOLETA_INFO } from '../constants/boleta';
import { formatearMoneda, formatearVacacionesGozadas, nombreMes } from '../constants/planilla';

export default function MiBoletaDetallePage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [boleta, setBoleta] = useState(null);
  const [empresa, setEmpresa] = useState(null);
  const [loading, setLoading] = useState(true);
  const [descargando, setDescargando] = useState(false);
  const [firmando, setFirmando] = useState(false);
  const [reclamoOpened, { open: openReclamo, close: closeReclamo }] = useDisclosure(false);
  const [mensajeReclamo, setMensajeReclamo] = useState('');
  const [enviandoReclamo, setEnviandoReclamo] = useState(false);

  const loadData = () => {
    setLoading(true);
    return Promise.all([fetchBoletaById(id), fetchEmpresas()])
      .then(([boletaData, empresas]) => {
        setBoleta(boletaData);
        setEmpresa(empresas[0] ?? null);
      })
      .catch(() => {
        notifications.show({ color: 'red', title: 'Error', message: 'No se pudo cargar la boleta' });
        navigate('/mi-portal');
      })
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    loadData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [id]);

  const handleDescargarPdf = async () => {
    setDescargando(true);
    try {
      const blob = await descargarBoletaPdf(id);
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = `boleta-${boleta.numeroDocumento ?? id}-${boleta.periodoMes}-${boleta.periodoAnio}.pdf`;
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);
    } catch (error) {
      notifications.show({ color: 'red', title: 'Error', message: 'No se pudo descargar el PDF' });
    } finally {
      setDescargando(false);
    }
  };

  const handleFirmar = async () => {
    setFirmando(true);
    try {
      await firmarBoleta(id);
      notifications.show({ color: 'green', title: 'Conformidad confirmada', message: '' });
      loadData();
    } catch (error) {
      const message = error.response?.data?.message || 'No se pudo confirmar la conformidad';
      notifications.show({ color: 'red', title: 'Error', message });
    } finally {
      setFirmando(false);
    }
  };

  const handleEnviarReclamo = async () => {
    if (!mensajeReclamo.trim()) return;
    setEnviandoReclamo(true);
    try {
      await crearSolicitudAjuste({ idBoleta: id, mensaje: mensajeReclamo.trim() });
      notifications.show({
        color: 'green',
        title: 'Reclamo enviado',
        message: 'El área de contabilidad revisará tu solicitud',
      });
      closeReclamo();
      setMensajeReclamo('');
    } catch (error) {
      const message = error.response?.data?.message || 'No se pudo enviar el reclamo';
      notifications.show({ color: 'red', title: 'Error', message });
    } finally {
      setEnviandoReclamo(false);
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

  return (
    <Stack>
      <Group justify="space-between" className="no-print">
        <Group>
          <Button variant="subtle" leftSection={<IconArrowLeft size={16} />} onClick={() => navigate('/mi-portal')}>
            Volver
          </Button>
          <Title order={2}>Boleta de Pago</Title>
          <Badge color={estado.color} size="lg">
            {estado.label}
          </Badge>
        </Group>
        <Group>
          <Button variant="light" leftSection={<IconPrinter size={16} />} onClick={() => window.print()}>
            Imprimir
          </Button>
          <Button
            variant="light"
            leftSection={<IconFileTypePdf size={16} />}
            loading={descargando}
            onClick={handleDescargarPdf}
          >
            Descargar PDF
          </Button>
          <Button variant="light" color="orange" leftSection={<IconMessageReport size={16} />} onClick={openReclamo}>
            Reportar un problema
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
            <Text size="sm" c="dimmed" mt={4}>
              DNI: {boleta.numeroDocumento ?? '—'} · Cargo: {boleta.cargo ?? '—'}
            </Text>
          </Grid.Col>
          <Grid.Col span={6}>
            <Text size="sm" c="dimmed">
              Datos previsionales y de abono
            </Text>
            <Text fw={600}>{boleta.nombreBanco ?? '—'}</Text>
            <Text size="sm" c="dimmed" mt={4}>
              Cuenta: {boleta.numeroCuentaBanco ?? '—'} · AFP/Pensión: {boleta.nombreSistemaPension ?? '—'}
            </Text>
          </Grid.Col>
        </Grid>

        {(boleta.diasVacacionesGozadas > 0 || Number(boleta.horasExtras25) > 0 || Number(boleta.horasExtras35) > 0) && (
          <Text size="sm" c="dimmed" mb="sm">
            {formatearVacacionesGozadas(
              boleta.diasVacacionesGozadas,
              boleta.vacacionesFechaInicio,
              boleta.vacacionesFechaFin,
            )}{' '}
            {Number(boleta.horasExtras25) > 0 && `Horas extra 25%: ${boleta.horasExtras25}h. `}
            {Number(boleta.horasExtras35) > 0 && `Horas extra 35%: ${boleta.horasExtras35}h.`}
          </Text>
        )}

        <Table withTableBorder verticalSpacing="sm">
          <Table.Thead>
            <Table.Tr>
              <Table.Th>Concepto</Table.Th>
              <Table.Th ta="right">Monto</Table.Th>
            </Table.Tr>
          </Table.Thead>
          <Table.Tbody>
            <Table.Tr>
              <Table.Td fw={600}>Sueldo base</Table.Td>
              <Table.Td ta="right">{formatearMoneda(boleta.sueldoBruto)}</Table.Td>
            </Table.Tr>
            {(boleta.movimientos ?? [])
              .filter((m) => m.tipoConcepto === 'INGRESO')
              .map((m, i) => (
                <Table.Tr key={`ing-${i}`}>
                  <Table.Td>{m.nombreConcepto}</Table.Td>
                  <Table.Td ta="right" c="green">
                    +{formatearMoneda(m.monto)}
                  </Table.Td>
                </Table.Tr>
              ))}
            {(boleta.movimientos ?? [])
              .filter((m) => m.tipoConcepto === 'DESCUENTO')
              .map((m, i) => (
                <Table.Tr key={`desc-${i}`}>
                  <Table.Td>{m.nombreConcepto}</Table.Td>
                  <Table.Td ta="right" c="red">
                    -{formatearMoneda(m.monto)}
                  </Table.Td>
                </Table.Tr>
              ))}
            {(boleta.movimientos ?? [])
              .filter((m) => m.tipoConcepto === 'APORTE_EMPLEADOR')
              .map((m, i) => (
                <Table.Tr key={`ap-${i}`}>
                  <Table.Td>
                    <Text span c="dimmed">
                      {m.nombreConcepto} (aporte del empleador)
                    </Text>
                  </Table.Td>
                  <Table.Td ta="right" c="dimmed">
                    {formatearMoneda(m.monto)}
                  </Table.Td>
                </Table.Tr>
              ))}
            <Table.Tr>
              <Table.Td fw={600}>Total descuentos</Table.Td>
              <Table.Td ta="right" c="red" fw={600}>
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

        <Group justify="center" mt="lg" className="no-print">
          {boleta.estadoBoleta === 'GENERADA' ? (
            <Button leftSection={<IconCheck size={16} />} loading={firmando} onClick={handleFirmar}>
              Confirmar conformidad
            </Button>
          ) : (
            <Text size="sm" c="green">
              ✓ Firmada {boleta.fechaFirma ? `el ${new Date(boleta.fechaFirma).toLocaleString('es-PE')}` : ''}
            </Text>
          )}
        </Group>
      </Card>

      <Modal
        opened={reclamoOpened}
        onClose={closeReclamo}
        title="Reportar un problema con esta boleta"
        centered
        className="no-print"
      >
        <Text size="sm" c="dimmed" mb="sm">
          Describe el error que encontraste (por ejemplo, horas extras o bonos mal calculados).
          Tu mensaje llegará directamente al área de contabilidad.
        </Text>
        <Textarea
          placeholder="Escribe tu reclamo aquí..."
          minRows={4}
          autosize
          value={mensajeReclamo}
          onChange={(event) => setMensajeReclamo(event.currentTarget.value)}
        />
        <Group justify="flex-end" mt="md">
          <Button variant="default" onClick={closeReclamo}>
            Cancelar
          </Button>
          <Button loading={enviandoReclamo} disabled={!mensajeReclamo.trim()} onClick={handleEnviarReclamo}>
            Enviar reclamo
          </Button>
        </Group>
      </Modal>
    </Stack>
  );
}
