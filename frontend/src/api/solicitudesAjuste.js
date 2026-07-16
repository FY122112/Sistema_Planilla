import client from './client';

export function crearSolicitudAjuste({ idBoleta, mensaje }) {
  return client.post('/api/solicitudes-ajuste', { idBoleta, mensaje }).then((res) => res.data);
}

export function fetchSolicitudesAjuste() {
  return client.get('/api/solicitudes-ajuste').then((res) => res.data);
}

export function fetchSolicitudesPendientesCount() {
  return client.get('/api/solicitudes-ajuste/pendientes/count').then((res) => res.data.pendientes);
}

export function atenderSolicitudAjuste(id) {
  return client.patch(`/api/solicitudes-ajuste/${id}/atender`).then((res) => res.data);
}
