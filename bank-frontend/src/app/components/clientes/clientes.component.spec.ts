import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';
import { ClientesComponent } from './clientes.component';
import { ApiService } from '../../services/api.service';
import { of, throwError } from 'rxjs';

describe('ClientesComponent', () => {
  let component: ClientesComponent;
  let fixture: ComponentFixture<ClientesComponent>;
  let apiService: jest.Mocked<ApiService>;

  const mockClientes = [
    {
      clienteId: 1,
      nombre: 'Juan Pérez',
      genero: 'Masculino',
      edad: 30,
      identificacion: '1234567890',
      direccion: 'Calle 123',
      telefono: '0987654321',
      contrasena: 'pass123',
      estado: true
    },
    {
      clienteId: 2,
      nombre: 'María López',
      genero: 'Femenino',
      edad: 25,
      identificacion: '0987654321',
      direccion: 'Avenida 456',
      telefono: '0123456789',
      contrasena: 'pass456',
      estado: true
    }
  ];

  beforeEach(async () => {
    const apiServiceMock = {
      getClientes: jest.fn(),
      createCliente: jest.fn(),
      updateCliente: jest.fn(),
      deleteCliente: jest.fn()
    };

    await TestBed.configureTestingModule({
      imports: [ClientesComponent, HttpClientTestingModule, FormsModule],
      providers: [
        { provide: ApiService, useValue: apiServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ClientesComponent);
    component = fixture.componentInstance;
    apiService = TestBed.inject(ApiService) as jest.Mocked<ApiService>;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load clientes on init', () => {
    apiService.getClientes.mockReturnValue(of(mockClientes));
    
    component.ngOnInit();

    expect(apiService.getClientes).toHaveBeenCalled();
    expect(component.clientes).toEqual(mockClientes);
  });

  it('should filter clientes by search term', () => {
    component.clientes = mockClientes;
    component.busqueda = 'juan';

    const filtered = component.clientesFiltrados;

    expect(filtered.length).toBe(1);
    expect(filtered[0].nombre).toBe('Juan Pérez');
  });

  it('should create a new cliente successfully', () => {
    const newCliente = {
      nombre: 'Carlos Gómez',
      genero: 'Masculino',
      edad: 35,
      identificacion: '1111111111',
      direccion: 'Plaza 789',
      telefono: '0999999999',
      contrasena: 'pass789',
      estado: true
    };

    apiService.createCliente.mockReturnValue(of({ ...newCliente, clienteId: 3 }));
    apiService.getClientes.mockReturnValue(of([...mockClientes, { ...newCliente, clienteId: 3 }]));

    component.nuevoCliente = newCliente;
    component.guardarCliente();

    expect(apiService.createCliente).toHaveBeenCalledWith(newCliente);
    expect(component.mensaje).toContain('exitosamente');
    expect(component.mensajeTipo).toBe('success');
  });

  it('should update an existing cliente successfully', () => {
    const updatedCliente = { ...mockClientes[0], nombre: 'Juan Carlos Pérez' };

    apiService.updateCliente.mockReturnValue(of(updatedCliente));
    apiService.getClientes.mockReturnValue(of(mockClientes));

    component.modoEdicion = true;
    component.clienteSeleccionado = updatedCliente;
    component.nuevoCliente = updatedCliente;
    component.guardarCliente();

    expect(apiService.updateCliente).toHaveBeenCalledWith(updatedCliente.clienteId, updatedCliente);
    expect(component.mensaje).toContain('actualizado');
  });

  it('should handle error when loading clientes', () => {
    apiService.getClientes.mockReturnValue(throwError(() => new Error('Network error')));

    component.cargarClientes();

    expect(component.mensajeTipo).toBe('error');
    expect(component.mensaje).toContain('Error');
  });

  it('should delete a cliente', () => {
    apiService.deleteCliente.mockReturnValue(of(void 0));
    apiService.getClientes.mockReturnValue(of([mockClientes[1]]));

    component.eliminarCliente(1);

    expect(apiService.deleteCliente).toHaveBeenCalledWith(1);
  });
});
