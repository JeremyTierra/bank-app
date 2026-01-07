package com.bank.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cuentas")
public class Cuenta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El número de cuenta es obligatorio")
    @Column(nullable = false, unique = true)
    private String numeroCuenta;
    
    @NotBlank(message = "El tipo de cuenta es obligatorio")
    @Column(nullable = false)
    private String tipoCuenta;
    
    @NotNull(message = "El saldo inicial es obligatorio")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal saldoInicial;
    
    @NotNull(message = "El estado es obligatorio")
    @Column(nullable = false)
    private Boolean estado;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    @OneToMany(mappedBy = "cuenta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Movimiento> movimientos = new ArrayList<>();
}
