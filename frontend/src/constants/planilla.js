export const TIPO_PLANILLA_OPTIONS = [
  { value: 'MENSUAL', label: 'Mensual' },
  { value: 'CTS', label: 'CTS' },
  { value: 'GRATIFICACION', label: 'Gratificación' },
  { value: 'LIQUIDACION', label: 'Liquidación' },
];

export const MESES = [
  { value: '1', label: 'Enero' },
  { value: '2', label: 'Febrero' },
  { value: '3', label: 'Marzo' },
  { value: '4', label: 'Abril' },
  { value: '5', label: 'Mayo' },
  { value: '6', label: 'Junio' },
  { value: '7', label: 'Julio' },
  { value: '8', label: 'Agosto' },
  { value: '9', label: 'Septiembre' },
  { value: '10', label: 'Octubre' },
  { value: '11', label: 'Noviembre' },
  { value: '12', label: 'Diciembre' },
];

export function nombreMes(mes) {
  return MESES.find((m) => Number(m.value) === Number(mes))?.label ?? mes;
}

export function nombreTipoPlanilla(tipo) {
  return TIPO_PLANILLA_OPTIONS.find((t) => t.value === tipo)?.label ?? tipo;
}

export function formatearMoneda(valor) {
  const numero = Number(valor ?? 0);
  return `S/ ${numero.toLocaleString('es-PE', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
}
