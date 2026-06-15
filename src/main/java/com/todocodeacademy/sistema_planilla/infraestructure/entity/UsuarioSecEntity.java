package com.todocodeacademy.sistema_planilla.infraestructure.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UsuarioSecEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idUsuario;

    @Column(nullable = false, unique = true, length = 50)
    String username;

    @Column(nullable = false, length = 255)
    String password;

    @Column(nullable = false, unique = true, length = 100)
    String email;

    @Column(nullable = false)
    boolean enabled;

    @Column(nullable = false)
    boolean accountNonExpired;

    @Column(nullable = false)
    boolean accountNonLocked;

    @Column(nullable = false)
    boolean credentialsNonExpired;

    @Column(name = "last_login")
    Instant lastLogin;

    // 🔥 RELACIÓN MUCHOS A MUCHOS
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "usuario_role",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    Set<RoleEntity> roles = new HashSet<>();

    // 🔥 RELACIONES
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_id")
    EmpleadoEntity empleado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id")
    EmpresaEntity empresa;

    // AUDITORÍA
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    Instant updatedAt;

    // =========================
    // 🔒 DEFAULTS
    // =========================
    @PrePersist
    public void prePersist() {
        this.enabled = true;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
    }
}