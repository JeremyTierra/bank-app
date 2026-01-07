import { Injectable, signal } from '@angular/core';

export interface Notification {
  message: string;
  type: 'success' | 'error' | 'info' | 'warning';
  duration?: number;
}

/**
 * Servicio centralizado para gestión de notificaciones
 * Proporciona una interfaz unificada para mostrar mensajes al usuario
 */
@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  public notification = signal<Notification | null>(null);
  private timeoutId: any;

  /**
   * Muestra una notificación con el tipo y duración especificados
   * @param message - Mensaje a mostrar
   * @param type - Tipo de notificación (success, error, info, warning)
   * @param duration - Duración en milisegundos (por defecto 5000ms)
   */
  show(message: string, type: 'success' | 'error' | 'info' | 'warning' = 'info', duration: number = 5000): void {
    // Limpiar timeout anterior si existe
    if (this.timeoutId) {
      clearTimeout(this.timeoutId);
    }

    this.notification.set({ message, type, duration });

    // Auto-ocultar después de la duración especificada
    this.timeoutId = setTimeout(() => {
      this.hide();
    }, duration);
  }

  /**
   * Muestra una notificación de éxito
   */
  success(message: string, duration?: number): void {
    this.show(message, 'success', duration);
  }

  /**
   * Muestra una notificación de error
   */
  error(message: string, duration?: number): void {
    this.show(message, 'error', duration);
  }

  /**
   * Muestra una notificación informativa
   */
  info(message: string, duration?: number): void {
    this.show(message, 'info', duration);
  }

  /**
   * Muestra una notificación de advertencia
   */
  warning(message: string, duration?: number): void {
    this.show(message, 'warning', duration);
  }

  /**
   * Oculta la notificación actual
   */
  hide(): void {
    this.notification.set(null);
    if (this.timeoutId) {
      clearTimeout(this.timeoutId);
      this.timeoutId = null;
    }
  }
}
