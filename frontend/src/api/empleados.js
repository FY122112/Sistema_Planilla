import client from './client';

export function fetchEmpleados() {
  return client.get('/api/empleados').then((res) => res.data);
}

export function searchEmpleados(query) {
  return client.get('/api/empleados/search', { params: { query } }).then((res) => res.data);
}
