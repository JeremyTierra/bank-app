import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { finalize } from 'rxjs';
import { LoadingService } from '../services/loading.service';

/**
 * Interceptor para controlar el estado de carga global
 * Activa/desactiva el indicador de carga automáticamente en las peticiones HTTP
 */
export const loadingInterceptor: HttpInterceptorFn = (req, next) => {
  const loadingService = inject(LoadingService);
  
  loadingService.show();

  return next(req).pipe(
    finalize(() => loadingService.hide())
  );
};
