package com.todocodeacademy.sistema_planilla.infraestructure.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(
        name="banco",
        indexes = {
                @Index(name = "idx_banco_codigo", columnList = "codigoBanco")
        }
)
public class BancoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idBanco;

    @Column(nullable = false, length = 100)
    String nombreBanco;

    @Column(nullable = false, unique = true, length = 20)
    String codigoBanco;

    @CreationTimestamp
    @Column(name="created_at", nullable = false, updatable = false)
    Instant createdAt;

    @UpdateTimestamp
    @Column(name="updated_at", nullable = false)
    Instant updatedAt;
}