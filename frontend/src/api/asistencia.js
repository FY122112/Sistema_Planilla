import client from './client';

export function fetchIdsEmpleadosSinAsistencia(idsEmpleados, mes, anio) {
  // Spring liga automáticamente un solo query param separado por comas a List<Long>,
  // evitando la ambigüedad de cómo axios serializa arrays (idsEmpleados[]= vs repetido).
  return client
    .get('/api/asistencia/faltantes', { params: { idsEmpleados: idsEmpleados.join(','), mes, anio } })
    .then((res) => res.data);
}
