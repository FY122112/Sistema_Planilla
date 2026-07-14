import client from './client';

export function fetchBoletas() {
  return client.get('/api/boletas').then((res) => res.data);
}

export function fetchBoletaById(id) {
  return client.get(`/api/boletas/${id}`).then((res) => res.data);
}

export function createBoleta({ idDetalle, fechaEmision, periodoMes, periodoAnio, sueldoBruto, totalDescuento }) {
  return client
    .post('/api/boletas', { idDetalle, fechaEmision, periodoMes, periodoAnio, sueldoBruto, totalDescuento })
    .then((res) => res.data);
}

export function updateBoleta(id, { estadoBoleta, rutaPdf, sueldoBruto, totalDescuento }) {
  return client
    .put(`/api/boletas/${id}`, { estadoBoleta, rutaPdf, sueldoBruto, totalDescuento })
    .then((res) => res.data);
}

export function deleteBoleta(id) {
  return client.delete(`/api/boletas/${id}`);
}
