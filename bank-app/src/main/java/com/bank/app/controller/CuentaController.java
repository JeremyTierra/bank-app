package com.bank.app.controller;

import com.bank.app.dto.CuentaDTO;
import com.bank.app.service.CuentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Cuenta entity operations.
 * Provides endpoints for account management.
 */
@RestController
@RequestMapping("/cuentas")
@RequiredArgsConstructor
@Slf4j
public class CuentaController {
    
    private final CuentaService cuentaService;
    
    @GetMapping
    public ResponseEntity<List<CuentaDTO>> getAllCuentas() {
        List<CuentaDTO> cuentas = cuentaService.findAll();
        return ResponseEntity.ok(cuentas);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CuentaDTO> getCuentaById(@PathVariable Long id) {
        CuentaDTO cuenta = cuentaService.findById(id);
        return ResponseEntity.ok(cuenta);
    }
    
    @GetMapping("/numero/{numeroCuenta}")
    public ResponseEntity<CuentaDTO> getCuentaByNumero(@PathVariable String numeroCuenta) {
        CuentaDTO cuenta = cuentaService.findByNumeroCuenta(numeroCuenta);
        return ResponseEntity.ok(cuenta);
    }
    
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<CuentaDTO>> getCuentasByCliente(@PathVariable Long clienteId) {
        List<CuentaDTO> cuentas = cuentaService.findByClienteId(clienteId);
        return ResponseEntity.ok(cuentas);
    }
    
    @PostMapping
    public ResponseEntity<CuentaDTO> createCuenta(@Valid @RequestBody CuentaDTO cuentaDTO) {
        CuentaDTO savedCuenta = cuentaService.save(cuentaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCuenta);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CuentaDTO> updateCuenta(
            @PathVariable Long id,
            @Valid @RequestBody CuentaDTO cuentaDTO) {
        CuentaDTO updatedCuenta = cuentaService.update(id, cuentaDTO);
        return ResponseEntity.ok(updatedCuenta);
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<CuentaDTO> patchCuenta(
            @PathVariable Long id,
            @RequestBody CuentaDTO cuentaDTO) {
        CuentaDTO currentCuenta = cuentaService.findById(id);
        
        if (cuentaDTO.getTipoCuenta() != null) currentCuenta.setTipoCuenta(cuentaDTO.getTipoCuenta());
        if (cuentaDTO.getSaldoInicial() != null) currentCuenta.setSaldoInicial(cuentaDTO.getSaldoInicial());
        if (cuentaDTO.getEstado() != null) currentCuenta.setEstado(cuentaDTO.getEstado());
        
        CuentaDTO updatedCuenta = cuentaService.update(id, currentCuenta);
        return ResponseEntity.ok(updatedCuenta);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCuenta(@PathVariable Long id) {
        cuentaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
