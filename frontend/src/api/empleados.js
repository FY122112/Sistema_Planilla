import client from './client';

export function fetchEmpleados() {
  return client.get('/api/empleados').then((res) => res.data);
}

export function fetchEmpleadoById(id) {
  return client.get(`/api/empleados/${id}`).then((res) => res.data);
}

export function createEmpleado(data) {
  return client.post('/api/empleados', data).then((res) => res.data);
}

export function updateEmpleado(id, data) {
  return client.put(`/api/empleados/${id}`, data).then((res) => res.data);
}

export function deleteEmpleado(id) {
  return client.delete(`/api/empleados/${id}`);
}

export function importarEmpleadosCsv(archivo) {
  const formData = new FormData();
  formData.append('archivo', archivo);
  return client.post('/api/empleados/importar-csv', formData).then((res) => res.data);
}
