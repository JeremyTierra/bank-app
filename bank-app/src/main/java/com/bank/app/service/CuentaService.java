package com.bank.app.service;

import com.bank.app.dto.CuentaDTO;
import com.bank.app.entity.Cliente;
import com.bank.app.entity.Cuenta;
import com.bank.app.exception.BusinessException;
import com.bank.app.exception.ResourceNotFoundException;
import com.bank.app.repository.ClienteRepository;
import com.bank.app.repository.CuentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CuentaService {
    
    @Autowired
    private CuentaRepository cuentaRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    public List<CuentaDTO> findAll() {
        return cuentaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public CuentaDTO findById(Long id) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con ID: " + id));
        return convertToDTO(cuenta);
    }
    
    public CuentaDTO findByNumeroCuenta(String numeroCuenta) {
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con número: " + numeroCuenta));
        return convertToDTO(cuenta);
    }
    
    public List<CuentaDTO> findByClienteId(Long clienteId) {
        return cuentaRepository.findByClienteClienteId(clienteId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public CuentaDTO save(CuentaDTO cuentaDTO) {
        if (cuentaDTO.getId() == null && 
            cuentaRepository.existsByNumeroCuenta(cuentaDTO.getNumeroCuenta())) {
            throw new BusinessException("Ya existe una cuenta con el número: " + cuentaDTO.getNumeroCuenta());
        }
        
        Cliente cliente = clienteRepository.findById(cuentaDTO.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + cuentaDTO.getClienteId()));
        
        Cuenta cuenta = convertToEntity(cuentaDTO);
        cuenta.setCliente(cliente);
        
        Cuenta savedCuenta = cuentaRepository.save(cuenta);
        return convertToDTO(savedCuenta);
    }
    
    public CuentaDTO update(Long id, CuentaDTO cuentaDTO) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con ID: " + id));
        
        cuenta.setTipoCuenta(cuentaDTO.getTipoCuenta());
        cuenta.setSaldoInicial(cuentaDTO.getSaldoInicial());
        cuenta.setEstado(cuentaDTO.getEstado());
        
        Cuenta updatedCuenta = cuentaRepository.save(cuenta);
        return convertToDTO(updatedCuenta);
    }
    
    public void delete(Long id) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con ID: " + id));
        cuentaRepository.delete(cuenta);
    }
    
    private CuentaDTO convertToDTO(Cuenta cuenta) {
        CuentaDTO dto = new CuentaDTO();
        dto.setId(cuenta.getId());
        dto.setNumeroCuenta(cuenta.getNumeroCuenta());
        dto.setTipoCuenta(cuenta.getTipoCuenta());
        dto.setSaldoInicial(cuenta.getSaldoInicial());
        dto.setEstado(cuenta.getEstado());
        dto.setClienteId(cuenta.getCliente().getClienteId());
        dto.setClienteNombre(cuenta.getCliente().getNombre());
        return dto;
    }
    
    private Cuenta convertToEntity(CuentaDTO dto) {
        Cuenta cuenta = new Cuenta();
        cuenta.setId(dto.getId());
        cuenta.setNumeroCuenta(dto.getNumeroCuenta());
        cuenta.setTipoCuenta(dto.getTipoCuenta());
        cuenta.setSaldoInicial(dto.getSaldoInicial());
        cuenta.setEstado(dto.getEstado());
        return cuenta;
    }
}
