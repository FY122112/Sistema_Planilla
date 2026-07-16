import { useEffect, useState } from 'react';
import { Badge, Button, Group, Table, Text, Title } from '@mantine/core';
import { notifications } from '@mantine/notifications';
import { atenderSolicitudAjuste, fetchSolicitudesAjuste } from '../api/solicitudesAjuste';
import { nombreMes } from '../constants/planilla';

export default function SolicitudesAjustePage() {
  const [solicitudes, setSolicitudes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [atendiendoId, setAtendiendoId] = useState(null);

  const loadData = () => {
    setLoading(true);
    return fetchSolicitudesAjuste()
      .then(setSolicitudes)
      .catch(() => {
        notifications.show({
          color: 'red',
          title: 'Error',
          message: 'No se pudo cargar el buzón de solicitudes',
        });
      })
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleAtender = async (solicitud) => {
    setAtendiendoId(solicitud.idSolicitud);
    try {
      await atenderSolicitudAjuste(solicitud.idSolicitud);
      notifications.show({ color: 'green', title: 'Solicitud atendida', message: '' });
      loadData();
    } catch (error) {
      const message = error.response?.data?.message || 'No se pudo actualizar la solicitud';
      notifications.show({ color: 'red', title: 'Error', message });
    } finally {
      setAtendiendoId(null);
    }
  };

  return (
    <>
      <Title order={2} mb="md">
        Buzón de Solicitudes de Ajuste
      </Title>

      <Table striped highlightOnHover withTableBorder verticalSpacing="sm">
        <Table.Thead>
          <Table.Tr>
            <Table.Th>Fecha</Table.Th>
            <Table.Th>Empleado</Table.Th>
            <Table.Th>Boleta</Table.Th>
            <Table.Th>Mensaje</Table.Th>
            <Table.Th>Estado</Table.Th>
            <Table.Th ta="right">Acciones</Table.Th>
          </Table.Tr>
        </Table.Thead>
        <Table.Tbody>
          {solicitudes.map((s) => (
            <Table.Tr key={s.idSolicitud}>
              <Table.Td>{new Date(s.fechaCreacion).toLocaleString('es-PE')}</Table.Td>
              <Table.Td>{s.nombreEmpleado}</Table.Td>
              <Table.Td>
                {nombreMes(s.periodoMes)} {s.periodoAnio}
              </Table.Td>
              <Table.Td maw={400}>{s.mensaje}</Table.Td>
              <Table.Td>
                <Badge color={s.estado === 'PENDIENTE' ? 'yellow' : 'green'}>
                  {s.estado === 'PENDIENTE' ? 'Pendiente' : 'Atendida'}
                </Badge>
              </Table.Td>
              <Table.Td>
                <Group justify="flex-end">
                  {s.estado === 'PENDIENTE' && (
                    <Button
                      size="xs"
                      variant="light"
                      loading={atendiendoId === s.idSolicitud}
                      onClick={() => handleAtender(s)}
                    >
                      Marcar atendida
                    </Button>
                  )}
                </Group>
              </Table.Td>
            </Table.Tr>
          ))}
        </Table.Tbody>
      </Table>

      {!loading && solicitudes.length === 0 && (
        <Text size="sm" c="dimmed" mt="sm">
          No hay solicitudes de ajuste registradas
        </Text>
      )}
    </>
  );
}
