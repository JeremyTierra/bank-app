package com.bank.app.controller;

import com.bank.app.dto.MovimientoDTO;
import com.bank.app.service.MovimientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Movimiento entity operations.
 * Handles transaction-related endpoints.
 */
@RestController
@RequestMapping("/movimientos")
@RequiredArgsConstructor
@Slf4j
public class MovimientoController {
    
    private final MovimientoService movimientoService;
    
    @GetMapping
    public ResponseEntity<List<MovimientoDTO>> getAllMovimientos() {
        List<MovimientoDTO> movimientos = movimientoService.findAll();
        return ResponseEntity.ok(movimientos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MovimientoDTO> getMovimientoById(@PathVariable Long id) {
        MovimientoDTO movimiento = movimientoService.findById(id);
        return ResponseEntity.ok(movimiento);
    }
    
    @GetMapping("/cuenta/{cuentaId}")
    public ResponseEntity<List<MovimientoDTO>> getMovimientosByCuenta(@PathVariable Long cuentaId) {
        List<MovimientoDTO> movimientos = movimientoService.findByCuentaId(cuentaId);
        return ResponseEntity.ok(movimientos);
    }
    
    @PostMapping
    public ResponseEntity<MovimientoDTO> createMovimiento(@Valid @RequestBody MovimientoDTO movimientoDTO) {
        MovimientoDTO savedMovimiento = movimientoService.save(movimientoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMovimiento);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<MovimientoDTO> updateMovimiento(
            @PathVariable Long id,
            @Valid @RequestBody MovimientoDTO movimientoDTO) {
        MovimientoDTO updatedMovimiento = movimientoService.update(id, movimientoDTO);
        return ResponseEntity.ok(updatedMovimiento);
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<MovimientoDTO> patchMovimiento(
            @PathVariable Long id,
            @RequestBody MovimientoDTO movimientoDTO) {
        MovimientoDTO currentMovimiento = movimientoService.findById(id);
        
        if (movimientoDTO.getTipoMovimiento() != null) {
            currentMovimiento.setTipoMovimiento(movimientoDTO.getTipoMovimiento());
        }
        
        MovimientoDTO updatedMovimiento = movimientoService.update(id, currentMovimiento);
        return ResponseEntity.ok(updatedMovimiento);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovimiento(@PathVariable Long id) {
        movimientoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
