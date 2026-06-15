package com.todocodeacademy.sistema_planilla.infraestructure.repository;

import com.todocodeacademy.sistema_planilla.infraestructure.entity.BoletaEntity;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.DetallePlanillaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface JpaBoletaRepository extends JpaRepository<BoletaEntity,Long> {

    /**
     * Busca una boleta por su detalle de planilla asociado.
     * @param detallePlanilla El detalle de planilla al que está asociada la boleta.
     * @return Un Optional que contiene la Boleta encontrada, o vacío si no existe.
     */
    Optional<BoletaEntity> getBoletaByDetallePlanilla(DetallePlanillaEntity detallePlanilla);

    /**
     * Busca boletas emitidas en un rango de fechas.
     * @param fechaInicio La fecha de inicio del rango (inclusive).
     * @param fechaFin La fecha de fin del rango (inclusive).
     * @return Una lista de Boleta emitidas en el rango especificado.
     */
    List<BoletaEntity> getBoletasByFechaEmisionBetween(LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Busca boletas que han sido firmadas.
     * @param firmada Booleano que indica si la boleta está firmada (true) o no (false).
     * @return Una lista de Boleta que cumplen con la condición de firma.
     */
    List<BoletaEntity> getBoletasByFirmadaStatus(Boolean firmada);

    /**
     * Busca boletas que han sido enviadas.
     * @param enviada Booleano que indica si la boleta ha sido enviada (true) o no (false).
     * @return Una lista de Boleta que cumplen con la condición de envío.
     */
    List<BoletaEntity> getBoletasByEnviadaStatus(Boolean enviada);
}





