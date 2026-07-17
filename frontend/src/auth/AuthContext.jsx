import { createContext, useCallback, useContext, useMemo, useState } from 'react';
import client from '../api/client';
import { rolesFromToken } from './jwt';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem('token'));
  const [username, setUsername] = useState(() => localStorage.getItem('username'));

  const login = useCallback(async (usernameValue, password) => {
    const { data } = await client.post('/auth/login', {
      username: usernameValue,
      password,
    });

    localStorage.setItem('token', data.jwt);
    localStorage.setItem('username', data.userName);
    setToken(data.jwt);
    setUsername(data.userName);

    // Se devuelven los roles recién decodificados del JWT (no los del state, que todavía
    // no se actualizó) para que LoginPage pueda decidir el redirect sin esperar un re-render.
    return rolesFromToken(data.jwt);
  }, []);

  const logout = useCallback(() => {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    setToken(null);
    setUsername(null);
  }, []);

  const roles = useMemo(() => rolesFromToken(token), [token]);

  const value = {
    token,
    username,
    roles,
    // Solo ADMINISTRADOR tiene permisos de backend para las pantallas administrativas
    // (@PreAuthorize en cada controlador); cualquier otro rol usa el portal de
    // autoservicio, vea o no su boleta ahí depende de si tiene un Empleado vinculado,
    // no del nombre del rol.
    isAdmin: roles.includes('ADMINISTRADOR'),
    isAuthenticated: Boolean(token),
    login,
    logout,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);

  if (!context) {
    throw new Error('useAuth debe usarse dentro de un AuthProvider');
  }

  return context;
}
