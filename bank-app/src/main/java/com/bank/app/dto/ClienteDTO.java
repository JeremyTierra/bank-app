package com.bank.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {
    
    private Long clienteId;
    
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    
    @NotBlank(message = "El género es obligatorio")
    private String genero;
    
    @NotNull(message = "La edad es obligatoria")
    private Integer edad;
    
    @NotBlank(message = "La identificación es obligatoria")
    private String identificacion;
    
    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;
    
    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;
    
    // La contraseña es opcional en actualizaciones (puede ser null si no se cambia)
    private String contrasena;
    
    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;
}
