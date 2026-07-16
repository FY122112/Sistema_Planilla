import { useEffect, useState } from 'react';
import {
  Button,
  Card,
  Checkbox,
  Group,
  Loader,
  Select,
  SimpleGrid,
  Stack,
  Text,
  TextInput,
  Title,
} from '@mantine/core';
import { useForm } from '@mantine/form';
import { notifications } from '@mantine/notifications';
import { useNavigate, useParams } from 'react-router-dom';
import { IconArrowLeft, IconDeviceFloppy } from '@tabler/icons-react';
import { createEmpleado, fetchEmpleadoById, updateEmpleado } from '../api/empleados';
import { fetchPuestos } from '../api/puestos';
import { fetchBancos } from '../api/bancos';
import { fetchSistemasPension } from '../api/sistemaPension';
import {
  ESTADO_CIVIL_OPTIONS,
  REGIMEN_LABORAL_OPTIONS,
  SEXO_OPTIONS,
  TIPO_DOCUMENTO_OPTIONS,
} from '../constants/empleado';
import { formatearMoneda } from '../constants/planilla';

const VALORES_INICIALES = {
  nombre: '',
  apellido: '',
  tipoDocumento: 'DNI',
  numeroDocumento: '',
  fechaNacimiento: '',
  sexo: '',
  estadoCivil: '',
  nacionalidad: 'Peruana',
  correo: '',
  telefono: '',
  direccionCompleta: '',
  distrito: '',
  provincia: '',
  departamento: '',
  fechaIngreso: '',
  idPuesto: '',
  regimenLaboral: 'GENERAL',
  tieneHijosCalificados: false,
  idSistemaPension: '',
  idBanco: '',
  codigoPension: '',
  nombreAfp: '',
  numeroCuentaBanco: '',
  estado: true,
  fechaCese: '',
};

export default function EmpleadoFormPage() {
  const { id } = useParams();
  const esEdicion = Boolean(id);
  const navigate = useNavigate();

  const [loading, setLoading] = useState(esEdicion);
  const [saving, setSaving] = useState(false);
  const [puestos, setPuestos] = useState([]);
  const [bancos, setBancos] = useState([]);
  const [sistemasPension, setSistemasPension] = useState([]);

  const form = useForm({
    initialValues: VALORES_INICIALES,
    validate: {
      nombre: (v) => (v.trim() ? null : 'El nombre es obligatorio'),
      apellido: (v) => (v.trim() ? null : 'El apellido es obligatorio'),
      numeroDocumento: (v) => (v.trim() ? null : 'El número de documento es obligatorio'),
      fechaNacimiento: (v) => (v ? null : 'La fecha de nacimiento es obligatoria'),
      sexo: (v) => (v ? null : 'El sexo es obligatorio'),
      estadoCivil: (v) => (v ? null : 'El estado civil es obligatorio'),
      correo: (v) => (!v || /^\S+@\S+\.\S+$/.test(v) ? null : 'Correo inválido'),
      fechaIngreso: (v) => (v ? null : 'La fecha de ingreso es obligatoria'),
      idPuesto: (v) => (v ? null : 'El puesto es obligatorio'),
      regimenLaboral: (v) => (v ? null : 'El régimen laboral es obligatorio'),
    },
  });

  useEffect(() => {
    Promise.all([fetchPuestos(), fetchBancos(), fetchSistemasPension()])
      .then(([puestosData, bancosData, sistemasData]) => {
        setPuestos(puestosData);
        setBancos(bancosData);
        setSistemasPension(sistemasData);
      })
      .catch(() => {
        notifications.show({
          color: 'red',
          title: 'Error',
          message: 'No se pudieron cargar los catálogos (puestos, bancos, sistema de pensión)',
        });
      });

    if (esEdicion) {
      fetchEmpleadoById(id)
        .then((empleado) => {
          form.setValues({
            nombre: empleado.nombre ?? '',
            apellido: empleado.apellido ?? '',
            tipoDocumento: empleado.tipoDocumento ?? 'DNI',
            numeroDocumento: empleado.numeroDocumento ?? '',
            fechaNacimiento: empleado.fechaNacimiento ?? '',
            sexo: empleado.sexo ?? '',
            estadoCivil: empleado.estadoCivil ?? '',
            nacionalidad: empleado.nacionalidad ?? '',
            correo: empleado.correo ?? '',
            telefono: empleado.telefono ?? '',
            direccionCompleta: empleado.direccionCompleta ?? '',
            distrito: empleado.distrito ?? '',
            provincia: empleado.provincia ?? '',
            departamento: empleado.departamento ?? '',
            fechaIngreso: empleado.fechaIngreso ?? '',
            idPuesto: empleado.idPuesto ? String(empleado.idPuesto) : '',
            regimenLaboral: empleado.regimenLaboral ?? 'GENERAL',
            tieneHijosCalificados: Boolean(empleado.tieneHijosCalificados),
            idSistemaPension: empleado.idSistemaPension ? String(empleado.idSistemaPension) : '',
            idBanco: empleado.idBanco ? String(empleado.idBanco) : '',
            codigoPension: empleado.codigoPension ?? '',
            nombreAfp: empleado.nombreAfp ?? '',
            numeroCuentaBanco: empleado.numeroCuentaBanco ?? '',
            estado: empleado.estado ?? true,
            fechaCese: empleado.fechaCese ?? '',
          });
        })
        .catch(() => {
          notifications.show({ color: 'red', title: 'Error', message: 'No se pudo cargar el empleado' });
          navigate('/empleados');
        })
        .finally(() => setLoading(false));
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [id]);

  const puestoSeleccionado = puestos.find((p) => String(p.idPuesto) === form.values.idPuesto);

  const handleSubmit = async (values) => {
    setSaving(true);

    const payloadComun = {
      nombre: values.nombre,
      apellido: values.apellido,
      tipoDocumento: values.tipoDocumento,
      numeroDocumento: values.numeroDocumento,
      fechaNacimiento: values.fechaNacimiento,
      sexo: values.sexo,
      estadoCivil: values.estadoCivil,
      nacionalidad: values.nacionalidad || null,
      correo: values.correo || null,
      telefono: values.telefono || null,
      direccionCompleta: values.direccionCompleta || null,
      distrito: values.distrito || null,
      provincia: values.provincia || null,
      departamento: values.departamento || null,
      fechaIngreso: values.fechaIngreso,
      idPuesto: Number(values.idPuesto),
      regimenLaboral: values.regimenLaboral,
      tieneHijosCalificados: values.tieneHijosCalificados,
      idSistemaPension: values.idSistemaPension ? Number(values.idSistemaPension) : null,
      idBanco: values.idBanco ? Number(values.idBanco) : null,
      codigoPension: values.codigoPension || null,
      nombreAfp: values.nombreAfp || null,
      numeroCuentaBanco: values.numeroCuentaBanco || null,
    };

    try {
      if (esEdicion) {
        await updateEmpleado(id, {
          ...payloadComun,
          estado: values.estado,
          fechaCese: values.estado ? null : values.fechaCese || null,
        });
        notifications.show({ color: 'green', title: 'Empleado actualizado', message: '' });
      } else {
        await createEmpleado(payloadComun);
        notifications.show({ color: 'green', title: 'Empleado creado', message: '' });
      }
      navigate('/empleados');
    } catch (error) {
      const message = error.response?.data?.message || 'No se pudo guardar el empleado';
      const detalles = error.response?.data?.details;
      notifications.show({
        color: 'red',
        title: 'Error',
        message: detalles?.length ? `${message}: ${detalles.join(', ')}` : message,
      });
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

  return (
    <Stack>
      <Group>
        <Button variant="subtle" leftSection={<IconArrowLeft size={16} />} onClick={() => navigate('/empleados')}>
          Volver
        </Button>
        <Title order={2}>{esEdicion ? 'Editar empleado' : 'Nuevo empleado'}</Title>
      </Group>

      <form onSubmit={form.onSubmit(handleSubmit)}>
        <Stack gap="lg">
          <Card withBorder padding="lg">
            <Title order={4} mb="md">
              Datos personales
            </Title>
            <SimpleGrid cols={{ base: 1, sm: 2, md: 3 }}>
              <TextInput name="nombre" label="Nombre" required {...form.getInputProps('nombre')} />
              <TextInput name="apellido" label="Apellido" required {...form.getInputProps('apellido')} />
              <Select
                name="tipoDocumento"
                label="Tipo de documento"
                data={TIPO_DOCUMENTO_OPTIONS}
                required
                {...form.getInputProps('tipoDocumento')}
              />
              <TextInput
                name="numeroDocumento"
                label="Número de documento"
                required
                {...form.getInputProps('numeroDocumento')}
              />
              <TextInput
                name="fechaNacimiento"
                label="Fecha de nacimiento"
                type="date"
                required
                {...form.getInputProps('fechaNacimiento')}
              />
              <Select name="sexo" label="Sexo" data={SEXO_OPTIONS} required {...form.getInputProps('sexo')} />
              <Select
                name="estadoCivil"
                label="Estado civil"
                data={ESTADO_CIVIL_OPTIONS}
                required
                {...form.getInputProps('estadoCivil')}
              />
              <TextInput name="nacionalidad" label="Nacionalidad" {...form.getInputProps('nacionalidad')} />
            </SimpleGrid>
          </Card>

          <Card withBorder padding="lg">
            <Title order={4} mb="md">
              Contacto y dirección
            </Title>
            <SimpleGrid cols={{ base: 1, sm: 2, md: 3 }}>
              <TextInput name="correo" label="Correo" {...form.getInputProps('correo')} />
              <TextInput
                name="telefono"
                label="Teléfono"
                description="Usado para el botón de WhatsApp en las boletas"
                {...form.getInputProps('telefono')}
              />
              <TextInput name="direccionCompleta" label="Dirección" {...form.getInputProps('direccionCompleta')} />
              <TextInput name="distrito" label="Distrito" {...form.getInputProps('distrito')} />
              <TextInput name="provincia" label="Provincia" {...form.getInputProps('provincia')} />
              <TextInput name="departamento" label="Departamento" {...form.getInputProps('departamento')} />
            </SimpleGrid>
          </Card>

          <Card withBorder padding="lg">
            <Title order={4} mb="md">
              Datos laborales
            </Title>
            <SimpleGrid cols={{ base: 1, sm: 2, md: 3 }}>
              <TextInput
                name="fechaIngreso"
                label="Fecha de ingreso"
                type="date"
                required
                {...form.getInputProps('fechaIngreso')}
              />
              <Select
                name="idPuesto"
                label="Puesto"
                placeholder="Selecciona un puesto"
                data={puestos.map((p) => ({ value: String(p.idPuesto), label: p.nombre }))}
                required
                {...form.getInputProps('idPuesto')}
              />
              <Select
                name="regimenLaboral"
                label="Régimen laboral"
                data={REGIMEN_LABORAL_OPTIONS}
                required
                {...form.getInputProps('regimenLaboral')}
              />
            </SimpleGrid>

            {puestoSeleccionado && (
              <Text size="sm" c="dimmed" mt="xs">
                Salario base del puesto: {formatearMoneda(puestoSeleccionado.salarioBase)}
              </Text>
            )}

            <Checkbox
              name="tieneHijosCalificados"
              label="Tiene hijos calificados (aplica a Asignación Familiar)"
              mt="md"
              {...form.getInputProps('tieneHijosCalificados', { type: 'checkbox' })}
            />

            {esEdicion && (
              <>
                <Checkbox
                  name="estado"
                  label="Empleado activo"
                  mt="md"
                  {...form.getInputProps('estado', { type: 'checkbox' })}
                />
                {!form.values.estado && (
                  <TextInput
                    name="fechaCese"
                    label="Fecha de cese"
                    type="date"
                    mt="sm"
                    maw={250}
                    {...form.getInputProps('fechaCese')}
                  />
                )}
              </>
            )}
          </Card>

          <Card withBorder padding="lg">
            <Title order={4} mb="md">
              Datos financieros
            </Title>
            <SimpleGrid cols={{ base: 1, sm: 2, md: 3 }}>
              <Select
                name="idSistemaPension"
                label="Sistema de pensión"
                placeholder="ONP / AFP"
                clearable
                data={sistemasPension.map((s) => ({ value: String(s.idSistema), label: `${s.nombre} (${s.tipo})` }))}
                {...form.getInputProps('idSistemaPension')}
              />
              <Select
                name="idBanco"
                label="Banco"
                placeholder="Banco de haberes"
                clearable
                data={bancos.map((b) => ({ value: String(b.id), label: b.nombreBanco }))}
                {...form.getInputProps('idBanco')}
              />
              <TextInput
                name="codigoPension"
                label="Código de pensión (CUSPP)"
                {...form.getInputProps('codigoPension')}
              />
              <TextInput name="nombreAfp" label="Nombre AFP" {...form.getInputProps('nombreAfp')} />
              <TextInput
                name="numeroCuentaBanco"
                label="Número de cuenta bancaria"
                {...form.getInputProps('numeroCuentaBanco')}
              />
            </SimpleGrid>
          </Card>

          <Group justify="flex-end">
            <Button variant="default" onClick={() => navigate('/empleados')}>
              Cancelar
            </Button>
            <Button type="submit" leftSection={<IconDeviceFloppy size={16} />} loading={saving}>
              Guardar
            </Button>
          </Group>
        </Stack>
      </form>
    </Stack>
  );
}
