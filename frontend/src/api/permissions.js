import client from './client';

export function fetchPermissions() {
  return client.get('/api/permiso').then((res) => res.data);
}
