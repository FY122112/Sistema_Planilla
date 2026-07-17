import { useState } from 'react';
import {
  Badge,
  Button,
  Card,
  Group,
  Modal,
  Select,
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
import { IconMessageCircle } from '@tabler/icons-react';
import { fetchReporteDiario, justificarAsistencia, marcarAsistencia } from '../api/asistencia';
import { ESTADO_ASISTENCIA_INFO } from '../constants/asistencia';

const hoyISO = () => new Date().toISOString().slice(0, 10);

export default function AsistenciaPage() {
  const [marcando, setMarcando] = useState(false);
  const [fecha, setFecha] = useState(hoyISO());
  const [reporte, setReporte] = useState([]);
  const [loadingReporte, setLoadingReporte] = useState(false);
  const [justificando, setJustificando] = useState(null);
  const [opened, { open, close }] = useDisclosure(false);
  const [saving, setSaving] = useState(false);

  const marcarForm = useForm({
    initialValues: { numeroDocumento: '', tipoMarca: 'entrada' },
    validate: {
      numeroDocumento: (value) => (value.trim().length === 0 ? 'El documento es obligatorio' : null),
    },
  });

  const justificarForm = useForm({
    initialValues: { motivo: '' },
    validate: {
      motivo: (value) => (value.trim().length === 0 ? 'El motivo es obligatorio' : null),
    },
  });

  const cargarReporte = (fechaConsulta) => {
    setLoadingReporte(true);
    fetchReporteDiario(fechaConsulta)
      .then(setReporte)
      .catch(() => {
        notifications.show({ color: 'red', title: 'Error', message: 'No se pudo cargar el reporte de asistencia' });
      })
      .finally(() => setLoadingReporte(false));
  };

  const handleMarcar = async (values) => {
    setMarcando(true);
    try {
      await marcarAsistencia(values.numeroDocumento, values.tipoMarca);
      notifications.show({
        color: 'green',
        title: 'Asistencia registrada',
        message: `Se marcó ${values.tipoMarca} para el documento ${values.numeroDocumento}`,
      });
      marcarForm.setFieldValue('numeroDocumento', '');
      if (fecha === hoyISO()) {
        cargarReporte(fecha);
      }
    } catch (error) {
      const message = error.response?.data?.message || 'No se pudo registrar la asistencia';
      notifications.show({ color: 'red', title: 'Error', message });
    } finally {
      setMarcando(false);
    }
  };

  const handleAbrirJustificar = (registro) => {
    setJustificando(registro);
    justificarForm.setValues({ motivo: registro.justificacion ?? '' });
    open();
  };

  const handleSubmitJustificar = async (values) => {
    setSaving(true);
    try {
      await justificarAsistencia(justificando.id, values.motivo);
      notifications.show({ color: 'green', title: 'Asistencia justificada', message: '' });
      close();
      setJustificando(null);
      cargarReporte(fecha);
    } catch (error) {
      const message = error.response?.data?.message || 'No se pudo justificar la asistencia';
      notifications.show({ color: 'red', title: 'Error', message });
    } finally {
      setSaving(false);
    }
  };

  return (
    <>
      <Title order={2} mb="md">
        Asistencia
      </Title>

      <Card withBorder mb="lg">
        <Title order={4} mb="sm">
          Marcar asistencia
        </Title>
        <form onSubmit={marcarForm.onSubmit(handleMarcar)}>
          <Group align="flex-end">
            <TextInput
              label="Número de documento"
              placeholder="DNI del empleado"
              style={{ flex: 1 }}
              {...marcarForm.getInputProps('numeroDocumento')}
            />
            <Select
              label="Tipo de marca"
              data={[
                { value: 'entrada', label: 'Entrada' },
                { value: 'salida', label: 'Salida' },
              ]}
              {...marcarForm.getInputProps('tipoMarca')}
            />
            <Button type="submit" loading={marcando}>
              Marcar
            </Button>
          </Group>
        </form>
      </Card>

      <Card withBorder>
        <Group justify="space-between" mb="sm">
          <Title order={4}>Reporte diario</Title>
          <Group>
            <TextInput
              type="date"
              value={fecha}
              onChange={(event) => setFecha(event.currentTarget.value)}
            />
            <Button variant="light" loading={loadingReporte} onClick={() => cargarReporte(fecha)}>
              Consultar
            </Button>
          </Group>
        </Group>

        <Table striped highlightOnHover withTableBorder>
          <Table.Thead>
            <Table.Tr>
              <Table.Th>Empleado</Table.Th>
              <Table.Th>Entrada</Table.Th>
              <Table.Th>Salida</Table.Th>
              <Table.Th>Tardanza</Table.Th>
              <Table.Th>Extras 25%</Table.Th>
              <Table.Th>Estado</Table.Th>
              <Table.Th>Justificación</Table.Th>
              <Table.Th ta="right">Acciones</Table.Th>
            </Table.Tr>
          </Table.Thead>
          <Table.Tbody>
            {reporte.map((registro) => {
              const estado = ESTADO_ASISTENCIA_INFO[registro.estado] ?? { label: registro.estado, color: 'gray' };
              return (
                <Table.Tr key={registro.id}>
                  <Table.Td>{registro.empleado}</Table.Td>
                  <Table.Td>{registro.horaEntrada ?? '—'}</Table.Td>
                  <Table.Td>{registro.horaSalida ?? '—'}</Table.Td>
                  <Table.Td>{registro.minutosTardanzas ? `${registro.minutosTardanzas} min` : '—'}</Table.Td>
                  <Table.Td>{registro.horasExtras25 ? `${registro.horasExtras25} h` : '—'}</Table.Td>
                  <Table.Td>
                    <Badge color={estado.color}>{estado.label}</Badge>
                  </Table.Td>
                  <Table.Td>
                    <Text size="sm" c="dimmed" lineClamp={1}>
                      {registro.justificacion || '—'}
                    </Text>
                  </Table.Td>
                  <Table.Td>
                    <Group justify="flex-end">
                      <Tooltip label="Justificar">
                        <ActionIcon variant="subtle" onClick={() => handleAbrirJustificar(registro)}>
                          <IconMessageCircle size={18} />
                        </ActionIcon>
                      </Tooltip>
                    </Group>
                  </Table.Td>
                </Table.Tr>
              );
            })}
          </Table.Tbody>
        </Table>

        {!loadingReporte && reporte.length === 0 && (
          <Text size="sm" c="dimmed" mt="sm">
            No hay marcas de asistencia registradas en esta fecha.
          </Text>
        )}
      </Card>

      <Modal opened={opened} onClose={close} title={`Justificar — ${justificando?.empleado ?? ''}`} centered>
        <form onSubmit={justificarForm.onSubmit(handleSubmitJustificar)}>
          <Textarea
            label="Motivo"
            placeholder="Motivo de la falta o tardanza"
            required
            autosize
            minRows={3}
            {...justificarForm.getInputProps('motivo')}
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
    </>
  );
}
