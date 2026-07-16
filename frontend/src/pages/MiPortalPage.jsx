import { useEffect, useState } from 'react';
import { Badge, Group, Loader, Stack, Table, Text, Title } from '@mantine/core';
import { notifications } from '@mantine/notifications';
import { useNavigate } from 'react-router-dom';
import { fetchMisBoletas } from '../api/boletas';
import { ESTADO_BOLETA_INFO } from '../constants/boleta';
import { formatearMoneda, nombreMes } from '../constants/planilla';

export default function MiPortalPage() {
  const navigate = useNavigate();
  const [boletas, setBoletas] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchMisBoletas()
      .then(setBoletas)
      .catch(() => {
        notifications.show({
          color: 'red',
          title: 'Error',
          message: 'No se pudo cargar tu historial de boletas',
        });
      })
      .finally(() => setLoading(false));
  }, []);

  if (loading) {
    return (
      <Group justify="center" mt="xl">
        <Loader />
      </Group>
    );
  }

  const anios = [...new Set(boletas.map((b) => b.periodoAnio))].sort((a, b) => b - a);

  return (
    <Stack>
      <Title order={2}>Mis boletas</Title>

      {anios.length === 0 && (
        <Text size="sm" c="dimmed">
          Todavía no tienes boletas generadas.
        </Text>
      )}

      {anios.map((anio) => (
        <Stack key={anio} gap="xs">
          <Title order={4}>{anio}</Title>
          <Table striped highlightOnHover withTableBorder verticalSpacing="sm">
            <Table.Thead>
              <Table.Tr>
                <Table.Th>Periodo</Table.Th>
                <Table.Th ta="right">Neto</Table.Th>
                <Table.Th>Estado</Table.Th>
                <Table.Th></Table.Th>
              </Table.Tr>
            </Table.Thead>
            <Table.Tbody>
              {boletas
                .filter((b) => b.periodoAnio === anio)
                .sort((a, b) => b.periodoMes - a.periodoMes)
                .map((boleta) => {
                  const estado = ESTADO_BOLETA_INFO[boleta.estadoBoleta] ?? {
                    label: boleta.estadoBoleta,
                    color: 'gray',
                  };
                  return (
                    <Table.Tr
                      key={boleta.idBoleta}
                      style={{ cursor: 'pointer' }}
                      onClick={() => navigate(`/mi-portal/boletas/${boleta.idBoleta}`)}
                    >
                      <Table.Td>{nombreMes(boleta.periodoMes)}</Table.Td>
                      <Table.Td ta="right" fw={600}>
                        {formatearMoneda(boleta.sueldoNeto)}
                      </Table.Td>
                      <Table.Td>
                        <Badge color={estado.color}>{estado.label}</Badge>
                      </Table.Td>
                      <Table.Td ta="right">
                        <Text size="sm" c="blue">
                          Ver
                        </Text>
                      </Table.Td>
                    </Table.Tr>
                  );
                })}
            </Table.Tbody>
          </Table>
        </Stack>
      ))}
    </Stack>
  );
}
