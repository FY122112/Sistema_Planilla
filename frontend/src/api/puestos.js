import client from './client';

export function fetchPuestos() {
  return client.get('/api/puestos').then((res) => res.data);
}
