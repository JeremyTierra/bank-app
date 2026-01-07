package com.bank.app.controller;

import com.bank.app.dto.ClienteDTO;
import com.bank.app.exception.BusinessException;
import com.bank.app.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Cliente entity operations.
 * Provides CRUD endpoints for client management.
 */
@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
@Slf4j
public class ClienteController {
    
    private final ClienteService clienteService;
    
    @GetMapping
    public ResponseEntity<List<ClienteDTO>> getAllClientes() {
        List<ClienteDTO> clientes = clienteService.findAll();
        return ResponseEntity.ok(clientes);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> getClienteById(@PathVariable Long id) {
        ClienteDTO cliente = clienteService.findById(id);
        return ResponseEntity.ok(cliente);
    }
    
    @PostMapping
    public ResponseEntity<ClienteDTO> createCliente(@Valid @RequestBody ClienteDTO clienteDTO) {
        // La contraseña es obligatoria solo al crear
        if (clienteDTO.getContrasena() == null || clienteDTO.getContrasena().trim().isEmpty()) {
            throw new BusinessException("La contraseña es obligatoria");
        }
        ClienteDTO savedCliente = clienteService.save(clienteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCliente);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> updateCliente(
            @PathVariable Long id,
            @Valid @RequestBody ClienteDTO clienteDTO) {
        ClienteDTO updatedCliente = clienteService.update(id, clienteDTO);
        return ResponseEntity.ok(updatedCliente);
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<ClienteDTO> patchCliente(
            @PathVariable Long id,
            @RequestBody ClienteDTO clienteDTO) {
        ClienteDTO currentCliente = clienteService.findById(id);
        
        if (clienteDTO.getNombre() != null) currentCliente.setNombre(clienteDTO.getNombre());
        if (clienteDTO.getGenero() != null) currentCliente.setGenero(clienteDTO.getGenero());
        if (clienteDTO.getEdad() != null) currentCliente.setEdad(clienteDTO.getEdad());
        if (clienteDTO.getDireccion() != null) currentCliente.setDireccion(clienteDTO.getDireccion());
        if (clienteDTO.getTelefono() != null) currentCliente.setTelefono(clienteDTO.getTelefono());
        if (clienteDTO.getContrasena() != null) currentCliente.setContrasena(clienteDTO.getContrasena());
        if (clienteDTO.getEstado() != null) currentCliente.setEstado(clienteDTO.getEstado());
        
        ClienteDTO updatedCliente = clienteService.update(id, currentCliente);
        return ResponseEntity.ok(updatedCliente);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
