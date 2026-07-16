import client from './client';

export function fetchUsuarios() {
  return client.get('/api/usuarios').then((res) => res.data);
}

export function createUsuario({ username, password, email, roleIds, empleadoId }) {
  return client
    .post('/api/usuarios', { username, password, email, roleIds, empleadoId })
    .then((res) => res.data);
}

export function updateUsuario(id, { username, password, email, enabled, empleadoId }) {
  return client
    .put(`/api/usuarios/${id}`, { username, password, email, enabled, empleadoId })
    .then((res) => res.data);
}

export function deleteUsuario(id) {
  return client.delete(`/api/usuarios/${id}`);
}
