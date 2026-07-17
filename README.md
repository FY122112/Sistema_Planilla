01: Módulo de Inicio y Planilla General (HU-001 a HU-015)
HU-001: Vista General de Planilla
•	Descripción: Como un Administrador de RR.HH., necesito una tabla mensual con el resumen de sueldos de todo el personal, con la finalidad de auditar los montos calculados antes de procesar los pagos.
•	Criterios de Validación:
o	El sistema debe cargar la grilla principal con las columnas: N°, Documento, Apellidos y Nombres, Días, Ingresos, Descuentos, Aportes y Neto.
o	Cada fila debe mostrar un icono de acción (lápiz) para editar la nómina individual.
•	Estado: Completado
•	Métricas: Valor: 200 | Prioridad: 1 | Estimación: 12h
HU-002: Filtro por Periodo Contable
•	Descripción: Como un Administrador de RR.HH., necesito seleccionar un mes y año específico en la cabecera, con la finalidad de consultar los históricos de planillas de periodos anteriores.
•	Criterios de Validación:
o	Deben existir dos selectores desplegables independientes para "Mes" y "Año".
o	Al cambiar los filtros, la tabla debe actualizar su contenido automáticamente sin recargar toda la página.
•	Estado: Completado
•	Métricas: Valor: 180 | Prioridad: 1 | Estimación: 8h
HU-003: Buscador de Colaboradores
•	Descripción: Como un Administrador de RR.HH., necesito filtrar la tabla digitando el nombre o DNI, con la finalidad de ubicar rápidamente el estado financiero de un empleado específico.
•	Criterios de Validación:
o	Al escribir en el campo "Search", la tabla debe filtrar las filas en tiempo real.
•	Estado: Completado
•	Métricas: Valor: 150 | Prioridad: 1 | Estimación: 6h
HU-004: Exportación de Datos a Excel
•	Descripción: Como un Administrador de RR.HH., necesito descargar el reporte de la planilla en formato .xlsx, con la finalidad de enviar el consolidado al área de contabilidad corporativa.
•	Criterios de Validación:
o	El botón "EXPORTAR" debe generar un archivo descargable compatible con Microsoft Excel que contenga los datos idénticos de la pantalla.
•	Estado: Completado
•	Nota: `PlanillaExcelExporterAdapter` (Apache POI) genera el .xlsx con encabezados, filas por detalle y totales; expuesto en `GET /api/planillas/{id}/export` y el botón "Exportar" en `PlanillaDetallePage.jsx`.
•	Métricas: Valor: 190 | Prioridad: 2 | Estimación: 16h
HU-005: Paginación de Registros
•	Descripción: Como un Administrador de RR.HH., necesito fragmentar el listado general en páginas de 10 registros, con la finalidad de optimizar el tiempo de carga de la plataforma web.
•	Criterios de Validación:
o	Los controles inferiores de navegación deben permitir avanzar, retroceder y saltar a páginas específicas correctamente.
•	Estado: Completado
•	Métricas: Valor: 100 | Prioridad: 3 | Estimación: 4h
HU-006: Ordenamiento Dinámico de Columnas
•	Descripción: Como un Administrador de RR.HH., necesito hacer clic en las cabeceras de la tabla, con la finalidad de ordenar la información alfabéticamente o por montos económicos.
•	Criterios de Validación:
o	Al hacer clic en "Apellidos y Nombres" o "Neto", las filas deben ordenarse de forma ascendente o descendente.
•	Estado: Completado
•	Nota: `PlanillaDetallePage.jsx` implementa `handleOrdenar`/`detallesOrdenados`, con encabezados clicables e ícono de orden ascendente/descendente para Empleado y Neto.
•	Métricas: Valor: 110 | Prioridad: 3 | Estimación: 6h
HU-007: Indicador de Estado de Boleta
•	Descripción: Como un Administrador de RR.HH., necesito visualizar una etiqueta de estado junto a cada registro, con la finalidad de conocer rápidamente qué boletas ya fueron cerradas y cuáles están pendientes.
•	Criterios de Validación:
o	El sistema debe renderizar etiquetas visuales de color (Verde para 'Generada', Amarillo para 'Pendiente').
•	Estado: Completado
•	Métricas: Valor: 140 | Prioridad: 2 | Estimación: 6h
HU-008: Alerta de Registros Incompletos
•	Descripción: Como un Administrador de RR.HH., necesito una notificación visual si un empleado no registra marcas de asistencia en el mes, con la finalidad de evitar procesar su sueldo con errores.
•	Criterios de Validación:
o	Aparecerá un banner de advertencia superior en caso de detectar campos de tiempo en cero o vacíos.
•	Estado: Completado
•	Nota: `GET /api/asistencia/faltantes` calcula qué empleados no tienen marcas en el periodo; `PlanillaDetallePage.jsx` muestra un `Alert` superior y resalta las filas afectadas.
•	Métricas: Valor: 120 | Prioridad: 2 | Estimación: 8h
HU-009: Resumen de Totales Mensuales
•	Descripción: Como un Administrador de RR.HH., necesito visualizar la sumatoria acumulada de los sueldos netos en el pie de la tabla, con la finalidad de conocer el desembolso total de la empresa en el periodo.
•	Criterios de Validación:
o	El campo final debe recalcular la suma vertical de la columna "Neto" de forma dinámica.
•	Estado: Completado
•	Nota: PlanillaDetallePage.jsx calcula los totales sobre los `detalles` y los renderiza en el `Tfoot`.
•	Métricas: Valor: 170 | Prioridad: 1 | Estimación: 6h
HU-010: Descarga Masiva de PDFs
•	Descripción: Como un Administrador de RR.HH., necesito un botón para descargar todas las boletas del mes en un solo archivo comprimido, con la finalidad de agilizar la entrega física al personal de planta.
•	Criterios de Validación:
o	El sistema empaquetará todas las boletas aprobadas en un formato .zip con los archivos nombrados por el DNI de cada trabajador.
•	Estado: Completado
•	Nota: `BoletaService.exportarZip` arma un .zip en memoria (java.util.zip) con un PDF por boleta del periodo, nombrado por DNI (o DNI-idBoleta si hay colisión); expuesto en `GET /api/boletas/zip` y el botón "Boletas (.zip)" en `PlanillaDetallePage.jsx`.
•	Métricas: Valor: 200 | Prioridad: 2 | Estimación: 24h
HU-011: Filtro Avanzado por Puesto
•	Descripción: Como un Administrador de RR.HH., necesito segmentar la planilla general por cargos laborales, con la finalidad de analizar los costos operativos por cada área de confección.
•	Criterios de Validación:
o	Un menú desplegable filtrará las filas de la grilla mostrando únicamente los roles seleccionados (ej. Operarios, Diseñadores).
•	Estado: Completado
•	Métricas: Valor: 130 | Prioridad: 3 | Estimación: 8h
HU-012: Logs de Auditoría Interna
•	Descripción: Como un Auditor de Seguridad, necesito registrar qué usuario modificó un monto de la planilla, con la finalidad de mantener la transparencia y detectar alteraciones no autorizadas.
•	Criterios de Validación:
o	El sistema guardará de forma invisible en la base de datos el nombre de usuario, fecha, hora y el monto previo alterado.
•	Estado: Completado
•	Nota: Nueva tabla `auditoria_cambio` (agregado `AuditoriaCambio`, vertical completa). Cada vez que `PlanillaService.actualizarDetalleMensual` recalcula un detalle ya generado y el sueldo neto realmente cambia, se registra usuario (del JWT autenticado), fecha, monto anterior y monto nuevo. No hay pantalla dedicada (la HU pide guardado invisible); consultable vía `GET /api/planillas/{id}/auditoria`. De paso se corrigió `AuthenticatedPrincipal` para implementar la interfaz de Spring Security del mismo nombre — sin eso, `Authentication.getName()` devolvía el `toString()` del record en vez del username.
•	Métricas: Valor: 150 | Prioridad: 2 | Estimación: 14h
HU-013: Cierre Técnico del Mes
•	Descripción: Como un Administrador de RR.HH., necesito bloquear las modificaciones de un periodo una vez transferidos los sueldos, con la finalidad de proteger la consistencia contable frente a cambios accidentales.
•	Criterios de Validación:
o	El botón de edición individual quedará inhabilitado si el estado del mes se cambia a "CERRADO".
•	Estado: Completado
•	Nota: PlanillaService bloquea update()/deleteById() cuando la planilla está cerrada (estaCerrada()); el frontend muestra el badge Cerrada/Abierta y deshabilita Eliminar. No existe pantalla de edición individual todavía (ver HU-031 a HU-045), así que no hay un "botón de edición" que bloquear.
•	Métricas: Valor: 180 | Prioridad: 1 | Estimación: 10h
HU-014: Adaptabilidad Responsiva de Interfaz
•	Descripción: Como un Gerente General, necesito acceder al panel de inicio desde dispositivos móviles o tablets, con la finalidad de revisar las métricas financieras fuera de la oficina.
•	Criterios de Validación:
o	La estructura CSS de las columnas de la tabla se acoplará correctamente a pantallas táctiles sin desbordarse.
•	Estado: Completado
•	Métricas: Valor: 120 | Prioridad: 3 | Estimación: 12h
HU-015: Gráfico Estadístico de Egresos
•	Descripción: Como un Gerente General, necesito visualizar un gráfico de barras con el histórico de gastos salariales de los últimos meses, con la finalidad de evaluar la tendencia del presupuesto laboral.
•	Criterios de Validación:
o	Se desplegará un componente gráfico dinámico que consuma la sumatoria de los netos mensuales almacenados.
•	Estado: Completado
•	Nota: `GET /api/planillas/resumen-mensual` suma `calcularTotalNeto()` de todas las planillas por periodo (mes/año, cualquier tipo); `PlanillasPage.jsx` lo renderiza con `@mantine/charts` (`BarChart`) en el panel de inicio del administrador.
•	Métricas: Valor: 160 | Prioridad: 3 | Estimación: 16h
02: Módulo de Registro y Gestión de Trabajadores (HU-016 a HU-030)
HU-016: Lista Maestro de Personal
•	Descripción: Como un Administrador de RR.HH., necesito un panel maestro con el padrón de todos los empleados contratados, con la finalidad de supervisar sus estados laborales activos.
•	Criterios de Validación:
o	La interfaz tabular listará obligatoriamente: DNI, Nombre Completo, Cargo, Sueldo Básico y Estado Contratación.
•	Estado: Completado
•	Métricas: Valor: 200 | Prioridad: 1 | Estimación: 12h
HU-017: Apertura de Ventana Modal de Registro
•	Descripción: Como un Administrador de RR.HH., necesito que el botón "Nuevo" abra un formulario limpio en pantalla superpuesta, con la finalidad de agilizar la digitación del personal sin salir de la vista actual.
•	Criterios de Validación:
o	La ventana emergente modal bloqueará la interacción con la lista del fondo hasta que sea guardada o cerrada.
•	Estado: Completado
•	Métricas: Valor: 120 | Prioridad: 1 | Estimación: 4h
HU-018: Restricción de Documento Duplicado
•	Descripción: Como un Administrador de RR.HH., necesito que el sistema impida guardar fichas con un DNI ya existente, con la finalidad de evitar duplicidad y asegurar la integridad de la base de datos.
•	Criterios de Validación:
o	Al intentar guardar un número de documento repetido, la base de datos MySQL abortará la operación y el sistema mostrará una alerta en letras rojas.
•	Estado: Completado
•	Métricas: Valor: 170 | Prioridad: 1 | Estimación: 8h
HU-019: Captura de Datos de Identidad
•	Descripción: Como un Administrador de RR.HH., necesito ingresar los Nombres, Apellidos y Fecha de Nacimiento del empleado, con la finalidad de construir su registro de identidad legal en el software.
•	Criterios de Validación:
o	Los campos de texto validarán caracteres alfabéticos y el campo de nacimiento obligará el formato de fecha válido (dd/mm/aaaa).
•	Estado: Completado
•	Métricas: Valor: 160 | Prioridad: 1 | Estimación: 8h
HU-020: Vinculación Automatizada de Sueldo por Cargo
•	Descripción: Como un Administrador de RR.HH., necesito asignar el puesto del trabajador desde una lista predefinida, con la finalidad de cargar de forma automática su salario básico legal establecido.
•	Criterios de Validación:
o	Al seleccionar por ejemplo el cargo "Operario de Confección", el campo "Salario Básico" se auto-rellenará con el valor S/ 1,800.00 de forma no editable.
•	Estado: Completado
•	Métricas: Valor: 180 | Prioridad: 1 | Estimación: 10h
HU-021: Registro de Fecha de Cese Laboral
•	Descripción: Como un Administrador de RR.HH., necesito ingresar la fecha de salida de un colaborador desvinculado, con la finalidad de darlo de baja del sistema de procesamiento activo.
•	Criterios de Validación:
o	Al guardar una fecha de cese, el estado del trabajador pasará inmediatamente de 'ACTIVO' a 'INACTIVO' en todas las grillas futuras.
•	Estado: Completado
•	Nota: EmpleadoFormPage.jsx tiene checkbox "Empleado activo" + campo fechaCese, y la lista muestra badge Activo/Cesado. El dominio expone Empleado.cesarEmpleado(), pero el flujo actual pasa por el PUT genérico en vez de invocar ese método explícitamente (deuda técnica menor).
•	Métricas: Valor: 140 | Prioridad: 2 | Estimación: 6h
HU-022: Cancelación de Entrada de Datos
•	Descripción: Como un Administrador de RR.HH., necesito un botón de cancelación dentro del formulario modal, con la finalidad de cerrar la interfaz sin registrar alteraciones accidentales en el maestro.
•	Criterios de Validación:
o	Al pulsar el botón "Cancelar", el formulario se cerrará por completo, borrará los datos digitados temporalmente y mantendrá la tabla intacta.
•	Estado: Completado
•	Métricas: Valor: 100 | Prioridad: 1 | Estimación: 2h
HU-023: Filtro Maestro por Condición
•	Descripción: Como un Administrador de RR.HH., necesito alternar la vista del listado de personal entre activos y cesados, con la finalidad de agilizar las auditorías de bajas de talento.
•	Criterios de Validación:
o	Un componente de selección superior ("Estado:") actualizará las filas mostrando solo a los empleados que cumplan la condición salarial seleccionada.
•	Estado: Completado
•	Métricas: Valor: 130 | Prioridad: 2 | Estimación: 6h
HU-024: Modificación de Ficha Contractual
•	Descripción: Como un Administrador de RR.HH., necesito un botón para editar fichas de personal creadas, con la finalidad de corregir errores de tipeo o actualizar cargos asignados.
•	Criterios de Validación:
o	Al presionar la acción editar, la ventana modal se cargará con los datos recuperados de la base de datos listos para ser actualizados.
•	Estado: Completado
•	Nota: Implementado como página navegable (/empleados/:id/editar) que precarga los datos, no como ventana modal superpuesta; funcionalmente cubre el objetivo de la historia.
•	Métricas: Valor: 160 | Prioridad: 1 | Estimación: 10h
HU-025: Validación de Entradas Requeridas
•	Descripción: Como un Administrador de RR.HH., necesito que el sistema evalúe que no queden casillas en blanco, con la finalidad de evitar la presumible persistencia de perfiles incompletos.
•	Criterios de Validación:
o	Al presionar "Guardar", las casillas vacías obligatorias se resaltarán con bordes rojos intermitentes y congelarán el envío de la petición.
•	Estado: Completado
•	Métricas: Valor: 150 | Prioridad: 1 | Estimación: 6h
HU-026: Registro del Sistema Previsional
•	Descripción: Como un Administrador de RR.HH., necesito seleccionar el régimen de pensiones (ONP o AFP específica) del colaborador, con la finalidad de calcular los descuentos de ley en las planillas.
•	Criterios de Validación:
o	El menú desplegable guardará el identificador exacto del fondo de jubilación del empleado para el procesamiento matemático de aportes.
•	Estado: Completado
•	Métricas: Valor: 190 | Prioridad: 1 | Estimación: 8h
HU-027: Captura de Datos Bancarios de Haberes
•	Descripción: Como un Administrador de RR.HH., necesito registrar la entidad financiera y el Código de Cuenta Interbancario (CCI), con la finalidad de automatizar las transferencias bancarias mensuales.
•	Criterios de Validación:
o	El campo de texto de la cuenta bancaria limitará la entrada únicamente a números y validará la longitud estándar.
•	Estado: Completado
•	Métricas: Valor: 140 | Prioridad: 2 | Estimación: 6h
HU-028: Importación Masiva mediante Archivos CSV
•	Descripción: Como un Administrador de RR.HH., necesito cargar un archivo de extensión .csv con datos de operarios nuevos, con la finalidad de evitar el registro manual uno por uno durante periodos de alta demanda textil.
•	Criterios de Validación:
o	El backend de Spring Boot parseará el archivo insertando las filas en bloque y arrojando una bitácora con los registros que contengan formatos de DNI o nombres inválidos.
•	Estado: Completado
•	Nota: `POST /api/empleados/importar-csv` (multipart) parsea el CSV (`EmpleadoCsvParser`) y procesa cada fila de forma independiente en `EmpleadoService.importarCsv`, validando formato de DNI (8 dígitos para DNI) y de nombres/apellidos; devuelve una bitácora por fila (éxito/error + motivo) sin abortar el resto del archivo. UI en `EmpleadosPage.jsx` ("Importar CSV").
•	Métricas: Valor: 180 | Prioridad: 3 | Estimación: 20h
HU-029: Eliminación Lógica de Registros
•	Descripción: Como un Administrador de RR.HH., necesito deshabilitar permanentemente a un colaborador erróneo de la lista maestro, con la finalidad de limpiar la interfaz sin destruir la consistencia histórica relacional.
•	Criterios de Validación:
o	Al dar de baja un registro, este dejará de renderizarse en el front-end pero su tupla permanecerá intacta en MySQL con bandera oculta para reportes contables pasados.
•	Estado: Completado
•	Nota: Nuevo campo `Empleado.eliminado` (distinto de `estado`, que es cese laboral real). `DELETE /api/empleados/{id}` ahora marca `eliminado=true` en vez de borrar la fila (antes hacía un delete físico que además hubiera fallado por el FK de detalle_planilla en cualquier empleado con historial). Los listados (`findAll`, `findByEstado`, `search`) excluyen empleados eliminados; el DNI de un registro eliminado puede reutilizarse para su corrección.
•	Métricas: Valor: 130 | Prioridad: 2 | Estimación: 6h
HU-030: Registro de Carga Familiar
•	Descripción: Como un Administrador de RR.HH., necesito registrar si el empleado cuenta con hijos menores de edad dependientes, con la finalidad de habilitar de forma legal el abono mensual de la Asignación Familiar.
•	Criterios de Validación:
o	Un componente interactivo de tipo checkbox guardará un estado booleano que servirá como llave condicional en el cálculo de la nómina.
•	Estado: Completado
•	Métricas: Valor: 180 | Prioridad: 1 | Estimación: 6h
03: Control de Tiempos y Procesamiento de Remuneración (HU-031 a HU-045)
HU-031: Registro de Asistencia Mensual
•	Descripción: Como un Contador, necesito ingresar los días y las horas efectivamente trabajadas por el colaborador, con la finalidad de calcular el sueldo proporcional bruto correspondiente al mes.
•	Criterios de Validación:
o	Los campos de texto superiores aceptarán valores enteros y flotantes, recalculando el valor proporcional sobre la base salarial del contrato de manera automática.
•	Estado: Completado
•	Nota: Actualizado 2026-07-17 — al generar la planilla, días no laborados y minutos de tardanza ya se calculan automáticamente a partir de las marcas reales de Asistencia del mes (PlanillaService#aplicarAsistenciaAutomatica: días hábiles lunes-viernes sin entrada registrada, y suma de minutosTardanzas). El Contador conserva la pantalla "Editar remuneración" (PlanillaDetalleEditarPage.jsx) y el endpoint PUT /api/planillas/{id}/detalles/{idDetalle} para ajustar manualmente estos valores si hace falta corregir algo.
•	Métricas: Valor: 200 | Prioridad: 1 | Estimación: 12h
HU-032: Control Dinámico de Ausentismos
•	Descripción: Como un Contador, necesito registrar los días no laborados injustificados del operario, con la finalidad de descontar proporcionalmente dichos días del haber total del periodo.
•	Criterios de Validación:
o	Cada unidad ingresada en la casilla "Días No Laborados" restará el equivalente exacto del factor de salario diario determinado por la ley de trabajo.
•	Estado: Completado
•	Nota: PlanillaService.aplicarVariablesMensuales() genera el movimiento AUSENTISMO = valorDia × díasNoLaborados (valorDia = sueldoBase / 30), ingresado desde la pantalla de edición del detalle.
•	Métricas: Valor: 180 | Prioridad: 1 | Estimación: 8h
HU-033: Procesamiento Numérico de Tardanzas
•	Descripción: Como un Contador, necesito registrar los minutos de tardanza acumulados por el empleado en el mes, con la finalidad de aplicar las retenciones salariales proporcionales en el bloque de descuentos.
•	Criterios de Validación:
o	El sistema computará el descuento multiplicando los minutos ingresados por el costo individual del minuto deducido del sueldo básico ordinario.
•	Estado: Completado
•	Nota: Movimiento TARDANZA = valorMinuto × minutosTardanza (valorMinuto = valorHora / 60, valorHora = valorDia / horas de jornada del puesto).
•	Métricas: Valor: 170 | Prioridad: 1 | Estimación: 8h
HU-034: Cálculo de Horas Extras al 25%
•	Descripción: Como un Contador, necesito digitar las dos primeras horas de sobretiempo acumuladas en una jornada, con la finalidad de procesar el pago con el recargo del 25% sobre el valor hora regular.
•	Criterios de Validación:
o	El algoritmo aplicará el factor de multiplicación de 1.25 sobre el valor de la hora base y sumará el resultado en la fila de ingresos adicionales.
•	Estado: Completado
•	Nota: Movimiento HORA_EXTRA_25 = valorHora × 1.25 × horas ingresadas; verificado en pantalla (desglose "Horas Extra 25%") y en el neto final tras guardar.
•	Métricas: Valor: 190 | Prioridad: 1 | Estimación: 12h
HU-035: Cálculo de Horas Extras al 35%
•	Descripción: Como un Contador, necesito ingresar las horas de sobretiempo que excedan las dos primeras de la jornada, con la finalidad de liquidar el sobretiempo avanzado con la tasa del 35% de recargo de ley.
•	Criterios de Validación:
o	El sistema aplicará de forma automática el factor de multiplicación de 1.35 sobre el valor de la hora base para las horas adicionales digitadas en este campo.
•	Estado: Completado
•	Nota: Movimiento HORA_EXTRA_35 = valorHora × 1.35 × horas ingresadas, en un campo separado de Horas Extra 25% (ambos tramos se digitan y calculan de forma independiente, no como un único campo con umbral automático de 2h).
•	Métricas: Valor: 190 | Prioridad: 1 | Estimación: 12h
HU-036: Liquidación Económica de Vacaciones Tomadas
•	Descripción: Como un Contador, necesito registrar los días gozados de vacaciones del trabajador en el periodo contable, con la finalidad de computar el pago correspondiente del descanso vacacional anual de manera aislada.
•	Criterios de Validación:
o	Los montos por concepto de vacaciones se calcularán de manera separada en el bloque de ingresos sin sufrir deducción alguna por ausencia en el puesto laboral.
•	Estado: Completado
•	Nota: Movimiento VACACIONES_GOZADAS = valorDia × días gozados, como ingreso independiente; no interactúa con el descuento de Ausentismo (son campos separados). No usa los contadores por periodo de ControlVacacionalService (ese servicio no atribuye días por mes) — es una entrada manual simple del Contador para este periodo de planilla.
•	Métricas: Valor: 160 | Prioridad: 2 | Estimación: 10h
HU-037: Liquidación Automatizada de la Asignación Familiar
•	Descripción: Como un Contador, necesito que el sistema valide si el trabajador aplica para la Asignación Familiar, con la finalidad de abonar el porcentaje legal correspondiente de manera desglosada.
•	Criterios de Validación:
o	Si la llave booleana de carga familiar de la HU-030 está activa, el backend inyectará de forma automática el monto fijo equivalente al 10% de la Remuneración Mínima Vital (S/ 113.00) en los ingresos.
•	Estado: Completado
•	Nota: PlanillaService.construirDetalleMensual() usa el ParametroLegal ASIGNACION_FAMILIAR y empleado.isTieneHijosCalificados(); el frontend muestra la columna asignacionFamiliar en el detalle de planilla.
•	Métricas: Valor: 180 | Prioridad: 1 | Estimación: 8h
HU-038: Retención del Aporte Obligatorio Previsional
•	Descripción: Como un Contador, necesito calcular de forma automática la tasa del aporte obligatorio al fondo de jubilación, con la finalidad de retener el porcentaje exacto según la afiliación del colaborador.
•	Criterios de Validación:
o	El sistema aplicará la retención obligatoria del 10% sobre la remuneración asegurable bruta y la mostrará en tiempo real en la columna central de Descuentos.
•	Estado: Completado
•	Nota: El descuento se calcula sobre la remuneración computable completa (sueldoBase + asignación familiar + ingresos remunerativos del periodo) vía PlanillaService.aplicarVariablesMensuales(), y ahora sí se ve "en tiempo real" en la pantalla de edición del detalle gracias al endpoint de preview (ver HU-044).
•	Métricas: Valor: 200 | Prioridad: 1 | Estimación: 10h
HU-039: Deducción Automatizada de Prima de Seguro AFP
•	Descripción: Como un Contador, necesito procesar la deducción de la prima de seguro previsional, con la finalidad de liquidar las retenciones destinadas a la cobertura de invalidez y sobrevivencia.
•	Criterios de Validación:
o	El algoritmo aplicará la tasa porcentual vigente indexada de forma automática al tipo de AFP registrado en el perfil maestro del empleado.
•	Estado: Completado
•	Nota: El descuento pensionario ahora se desglosa en dos movimientos independientes — AFP_APORTE y AFP_COMISION — visibles por separado en el desglose de la pantalla de edición (antes iban combinados en un solo monto).
•	Métricas: Valor: 170 | Prioridad: 1 | Estimación: 10h
HU-040: Cálculo de Aporte Patronal de Salud (EsSalud)
•	Descripción: Como un Contador, necesito visualizar el cálculo del aporte de salud a cargo del empleador, con la finalidad de estimar los costos laborales complementarios de TextiLima SAC.
•	Criterios de Validación:
o	El bloque derecho de la pantalla ("Aportes") mostrará el resultado de la aplicación del 9% obligatorio calculado sobre la base de la remuneración bruta mensual del empleado.
•	Estado: Completado
•	Nota: Movimiento ESSALUD = remuneración computable × parámetro legal ESSALUD, clasificado como APORTE_EMPLEADOR (no reduce el neto del trabajador) y mostrado en la sección "Aportes del empleador" del desglose. Requiere que el Administrador cree el ParametroLegal con código ESSALUD (valor 0.09) vía el CRUD ya existente — igual que ASIGNACION_FAMILIAR, no viene sembrado por código.
•	Métricas: Valor: 180 | Prioridad: 1 | Estimación: 10h
HU-041: Cálculo de Cargas de Seguro Vida Ley
•	Descripción: Como un Contador, necesito calcular la prima del seguro Vida Ley cubierto por la empresa, con la finalidad de auditar las obligaciones de protección obligatoria desde el primer día de labores.
•	Criterios de Validación:
o	El software calculará y mostrará de forma no editable el porcentaje fijo correspondiente a este seguro corporativo dentro de la columna de aportes del empleador.
•	Estado: Completado
•	Nota: Movimiento VIDA_LEY = valor del ParametroLegal con código VIDA_LEY, aplicado como monto fijo mensual (no como porcentaje, así funciona la prima real de este seguro), clasificado como APORTE_EMPLEADOR. También requiere que el Administrador cree ese ParametroLegal manualmente.
•	Métricas: Valor: 140 | Prioridad: 2 | Estimación: 8h
HU-042: Registro de Bonificaciones Extraordinarias de Eficiencia
•	Descripción: Como un Contador, necesito digitar bonos de productividad variables no remunerativos, con la finalidad de premiar la velocidad y la reducción de mermas en los talleres de confección.
•	Criterios de Validación:
o	El input numérico libre adicionará su monto de forma directa a la bolsa de ingresos totales sin alterar las bases de cálculo de las retenciones de AFP.
•	Estado: Completado
•	Nota: Movimiento BONIFICACION_EFICIENCIA con esRemunerativo=false, agregado después de calcular la base de AFP/EsSalud/Vida Ley — queda excluido de esas bases tal como pide el criterio, pero sí suma al bruto y al neto.
•	Métricas: Valor: 150 | Prioridad: 2 | Estimación: 6h
HU-043: Inyección de Comisiones Comerciales
•	Descripción: Como un Contador, necesito agregar dinero acumulado por comisiones de ventas, con la finalidad de liquidar las metas comerciales alcanzadas por el personal de distribución textil.
•	Criterios de Validación:
o	El sistema sumará la cifra directamente en los ingresos afectos a cálculo dentro del periodo contable mensual que se encuentre en procesamiento.
•	Estado: Completado
•	Nota: Movimiento COMISION_COMERCIAL con esRemunerativo=true — sí forma parte de la base de AFP/EsSalud/Vida Ley, a diferencia de la bonificación de eficiencia (HU-042).
•	Métricas: Valor: 150 | Prioridad: 2 | Estimación: 6h
HU-044: Recálculo en Vivo de Remuneración Neta
•	Descripción: Como un Contador, necesito que los cambios en las variables numéricas muestren el impacto económico al instante, con la finalidad de validar visualmente los montos antes de grabarlos.
•	Criterios de Validación:
o	El campo final destacado en la esquina inferior derecha ("NETO A PAGAR S/") actualizará su valor numérico mediante lógica JavaScript ante cualquier evento de cambio en las cajas de texto.
•	Estado: Completado
•	Nota: El recálculo en vivo se implementó vía un endpoint de preview en backend (POST .../detalles/{id}/preview, sin persistir) que el frontend llama con debounce de 300ms mientras el Contador escribe — decisión deliberada en vez de duplicar las fórmulas en JavaScript, para que el preview sea siempre idéntico al cálculo que se termina guardando.
•	Métricas: Valor: 200 | Prioridad: 1 | Estimación: 8h
HU-045: Persistencia del Cálculo Salarial Mensual
•	Descripción: Como un Contador, necesito presionar un botón verde de confirmación, con la finalidad de registrar las sumas finales calculadas en el histórico mensual de planillas de la base de datos.
•	Criterios de Validación:
o	Al pulsar el comando de guardado ("Modificar Remuneración"), el sistema grabará los totales calculados en MySQL y redireccionará al usuario de manera automática al panel de control inicial de planillas.
•	Estado: Completado
•	Nota: Botón "Modificar Remuneración" en la pantalla de edición del detalle → PUT /api/planillas/{id}/detalles/{idDetalle} → PlanillaService.actualizarDetalleMensual() guarda los totales en MySQL (bloqueado si la planilla está cerrada) y redirige de vuelta al detalle de la planilla.
•	Métricas: Valor: 200 | Prioridad: 1 | Estimación: 6h
04: Formato de Boletas y Autogestión (HU-046 a HU-060)
HU-046: Estructuración Gráfica de Boleta Legal
•	Descripción: Como un Empleado, necesito visualizar mi documento de boleta de pago estructurado bajo la normativa del MTPE, con la finalidad de comprender mis abonos y deducciones de manera clara y transparente.
•	Criterios de Validación:
o	La pantalla renderizará un diseño formal con el encabezado de identidad corporativa de TextiLima SAC junto con su número de RUC y dirección física en la zona superior izquierda.
•	Estado: Completado
•	Nota: BoletaDetallePage.jsx renderiza razón social, RUC y dirección (desde la Empresa) en el encabezado de la boleta imprimible.
•	Métricas: Valor: 200 | Prioridad: 1 | Estimación: 16h
HU-047: Mapeo Automatizado de Datos Contractuales
•	Descripción: Como un Empleado, necesito validar mi información de Código, DNI y Cargo impresos en el encabezado del formato, con la finalidad de asegurar que no existan inconsistencias de identidad en mi comprobante salarial.
•	Criterios de Validación:
o	El sistema recuperará y renderizará de forma fidedigna los datos ingresados en el maestro del personal del Sprint 2 sin opción a ser modificados desde esta pantalla.
•	Estado: Completado
•	Nota: BoletaResponseDTO ahora incluye numeroDocumento y cargo (tomados del Empleado/Puesto vinculado al DetallePlanilla), mostrados de solo lectura bajo "Trabajador" en BoletaDetallePage.jsx.
•	Métricas: Valor: 150 | Prioridad: 1 | Estimación: 6h
HU-048: Visualización de Datos Previsionales y de Abono
•	Descripción: Como un Empleado, necesito verificar mi número de cuenta de ahorros y fondo previsional en la boleta, con la finalidad de constatar dónde se realizará el depósito del dinero neto mensual.
•	Criterios de Validación:
o	Se mostrarán ordenadamente las variables dinámicas de la cuenta de haberes y el nombre de la AFP del periodo contable activo en la sección superior derecha.
•	Estado: Completado
•	Nota: BoletaResponseDTO agrega nombreBanco, numeroCuentaBanco y nombreSistemaPension; se muestran en una sección "Datos previsionales y de abono" en BoletaDetallePage.jsx.
•	Métricas: Valor: 160 | Prioridad: 1 | Estimación: 8h
HU-049: Desglose de Periodos Vacacionales Gozados
•	Descripción: Como un Empleado, necesito revisar los días exactos y las fechas de vigencia de mis vacaciones liquidadas, con la finalidad de llevar el control cronológico de mis periodos de descanso anuales.
•	Criterios de Validación:
o	Una cuadrícula complementaria detallará de forma automática las fechas de inicio y fin junto con la cantidad de días de descanso registrados en la HU-036.
•	Estado: Completado
•	Nota: `DetallePlanilla` ahora guarda `vacacionesFechaInicio`/`vacacionesFechaFin` junto al conteo de días (opcionales, para no romper detalles ya persistidos). El Contador los captura en `PlanillaDetalleEditarPage.jsx`; la boleta (`BoletaDetallePage.jsx` y `MiBoletaDetallePage.jsx`) muestra "Vacaciones gozadas: N día(s) (del DD/MM/AAAA al DD/MM/AAAA)".
•	Métricas: Valor: 130 | Prioridad: 2 | Estimación: 6h
HU-050: Detalle Analítico de Horas Extras en Boleta
•	Descripción: Como un Empleado, necesito verificar la cantidad exacta de horas de sobretiempo calculadas y el monto pagado por cada tasa, con la finalidad de transparentar los montos variables de mi jornada laboral.
•	Criterios de Validación:
o	Las casillas etiquetadas como "N° Horas Extras" y "Pago Horas Extras" renderizarán de forma exact la información procesada previamente por el contador en el módulo de cálculo.
•	Estado: Completado
•	Nota: BoletaResponseDTO expone horasExtras25/horasExtras35 (cantidad de horas) además de los movimientos HORA_EXTRA_25/HORA_EXTRA_35 (monto pagado por cada tasa), ambos visibles en BoletaDetallePage.jsx.
•	Métricas: Valor: 140 | Prioridad: 1 | Estimación: 8h
HU-051: Segmentación Central de Conceptos Monetarios
•	Descripción: Como un Empleado, necesito visualizar las celdas de Concepto, Ingresos y Deducciones de manera simétrica y alineada, con la finalidad de facilitar la lectura y comparación vertical de los rubros.
•	Criterios de Validación:
o	El bloque central de la boleta separará de forma rigurosa los conceptos remunerativos y bonos en una columna izquierda y las retenciones previsionales de jubilación en una columna derecha.
•	Estado: Completado
•	Nota: BoletaResponseDTO ahora expone la lista completa de movimientos (mismo desglose por TipoConcepto ya usado en el Módulo 03); BoletaDetallePage.jsx los renderiza en tres secciones — Ingresos, Descuentos y Aportes del empleador — no solo agregados.
•	Métricas: Valor: 180 | Prioridad: 1 | Estimación: 12h
HU-052: Fila de Totales de Control Vertical
•	Descripción: Como un Empleado, necesito visualizar una fila horizontal dedicada a los totales en el pie de la matriz de la boleta, con la finalidad de certificar de forma visual la cuadratura exacta del dinero neto a percibir.
•	Criterios de Validación:
o	La celda inferior mostrará la suma final vertical de la columna de ingresos, de la columna de deducciones y el gran total resultante que deberá coincidir al céntimo con la base de datos de planillas.
•	Estado: Completado
•	Nota: Boleta.calcularSueldoNeto() en backend + fila de Neto en BoletaDetallePage.jsx.
•	Métricas: Valor: 190 | Prioridad: 1 | Estimación: 8h
HU-053: Comando de Impresión Nativa de Documento
•	Descripción: Como un Empleado, necesito un botón interactivo para imprimir mi boleta, con la finalidad de obtener una copia en papel de respaldo físico para mis trámites de archivo personales.
•	Criterios de Validación:
o	Al pulsar el icono de impresión, el sistema invocará de forma directa la ventana de diálogo de impresión nativa del navegador web, formateando el lienzo para ocultar los menús del sistema informático.
•	Estado: Completado
•	Nota: Botón "Imprimir" en BoletaDetallePage.jsx invoca window.print(), con clase no-print en la barra de acciones.
•	Métricas: Valor: 140 | Prioridad: 2 | Estimación: 6h
HU-054: Exportación Limpia a Formato Documental PDF
•	Descripción: Como un Empleado, necesito un botón para descargar mi comprobante de pago en formato .pdf, con la finalidad de guardarlo digitalmente en mis dispositivos o enviarlo de manera remota.
•	Criterios de Validación:
o	El sistema procesará el renderizado mediante librerías de backend generando un archivo PDF descargable de forma instantánea que mantenga las tipografías y las tablas perfectamente alineadas.
•	Estado: Completado
•	Nota: Nuevo endpoint GET /api/boletas/{id}/pdf, generado en backend con OpenPDF (BoletaPdfRendererAdapter) — encabezado de empresa, datos del trabajador, tabla itemizada y totales. Botón "Descargar PDF" en BoletaDetallePage.jsx.
•	Métricas: Valor: 200 | Prioridad: 1 | Estimación: 16h
HU-055: Portal de Autoservicio para Login de Operarios
•	Descripción: Como un Operario de Planta, necesito una caja de inicio de sesión segura ingresando mi número de DNI y contraseña privada, con la finalidad de acceder de forma confidencial a mi repositorio individual de planillas.
•	Criterios de Validación:
o	El sistema validará las credenciales en la base de datos MySQL y, al detectar el rol de usuario empleado, restringirá la navegación impidiendo el acceso a los módulos de administración de personal o configuración global.
•	Estado: Completado
•	Nota: Nuevo rol EMPLEADO + JWT con claim idEmpleado (UsuarioSec ya se podía vincular a un Empleado, pero UsuarioMapper ignoraba ese dato al crear la cuenta — se corrigió). Login redirige a /mi-portal (EmpleadoLayout, sin menú administrativo) en vez de /planillas. La restricción es real en backend: 10 controladores administrativos (Empleado, Planilla, Empresa, Puesto, Banco, SistemaPension, ConceptoPago, ParametroLegal, Permission, Asistencia) quedaron con @PreAuthorize("hasRole('ADMINISTRADOR')"), y las guardas de ruta NoEmpleadoRoute/EmpleadoOnlyRoute bloquean la navegación de cada lado en el frontend.
•	Métricas: Valor: 190 | Prioridad: 1 | Estimación: 14h
HU-056: Repositorio de Históricos de Boletas Anuales
•	Descripción: Como un Empleado, necesito acceder a un listado con mis comprobantes de pago de meses y años fiscales pasados, con la finalidad de agilizar los sustentos de ingresos exigidos para la postulación a créditos financieros.
•	Criterios de Validación:
o	La interfaz proveerá una grilla secundaria interactiva ordenada por años donde se listarán todos los registros históricos de remuneraciones netas aprobadas listos para su descarga digital.
•	Estado: Completado
•	Nota: Nuevo endpoint GET /api/boletas/me (BoletaController, restringido a hasRole('EMPLEADO'), filtra por el idEmpleado del JWT vía BoletaRepositoryPort.findByEmpleadoId). MiPortalPage.jsx agrupa las boletas propias por año, cada una con su PDF descargable.
•	Métricas: Valor: 160 | Prioridad: 2 | Estimación: 12h
HU-057: Notificación de Disponibilidad por Correo Electrónico
•	Descripción: Como un Empleado, necesito una alerta automática en mi correo personal cuando mi nómina haya sido procesada, con la finalidad de enterarme al instante de la liberación de mi pago mensual.
•	Criterios de Validación:
o	El backend de Spring Boot disparará un trigger de correo electrónico al cambio del estado de la boleta mensual a "Generada", adjuntando un enlace seguro directo al portal de autoservicio web.
•	Estado: Completado
•	Nota: BoletaService.save() dispara el correo automáticamente al generar la boleta (si el empleado tiene correo registrado); el envío es "best effort" — un fallo de SMTP nunca bloquea el guardado, solo se loguea. Va más allá del criterio original: además del enlace al portal, el correo lleva la boleta en PDF adjunta (EmailSenderAdapter con MimeMessageHelper, reutiliza BoletaPdfRendererPort). Probado de punta a punta con envío real (Gmail SMTP + contraseña de aplicación) — las credenciales viven en application.properties, mismo patrón que la contraseña de MySQL y la clave del JWT.
•	Métricas: Valor: 150 | Prioridad: 3 | Estimación: 12h
HU-058: Firma Digital de Conformidad de Haberes
•	Descripción: Como un Empleado, necesito marcar un componente de verificación interactivo dentro del portal, con la finalidad de firmar virtualmente mi boleta expresando conformidad con el abono recibido.
•	Criterios de Validación:
o	Al pulsar el check de conformidad, el sistema almacenará un token de autenticidad en la base de datos, mutando el estado interno del registro de la boleta de pago a "FIRMADO POR EMPLEADO".
•	Estado: Completado
•	Nota: Nuevo endpoint PATCH /api/boletas/{id}/firmar (hasRole('EMPLEADO'), verifica dueño y que la boleta esté en GENERADA) — Boleta.marcarComoFirmada() ahora también registra fechaFirma. Botón "Confirmar conformidad" en MiBoletaDetallePage.jsx, disparado por el propio Empleado (ya no por el Administrador: se le quitó esa transición a su botón genérico en BoletaDetallePage.jsx). "Token de autenticidad" se interpretó como el registro inmutable de estado + fecha/hora de firma, no un token criptográfico (no hay infraestructura de ese tipo en el proyecto).
•	Métricas: Valor: 170 | Prioridad: 2 | Estimación: 18h
HU-059: Emisión de Solicitudes de Ajustes en Conceptos
•	Descripción: Como un Empleado, necesito un buzón interno para reportar un reclamo por errores en mis horas extras o bonos calculados, con la finalidad de notificar directamente al área de contabilidad sobre inconsistencias.
•	Criterios de Validación:
o	Una caja de comentarios procesará el envío de un ticket de soporte de texto que aparecerá de forma inmediata como una alerta de notificación en la pantalla de inicio del administrador del sistema web.
•	Estado: Completado
•	Nota: Nuevo agregado `SolicitudAjuste` (vertical completa: domain/port/service/entity/repository/DTOs/controller). El empleado reporta un reclamo sobre una boleta propia desde "Reportar un problema" en `MiBoletaDetallePage.jsx` (`POST /api/solicitudes-ajuste`, valida que la boleta le pertenezca). El administrador ve un `Alert` con el conteo de pendientes en el panel de inicio (`PlanillasPage.jsx`, vía `GET /api/solicitudes-ajuste/pendientes/count`) y gestiona el buzón completo en `/solicitudes-ajuste` (`SolicitudesAjustePage.jsx`), pudiendo marcar cada ticket como atendido.
•	Métricas: Valor: 130 | Prioridad: 3 | Estimación: 10h
HU-060: Compartición Automatizada mediante la API de WhatsApp
•	Descripción: Como un Administrador de RR.HH., necesito un acceso directo para remitir el comprobante de haberes por chat, con la finalidad de agilizar las entregas a los operarios textiles que no cuenten con un correo electrónico institucional activo.
•	Criterios de Validación:
o	Al hacer clic en el icono de mensajería de la grilla, el sistema abrirá un enlace parametrizado hacia la API de WhatsApp Web cargando de forma automática el número celular del operario y un texto predefinido con el link seguro de descarga del PDF.
•	Estado: Completado
•	Nota: `JwtUtils.createBoletaShareToken`/`validateBoletaShareToken` emiten un JWT de alcance mínimo (solo `idBoleta`, sin `authorities`, 72h de validez, claim `tipo=COMPARTIR_BOLETA` para que no se acepte un JWT de login ni el token de otra boleta). `POST /api/boletas/{id}/compartir` (solo ADMINISTRADOR) genera el token; `GET /api/boletas/compartida/pdf?token=...` (público, sin sesión, abierto explícitamente en `SecurityConfig`) sirve el PDF de esa boleta puntual. El botón "Enviar por WhatsApp" en `BoletaDetallePage.jsx` ahora pega ese enlace directo al PDF en el mensaje de wa.me, en vez de un link a la página interna que exigía login.
•	Métricas: Valor: 130 | Prioridad: 3 | Estimación: 16h
