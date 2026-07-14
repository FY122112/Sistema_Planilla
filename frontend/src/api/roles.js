import client from './client';

export function fetchRoles() {
  return client.get('/api/roles').then((res) => res.data);
}
