import { useEffect, useState } from 'react';
import { ActionIcon, Badge, Group, Table, Text, Title, Tooltip } from '@mantine/core';
import { useNavigate } from 'react-router-dom';
import { notifications } from '@mantine/notifications';
import { IconEye } from '@tabler/icons-react';
import { fetchBoletas } from '../api/boletas';
import { ESTADO_BOLETA_INFO } from '../constants/boleta';
import { formatearMoneda, nombreMes } from '../constants/planilla';

export default function BoletasPage() {
  const navigate = useNavigate();
  const [boletas, setBoletas] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchBoletas()
      .then(setBoletas)
      .catch(() => {
        notifications.show({
          color: 'red',
          title: 'Error',
          message: 'No se pudo cargar la lista de boletas',
        });
      })
      .finally(() => setLoading(false));
  }, []);

  return (
    <>
      <Title order={2} mb="md">
        Boletas
      </Title>

      <Table striped highlightOnHover withTableBorder verticalSpacing="sm">
        <Table.Thead>
          <Table.Tr>
            <Table.Th>Empleado</Table.Th>
            <Table.Th>Periodo</Table.Th>
            <Table.Th ta="right">Bruto</Table.Th>
            <Table.Th ta="right">Descuentos</Table.Th>
            <Table.Th ta="right">Neto</Table.Th>
            <Table.Th>Estado</Table.Th>
            <Table.Th ta="right">Acciones</Table.Th>
          </Table.Tr>
        </Table.Thead>
        <Table.Tbody>
          {boletas.map((boleta) => {
            const estado = ESTADO_BOLETA_INFO[boleta.estadoBoleta] ?? { label: boleta.estadoBoleta, color: 'gray' };
            return (
              <Table.Tr key={boleta.idBoleta}>
                <Table.Td>{boleta.empleado ?? '—'}</Table.Td>
                <Table.Td>
                  {nombreMes(boleta.periodoMes)} {boleta.periodoAnio}
                </Table.Td>
                <Table.Td ta="right">{formatearMoneda(boleta.sueldoBruto)}</Table.Td>
                <Table.Td ta="right" c="red">
                  -{formatearMoneda(boleta.totalDescuento)}
                </Table.Td>
                <Table.Td ta="right" fw={700}>
                  {formatearMoneda(boleta.sueldoNeto)}
                </Table.Td>
                <Table.Td>
                  <Badge color={estado.color}>{estado.label}</Badge>
                </Table.Td>
                <Table.Td>
                  <Group justify="flex-end">
                    <Tooltip label="Ver boleta">
                      <ActionIcon variant="subtle" onClick={() => navigate(`/boletas/${boleta.idBoleta}`)}>
                        <IconEye size={18} />
                      </ActionIcon>
                    </Tooltip>
                  </Group>
                </Table.Td>
              </Table.Tr>
            );
          })}
        </Table.Tbody>
      </Table>

      {!loading && boletas.length === 0 && (
        <Text size="sm" c="dimmed" mt="sm">
          No hay boletas generadas todavía. Puedes generarlas desde el detalle de una planilla.
        </Text>
      )}
    </>
  );
}
