import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ApiService } from './api.service';

describe('ApiService', () => {
  let service: ApiService;
  let httpMock: HttpTestingController;
  const baseUrl = '/api';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ApiService]
    });
    service = TestBed.inject(ApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('Clientes', () => {
    it('should get all clientes', () => {
      const mockClientes = [
        { clienteId: 1, nombre: 'Juan Pérez', estado: true },
        { clienteId: 2, nombre: 'María López', estado: true }
      ];

      service.getClientes().subscribe(clientes => {
        expect(clientes.length).toBe(2);
        expect(clientes).toEqual(mockClientes);
      });

      const req = httpMock.expectOne(`${baseUrl}/clientes`);
      expect(req.request.method).toBe('GET');
      req.flush(mockClientes);
    });

    it('should create a cliente', () => {
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

      service.createCliente(newCliente).subscribe(cliente => {
        expect(cliente).toEqual({ ...newCliente, clienteId: 3 });
      });

      const req = httpMock.expectOne(`${baseUrl}/clientes`);
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(newCliente);
      req.flush({ ...newCliente, clienteId: 3 });
    });

    it('should update a cliente', () => {
      const updatedCliente = {
        clienteId: 1,
        nombre: 'Juan Carlos Pérez',
        genero: 'Masculino',
        edad: 31,
        identificacion: '1234567890',
        direccion: 'Calle 123',
        telefono: '0987654321',
        contrasena: 'pass123',
        estado: true
      };

      service.updateCliente(1, updatedCliente).subscribe(cliente => {
        expect(cliente).toEqual(updatedCliente);
      });

      const req = httpMock.expectOne(`${baseUrl}/clientes/1`);
      expect(req.request.method).toBe('PUT');
      req.flush(updatedCliente);
    });

    it('should delete a cliente', () => {
      service.deleteCliente(1).subscribe(response => {
        expect(response).toBeUndefined();
      });

      const req = httpMock.expectOne(`${baseUrl}/clientes/1`);
      expect(req.request.method).toBe('DELETE');
      req.flush(null);
    });
  });

  describe('Cuentas', () => {
    it('should get all cuentas', () => {
      const mockCuentas = [
        { id: 1, numeroCuenta: '12345678', tipoCuenta: 'Ahorro', saldoInicial: 1000 },
        { id: 2, numeroCuenta: '87654321', tipoCuenta: 'Corriente', saldoInicial: 2000 }
      ];

      service.getCuentas().subscribe(cuentas => {
        expect(cuentas.length).toBe(2);
        expect(cuentas).toEqual(mockCuentas);
      });

      const req = httpMock.expectOne(`${baseUrl}/cuentas`);
      expect(req.request.method).toBe('GET');
      req.flush(mockCuentas);
    });
  });

  describe('Movimientos', () => {
    it('should get all movimientos', () => {
      const mockMovimientos = [
        { id: 1, fecha: '2026-01-01', tipoMovimiento: 'Depósito', valor: 100 },
        { id: 2, fecha: '2026-01-02', tipoMovimiento: 'Retiro', valor: -50 }
      ];

      service.getMovimientos().subscribe(movimientos => {
        expect(movimientos.length).toBe(2);
        expect(movimientos).toEqual(mockMovimientos);
      });

      const req = httpMock.expectOne(`${baseUrl}/movimientos`);
      expect(req.request.method).toBe('GET');
      req.flush(mockMovimientos);
    });
  });

  describe('Reportes', () => {
    it('should get reporte with correct parameters', () => {
      const clienteId = 1;
      const fechaInicio = '2026-01-01T00:00:00.000Z';
      const fechaFin = '2026-01-07T23:59:59.999Z';
      const mockReporte = [
        { fecha: '2026-01-01', numeroCuenta: '12345678', tipo: 'Ahorro', movimiento: 100 }
      ];

      service.getReporte(clienteId, fechaInicio, fechaFin).subscribe(reporte => {
        expect(reporte).toEqual(mockReporte);
      });

      const req = httpMock.expectOne(
        (request) => request.url.includes('/reportes') && 
                     request.params.get('clienteId') === clienteId.toString()
      );
      expect(req.request.method).toBe('GET');
      req.flush(mockReporte);
    });
  });
});
