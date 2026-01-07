package com.bank.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class Persona {
    
    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;
    
    @NotBlank(message = "El género es obligatorio")
    @Column(nullable = false)
    private String genero;
    
    @NotNull(message = "La edad es obligatoria")
    @Column(nullable = false)
    private Integer edad;
    
    @NotBlank(message = "La identificación es obligatoria")
    @Column(nullable = false, unique = true)
    private String identificacion;
    
    @NotBlank(message = "La dirección es obligatoria")
    @Column(nullable = false)
    private String direccion;
    
    @NotBlank(message = "El teléfono es obligatorio")
    @Column(nullable = false)
    private String telefono;
}
