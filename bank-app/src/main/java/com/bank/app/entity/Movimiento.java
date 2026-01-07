package com.bank.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movimientos")
public class Movimiento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "La fecha es obligatoria")
    @Column(nullable = false)
    private LocalDateTime fecha;
    
    @NotBlank(message = "El tipo de movimiento es obligatorio")
    @Column(nullable = false)
    private String tipoMovimiento;
    
    @NotNull(message = "El valor es obligatorio")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;
    
    @NotNull(message = "El saldo es obligatorio")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal saldo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuenta_id", nullable = false)
    private Cuenta cuenta;
    
    @PrePersist
    protected void onCreate() {
        if (fecha == null) {
            fecha = LocalDateTime.now();
        }
    }
}
