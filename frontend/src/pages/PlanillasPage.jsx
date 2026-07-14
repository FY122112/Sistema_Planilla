import { useEffect, useState } from 'react';
import {
  ActionIcon,
  Badge,
  Button,
  Group,
  Modal,
  NumberInput,
  Select,
  Table,
  Text,
  Title,
  Tooltip,
} from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import { useForm } from '@mantine/form';
import { notifications } from '@mantine/notifications';
import { useNavigate } from 'react-router-dom';
import {
  IconEye,
  IconLock,
  IconLockOpen,
  IconPlus,
  IconTrash,
} from '@tabler/icons-react';
import {
  abrirPlanilla,
  cerrarPlanilla,
  deletePlanilla,
  fetchPlanillas,
  generarPlanilla,
} from '../api/planillas';
import { MESES, TIPO_PLANILLA_OPTIONS, nombreMes, nombreTipoPlanilla } from '../constants/planilla';
import ConfirmModal from '../components/ConfirmModal';

export default function PlanillasPage() {
  const navigate = useNavigate();
  const [planillas, setPlanillas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [opened, { open, close }] = useDisclosure(false);
  const [togglingId, setTogglingId] = useState(null);
  const [toDelete, setToDelete] = useState(null);
  const [deleting, setDeleting] = useState(false);

  const currentYear = new Date().getFullYear();

  const form = useForm({
    initialValues: {
      mes: String(new Date().getMonth() + 1),
      anio: currentYear,
      tipoPlanilla: 'MENSUAL',
    },
    validate: {
      mes: (value) => (value ? null : 'El mes es obligatorio'),
      anio: (value) => (value && value > 0 ? null : 'El año es obligatorio'),
      tipoPlanilla: (value) => (value ? null : 'El tipo es obligatorio'),
    },
  });

  const loadData = () => {
    setLoading(true);
    return fetchPlanillas()
      .then(setPlanillas)
      .catch(() => {
        notifications.show({
          color: 'red',
          title: 'Error',
          message: 'No se pudo cargar la lista de planillas',
        });
      })
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleGenerar = async (values) => {
    setSaving(true);
    try {
      await generarPlanilla({
        mes: Number(values.mes),
        anio: Number(values.anio),
        tipoPlanilla: values.tipoPlanilla,
      });

      notifications.show({
        color: 'green',
        title: 'Planilla generada',
        message: `Planilla de ${nombreMes(values.mes)} ${values.anio} creada correctamente`,
      });

      close();
      form.reset();
      loadData();
    } catch (error) {
      const message = error.response?.data?.message || 'No se pudo generar la planilla';
      notifications.show({ color: 'red', title: 'Error', message });
    } finally {
      setSaving(false);
    }
  };

  const handleToggleEstado = async (planilla) => {
    setTogglingId(planilla.idPlanilla);
    try {
      if (planilla.cerrada) {
        await abrirPlanilla(planilla.idPlanilla);
      } else {
        await cerrarPlanilla(planilla.idPlanilla);
      }
      loadData();
    } catch (error) {
      const message = error.response?.data?.message || 'No se pudo cambiar el estado de la planilla';
      notifications.show({ color: 'red', title: 'Error', message });
    } finally {
      setTogglingId(null);
    }
  };

  const handleDelete = async () => {
    if (!toDelete) return;
    setDeleting(true);
    try {
      await deletePlanilla(toDelete.idPlanilla);
      notifications.show({ color: 'green', title: 'Planilla eliminada', message: '' });
      setToDelete(null);
      loadData();
    } catch (error) {
      const message = error.response?.data?.message || 'No se pudo eliminar la planilla';
      notifications.show({ color: 'red', title: 'Error', message });
    } finally {
      setDeleting(false);
    }
  };

  return (
    <>
      <Group justify="space-between" mb="md">
        <Title order={2}>Planillas</Title>
        <Button leftSection={<IconPlus size={16} />} onClick={open}>
          Generar planilla
        </Button>
      </Group>

      <Table striped highlightOnHover withTableBorder verticalSpacing="sm">
        <Table.Thead>
          <Table.Tr>
            <Table.Th>Periodo</Table.Th>
            <Table.Th>Tipo</Table.Th>
            <Table.Th>Empleados</Table.Th>
            <Table.Th>Fecha generada</Table.Th>
            <Table.Th>Estado</Table.Th>
            <Table.Th ta="right">Acciones</Table.Th>
          </Table.Tr>
        </Table.Thead>
        <Table.Tbody>
          {planillas.map((planilla) => (
            <Table.Tr key={planilla.idPlanilla}>
              <Table.Td>
                {nombreMes(planilla.mes)} {planilla.anio}
              </Table.Td>
              <Table.Td>{nombreTipoPlanilla(planilla.tipoPlanilla)}</Table.Td>
              <Table.Td>{planilla.cantidadDetalles}</Table.Td>
              <Table.Td>{planilla.fechaGenerada}</Table.Td>
              <Table.Td>
                <Badge color={planilla.cerrada ? 'green' : 'yellow'}>
                  {planilla.cerrada ? 'Cerrada' : 'Abierta'}
                </Badge>
              </Table.Td>
              <Table.Td>
                <Group justify="flex-end" gap="xs">
                  <Tooltip label="Ver detalle">
                    <ActionIcon
                      variant="subtle"
                      onClick={() => navigate(`/planillas/${planilla.idPlanilla}`)}
                    >
                      <IconEye size={18} />
                    </ActionIcon>
                  </Tooltip>
                  <Tooltip label={planilla.cerrada ? 'Reabrir' : 'Cerrar'}>
                    <ActionIcon
                      variant="subtle"
                      color={planilla.cerrada ? 'blue' : 'orange'}
                      loading={togglingId === planilla.idPlanilla}
                      onClick={() => handleToggleEstado(planilla)}
                    >
                      {planilla.cerrada ? <IconLockOpen size={18} /> : <IconLock size={18} />}
                    </ActionIcon>
                  </Tooltip>
                  <Tooltip label={planilla.cerrada ? 'No se puede eliminar una planilla cerrada' : 'Eliminar'}>
                    <ActionIcon
                      variant="subtle"
                      color="red"
                      disabled={planilla.cerrada}
                      onClick={() => setToDelete(planilla)}
                    >
                      <IconTrash size={18} />
                    </ActionIcon>
                  </Tooltip>
                </Group>
              </Table.Td>
            </Table.Tr>
          ))}
        </Table.Tbody>
      </Table>

      {!loading && planillas.length === 0 && (
        <Text size="sm" c="dimmed" mt="sm">
          No hay planillas generadas todavía
        </Text>
      )}

      <Modal opened={opened} onClose={close} title="Generar planilla" centered>
        <form onSubmit={form.onSubmit(handleGenerar)}>
          <Select
            label="Mes"
            data={MESES}
            required
            {...form.getInputProps('mes')}
          />
          <NumberInput
            label="Año"
            min={2000}
            max={2100}
            required
            mt="sm"
            {...form.getInputProps('anio')}
          />
          <Select
            label="Tipo de planilla"
            data={TIPO_PLANILLA_OPTIONS}
            required
            mt="sm"
            {...form.getInputProps('tipoPlanilla')}
          />
          <Group justify="flex-end" mt="lg">
            <Button variant="default" onClick={close}>
              Cancelar
            </Button>
            <Button type="submit" loading={saving}>
              Generar
            </Button>
          </Group>
        </form>
      </Modal>

      <ConfirmModal
        opened={Boolean(toDelete)}
        title="Eliminar planilla"
        message={
          toDelete
            ? `¿Seguro que quieres eliminar la planilla de ${nombreMes(toDelete.mes)} ${toDelete.anio}? Esta acción no se puede deshacer.`
            : ''
        }
        loading={deleting}
        onConfirm={handleDelete}
        onCancel={() => setToDelete(null)}
      />
    </>
  );
}
