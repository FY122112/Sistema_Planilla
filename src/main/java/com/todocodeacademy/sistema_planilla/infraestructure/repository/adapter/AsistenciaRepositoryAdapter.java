package com.todocodeacademy.sistema_planilla.infraestructure.repository.adapter;

import com.todocodeacademy.sistema_planilla.aplication.ports.output.AsistenciaRepositoryPort;
import com.todocodeacademy.sistema_planilla.domain.model.Asistencia;
import com.todocodeacademy.sistema_planilla.domain.model.Empleado;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.AsistenciaEntity;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.EmpleadoEntity;
import com.todocodeacademy.sistema_planilla.infraestructure.mapper.AsistenciaEntMapper;
import com.todocodeacademy.sistema_planilla.infraestructure.mapper.EmpleadoEntMapper;
import com.todocodeacademy.sistema_planilla.infraestructure.repository.JpaAsistenciaRepository;
import com.todocodeacademy.sistema_planilla.infraestructure.repository.JpaEmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AsistenciaRepositoryAdapter implements AsistenciaRepositoryPort {

    private final JpaAsistenciaRepository repository;

    private final JpaEmpresaRepository empRepo;

    private final EmpleadoEntMapper mapperEmp;

    private final AsistenciaEntMapper mapper;

    @Override
    public List<Asistencia> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Asistencia> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Asistencia> findByEmpleadoAndFecha(Empleado empleado, LocalDate fecha) {
        EmpleadoEntity emple = mapperEmp.toEntity(empleado);
        return repository.findByEmpleadoAndFecha(emple, fecha).map(mapper::toDomain);

    }

    @Override
    public Asistencia save(Asistencia banco) {

        AsistenciaEntity entity = mapper.toEntity(banco);

        AsistenciaEntity saveEntity = repository.save(entity);

        // AsistenciaEntMapper#toEntity solo pone el id en el Empleado de la entidad (referencia
        // por FK, correcto para el insert/update). Al insertar (persist), Hibernate no resuelve
        // esa referencia contra la fila real, así que saveEntity.getEmpleado() queda con nombre/
        // demás campos en null y toDomain() revienta con "El nombre es obligatorio" pese a que
        // el insert ya se guardó bien. Se restaura desde el Empleado de dominio ya completo
        // (el mismo que entró a este método) en vez de confiar en lo que deja Hibernate.
        saveEntity.setEmpleado(mapperEmp.toEntity(banco.getEmpleado()));

        return mapper.toDomain(saveEntity);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Long> findIdsEmpleadosConAsistencia(List<Long> idsEmpleados, LocalDate fechaInicio, LocalDate fechaFin) {
        return repository.findByEmpleado_IdEmpleadoInAndFechaBetween(idsEmpleados, fechaInicio, fechaFin)
                .stream()
                .map(a -> a.getEmpleado().getIdEmpleado())
                .distinct()
                .toList();
    }
}
