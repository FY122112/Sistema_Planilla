import client from './client';

export function fetchEmpresas() {
  return client.get('/api/empresa').then((res) => res.data);
}
