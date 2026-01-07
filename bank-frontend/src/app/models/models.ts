/**
 * Modelos de datos de la aplicación bancaria
 * Define las interfaces TypeScript que representan las entidades del dominio
 */

/**
 * Representa un cliente del banco
 */
export interface Cliente {
  /** ID único del cliente (generado por el backend) */
  clienteId?: number;
  
  /** Nombre completo del cliente */
  nombre: string;
  
  /** Género del cliente (Masculino, Femenino, Otro) */
  genero: string;
  
  /** Edad del cliente (debe ser mayor de 18 años) */
  edad: number;
  
  /** Número de identificación único del cliente */
  identificacion: string;
  
  /** Dirección de residencia */
  direccion: string;
  
  /** Número de teléfono de contacto */
  telefono: string;
  
  /** Contraseña para acceso (encriptada en el backend) */
  contrasena: string;
  
  /** Estado del cliente (true=activo, false=inactivo) */
  estado: boolean;
}

/**
 * Representa una cuenta bancaria
 */
export interface Cuenta {
  /** ID único de la cuenta (generado por el backend) */
  id?: number;
  
  /** Número de cuenta único */
  numeroCuenta: string;
  
  /** Tipo de cuenta (Ahorros, Corriente) */
  tipoCuenta: string;
  
  /** Saldo inicial al crear la cuenta */
  saldoInicial: number;
  
  /** Estado de la cuenta (true=activa, false=inactiva) */
  estado: boolean;
  
  /** ID del cliente propietario de la cuenta */
  clienteId: number;
  
  /** Nombre del cliente (solo para visualización, no se envía al backend) */
  clienteNombre?: string;
}

/**
 * Representa un movimiento o transacción bancaria
 */
export interface Movimiento {
  /** ID único del movimiento (generado por el backend) */
  id?: number;
  
  /** Fecha y hora del movimiento (generada por el backend) */
  fecha?: string;
  
  /** Tipo de movimiento (Deposito, Retiro, Credito, Debito) */
  tipoMovimiento: string;
  
  /** Valor del movimiento (positivo para créditos, negativo para débitos) */
  valor: number;
  
  /** Saldo resultante después del movimiento (calculado por el backend) */
  saldo?: number;
  
  /** Número de cuenta asociada al movimiento */
  numeroCuenta: string;
}

/**
 * Representa un registro en el reporte de movimientos
 * Incluye información consolidada de cliente, cuenta y movimiento
 */
export interface ReporteMovimiento {
  /** Fecha del movimiento en formato ISO */
  fecha: string;
  
  /** Nombre del cliente */
  cliente: string;
  
  /** Número de cuenta */
  numeroCuenta: string;
  
  /** Tipo de cuenta (Ahorros, Corriente) */
  tipo: string;
  
  /** Saldo antes del movimiento */
  saldoInicial: number;
  
  /** Estado de la cuenta en el momento del movimiento */
  estado: boolean;
  
  /** Valor del movimiento (positivo o negativo) */
  movimiento: number;
  
  /** Saldo después del movimiento */
  saldoDisponible: number;
}
