package com.bank.app.service;

import com.bank.app.dto.ClienteDTO;
import com.bank.app.entity.Cliente;
import com.bank.app.exception.BusinessException;
import com.bank.app.exception.ResourceNotFoundException;
import com.bank.app.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for Cliente entity operations.
 * Handles business logic, validations, and password encryption.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ClienteService {
    
    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    
    /**
     * Retrieves all clients from the database.
     * 
     * @return List of ClienteDTO objects
     */
    @Transactional(readOnly = true)
    public List<ClienteDTO> findAll() {
        log.debug("Finding all clientes");
        return clienteRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Finds a client by their ID.
     * 
     * @param id the client ID
     * @return ClienteDTO object
     * @throws ResourceNotFoundException if client not found
     */
    @Transactional(readOnly = true)
    public ClienteDTO findById(Long id) {
        log.debug("Finding cliente with id: {}", id);
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));
        return convertToDTO(cliente);
    }
    
    /**
     * Creates a new client with encrypted password.
     * 
     * @param clienteDTO the client data
     * @return saved ClienteDTO object
     * @throws BusinessException if identification already exists
     */
    public ClienteDTO save(ClienteDTO clienteDTO) {
        log.info("Creating new cliente with identificacion: {}", clienteDTO.getIdentificacion());
        
        if (clienteDTO.getClienteId() == null && 
            clienteRepository.existsByIdentificacion(clienteDTO.getIdentificacion())) {
            throw new BusinessException("Ya existe un cliente con la identificación: " + clienteDTO.getIdentificacion());
        }
        
        Cliente cliente = convertToEntity(clienteDTO);
        Cliente savedCliente = clienteRepository.save(cliente);
        log.info("Cliente created successfully with id: {}", savedCliente.getClienteId());
        return convertToDTO(savedCliente);
    }
    
    /**
     * Updates an existing client.
     * If a new password is provided, it will be encrypted.
     * 
     * @param id the client ID
     * @param clienteDTO the updated client data
     * @return updated ClienteDTO object
     * @throws ResourceNotFoundException if client not found
     */
    public ClienteDTO update(Long id, ClienteDTO clienteDTO) {
        log.info("Updating cliente with id: {}", id);
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));
        
        cliente.setNombre(clienteDTO.getNombre());
        cliente.setGenero(clienteDTO.getGenero());
        cliente.setEdad(clienteDTO.getEdad());
        cliente.setDireccion(clienteDTO.getDireccion());
        cliente.setTelefono(clienteDTO.getTelefono());
        
        // Only update password if provided (not empty)
        if (clienteDTO.getContrasena() != null && !clienteDTO.getContrasena().isEmpty()) {
            cliente.setContrasena(passwordEncoder.encode(clienteDTO.getContrasena()));
        }
        
        cliente.setEstado(clienteDTO.getEstado());
        
        Cliente updatedCliente = clienteRepository.save(cliente);
        log.info("Cliente updated successfully with id: {}", id);
        return convertToDTO(updatedCliente);
    }
    
    /**
     * Deletes a client by ID.
     * 
     * @param id the client ID
     * @throws ResourceNotFoundException if client not found
     */
    public void delete(Long id) {
        log.info("Deleting cliente with id: {}", id);
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));
        clienteRepository.delete(cliente);
        log.info("Cliente deleted successfully with id: {}", id);
    }
    
    /**
     * Converts Cliente entity to ClienteDTO.
     * Note: Password is intentionally NOT included in DTO for security.
     * 
     * @param cliente the entity
     * @return ClienteDTO object
     */
    private ClienteDTO convertToDTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setClienteId(cliente.getClienteId());
        dto.setNombre(cliente.getNombre());
        dto.setGenero(cliente.getGenero());
        dto.setEdad(cliente.getEdad());
        dto.setIdentificacion(cliente.getIdentificacion());
        dto.setDireccion(cliente.getDireccion());
        dto.setTelefono(cliente.getTelefono());
        // Do NOT expose password in responses
        dto.setContrasena(null);
        dto.setEstado(cliente.getEstado());
        return dto;
    }
    
    /**
     * Converts ClienteDTO to Cliente entity with encrypted password.
     * 
     * @param dto the DTO
     * @return Cliente entity
     */
    private Cliente convertToEntity(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setClienteId(dto.getClienteId());
        cliente.setNombre(dto.getNombre());
        cliente.setGenero(dto.getGenero());
        cliente.setEdad(dto.getEdad());
        cliente.setIdentificacion(dto.getIdentificacion());
        cliente.setDireccion(dto.getDireccion());
        cliente.setTelefono(dto.getTelefono());
        // Encrypt password before storing
        cliente.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        cliente.setEstado(dto.getEstado());
        return cliente;
    }
}
