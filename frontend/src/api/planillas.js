import client from './client';

export function fetchPlanillas() {
  return client.get('/api/planillas').then((res) => res.data);
}

export function fetchPlanillaById(id) {
  return client.get(`/api/planillas/${id}`).then((res) => res.data);
}

export function generarPlanilla({ mes, anio, tipoPlanilla }) {
  return client
    .post('/api/planillas/generar', { mes, anio, tipoPlanilla })
    .then((res) => res.data);
}

export function cerrarPlanilla(id) {
  return client.patch(`/api/planillas/${id}/cerrar`).then((res) => res.data);
}

export function abrirPlanilla(id) {
  return client.patch(`/api/planillas/${id}/abrir`).then((res) => res.data);
}

export function deletePlanilla(id) {
  return client.delete(`/api/planillas/${id}`);
}
