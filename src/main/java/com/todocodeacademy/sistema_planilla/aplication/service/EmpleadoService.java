package com.todocodeacademy.sistema_planilla.aplication.service;

import com.todocodeacademy.sistema_planilla.aplication.command.EmpleadoImportRow;
import com.todocodeacademy.sistema_planilla.aplication.command.ImportacionEmpleadoResultado;
import com.todocodeacademy.sistema_planilla.aplication.command.ImportacionEmpleadosResumen;
import com.todocodeacademy.sistema_planilla.aplication.ports.input.EmpleadoServicePort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.EmpleadoRepositoryPort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.PuestoRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.Empleado;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.EstadoCivil;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.RegimenLaboral;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.Sexo;
import com.todocodeacademy.sistema_planilla.domain.model.Enum.TipoDocumento;
import com.todocodeacademy.sistema_planilla.domain.model.Puesto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class EmpleadoService implements EmpleadoServicePort {

    private final EmpleadoRepositoryPort empleadoRepository;
    private final PuestoRepositoryPort puestoRepository;

    @Override
    public List<Empleado> findAll() {
        return empleadoRepository.findAll().stream()
                .filter(e -> !e.isEliminado())
                .toList();
    }

    @Override
    public Empleado findById(Long id) {
        return empleadoRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Empleado no encontrado"));
    }

    // El chequeo de duplicado ignora empleados ya eliminados lógicamente: si el DNI
    // duplicado original fue dado de baja (HU-029, p. ej. un alta mal tipeada), se debe
    // poder volver a registrar ese documento correctamente.
    @Override
    public Empleado save(Empleado empleado) {

        empleadoRepository
                .findByNumeroDocumento(empleado.getNumeroDocumento())
                .ifPresent(e -> {
                    throw new IllegalArgumentException(
                            "Ya existe un empleado con el documento: "
                                    + empleado.getNumeroDocumento()
                    );
                });

        return empleadoRepository.save(empleado);
    }

    @Override
    public Empleado update(Long id, Empleado empleado) {

        findById(id);

        empleadoRepository
                .findByNumeroDocumento(empleado.getNumeroDocumento())
                .ifPresent(existente -> {

                    if (!existente.getIdEmpleado().equals(id)) {
                        throw new IllegalArgumentException(
                                "Ya existe un empleado con el documento: "
                                        + empleado.getNumeroDocumento()
                        );
                    }
                });

        return empleadoRepository.save(empleado);
    }

    // Baja lógica (HU-029): la fila se conserva intacta para no romper el historial de
    // planillas/boletas que ya referencian este id, solo se marca como eliminada y deja de
    // listarse. Ya no es un borrado físico (antes hacía empleadoRepository.deleteById(id),
    // que además hubiera fallado por el FK de detalle_planilla en cualquier empleado con
    // historial de planillas).
    @Override
    public void deleteById(Long id) {
        Empleado empleado = findById(id);
        empleado.eliminarLogicamente();
        empleadoRepository.save(empleado);
    }

    @Override
    public List<Empleado> findByEstado(Boolean estado) {
        return empleadoRepository.findByEstado(estado).stream()
                .filter(e -> !e.isEliminado())
                .toList();
    }

    @Override
    public List<Empleado> findByTieneHijosCalificados(Boolean tieneHijosCalificados) {
        return empleadoRepository.findByTieneHijosCalificados(tieneHijosCalificados).stream()
                .filter(e -> !e.isEliminado())
                .toList();
    }

    @Override
    public List<Empleado> searchByDniOrNameOrLastName(String query) {
        return empleadoRepository.searchByDniOrNameOrLastName(query).stream()
                .filter(e -> !e.isEliminado())
                .toList();
    }

    // =========================
    // IMPORTACIÓN MASIVA (HU-028)
    // =========================
    //
    // Cada fila se procesa de forma independiente: un DNI o nombre con formato inválido en
    // una fila no debe abortar el resto del archivo, solo queda registrada en la bitácora
    // (detalle) como fallida con el motivo.

    private static final Pattern PATRON_DNI = Pattern.compile("^\\d{8}$");
    private static final Pattern PATRON_NOMBRE = Pattern.compile("^[\\p{L} .'-]+$");

    @Override
    public ImportacionEmpleadosResumen importarCsv(List<EmpleadoImportRow> filas) {

        List<ImportacionEmpleadoResultado> detalle = new ArrayList<>();
        int exitosos = 0;

        for (EmpleadoImportRow fila : filas) {

            try {
                Empleado empleado = construirDesdeFila(fila);
                save(empleado);

                detalle.add(new ImportacionEmpleadoResultado(
                        fila.numeroFila(), fila.numeroDocumento(), true, "Registrado correctamente"
                ));
                exitosos++;

            } catch (Exception e) {
                detalle.add(new ImportacionEmpleadoResultado(
                        fila.numeroFila(), fila.numeroDocumento(), false, e.getMessage()
                ));
            }
        }

        return new ImportacionEmpleadosResumen(filas.size(), exitosos, filas.size() - exitosos, detalle);
    }

    private Empleado construirDesdeFila(EmpleadoImportRow fila) {

        if (fila.nombre() == null || !PATRON_NOMBRE.matcher(fila.nombre().trim()).matches()) {
            throw new IllegalArgumentException("Nombre con formato inválido");
        }

        if (fila.apellido() == null || !PATRON_NOMBRE.matcher(fila.apellido().trim()).matches()) {
            throw new IllegalArgumentException("Apellido con formato inválido");
        }

        TipoDocumento tipoDocumento;
        try {
            tipoDocumento = TipoDocumento.valueOf(fila.tipoDocumento().trim().toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Tipo de documento inválido: " + fila.tipoDocumento());
        }

        String numeroDocumento = fila.numeroDocumento() != null ? fila.numeroDocumento().trim() : "";
        if (tipoDocumento == TipoDocumento.DNI && !PATRON_DNI.matcher(numeroDocumento).matches()) {
            throw new IllegalArgumentException("El DNI debe tener exactamente 8 dígitos numéricos");
        }
        if (numeroDocumento.isBlank()) {
            throw new IllegalArgumentException("El número de documento es obligatorio");
        }

        LocalDate fechaNacimiento = parseFecha(fila.fechaNacimiento(), "fecha de nacimiento");
        LocalDate fechaIngreso = parseFecha(fila.fechaIngreso(), "fecha de ingreso");

        Sexo sexo = parseEnum(Sexo.class, fila.sexo(), "sexo");
        EstadoCivil estadoCivil = parseEnum(EstadoCivil.class, fila.estadoCivil(), "estado civil");
        RegimenLaboral regimenLaboral = parseEnum(RegimenLaboral.class, fila.regimenLaboral(), "régimen laboral");

        Puesto puesto = puestoRepository.findByNombrePuesto(fila.puesto() != null ? fila.puesto().trim() : "")
                .orElseThrow(() -> new IllegalArgumentException("Puesto no encontrado: " + fila.puesto()));

        boolean tieneHijosCalificados = "true".equalsIgnoreCase(fila.tieneHijosCalificados())
                || "si".equalsIgnoreCase(fila.tieneHijosCalificados())
                || "1".equals(fila.tieneHijosCalificados());

        Empleado empleado = new Empleado(
                fila.nombre().trim(),
                fila.apellido().trim(),
                numeroDocumento,
                fechaIngreso,
                puesto
        );

        empleado.actualizarDatosPersonales(
                tipoDocumento,
                fechaNacimiento,
                sexo,
                estadoCivil,
                blankToNull(fila.nacionalidad())
        );

        empleado.actualizarContacto(blankToNull(fila.correo()));
        empleado.actualizarTelefono(blankToNull(fila.telefono()));
        empleado.actualizarRegimenLaboral(regimenLaboral, tieneHijosCalificados);

        return empleado;
    }

    private LocalDate parseFecha(String valor, String campo) {
        try {
            return LocalDate.parse(valor.trim());
        } catch (DateTimeParseException | NullPointerException e) {
            throw new IllegalArgumentException(
                    "Formato de " + campo + " inválido (use AAAA-MM-DD): " + valor
            );
        }
    }

    // EstadoCivil usa constantes en formato "Soltero" (no "SOLTERO"), así que la
    // comparación es insensible a mayúsculas/minúsculas en vez de normalizar el texto de
    // entrada a un solo caso fijo.
    private <E extends Enum<E>> E parseEnum(Class<E> tipo, String valor, String campo) {

        String limpio = valor != null ? valor.trim() : "";

        for (E constante : tipo.getEnumConstants()) {
            if (constante.name().equalsIgnoreCase(limpio)) {
                return constante;
            }
        }

        throw new IllegalArgumentException("Valor de " + campo + " inválido: " + valor);
    }

    private String blankToNull(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }
}