import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';
import { CuentasComponent } from './cuentas.component';
import { ApiService } from '../../services/api.service';
import { of, throwError } from 'rxjs';

describe('CuentasComponent', () => {
  let component: CuentasComponent;
  let fixture: ComponentFixture<CuentasComponent>;
  let apiService: jest.Mocked<ApiService>;

  const mockClientes = [
    { clienteId: 1, nombre: 'Juan Pérez', estado: true }
  ];

  const mockCuentas = [
    {
      id: 1,
      numeroCuenta: '12345678',
      tipoCuenta: 'Ahorro',
      saldoInicial: 1000,
      estado: true,
      clienteId: 1,
      clienteNombre: 'Juan Pérez'
    },
    {
      id: 2,
      numeroCuenta: '87654321',
      tipoCuenta: 'Corriente',
      saldoInicial: 2000,
      estado: true,
      clienteId: 1,
      clienteNombre: 'Juan Pérez'
    }
  ];

  beforeEach(async () => {
    const apiServiceMock = {
      getClientes: jest.fn(),
      getCuentas: jest.fn(),
      createCuenta: jest.fn(),
      updateCuenta: jest.fn(),
      deleteCuenta: jest.fn()
    };

    await TestBed.configureTestingModule({
      imports: [CuentasComponent, HttpClientTestingModule, FormsModule],
      providers: [
        { provide: ApiService, useValue: apiServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(CuentasComponent);
    component = fixture.componentInstance;
    apiService = TestBed.inject(ApiService) as jest.Mocked<ApiService>;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load cuentas and clientes on init', () => {
    apiService.getCuentas.mockReturnValue(of(mockCuentas));
    apiService.getClientes.mockReturnValue(of(mockClientes as any));
    
    component.ngOnInit();

    expect(apiService.getCuentas).toHaveBeenCalled();
    expect(apiService.getClientes).toHaveBeenCalled();
    expect(component.cuentas).toEqual(mockCuentas);
    expect(component.clientes).toEqual(mockClientes);
  });

  it('should filter cuentas by search term', () => {
    component.cuentas = mockCuentas;
    component.busqueda = '12345678';

    const filtered = component.cuentasFiltradas;

    expect(filtered.length).toBe(1);
    expect(filtered[0].numeroCuenta).toBe('12345678');
  });

  it('should create a new cuenta successfully', () => {
    const newCuenta = {
      numeroCuenta: '11111111',
      tipoCuenta: 'Ahorro',
      saldoInicial: 500,
      estado: true,
      clienteId: 1
    };

    apiService.createCuenta.mockReturnValue(of({ ...newCuenta, id: 3 }));
    apiService.getCuentas.mockReturnValue(of([...mockCuentas, { ...newCuenta, id: 3, clienteNombre: 'Juan Pérez' }]));

    component.nuevaCuenta = newCuenta;
    component.guardarCuenta();

    expect(apiService.createCuenta).toHaveBeenCalledWith(newCuenta);
    expect(component.mensaje).toContain('exitosamente');
    expect(component.mensajeTipo).toBe('success');
  });

  it('should validate required fields', () => {
    component.nuevaCuenta = {
      numeroCuenta: '',
      tipoCuenta: '',
      saldoInicial: 0,
      estado: true,
      clienteId: 0
    };

    component.guardarCuenta();

    expect(component.mensajeTipo).toBe('error');
    expect(component.mensaje).toContain('campos');
  });

  it('should update an existing cuenta successfully', () => {
    const updatedCuenta = { ...mockCuentas[0], saldoInicial: 1500 };

    apiService.updateCuenta.mockReturnValue(of(updatedCuenta));
    apiService.getCuentas.mockReturnValue(of(mockCuentas));

    component.modoEdicion = true;
    component.cuentaSeleccionada = updatedCuenta;
    component.nuevaCuenta = updatedCuenta;
    component.guardarCuenta();

    expect(apiService.updateCuenta).toHaveBeenCalledWith(updatedCuenta.id, updatedCuenta);
    expect(component.mensaje).toContain('actualizada');
  });

  it('should delete a cuenta', () => {
    apiService.deleteCuenta.mockReturnValue(of(void 0));
    apiService.getCuentas.mockReturnValue(of([mockCuentas[1]]));

    component.eliminarCuenta(1);

    expect(apiService.deleteCuenta).toHaveBeenCalledWith(1);
  });

  it('should handle error when loading cuentas', () => {
    apiService.getCuentas.mockReturnValue(throwError(() => new Error('Network error')));
    apiService.getClientes.mockReturnValue(of(mockClientes as any));

    component.ngOnInit();

    expect(component.mensajeTipo).toBe('error');
  });
});
