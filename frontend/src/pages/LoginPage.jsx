import { useState } from 'react';
import { Button, Card, Container, PasswordInput, Text, TextInput, Title } from '@mantine/core';
import { useForm } from '@mantine/form';
import { notifications } from '@mantine/notifications';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';

export default function LoginPage() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);

  const form = useForm({
    initialValues: { username: '', password: '' },
    validate: {
      username: (value) => (value.trim().length === 0 ? 'El usuario es obligatorio' : null),
      password: (value) => (value.length === 0 ? 'La contraseña es obligatoria' : null),
    },
  });

  const handleSubmit = async (values) => {
    setLoading(true);
    try {
      await login(values.username, values.password);
      navigate('/planillas', { replace: true });
    } catch (error) {
      const message =
        error.response?.data?.message || 'Usuario o contraseña incorrectos';
      notifications.show({ color: 'red', title: 'No se pudo iniciar sesión', message });
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container size={420} my={80}>
      <Title ta="center">Sistema Planilla</Title>
      <Text ta="center" c="dimmed" size="sm" mt={5}>
        TextiLima SAC
      </Text>

      <Card withBorder shadow="sm" padding="lg" radius="md" mt="xl">
        <form onSubmit={form.onSubmit(handleSubmit)}>
          <TextInput
            label="Usuario"
            placeholder="tu.usuario"
            required
            {...form.getInputProps('username')}
          />
          <PasswordInput
            label="Contraseña"
            placeholder="Tu contraseña"
            required
            mt="md"
            {...form.getInputProps('password')}
          />
          <Button type="submit" fullWidth mt="xl" loading={loading}>
            Iniciar sesión
          </Button>
        </form>
      </Card>
    </Container>
  );
}
