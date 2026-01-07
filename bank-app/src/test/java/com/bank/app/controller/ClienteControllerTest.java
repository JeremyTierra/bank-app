package com.bank.app.controller;

import com.bank.app.dto.ClienteDTO;
import com.bank.app.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {
    
    @Mock
    private ClienteService clienteService;
    
    @InjectMocks
    private ClienteController clienteController;
    
    private ClienteDTO clienteDTO;
    
    @BeforeEach
    void setUp() {
        clienteDTO = new ClienteDTO();
        clienteDTO.setClienteId(1L);
        clienteDTO.setNombre("Jose Lema");
        clienteDTO.setGenero("Masculino");
        clienteDTO.setEdad(30);
        clienteDTO.setIdentificacion("1234567890");
        clienteDTO.setDireccion("Otavalo sn y principal");
        clienteDTO.setTelefono("098254785");
        clienteDTO.setContrasena("1234");
        clienteDTO.setEstado(true);
    }
    
    @Test
    void getAllClientes_ShouldReturnListOfClientes() {
        List<ClienteDTO> clientes = Arrays.asList(clienteDTO);
        when(clienteService.findAll()).thenReturn(clientes);
        
        ResponseEntity<List<ClienteDTO>> response = clienteController.getAllClientes();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Jose Lema", response.getBody().get(0).getNombre());
        verify(clienteService, times(1)).findAll();
    }
    
    @Test
    void getClienteById_ShouldReturnCliente() {
        when(clienteService.findById(1L)).thenReturn(clienteDTO);
        
        ResponseEntity<ClienteDTO> response = clienteController.getClienteById(1L);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Jose Lema", response.getBody().getNombre());
        assertEquals("098254785", response.getBody().getTelefono());
    }
    
    @Test
    void createCliente_ShouldReturnCreatedCliente() {
        when(clienteService.save(any(ClienteDTO.class))).thenReturn(clienteDTO);
        
        ResponseEntity<ClienteDTO> response = clienteController.createCliente(clienteDTO);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Jose Lema", response.getBody().getNombre());
        assertTrue(response.getBody().getEstado());
    }
    
    @Test
    void updateCliente_ShouldReturnUpdatedCliente() {
        clienteDTO.setTelefono("099999999");
        when(clienteService.update(eq(1L), any(ClienteDTO.class))).thenReturn(clienteDTO);
        
        ResponseEntity<ClienteDTO> response = clienteController.updateCliente(1L, clienteDTO);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("099999999", response.getBody().getTelefono());
    }
    
    @Test
    void deleteCliente_ShouldReturnNoContent() {
        ResponseEntity<Void> response = clienteController.deleteCliente(1L);
        
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(clienteService, times(1)).delete(1L);
    }
}
