import { useEffect, useState } from 'react';
import {
  Badge,
  Button,
  Card,
  Divider,
  Group,
  Loader,
  NumberInput,
  SimpleGrid,
  Stack,
  Table,
  Text,
  TextInput,
  Title,
} from '@mantine/core';
import { useForm } from '@mantine/form';
import { useDebouncedValue } from '@mantine/hooks';
import { notifications } from '@mantine/notifications';
import { useNavigate, useParams } from 'react-router-dom';
import { IconArrowLeft, IconDeviceFloppy } from '@tabler/icons-react';
import { fetchPlanillaById, previewDetallePlanilla, updateDetallePlanilla } from '../api/planillas';
import { formatearMoneda, nombreMes } from '../constants/planilla';

const VALORES_INICIALES = {
  diasNoLaborados: 0,
  minutosTardanza: 0,
  horasExtras25: 0,
  horasExtras35: 0,
  diasVacacionesGozadas: 0,
  vacacionesFechaInicio: '',
  vacacionesFechaFin: '',
  bonificacionEficiencia: 0,
  comisionComercial: 0,
};

function numOrZero(v) {
  return v === '' || v === null || v === undefined ? 0 : Number(v);
}

function dateOrNull(v) {
  return v ? v : null;
}

export default function PlanillaDetalleEditarPage() {
  const { id, idDetalle } = useParams();
  const navigate = useNavigate();

  const [planilla, setPlanilla] = useState(null);
  const [detalle, setDetalle] = useState(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [preview, setPreview] = useState(null);
  const [previewLoading, setPreviewLoading] = useState(false);

  const form = useForm({
    initialValues: VALORES_INICIALES,
    validate: {
      vacacionesFechaFin: (value, values) =>
        value && values.vacacionesFechaInicio && value < values.vacacionesFechaInicio
          ? 'La fecha de fin no puede ser anterior a la fecha de inicio'
          : null,
    },
  });
  const [debouncedValues] = useDebouncedValue(form.values, 300);

  useEffect(() => {
    fetchPlanillaById(id)
      .then((planillaData) => {
        if (planillaData.cerrada) {
          notifications.show({
            color: 'yellow',
            title: 'Planilla cerrada',
            message: 'No se puede editar el detalle de una planilla cerrada',
          });
          navigate(`/planillas/${id}`);
          return;
        }

        const detalleData = planillaData.detalles.find(
          (d) => String(d.idDetalle) === String(idDetalle),
        );

        if (!detalleData) {
          notifications.show({ color: 'red', title: 'Error', message: 'Detalle no encontrado' });
          navigate(`/planillas/${id}`);
          return;
        }

        setPlanilla(planillaData);
        setDetalle(detalleData);
        setPreview(detalleData);

        form.setValues({
          diasNoLaborados: detalleData.diasNoLaborados ?? 0,
          minutosTardanza: detalleData.minutosTardanza ?? 0,
          horasExtras25: Number(detalleData.horasExtras25 ?? 0),
          horasExtras35: Number(detalleData.horasExtras35 ?? 0),
          diasVacacionesGozadas: detalleData.diasVacacionesGozadas ?? 0,
          vacacionesFechaInicio: detalleData.vacacionesFechaInicio ?? '',
          vacacionesFechaFin: detalleData.vacacionesFechaFin ?? '',
          bonificacionEficiencia: Number(detalleData.bonificacionEficiencia ?? 0),
          comisionComercial: Number(detalleData.comisionComercial ?? 0),
        });
      })
      .catch(() => {
        notifications.show({ color: 'red', title: 'Error', message: 'No se pudo cargar la planilla' });
        navigate(`/planillas/${id}`);
      })
      .finally(() => setLoading(false));
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [id, idDetalle]);

  useEffect(() => {
    if (loading) return;

    const payload = {
      diasNoLaborados: numOrZero(debouncedValues.diasNoLaborados),
      minutosTardanza: numOrZero(debouncedValues.minutosTardanza),
      horasExtras25: numOrZero(debouncedValues.horasExtras25),
      horasExtras35: numOrZero(debouncedValues.horasExtras35),
      diasVacacionesGozadas: numOrZero(debouncedValues.diasVacacionesGozadas),
      vacacionesFechaInicio: dateOrNull(debouncedValues.vacacionesFechaInicio),
      vacacionesFechaFin: dateOrNull(debouncedValues.vacacionesFechaFin),
      bonificacionEficiencia: numOrZero(debouncedValues.bonificacionEficiencia),
      comisionComercial: numOrZero(debouncedValues.comisionComercial),
    };

    setPreviewLoading(true);
    previewDetallePlanilla(id, idDetalle, payload)
      .then((data) => setPreview(data))
      .catch(() => {
        notifications.show({ color: 'red', title: 'Error', message: 'No se pudo recalcular el detalle' });
      })
      .finally(() => setPreviewLoading(false));
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [debouncedValues, loading]);

  const handleSubmit = async (values) => {
    setSaving(true);
    try {
      await updateDetallePlanilla(id, idDetalle, {
        diasNoLaborados: numOrZero(values.diasNoLaborados),
        minutosTardanza: numOrZero(values.minutosTardanza),
        horasExtras25: numOrZero(values.horasExtras25),
        horasExtras35: numOrZero(values.horasExtras35),
        diasVacacionesGozadas: numOrZero(values.diasVacacionesGozadas),
        vacacionesFechaInicio: dateOrNull(values.vacacionesFechaInicio),
        vacacionesFechaFin: dateOrNull(values.vacacionesFechaFin),
        bonificacionEficiencia: numOrZero(values.bonificacionEficiencia),
        comisionComercial: numOrZero(values.comisionComercial),
      });
      notifications.show({ color: 'green', title: 'Remuneración actualizada', message: '' });
      navigate(`/planillas/${id}`);
    } catch (error) {
      const message = error.response?.data?.message || 'No se pudo guardar el detalle';
      notifications.show({ color: 'red', title: 'Error', message });
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return (
      <Group justify="center" mt="xl">
        <Loader />
      </Group>
    );
  }

  if (!planilla || !detalle) {
    return null;
  }

  const ingresos = (preview?.movimientos ?? []).filter((m) => m.tipoConcepto === 'INGRESO');
  const descuentos = (preview?.movimientos ?? []).filter((m) => m.tipoConcepto === 'DESCUENTO');
  const aportes = (preview?.movimientos ?? []).filter((m) => m.tipoConcepto === 'APORTE_EMPLEADOR');

  return (
    <Stack>
      <Group>
        <Button variant="subtle" leftSection={<IconArrowLeft size={16} />} onClick={() => navigate(`/planillas/${id}`)}>
          Volver
        </Button>
        <Title order={2}>Editar remuneración — {detalle.empleado}</Title>
        <Badge color="gray" size="lg">
          {nombreMes(planilla.mes)} {planilla.anio}
        </Badge>
      </Group>

      <Card withBorder padding="md">
        <Group gap="xl">
          <Text size="sm">
            <Text span fw={600}>
              Sueldo base:
            </Text>{' '}
            {formatearMoneda(detalle.sueldoBase)}
          </Text>
          <Text size="sm">
            <Text span fw={600}>
              Asignación familiar:
            </Text>{' '}
            {formatearMoneda(detalle.asignacionFamiliar)}
          </Text>
        </Group>
      </Card>

      <form onSubmit={form.onSubmit(handleSubmit)}>
        <Stack gap="lg">
          <Card withBorder padding="lg">
            <Title order={4} mb="md">
              Variables del periodo
            </Title>
            <SimpleGrid cols={{ base: 1, sm: 2, md: 3 }}>
              <NumberInput
                label="Días no laborados"
                min={0}
                {...form.getInputProps('diasNoLaborados')}
              />
              <NumberInput
                label="Minutos de tardanza"
                min={0}
                {...form.getInputProps('minutosTardanza')}
              />
              <NumberInput
                label="Horas extra 25%"
                min={0}
                decimalScale={2}
                {...form.getInputProps('horasExtras25')}
              />
              <NumberInput
                label="Horas extra 35%"
                min={0}
                decimalScale={2}
                {...form.getInputProps('horasExtras35')}
              />
              <NumberInput
                label="Días de vacaciones gozadas"
                min={0}
                {...form.getInputProps('diasVacacionesGozadas')}
              />
              <TextInput
                type="date"
                label="Inicio del periodo de vacaciones"
                disabled={!numOrZero(form.values.diasVacacionesGozadas)}
                {...form.getInputProps('vacacionesFechaInicio')}
              />
              <TextInput
                type="date"
                label="Fin del periodo de vacaciones"
                disabled={!numOrZero(form.values.diasVacacionesGozadas)}
                {...form.getInputProps('vacacionesFechaFin')}
              />
              <NumberInput
                label="Bonificación de eficiencia (S/)"
                min={0}
                decimalScale={2}
                {...form.getInputProps('bonificacionEficiencia')}
              />
              <NumberInput
                label="Comisión comercial (S/)"
                min={0}
                decimalScale={2}
                {...form.getInputProps('comisionComercial')}
              />
            </SimpleGrid>
          </Card>

          <Card withBorder padding="lg">
            <Group justify="space-between" mb="md">
              <Title order={4}>Desglose recalculado</Title>
              {previewLoading && <Loader size="xs" />}
            </Group>

            <Table verticalSpacing="xs">
              <Table.Tbody>
                {ingresos.map((m, i) => (
                  <Table.Tr key={`ing-${i}`}>
                    <Table.Td>{m.nombreConcepto}</Table.Td>
                    <Table.Td ta="right" c="green">
                      +{formatearMoneda(m.monto)}
                    </Table.Td>
                  </Table.Tr>
                ))}
                {descuentos.map((m, i) => (
                  <Table.Tr key={`desc-${i}`}>
                    <Table.Td>{m.nombreConcepto}</Table.Td>
                    <Table.Td ta="right" c="red">
                      -{formatearMoneda(m.monto)}
                    </Table.Td>
                  </Table.Tr>
                ))}
                {aportes.map((m, i) => (
                  <Table.Tr key={`ap-${i}`}>
                    <Table.Td>
                      <Text span c="dimmed">
                        {m.nombreConcepto} (aporte del empleador, no afecta el neto)
                      </Text>
                    </Table.Td>
                    <Table.Td ta="right" c="dimmed">
                      {formatearMoneda(m.monto)}
                    </Table.Td>
                  </Table.Tr>
                ))}
              </Table.Tbody>
            </Table>

            <Divider my="sm" />

            <Group justify="space-between">
              <Text fw={600}>Sueldo bruto</Text>
              <Text>{formatearMoneda(preview?.sueldoBruto)}</Text>
            </Group>
            <Group justify="space-between">
              <Text fw={600}>Total descuentos</Text>
              <Text c="red">-{formatearMoneda(preview?.totalDescuento)}</Text>
            </Group>
            <Group justify="space-between">
              <Text fw={600}>Aportes del empleador</Text>
              <Text c="dimmed">{formatearMoneda(preview?.totalAportesEmpleador)}</Text>
            </Group>
            <Group justify="space-between" mt="xs">
              <Text fw={700} size="lg">
                Neto a pagar
              </Text>
              <Text fw={700} size="lg">
                {formatearMoneda(preview?.sueldoNeto)}
              </Text>
            </Group>
          </Card>

          <Group justify="flex-end">
            <Button variant="default" onClick={() => navigate(`/planillas/${id}`)}>
              Cancelar
            </Button>
            <Button type="submit" leftSection={<IconDeviceFloppy size={16} />} loading={saving}>
              Modificar Remuneración
            </Button>
          </Group>
        </Stack>
      </form>
    </Stack>
  );
}
