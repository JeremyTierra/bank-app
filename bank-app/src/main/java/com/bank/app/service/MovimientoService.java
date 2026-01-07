package com.bank.app.service;

import com.bank.app.dto.MovimientoDTO;
import com.bank.app.entity.Cuenta;
import com.bank.app.entity.Movimiento;
import com.bank.app.exception.BusinessException;
import com.bank.app.exception.ResourceNotFoundException;
import com.bank.app.repository.CuentaRepository;
import com.bank.app.repository.MovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MovimientoService {
    
    @Autowired
    private MovimientoRepository movimientoRepository;
    
    @Autowired
    private CuentaRepository cuentaRepository;
    
    @Value("${app.daily-withdrawal-limit}")
    private BigDecimal dailyWithdrawalLimit;
    
    public List<MovimientoDTO> findAll() {
        return movimientoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public MovimientoDTO findById(Long id) {
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movimiento no encontrado con ID: " + id));
        return convertToDTO(movimiento);
    }
    
    public List<MovimientoDTO> findByCuentaId(Long cuentaId) {
        return movimientoRepository.findByCuentaIdOrderByFechaDesc(cuentaId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public MovimientoDTO save(MovimientoDTO movimientoDTO) {
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(movimientoDTO.getNumeroCuenta())
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con número: " + movimientoDTO.getNumeroCuenta()));
        
        if (!cuenta.getEstado()) {
            throw new BusinessException("La cuenta está inactiva");
        }
        
        // Calcular saldo actual
        BigDecimal saldoActual = calcularSaldoActual(cuenta);
        
        // Validar movimiento
        BigDecimal valorMovimiento = movimientoDTO.getValor();
        
        // Créditos son positivos, débitos son negativos
        if (valorMovimiento.compareTo(BigDecimal.ZERO) < 0) {
            // Es un débito (retiro)
            BigDecimal saldoNuevo = saldoActual.add(valorMovimiento);
            
            if (saldoNuevo.compareTo(BigDecimal.ZERO) < 0) {
                throw new BusinessException("Saldo no disponible");
            }
            
            // Validar límite diario
            BigDecimal retirosHoy = movimientoRepository.sumWithdrawalsToday(cuenta.getId());
            BigDecimal totalRetiros = retirosHoy.add(valorMovimiento.abs());
            
            if (totalRetiros.compareTo(dailyWithdrawalLimit) > 0) {
                throw new BusinessException("Cupo diario Excedido");
            }
        }
        
        // Calcular nuevo saldo
        BigDecimal nuevoSaldo = saldoActual.add(valorMovimiento);
        
        // Crear movimiento
        Movimiento movimiento = new Movimiento();
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setTipoMovimiento(movimientoDTO.getTipoMovimiento());
        movimiento.setValor(valorMovimiento);
        movimiento.setSaldo(nuevoSaldo);
        movimiento.setCuenta(cuenta);
        
        Movimiento savedMovimiento = movimientoRepository.save(movimiento);
        return convertToDTO(savedMovimiento);
    }
    
    public MovimientoDTO update(Long id, MovimientoDTO movimientoDTO) {
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movimiento no encontrado con ID: " + id));
        
        // Actualizar campos permitidos
        movimiento.setTipoMovimiento(movimientoDTO.getTipoMovimiento());
        
        Movimiento updatedMovimiento = movimientoRepository.save(movimiento);
        return convertToDTO(updatedMovimiento);
    }
    
    public void delete(Long id) {
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movimiento no encontrado con ID: " + id));
        movimientoRepository.delete(movimiento);
    }
    
    private BigDecimal calcularSaldoActual(Cuenta cuenta) {
        List<Movimiento> movimientos = movimientoRepository.findByCuentaIdOrderByFechaDesc(cuenta.getId());
        
        if (movimientos.isEmpty()) {
            return cuenta.getSaldoInicial();
        }
        
        // El último movimiento tiene el saldo actual
        return movimientos.get(0).getSaldo();
    }
    
    private MovimientoDTO convertToDTO(Movimiento movimiento) {
        MovimientoDTO dto = new MovimientoDTO();
        dto.setId(movimiento.getId());
        dto.setFecha(movimiento.getFecha());
        dto.setTipoMovimiento(movimiento.getTipoMovimiento());
        dto.setValor(movimiento.getValor());
        dto.setSaldo(movimiento.getSaldo());
        dto.setNumeroCuenta(movimiento.getCuenta().getNumeroCuenta());
        return dto;
    }
}
