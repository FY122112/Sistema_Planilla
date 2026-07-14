// Decodifica el payload de un JWT en el navegador (sin validar la firma - eso ya lo
// hizo el backend; aquí solo se lee para adaptar la interfaz, como mostrar/ocultar
// pantallas según el rol).
export function decodeJwtPayload(token) {
  if (!token) return null;

  try {
    const [, payload] = token.split('.');
    const json = atob(payload.replace(/-/g, '+').replace(/_/g, '/'));
    return JSON.parse(decodeURIComponent(escape(json)));
  } catch {
    return null;
  }
}

export function rolesFromToken(token) {
  const payload = decodeJwtPayload(token);
  const authorities = payload?.authorities ?? [];
  return authorities
    .filter((a) => a.startsWith('ROLE_'))
    .map((a) => a.replace('ROLE_', ''));
}
