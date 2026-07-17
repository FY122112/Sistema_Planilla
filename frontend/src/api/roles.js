import client from './client';

export function fetchRoles() {
  return client.get('/api/roles').then((res) => res.data);
}

export function createRole(data) {
  return client.post('/api/roles', data).then((res) => res.data);
}

export function updateRole(id, data) {
  return client.put(`/api/roles/${id}`, data).then((res) => res.data);
}

export function deleteRole(id) {
  return client.delete(`/api/roles/${id}`);
}
