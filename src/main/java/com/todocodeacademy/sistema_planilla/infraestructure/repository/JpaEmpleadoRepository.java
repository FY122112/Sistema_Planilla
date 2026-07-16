package com.todocodeacademy.sistema_planilla.infraestructure.repository;

import com.todocodeacademy.sistema_planilla.infraestructure.entity.EmpleadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface JpaEmpleadoRepository extends JpaRepository<EmpleadoEntity,Long> {

    // El DNI ya no es único a nivel de columna (HU-029: puede repetirse entre un registro
    // eliminado lógicamente y su corrección), así que la búsqueda "activa" excluye los
    // eliminados explícitamente para no romper la unicidad esperada del resultado.
    Optional<EmpleadoEntity> findByNumeroDocumentoAndEliminadoFalse(String numeroDocumento);

    /**
     * Busca todos los empleados que tienen un estado específico.
     * @param estado El estado del empleado (true para activo, false para inactivo/cesado).
     * @return Una lista de empleados que coinciden con el estado especificado.
     */
    List<EmpleadoEntity> findByEstado(Boolean estado);

    /**
     * Busca todos los empleados que tienen hijos calificados para asignación familiar.
     * @param tieneHijosCalificados Booleano que indica si tiene hijos calificados (true) o no (false).
     * @return Una lista de empleados con hijos calificados.
     */
    List<EmpleadoEntity> findByTieneHijosCalificados(Boolean tieneHijosCalificados);

    /**
     * Busca empleados cuya fecha de ingreso se encuentra dentro de un rango especificado.
     * @param startDate La fecha de inicio del rango (inclusive).
     * @param endDate La fecha de fin del rango (inclusive).
     * @return Una lista de empleados que ingresaron en el rango de fechas.
     */
    List<EmpleadoEntity> findByFechaIngresoBetween(LocalDate startDate, LocalDate endDate);

    @Query("""
       SELECT e FROM EmpleadoEntity e
       WHERE 
           :query IS NULL 
           OR :query = ''
           OR LOWER(e.numeroDocumento) LIKE LOWER(CONCAT('%', :query, '%'))
           OR LOWER(e.nombre) LIKE LOWER(CONCAT('%', :query, '%'))
           OR LOWER(e.apellido) LIKE LOWER(CONCAT('%', :query, '%'))
       """)
    List<EmpleadoEntity> searchByDniOrNameOrLastName(@Param("query") String query);
}
