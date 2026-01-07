/**
 * Constantes de la aplicación
 * Centraliza valores reutilizables para mejor mantenibilidad
 */

/**
 * Tipos de cuenta disponibles
 */
export const TIPOS_CUENTA = ['Ahorros', 'Corriente'] as const;
export type TipoCuenta = typeof TIPOS_CUENTA[number];

/**
 * Tipos de movimiento bancario
 */
export const TIPOS_MOVIMIENTO = ['Deposito', 'Retiro', 'Credito', 'Debito'] as const;
export type TipoMovimiento = typeof TIPOS_MOVIMIENTO[number];

/**
 * Géneros disponibles para clientes
 */
export const GENEROS = ['Masculino', 'Femenino', 'Otro'] as const;
export type Genero = typeof GENEROS[number];

/**
 * Configuración de validaciones
 */
export const VALIDACIONES = {
  EDAD_MINIMA: 18,
  EDAD_MAXIMA: 120,
  TELEFONO_MIN_LENGTH: 7,
  TELEFONO_MAX_LENGTH: 15,
  IDENTIFICACION_MIN_LENGTH: 5,
  IDENTIFICACION_MAX_LENGTH: 20,
  PASSWORD_MIN_LENGTH: 4,
  SALDO_MINIMO: 0,
  MONTO_MINIMO_TRANSACCION: 1
} as const;

/**
 * Mensajes de la aplicación
 */
export const MENSAJES = {
  ERROR: {
    CAMPOS_REQUERIDOS: 'Por favor complete todos los campos obligatorios',
    EDAD_INVALIDA: `La edad debe estar entre ${VALIDACIONES.EDAD_MINIMA} y ${VALIDACIONES.EDAD_MAXIMA} años`,
    PASSWORD_REQUERIDA: 'La contraseña es obligatoria para crear un cliente',
    SALDO_INSUFICIENTE: 'Saldo insuficiente para realizar la operación',
    MONTO_INVALIDO: 'El monto debe ser mayor a cero',
    CONFIRMACION: '¿Está seguro de realizar esta operación?'
  },
  EXITO: {
    CREADO: 'Registro creado exitosamente',
    ACTUALIZADO: 'Registro actualizado exitosamente',
    ELIMINADO: 'Registro eliminado exitosamente',
    GUARDADO: 'Cambios guardados exitosamente'
  }
} as const;

/**
 * Configuración de la aplicación
 */
export const APP_CONFIG = {
  DURACION_MENSAJE: 5000,  // milisegundos
  DEBOUNCE_SEARCH: 300,    // milisegundos
  ITEMS_POR_PAGINA: 10,
  MAX_FILE_SIZE: 5242880,  // 5MB en bytes
} as const;
