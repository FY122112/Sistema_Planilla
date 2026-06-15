package com.todocodeacademy.sistema_planilla.aplication.service;

import com.todocodeacademy.sistema_planilla.aplication.command.MarcarAsistneciaCommand;
import com.todocodeacademy.sistema_planilla.aplication.ports.input.AsistenciaServicePort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.AsistenciaRepositoryPort;
import com.todocodeacademy.sistema_planilla.aplication.ports.output.EmpleadoRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.Asistencia;
import com.todocodeacademy.sistema_planilla.domain.model.Empleado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AsistenciaService implements AsistenciaServicePort {

    private final AsistenciaRepositoryPort asisRepo;
    private final EmpleadoRepositoryPort empleadoRepo;

    public Asistencia marcarAsistencia(MarcarAsistneciaCommand commnad){

        Empleado empleado = empleadoRepo.findByNumeroDocumento(commnad.numeroDocumento()).orElseThrow( ()->new IllegalArgumentException("Empleado no encontrado"));

        LocalDate hoy = LocalDate.now();
        LocalTime horaActual = LocalTime.now();



        Optional<Asistencia> asistenciaOpt =
                asisRepo.findByEmpleadoAndFecha(empleado, hoy);

        Asistencia asistencia;

        if (asistenciaOpt.isPresent()) {
            asistencia = asistenciaOpt.get();

            if ("entrada".equals(commnad.tipoMarca())) {
                asistencia.registrarEntrada(horaActual);
            } else if ("salida".equals(commnad.tipoMarca())) {
                asistencia.registrarSalida(horaActual);
            } else {
                throw new IllegalArgumentException("Tipo inválido");
            }

        } else {
            if ("salida".equals(commnad.tipoMarca())) {
                throw new IllegalStateException("No hay entrada previa");
            }

            asistencia = Asistencia.crear(empleado, hoy);
            asistencia.registrarEntrada(horaActual);
        }

        // 🔥 lógica de negocio REAL
        //calcularTardanzaYExtras(asistencia, empleado);


        // 🔥 dominio

        //asistencia.calcularHorasExtras(asistencia.getHoraSalida());

        asistencia.calcularTardanza(LocalTime.of(8,0));

        asistencia.calcularHorasExtras(
                LocalTime.of(17,0)
        );

        return asisRepo.save(asistencia);
    }

    @Override
    public Asistencia justificarAsistencia(Long id, String motivo) {

        Asistencia asistencia = asisRepo.findById(id).orElseThrow(()-> new RuntimeException("Asistencia no encontrado") );

        asistencia.justificar(motivo);

        Asistencia actualiazada = asisRepo.save(asistencia);

        return actualiazada;

    }

    @Override
    public List<Asistencia> mostrarReporteDiario(LocalDate fecha) {

        List<Empleado> empleados = empleadoRepo.findAll();

        List<Asistencia> asistencias = new ArrayList<>();

        for (Empleado empleado : empleados) {

            Optional<Asistencia> asistenciaOpt = asisRepo.findByEmpleadoAndFecha(empleado, fecha);

            if (asistenciaOpt.isPresent()) {

                asistencias.add(asistenciaOpt.get());
            }

        }

        return asistencias;
    }


    @Override
    public List<Asistencia> findAll() {
        return asisRepo.findAll();
    }

    @Override
    public Asistencia findById(long id) {
        Asistencia asistencia = asisRepo.findById(id).orElseThrow(()-> new RuntimeException("Asistencia no encontrado") );

        return asistencia;

    }

    @Override
    public Asistencia save(Asistencia asistencia) {

        return asisRepo.save(asistencia);
    }

    @Override
    public Asistencia update(Long id, Asistencia asistencia) {

        Asistencia actual = asisRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Asistencia no encontrado"));

        // 🔥 aplicar cambios usando lógica de dominio
        if (asistencia.getHoraEntrada() != null) {
            actual.registrarEntrada(asistencia.getHoraEntrada());
        }

        if (asistencia.getHoraSalida() != null) {
            actual.registrarSalida(asistencia.getHoraSalida());
        }

        if (asistencia.getMinutosTardanzas() != null) {
            actual.marcarTardanza(asistencia.getMinutosTardanzas());
        }

        if (asistencia.getHorasExtras25() != null || asistencia.getHorasExtras10() != null) {
            actual.registrarHorasExtras(
                    asistencia.getHorasExtras25(),
                    asistencia.getHorasExtras10()
            );
        }

        if (asistencia.getJustificacion() != null) {
            actual.justificar(asistencia.getJustificacion());
        }

        return asisRepo.save(actual);


    }

    @Override
    public void deleteById(Long id) {
        asisRepo.deleteById(id);
    }
}
