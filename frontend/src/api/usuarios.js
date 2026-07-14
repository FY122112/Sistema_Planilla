import client from './client';

export function fetchUsuarios() {
  return client.get('/api/usuarios').then((res) => res.data);
}

export function createUsuario({ username, password, email, roleIds }) {
  return client
    .post('/api/usuarios', { username, password, email, roleIds })
    .then((res) => res.data);
}
