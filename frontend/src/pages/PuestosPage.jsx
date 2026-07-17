import { useEffect, useState } from 'react';
import {
  Button,
  Group,
  Modal,
  NumberInput,
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
import { IconEdit, IconPlus, IconTrash } from '@tabler/icons-react';
import { createPuesto, deletePuesto, fetchPuestos, updatePuesto } from '../api/puestos';
import { formatearMoneda } from '../constants/planilla';
import ConfirmModal from '../components/ConfirmModal';

const initialValues = {
  nombre: '',
  salarioBase: 0,
  jornadaLaboralHoras: 8,
  horaInicioJornada: '08:00',
  horaFinJornada: '17:00',
  descripcion: '',
};

export default function PuestosPage() {
  const [puestos, setPuestos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [opened, { open, close }] = useDisclosure(false);
  const [editando, setEditando] = useState(null);
  const [toDelete, setToDelete] = useState(null);
  const [deleting, setDeleting] = useState(false);

  const form = useForm({
    initialValues,
    validate: {
      nombre: (value) => (value.trim().length === 0 ? 'El nombre es obligatorio' : null),
      salarioBase: (value) => (value > 0 ? null : 'El salario debe ser mayor a 0'),
    },
  });

  const loadData = () => {
    setLoading(true);
    return fetchPuestos()
      .then(setPuestos)
      .catch(() => {
        notifications.show({ color: 'red', title: 'Error', message: 'No se pudieron cargar los puestos' });
      })
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleAbrirCrear = () => {
    setEditando(null);
    form.setValues(initialValues);
    open();
  };

  const handleAbrirEditar = (puesto) => {
    setEditando(puesto);
    form.setValues({
      nombre: puesto.nombre,
      salarioBase: Number(puesto.salarioBase),
      jornadaLaboralHoras: puesto.jornadaLaboralHoras ?? 8,
      horaInicioJornada: puesto.horaInicioJornada?.slice(0, 5) ?? '08:00',
      horaFinJornada: puesto.horaFinJornada?.slice(0, 5) ?? '17:00',
      descripcion: puesto.descripcion ?? '',
    });
    open();
  };

  const handleSubmit = async (values) => {
    setSaving(true);
    try {
      if (editando) {
        await updatePuesto(editando.idPuesto, values);
        notifications.show({ color: 'green', title: 'Puesto actualizado', message: values.nombre });
      } else {
        await createPuesto(values);
        notifications.show({ color: 'green', title: 'Puesto creado', message: values.nombre });
      }

      form.reset();
      setEditando(null);
      close();
      loadData();
    } catch (error) {
      const message = error.response?.data?.message || 'No se pudo guardar el puesto';
      notifications.show({ color: 'red', title: 'Error', message });
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async () => {
    if (!toDelete) return;
    setDeleting(true);
    try {
      await deletePuesto(toDelete.idPuesto);
      notifications.show({ color: 'green', title: 'Puesto eliminado', message: '' });
      setToDelete(null);
      loadData();
    } catch (error) {
      const message = error.response?.data?.message || 'No se pudo eliminar el puesto (puede estar en uso)';
      notifications.show({ color: 'red', title: 'Error', message });
    } finally {
      setDeleting(false);
    }
  };

  return (
    <>
      <Group justify="space-between" mb="md">
        <Title order={2}>Puestos</Title>
        <Button leftSection={<IconPlus size={16} />} onClick={handleAbrirCrear}>
          Nuevo puesto
        </Button>
      </Group>

      <Table striped highlightOnHover withTableBorder>
        <Table.Thead>
          <Table.Tr>
            <Table.Th>Nombre</Table.Th>
            <Table.Th ta="right">Salario base</Table.Th>
            <Table.Th>Jornada</Table.Th>
            <Table.Th>Descripción</Table.Th>
            <Table.Th ta="right">Acciones</Table.Th>
          </Table.Tr>
        </Table.Thead>
        <Table.Tbody>
          {puestos.map((puesto) => (
            <Table.Tr key={puesto.idPuesto}>
              <Table.Td>{puesto.nombre}</Table.Td>
              <Table.Td ta="right">{formatearMoneda(puesto.salarioBase)}</Table.Td>
              <Table.Td>
                {puesto.jornadaLaboralHoras}h ({puesto.horaInicioJornada?.slice(0, 5)} - {puesto.horaFinJornada?.slice(0, 5)})
              </Table.Td>
              <Table.Td>
                <Text size="sm" c="dimmed" lineClamp={1}>
                  {puesto.descripcion || '—'}
                </Text>
              </Table.Td>
              <Table.Td>
                <Group justify="flex-end">
                  <Tooltip label="Editar">
                    <ActionIcon variant="subtle" onClick={() => handleAbrirEditar(puesto)}>
                      <IconEdit size={18} />
                    </ActionIcon>
                  </Tooltip>
                  <Tooltip label="Eliminar">
                    <ActionIcon variant="subtle" color="red" onClick={() => setToDelete(puesto)}>
                      <IconTrash size={18} />
                    </ActionIcon>
                  </Tooltip>
                </Group>
              </Table.Td>
            </Table.Tr>
          ))}
        </Table.Tbody>
      </Table>

      {!loading && puestos.length === 0 && (
        <Text size="sm" c="dimmed" mt="sm">
          No hay puestos registrados
        </Text>
      )}

      <Modal opened={opened} onClose={close} title={editando ? 'Editar puesto' : 'Nuevo puesto'} centered>
        <form onSubmit={form.onSubmit(handleSubmit)}>
          <TextInput label="Nombre" placeholder="Operario de Corte" required {...form.getInputProps('nombre')} />
          <NumberInput
            label="Salario base"
            placeholder="1700"
            required
            min={0}
            decimalScale={2}
            mt="sm"
            {...form.getInputProps('salarioBase')}
          />
          <NumberInput
            label="Jornada laboral (horas)"
            min={1}
            max={24}
            mt="sm"
            {...form.getInputProps('jornadaLaboralHoras')}
          />
          <Group grow mt="sm">
            <TextInput
              type="time"
              label="Hora inicio"
              {...form.getInputProps('horaInicioJornada')}
            />
            <TextInput
              type="time"
              label="Hora fin"
              {...form.getInputProps('horaFinJornada')}
            />
          </Group>
          <Textarea
            label="Descripción"
            placeholder="Detalle del puesto"
            mt="sm"
            autosize
            minRows={2}
            {...form.getInputProps('descripcion')}
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

      <ConfirmModal
        opened={Boolean(toDelete)}
        title="Eliminar puesto"
        message={
          toDelete
            ? `¿Seguro que quieres eliminar el puesto "${toDelete.nombre}"? Esta acción no se puede deshacer.`
            : ''
        }
        loading={deleting}
        onConfirm={handleDelete}
        onCancel={() => setToDelete(null)}
      />
    </>
  );
}
