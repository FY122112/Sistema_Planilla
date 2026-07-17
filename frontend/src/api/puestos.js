import client from './client';

export function fetchPuestos() {
  return client.get('/api/puestos').then((res) => res.data);
}

export function createPuesto(data) {
  return client.post('/api/puestos', data).then((res) => res.data);
}

export function updatePuesto(id, data) {
  return client.put(`/api/puestos/${id}`, data).then((res) => res.data);
}

export function deletePuesto(id) {
  return client.delete(`/api/puestos/${id}`);
}
