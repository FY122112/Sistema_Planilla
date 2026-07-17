import { useState } from 'react';
import { Button, Card, Group, Stack, Text, Title } from '@mantine/core';
import { notifications } from '@mantine/notifications';
import { IconClockPlay, IconClockStop } from '@tabler/icons-react';
import { marcarMiAsistencia } from '../api/asistencia';

export default function MiAsistenciaPage() {
  const [marcando, setMarcando] = useState(null);
  const [ultimaMarca, setUltimaMarca] = useState(null);

  const handleMarcar = async (tipoMarca) => {
    setMarcando(tipoMarca);
    try {
      const resultado = await marcarMiAsistencia(tipoMarca);
      setUltimaMarca(resultado);
      notifications.show({
        color: 'green',
        title: tipoMarca === 'entrada' ? 'Entrada registrada' : 'Salida registrada',
        message: `Hora: ${tipoMarca === 'entrada' ? resultado.horaEntrada : resultado.horaSalida}`,
      });
    } catch (error) {
      const message = error.response?.data?.message || 'No se pudo registrar la marca';
      notifications.show({ color: 'red', title: 'Error', message });
    } finally {
      setMarcando(null);
    }
  };

  return (
    <Stack>
      <Title order={2}>Mi asistencia</Title>

      <Card withBorder>
        <Text size="sm" c="dimmed" mb="md">
          Marca tu entrada al llegar y tu salida al terminar tu jornada.
        </Text>

        <Group>
          <Button
            leftSection={<IconClockPlay size={18} />}
            loading={marcando === 'entrada'}
            onClick={() => handleMarcar('entrada')}
          >
            Marcar entrada
          </Button>
          <Button
            variant="light"
            leftSection={<IconClockStop size={18} />}
            loading={marcando === 'salida'}
            onClick={() => handleMarcar('salida')}
          >
            Marcar salida
          </Button>
        </Group>

        {ultimaMarca && (
          <Text size="sm" c="dimmed" mt="lg">
            Hoy ({ultimaMarca.fecha}): entrada {ultimaMarca.horaEntrada ?? '—'}, salida{' '}
            {ultimaMarca.horaSalida ?? '—'}
          </Text>
        )}
      </Card>
    </Stack>
  );
}
