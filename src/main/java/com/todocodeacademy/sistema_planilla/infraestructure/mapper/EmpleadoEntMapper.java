package com.todocodeacademy.sistema_planilla.infraestructure.mapper;

import com.todocodeacademy.sistema_planilla.domain.model.Empleado;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.BancoEntity;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.EmpleadoEntity;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.PuestoEntity;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.SistemaPensionEntity;
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
                mapperPuesto.toDomain(entity.getPuesto()),
                entity.getRegimenLaboral(),
                entity.isTieneHijosCalificados(),
                mapperSistema.toDomain(entity.getSistemaPension()),
                mapperBanco.toDomain(entity.getBanco()),
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

        // ==========================================
        // PUESTO
        // ==========================================

        if (domain.getPuesto() != null) {

            PuestoEntity puesto = new PuestoEntity();
            puesto.setIdPuesto(domain.getPuesto().getIdPuesto());

            entity.setPuesto(puesto);
        }

        // ==========================================
        // REGIMEN LABORAL
        // ==========================================

        entity.setRegimenLaboral(domain.getRegimenLaboral());
        entity.setTieneHijosCalificados(
                domain.isTieneHijosCalificados()
        );

        // ==========================================
        // SISTEMA PENSION
        // ==========================================

        if (domain.getSistemaPension() != null) {

            SistemaPensionEntity sistema =
                    new SistemaPensionEntity();

            sistema.setIdSistema(
                    domain.getSistemaPension().getIdSistema()
            );

            entity.setSistemaPension(sistema);
        }

        // ==========================================
        // BANCO
        // ==========================================

        if (domain.getBanco() != null) {

            BancoEntity banco = new BancoEntity();

            banco.setIdBanco(
                    domain.getBanco().getIdBanco()
            );

            entity.setBanco(banco);
        }

        // ==========================================
        // DATOS FINANCIEROS
        // ==========================================

        entity.setCodigoPension(
                domain.getCodigoPension()
        );

        entity.setNombreAfp(
                domain.getNombreAfp()
        );

        entity.setNumeroCuentaBanco(
                domain.getNumeroCuentaBanco()
        );

        return entity;
    }
}