package com.bank.app.dto;

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
public class MovimientoDTO {
    
    private Long id;
    
    private LocalDateTime fecha;
    
    @NotBlank(message = "El tipo de movimiento es obligatorio")
    private String tipoMovimiento;
    
    @NotNull(message = "El valor es obligatorio")
    private BigDecimal valor;
    
    private BigDecimal saldo;
    
    @NotBlank(message = "El número de cuenta es obligatorio")
    private String numeroCuenta;
}
