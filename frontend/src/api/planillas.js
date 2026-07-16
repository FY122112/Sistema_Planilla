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

export function previewDetallePlanilla(idPlanilla, idDetalle, valores) {
  return client
    .post(`/api/planillas/${idPlanilla}/detalles/${idDetalle}/preview`, valores)
    .then((res) => res.data);
}

export function updateDetallePlanilla(idPlanilla, idDetalle, valores) {
  return client
    .put(`/api/planillas/${idPlanilla}/detalles/${idDetalle}`, valores)
    .then((res) => res.data);
}

export function descargarPlanillaExcel(id) {
  return client.get(`/api/planillas/${id}/export`, { responseType: 'blob' }).then((res) => res.data);
}

export function fetchResumenMensual(ultimosMeses = 12) {
  return client
    .get('/api/planillas/resumen-mensual', { params: { ultimosMeses } })
    .then((res) => res.data);
}
