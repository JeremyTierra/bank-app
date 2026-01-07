package com.bank.app.service;

import com.bank.app.dto.ReporteMovimientoDTO;
import com.bank.app.entity.Movimiento;
import com.bank.app.exception.ResourceNotFoundException;
import com.bank.app.repository.ClienteRepository;
import com.bank.app.repository.MovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReporteService {
    
    @Autowired
    private MovimientoRepository movimientoRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    public List<ReporteMovimientoDTO> generarReporte(Long clienteId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        // Verificar que el cliente existe
        clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + clienteId));
        
        List<Movimiento> movimientos = movimientoRepository.findByClienteAndFechaBetween(
                clienteId, fechaInicio, fechaFin);
        
        return movimientos.stream()
                .map(this::convertToReporteDTO)
                .collect(Collectors.toList());
    }
    
    private ReporteMovimientoDTO convertToReporteDTO(Movimiento movimiento) {
        ReporteMovimientoDTO dto = new ReporteMovimientoDTO();
        dto.setFecha(movimiento.getFecha());
        dto.setCliente(movimiento.getCuenta().getCliente().getNombre());
        dto.setNumeroCuenta(movimiento.getCuenta().getNumeroCuenta());
        dto.setTipo(movimiento.getCuenta().getTipoCuenta());
        dto.setSaldoInicial(movimiento.getCuenta().getSaldoInicial());
        dto.setEstado(movimiento.getCuenta().getEstado());
        dto.setMovimiento(movimiento.getValor());
        dto.setSaldoDisponible(movimiento.getSaldo());
        return dto;
    }
}
