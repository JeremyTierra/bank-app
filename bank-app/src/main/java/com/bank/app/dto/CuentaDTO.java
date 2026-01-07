package com.bank.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuentaDTO {
    
    private Long id;
    
    @NotBlank(message = "El número de cuenta es obligatorio")
    private String numeroCuenta;
    
    @NotBlank(message = "El tipo de cuenta es obligatorio")
    private String tipoCuenta;
    
    @NotNull(message = "El saldo inicial es obligatorio")
    private BigDecimal saldoInicial;
    
    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;
    
    @NotNull(message = "El ID del cliente es obligatorio")
    private Long clienteId;
    
    private String clienteNombre;
}
