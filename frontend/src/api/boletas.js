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

export function descargarBoletaPdf(id) {
  return client.get(`/api/boletas/${id}/pdf`, { responseType: 'blob' }).then((res) => res.data);
}

export function fetchMisBoletas() {
  return client.get('/api/boletas/me').then((res) => res.data);
}

export function firmarBoleta(id) {
  return client.patch(`/api/boletas/${id}/firmar`).then((res) => res.data);
}

export function descargarBoletasZip(mes, anio) {
  return client
    .get('/api/boletas/zip', { params: { mes, anio }, responseType: 'blob' })
    .then((res) => res.data);
}

export function crearEnlaceCompartidoBoleta(id) {
  return client.post(`/api/boletas/${id}/compartir`).then((res) => res.data);
}
