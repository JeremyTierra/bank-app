import { Injectable, signal } from '@angular/core';

/**
 * Servicio para gestionar el estado de carga global de la aplicación
 * Utiliza Angular Signals para reactividad eficiente
 */
@Injectable({
  providedIn: 'root'
})
export class LoadingService {
  private loadingCount = 0;
  public isLoading = signal<boolean>(false);

  /**
   * Incrementa el contador de peticiones y muestra el indicador de carga
   */
  show(): void {
    this.loadingCount++;
    this.isLoading.set(true);
  }

  /**
   * Decrementa el contador y oculta el indicador cuando no hay peticiones activas
   */
  hide(): void {
    this.loadingCount--;
    if (this.loadingCount <= 0) {
      this.loadingCount = 0;
      this.isLoading.set(false);
    }
  }

  /**
   * Reinicia el estado de carga (útil para testing o casos especiales)
   */
  reset(): void {
    this.loadingCount = 0;
    this.isLoading.set(false);
  }
}
