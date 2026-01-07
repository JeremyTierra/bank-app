import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Cliente, Cuenta, Movimiento, ReporteMovimiento } from '../models/models';

/**
 * Servicio principal para comunicación con la API REST del backend
 * Proporciona métodos CRUD para todas las entidades del sistema bancario
 * 
 * @remarks
 * - Todas las peticiones pasan por interceptors para manejo de errores y loading
 * - Utiliza paths relativos (/api) para compatibilidad con nginx proxy
 * - Los observables no se suscriben aquí, eso es responsabilidad de los componentes
 */
@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private readonly baseUrl = '/api';

  constructor(private readonly http: HttpClient) {}

  // ==================== CLIENTES ====================
  
  /**
   * Obtiene la lista completa de clientes
   * @returns Observable con array de clientes
   */
  getClientes(): Observable<Cliente[]> {
    return this.http.get<Cliente[]>(`${this.baseUrl}/clientes`);
  }

  /**
   * Obtiene un cliente específico por ID
   * @param id - ID del cliente
   * @returns Observable con los datos del cliente
   */
  getCliente(id: number): Observable<Cliente> {
    return this.http.get<Cliente>(`${this.baseUrl}/clientes/${id}`);
  }

  /**
   * Crea un nuevo cliente
   * @param cliente - Datos del cliente a crear
   * @returns Observable con el cliente creado (incluye ID generado)
   */
  createCliente(cliente: Cliente): Observable<Cliente> {
    return this.http.post<Cliente>(`${this.baseUrl}/clientes`, cliente);
  }

  /**
   * Actualiza un cliente existente
   * @param id - ID del cliente a actualizar
   * @param cliente - Datos actualizados del cliente
   * @returns Observable con el cliente actualizado
   */
  updateCliente(id: number, cliente: Cliente): Observable<Cliente> {
    return this.http.put<Cliente>(`${this.baseUrl}/clientes/${id}`, cliente);
  }

  /**
   * Elimina un cliente
   * @param id - ID del cliente a eliminar
   * @returns Observable vacío
   */
  deleteCliente(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/clientes/${id}`);
  }

  // ==================== CUENTAS ====================
  
  /**
   * Obtiene la lista completa de cuentas
   * @returns Observable con array de cuentas
   */
  getCuentas(): Observable<Cuenta[]> {
    return this.http.get<Cuenta[]>(`${this.baseUrl}/cuentas`);
  }

  /**
   * Obtiene una cuenta específica por ID
   * @param id - ID de la cuenta
   * @returns Observable con los datos de la cuenta
   */
  getCuenta(id: number): Observable<Cuenta> {
    return this.http.get<Cuenta>(`${this.baseUrl}/cuentas/${id}`);
  }

  /**
   * Obtiene todas las cuentas de un cliente específico
   * @param clienteId - ID del cliente
   * @returns Observable con array de cuentas del cliente
   */
  getCuentasByCliente(clienteId: number): Observable<Cuenta[]> {
    return this.http.get<Cuenta[]>(`${this.baseUrl}/cuentas/cliente/${clienteId}`);
  }

  /**
   * Crea una nueva cuenta
   * @param cuenta - Datos de la cuenta a crear
   * @returns Observable con la cuenta creada (incluye ID generado)
   */
  createCuenta(cuenta: Cuenta): Observable<Cuenta> {
    return this.http.post<Cuenta>(`${this.baseUrl}/cuentas`, cuenta);
  }

  /**
   * Actualiza una cuenta existente
   * @param id - ID de la cuenta a actualizar
   * @param cuenta - Datos actualizados de la cuenta
   * @returns Observable con la cuenta actualizada
   */
  updateCuenta(id: number, cuenta: Cuenta): Observable<Cuenta> {
    return this.http.put<Cuenta>(`${this.baseUrl}/cuentas/${id}`, cuenta);
  }

  /**
   * Elimina una cuenta
   * @param id - ID de la cuenta a eliminar
   * @returns Observable vacío
   */
  deleteCuenta(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/cuentas/${id}`);
  }

  // ==================== MOVIMIENTOS ====================
  
  /**
   * Obtiene la lista completa de movimientos
   * @returns Observable con array de movimientos
   */
  getMovimientos(): Observable<Movimiento[]> {
    return this.http.get<Movimiento[]>(`${this.baseUrl}/movimientos`);
  }

  /**
   * Obtiene un movimiento específico por ID
   * @param id - ID del movimiento
   * @returns Observable con los datos del movimiento
   */
  getMovimiento(id: number): Observable<Movimiento> {
    return this.http.get<Movimiento>(`${this.baseUrl}/movimientos/${id}`);
  }

  /**
   * Obtiene todos los movimientos de una cuenta específica
   * @param cuentaId - ID de la cuenta
   * @returns Observable con array de movimientos de la cuenta
   */
  getMovimientosByCuenta(cuentaId: number): Observable<Movimiento[]> {
    return this.http.get<Movimiento[]>(`${this.baseUrl}/movimientos/cuenta/${cuentaId}`);
  }

  /**
   * Crea un nuevo movimiento (depósito, retiro, etc.)
   * @param movimiento - Datos del movimiento a crear
   * @returns Observable con el movimiento creado
   * @remarks El backend actualiza automáticamente el saldo de la cuenta
   */
  createMovimiento(movimiento: Movimiento): Observable<Movimiento> {
    return this.http.post<Movimiento>(`${this.baseUrl}/movimientos`, movimiento);
  }

  /**
   * Actualiza un movimiento existente
   * @param id - ID del movimiento a actualizar
   * @param movimiento - Datos actualizados del movimiento
   * @returns Observable con el movimiento actualizado
   */
  updateMovimiento(id: number, movimiento: Movimiento): Observable<Movimiento> {
    return this.http.put<Movimiento>(`${this.baseUrl}/movimientos/${id}`, movimiento);
  }

  /**
   * Elimina un movimiento
   * @param id - ID del movimiento a eliminar
   * @returns Observable vacío
   */
  deleteMovimiento(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/movimientos/${id}`);
  }

  // ==================== REPORTES ====================
  
  /**
   * Genera un reporte de movimientos para un cliente en un rango de fechas
   * @param clienteId - ID del cliente
   * @param fechaInicio - Fecha de inicio en formato ISO (YYYY-MM-DDTHH:mm:ss.sssZ)
   * @param fechaFin - Fecha de fin en formato ISO (YYYY-MM-DDTHH:mm:ss.sssZ)
   * @returns Observable con array de movimientos del reporte
   */
  getReporte(clienteId: number, fechaInicio: string, fechaFin: string): Observable<ReporteMovimiento[]> {
    const params = new HttpParams()
      .set('clienteId', clienteId.toString())
      .set('fechaInicio', fechaInicio)
      .set('fechaFin', fechaFin);
    
    return this.http.get<ReporteMovimiento[]>(`${this.baseUrl}/reportes`, { params });
  }
}
