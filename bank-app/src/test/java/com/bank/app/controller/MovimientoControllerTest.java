package com.bank.app.controller;

import com.bank.app.dto.MovimientoDTO;
import com.bank.app.service.MovimientoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class MovimientoControllerTest {
    
    @Mock
    private MovimientoService movimientoService;
    
    @InjectMocks
    private MovimientoController movimientoController;
    
    private MovimientoDTO movimientoDTO;
    
    @BeforeEach
    void setUp() {
        movimientoDTO = new MovimientoDTO();
        movimientoDTO.setId(1L);
        movimientoDTO.setFecha(LocalDateTime.now());
        movimientoDTO.setTipoMovimiento("Retiro");
        movimientoDTO.setValor(new BigDecimal("-575.00"));
        movimientoDTO.setSaldo(new BigDecimal("1425.00"));
        movimientoDTO.setNumeroCuenta("478758");
    }
    
    @Test
    void getAllMovimientos_ShouldReturnListOfMovimientos() {
        List<MovimientoDTO> movimientos = Arrays.asList(movimientoDTO);
        when(movimientoService.findAll()).thenReturn(movimientos);
        
        ResponseEntity<List<MovimientoDTO>> response = movimientoController.getAllMovimientos();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Retiro", response.getBody().get(0).getTipoMovimiento());
        assertEquals("478758", response.getBody().get(0).getNumeroCuenta());
        verify(movimientoService, times(1)).findAll();
    }
    
    @Test
    void getMovimientoById_ShouldReturnMovimiento() {
        when(movimientoService.findById(1L)).thenReturn(movimientoDTO);
        
        ResponseEntity<MovimientoDTO> response = movimientoController.getMovimientoById(1L);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Retiro", response.getBody().getTipoMovimiento());
        assertEquals(new BigDecimal("-575.00"), response.getBody().getValor());
    }
    
    @Test
    void createMovimiento_ShouldReturnCreatedMovimiento() {
        when(movimientoService.save(any(MovimientoDTO.class))).thenReturn(movimientoDTO);
        
        ResponseEntity<MovimientoDTO> response = movimientoController.createMovimiento(movimientoDTO);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Retiro", response.getBody().getTipoMovimiento());
        assertEquals(new BigDecimal("1425.00"), response.getBody().getSaldo());
    }
    
    @Test
    void deleteMovimiento_ShouldReturnNoContent() {
        ResponseEntity<Void> response = movimientoController.deleteMovimiento(1L);
        
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(movimientoService, times(1)).delete(1L);
    }
}
