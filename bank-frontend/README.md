# Bank App - Frontend

Sistema bancario completo desarrollado con Angular 19 y buenas prácticas de nivel senior.

## 🚀 Características Principales

### Arquitectura
- **Angular 19** con Standalone Components
- **TypeScript 5.7** con configuración strict
- **Signals** para gestión de estado reactivo
- **Dependency Injection** mediante constructor injection
- **HTTP Interceptors** para manejo centralizado de errores y loading
- **Servicios reutilizables** con documentación JSDoc completa

### Funcionalidades
- ✅ **Gestión de Clientes**: CRUD completo con validaciones
- ✅ **Gestión de Cuentas**: Vinculación con clientes, múltiples tipos de cuenta
- ✅ **Movimientos Bancarios**: Depósitos, retiros, créditos y débitos
- ✅ **Reportes**: Generación de reportes con exportación a PDF y JSON
- ✅ **Búsqueda y Filtrado**: Búsqueda en tiempo real
- ✅ **Loading Spinner**: Indicador de carga global automático
- ✅ **Manejo de Errores**: Sistema centralizado de notificaciones

### Buenas Prácticas Implementadas

#### 1. Arquitectura y Organización
```
src/app/
├── components/       # Componentes de la aplicación
│   ├── clientes/
│   ├── cuentas/
│   ├── movimientos/
│   ├── reportes/
│   ├── home/
│   └── shared/      # Componentes reutilizables
├── services/        # Servicios para lógica de negocio
├── interceptors/    # HTTP Interceptors
├── models/          # Interfaces TypeScript
├── constants/       # Constantes y configuración
└── environments/    # Configuración por ambiente
```

#### 2. TypeScript Strict Mode
- `strict: true` - Máxima seguridad de tipos
- `noImplicitReturns: true` - Todas las funciones deben retornar
- `noFallthroughCasesInSwitch: true` - Switch statements seguros
- `strictTemplates: true` - Templates tipados

#### 3. Servicios con JSDoc
Todos los servicios incluyen documentación completa:
```typescript
/**
 * Obtiene la lista completa de clientes
 * @returns Observable con array de clientes
 */
getClientes(): Observable<Cliente[]>
```

#### 4. Interceptors HTTP
- **Error Interceptor**: Manejo centralizado de errores HTTP
- **Loading Interceptor**: Indicador de carga automático

#### 5. Signals para Reactividad
```typescript
public isLoading = signal<boolean>(false);
```

#### 6. Constantes Tipadas
```typescript
export const TIPOS_CUENTA = ['Ahorros', 'Corriente'] as const;
export type TipoCuenta = typeof TIPOS_CUENTA[number];
```

#### 7. Inmutabilidad
- Uso de `readonly` en propiedades que no cambian
- Constructor injection con `private readonly`

#### 8. Testing
- **26 tests unitarios** con Jest
- Testing de componentes y servicios
- Mocking de dependencias
- 100% de tests passing

## 📋 Requisitos

- Node.js 20+ 
- npm 10+
- Angular CLI 19+

## 🛠️ Instalación y Desarrollo

### Instalación de Dependencias
```bash
npm install --legacy-peer-deps
```

### Servidor de Desarrollo
```bash
npm start
# Abre http://localhost:4200
```

### Build de Producción
```bash
npm run build
# Archivos en dist/bank-frontend/browser
```

### Tests
```bash
# Ejecutar todos los tests
npm test

# Tests en modo watch
npm run test:watch

# Coverage
npm run test:coverage
```

## 🐳 Docker

### Build de Imagen
```bash
docker build -t bank-frontend .
```

### Ejecutar Contenedor
```bash
docker run -p 80:80 bank-frontend
```

### Docker Compose (Stack Completo)
```bash
cd ../bank-app
docker-compose up -d
# Frontend disponible en http://localhost
```

## 📝 Estructura de Componentes

### ClientesComponent
- CRUD completo de clientes
- Búsqueda en tiempo real
- Validaciones de formulario
- Contraseña opcional en actualización

### CuentasComponent
- Gestión de cuentas bancarias
- Vinculación con clientes
- Tipos de cuenta (Ahorros/Corriente)
- Validación de saldos

### MovimientosComponent
- Registro de transacciones
- Cálculo automático de signos (débito/crédito)
- Validación de saldos
- Histórico de movimientos

### ReportesComponent
- Filtrado por cliente y fechas
- Validaciones de rango de fechas
- Exportación a PDF (jsPDF)
- Exportación a JSON
- Cálculo de totales

## 🎨 Estilos y UI

- **Diseño Responsivo**: Mobile-first approach
- **Material Icons**: Iconografía consistente
- **Sidebar Navigation**: Navegación intuitiva
- **Feedback Visual**: Mensajes de éxito/error
- **Loading States**: Indicadores de carga

## 🔒 Seguridad

- **No exposición de contraseñas**: Las contraseñas nunca se muestran en el frontend
- **Validaciones**: Tanto en frontend como backend
- **CORS**: Configurado correctamente con nginx
- **TypeScript Strict**: Prevención de errores en tiempo de compilación

## 📊 Performance

- **Lazy Loading**: Componentes standalone
- **OnPush Change Detection**: Optimización de rendimiento
- **Debounce en búsquedas**: Reducción de peticiones HTTP
- **HTTP Interceptors**: Gestión eficiente de peticiones

## 🧪 Testing Strategy

```typescript
// Ejemplo de test
it('should create a new cliente successfully', () => {
  apiService.createCliente.mockReturnValue(of(newCliente));
  component.guardarCliente();
  expect(apiService.createCliente).toHaveBeenCalledWith(newCliente);
});
```

## 📚 Recursos y Referencias

- [Angular 19 Documentation](https://angular.dev)
- [TypeScript Handbook](https://www.typescriptlang.org/docs/)
- [RxJS Documentation](https://rxjs.dev/)
- [Jest Testing](https://jestjs.io/)

## 🤝 Convenciones de Código

### Naming Conventions
- **Componentes**: PascalCase (ClientesComponent)
- **Servicios**: PascalCase + Service (ApiService)
- **Interfaces**: PascalCase (Cliente, Cuenta)
- **Constantes**: UPPER_SNAKE_CASE (TIPOS_CUENTA)
- **Variables**: camelCase (clienteSeleccionado)

### Estructura de Archivos
```
nombre.component.ts
nombre.component.html
nombre.component.css
nombre.component.spec.ts
```

## 🚀 Próximas Mejoras

- [ ] Implementar paginación en listados
- [ ] Agregar filtros avanzados
- [ ] Implementar guards para rutas
- [ ] Agregar animaciones con Angular Animations
- [ ] Implementar service worker para PWA
- [ ] Agregar internacionalización (i18n)

## 📄 Licencia

Sistema desarrollado como prueba técnica para posición Senior.

---

**Versión**: 1.0.0  
**Angular**: 19.2.0  
**TypeScript**: 5.7.2  
**Autor**: Devsu Technical Test
