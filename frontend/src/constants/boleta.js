export const ESTADO_BOLETA_INFO = {
  GENERADA: { label: 'Generada', color: 'blue' },
  FIRMADA: { label: 'Firmada', color: 'yellow' },
  ENVIADA: { label: 'Enviada', color: 'grape' },
  PAGADA: { label: 'Pagada', color: 'green' },
};

// Orden del flujo de vida de una boleta, usado para ofrecer "avanzar al siguiente estado".
export const ORDEN_ESTADOS_BOLETA = ['GENERADA', 'FIRMADA', 'ENVIADA', 'PAGADA'];

export function siguienteEstado(estadoActual) {
  const index = ORDEN_ESTADOS_BOLETA.indexOf(estadoActual);
  if (index === -1 || index === ORDEN_ESTADOS_BOLETA.length - 1) return null;
  return ORDEN_ESTADOS_BOLETA[index + 1];
}

// Igual que siguienteEstado, pero para el botón de avance del Administrador: GENERADA →
// FIRMADA ahora es una acción exclusiva del propio empleado (portal de autoservicio,
// "Confirmar conformidad"), así que el admin no debe poder saltarla.
export function siguienteEstadoAdmin(estadoActual) {
  const siguiente = siguienteEstado(estadoActual);
  return siguiente === 'FIRMADA' ? null : siguiente;
}
