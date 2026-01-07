package com.bank.app.controller;

import com.bank.app.dto.ReporteMovimientoDTO;
import com.bank.app.service.ReporteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST Controller for generating transaction reports.
 */
@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
@Slf4j
public class ReporteController {
    
    private final ReporteService reporteService;
    
    @GetMapping
    public ResponseEntity<List<ReporteMovimientoDTO>> generarReporte(
            @RequestParam Long clienteId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        
        List<ReporteMovimientoDTO> reporte = reporteService.generarReporte(clienteId, fechaInicio, fechaFin);
        return ResponseEntity.ok(reporte);
    }
}
