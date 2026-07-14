import client from './client';

export function fetchBancos() {
  return client.get('/api/banco').then((res) => res.data);
}
