import client from './client';

export function fetchIdsEmpleadosSinAsistencia(idsEmpleados, mes, anio) {
  // Spring liga automáticamente un solo query param separado por comas a List<Long>,
  // evitando la ambigüedad de cómo axios serializa arrays (idsEmpleados[]= vs repetido).
  return client
    .get('/api/asistencia/faltantes', { params: { idsEmpleados: idsEmpleados.join(','), mes, anio } })
    .then((res) => res.data);
}

export function marcarAsistencia(numeroDocumento, tipoMarca) {
  return client.post('/api/asistencia', { numeroDocumento, tipoMarca }).then((res) => res.data);
}

export function fetchReporteDiario(fecha) {
  return client.get('/api/asistencia', { params: { fecha } }).then((res) => res.data);
}

export function justificarAsistencia(id, motivo) {
  return client.put(`/api/asistencia/${id}`, { motivo }).then((res) => res.data);
}

export function marcarMiAsistencia(tipoMarca) {
  return client.post('/api/asistencia/mi-marca', { tipoMarca }).then((res) => res.data);
}
