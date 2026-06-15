package com.todocodeacademy.sistema_planilla.infraestructure.mapper;

import com.todocodeacademy.sistema_planilla.domain.model.Empleado;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.EmpleadoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmpleadoEntMapper {

    private final PuestoEntMapper mapperPuesto;
    private final SistemaPensionEntMapper mapperSistema;
    private final BancoEntMapper mapperBanco;

    // ==========================================
    // ENTITY → DOMAIN
    // ==========================================

    public Empleado toDomain(EmpleadoEntity entity) {

        if (entity == null) {
            return null;
        }

        return Empleado.reconstruir(
                entity.getIdEmpleado(),
                entity.getNombre(),
                entity.getApellido(),
                entity.getTipoDocumento(),
                entity.getNumeroDocumento(),
                entity.getFechaNacimiento(),
                entity.getSexo(),
                entity.getEstadoCivil(),
                entity.getNacionalidad(),
                entity.getCorreo(),
                entity.getDireccionCompleta(),
                entity.getDistrito(),
                entity.getProvincia(),
                entity.getDepartamento(),
                entity.getFechaIngreso(),
                entity.isEstado(),
                entity.getFechaCese(),

                entity.getPuesto() != null
                        ? mapperPuesto.toDomain(entity.getPuesto())
                        : null,

                entity.getRegimenLaboral(),
                entity.isTieneHijosCalificados(),

                entity.getSistemaPension() != null
                        ? mapperSistema.toDomain(entity.getSistemaPension())
                        : null,

                entity.getBanco() != null
                        ? mapperBanco.toDomain(entity.getBanco())
                        : null,

                entity.getCodigoPension(),
                entity.getNombreAfp(),
                entity.getNumeroCuentaBanco(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    // ==========================================
    // DOMAIN → ENTITY
    // ==========================================

    public EmpleadoEntity toEntity(Empleado domain) {

        if (domain == null) {
            return null;
        }

        EmpleadoEntity entity = new EmpleadoEntity();

        entity.setIdEmpleado(domain.getIdEmpleado());
        entity.setNombre(domain.getNombre());
        entity.setApellido(domain.getApellido());

        entity.setTipoDocumento(domain.getTipoDocumento());
        entity.setNumeroDocumento(domain.getNumeroDocumento());

        entity.setFechaNacimiento(domain.getFechaNacimiento());
        entity.setSexo(domain.getSexo());
        entity.setEstadoCivil(domain.getEstadoCivil());

        entity.setNacionalidad(domain.getNacionalidad());
        entity.setCorreo(domain.getCorreo());

        entity.setDireccionCompleta(domain.getDireccionCompleta());
        entity.setDistrito(domain.getDistrito());
        entity.setProvincia(domain.getProvincia());
        entity.setDepartamento(domain.getDepartamento());

        entity.setFechaIngreso(domain.getFechaIngreso());
        entity.setEstado(domain.estaActivo());
        entity.setFechaCese(domain.getFechaCese());

        // MAPEAR COMPLETO
        if (domain.getPuesto() != null) {
            entity.setPuesto(
                    mapperPuesto.toEntity(domain.getPuesto())
            );
        }

        entity.setRegimenLaboral(domain.getRegimenLaboral());
        entity.setTieneHijosCalificados(
                domain.isTieneHijosCalificados()
        );

        if (domain.getSistemaPension() != null) {
            entity.setSistemaPension(
                    mapperSistema.toEntity(
                            domain.getSistemaPension()
                    )
            );
        }

        if (domain.getBanco() != null) {
            entity.setBanco(
                    mapperBanco.toEntity(
                            domain.getBanco()
                    )
            );
        }

        entity.setCodigoPension(domain.getCodigoPension());
        entity.setNombreAfp(domain.getNombreAfp());
        entity.setNumeroCuentaBanco(domain.getNumeroCuentaBanco());

        return entity;
    }
}