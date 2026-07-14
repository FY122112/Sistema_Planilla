import client from './client';

export function fetchSistemasPension() {
  return client.get('/api/sistema-pension').then((res) => res.data);
}
