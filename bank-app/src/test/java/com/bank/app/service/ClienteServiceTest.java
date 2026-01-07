package com.bank.app.service;

import com.bank.app.dto.ClienteDTO;
import com.bank.app.entity.Cliente;
import com.bank.app.exception.BusinessException;
import com.bank.app.exception.ResourceNotFoundException;
import com.bank.app.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ClienteService.
 * Tests business logic, validations, and password encryption.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ClienteService Tests")
class ClienteServiceTest {
    
    @Mock
    private ClienteRepository clienteRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @InjectMocks
    private ClienteService clienteService;
    
    private Cliente cliente;
    private ClienteDTO clienteDTO;
    
    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setClienteId(1L);
        cliente.setNombre("Jose Lema");
        cliente.setGenero("Masculino");
        cliente.setEdad(30);
        cliente.setIdentificacion("1234567890");
        cliente.setDireccion("Otavalo sn y principal");
        cliente.setTelefono("098254785");
        cliente.setContrasena("$2a$10$encodedPassword");
        cliente.setEstado(true);
        
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
    @DisplayName("Should find all clientes successfully")
    void findAll_ShouldReturnListOfClientes() {
        // Arrange
        when(clienteRepository.findAll()).thenReturn(Arrays.asList(cliente));
        
        // Act
        List<ClienteDTO> result = clienteService.findAll();
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Jose Lema", result.get(0).getNombre());
        assertNull(result.get(0).getContrasena(), "Password should not be exposed");
        verify(clienteRepository, times(1)).findAll();
    }
    
    @Test
    @DisplayName("Should find cliente by ID successfully")
    void findById_WhenClienteExists_ShouldReturnCliente() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        
        // Act
        ClienteDTO result = clienteService.findById(1L);
        
        // Assert
        assertNotNull(result);
        assertEquals("Jose Lema", result.getNombre());
        assertEquals("1234567890", result.getIdentificacion());
        assertNull(result.getContrasena(), "Password should not be exposed");
    }
    
    @Test
    @DisplayName("Should throw exception when cliente not found by ID")
    void findById_WhenClienteNotExists_ShouldThrowException() {
        // Arrange
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            clienteService.findById(999L);
        });
    }
    
    @Test
    @DisplayName("Should save new cliente with encrypted password")
    void save_WhenNewCliente_ShouldEncryptPasswordAndSave() {
        // Arrange
        clienteDTO.setClienteId(null); // Mark as new cliente
        when(clienteRepository.existsByIdentificacion(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$encodedPassword");
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        
        // Act
        ClienteDTO result = clienteService.save(clienteDTO);
        
        // Assert
        assertNotNull(result);
        assertEquals("Jose Lema", result.getNombre());
        assertNull(result.getContrasena(), "Password should not be exposed");
        verify(passwordEncoder, times(1)).encode("1234");
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }
    
    @Test
    @DisplayName("Should throw exception when identificacion already exists")
    void save_WhenIdentificacionExists_ShouldThrowBusinessException() {
        // Arrange
        clienteDTO.setClienteId(null); // New cliente
        when(clienteRepository.existsByIdentificacion("1234567890")).thenReturn(true);
        
        // Act & Assert
        assertThrows(BusinessException.class, () -> {
            clienteService.save(clienteDTO);
        });
        verify(clienteRepository, never()).save(any());
    }
    
    @Test
    @DisplayName("Should update cliente and encrypt new password")
    void update_WhenPasswordProvided_ShouldEncryptAndUpdate() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(passwordEncoder.encode("newPassword")).thenReturn("$2a$10$newEncodedPassword");
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        
        clienteDTO.setContrasena("newPassword");
        
        // Act
        ClienteDTO result = clienteService.update(1L, clienteDTO);
        
        // Assert
        assertNotNull(result);
        verify(passwordEncoder, times(1)).encode("newPassword");
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }
    
    @Test
    @DisplayName("Should not update password when empty string provided")
    void update_WhenPasswordEmpty_ShouldNotEncrypt() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        
        clienteDTO.setContrasena("");
        
        // Act
        ClienteDTO result = clienteService.update(1L, clienteDTO);
        
        // Assert
        assertNotNull(result);
        verify(passwordEncoder, never()).encode(anyString());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }
    
    @Test
    @DisplayName("Should delete cliente successfully")
    void delete_WhenClienteExists_ShouldDelete() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        doNothing().when(clienteRepository).delete(any(Cliente.class));
        
        // Act
        clienteService.delete(1L);
        
        // Assert
        verify(clienteRepository, times(1)).delete(cliente);
    }
    
    @Test
    @DisplayName("Should throw exception when deleting non-existent cliente")
    void delete_WhenClienteNotExists_ShouldThrowException() {
        // Arrange
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            clienteService.delete(999L);
        });
        verify(clienteRepository, never()).delete(any());
    }
}
