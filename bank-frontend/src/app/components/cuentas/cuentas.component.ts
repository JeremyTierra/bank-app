import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';
import { Cuenta, Cliente } from '../../models/models';

@Component({
  selector: 'app-cuentas',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './cuentas.component.html',
  styleUrl: './cuentas.component.css'
})
export class CuentasComponent implements OnInit {
  cuentas: Cuenta[] = [];
  clientes: Cliente[] = [];
  cuentaSeleccionada: Cuenta | null = null;
  modoEdicion = false;
  busqueda = '';
  mensaje = '';
  mensajeTipo: 'success' | 'error' = 'success';

  nuevaCuenta: Cuenta = this.inicializarCuenta();

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    this.cargarCuentas();
    this.cargarClientes();
  }

  inicializarCuenta(): Cuenta {
    return {
      numeroCuenta: '',
      tipoCuenta: '',
      saldoInicial: 0,
      estado: true,
      clienteId: 0
    };
  }

  cargarCuentas() {
    this.apiService.getCuentas().subscribe({
      next: (data) => this.cuentas = data,
      error: (error) => this.mostrarMensaje('Error al cargar cuentas: ' + error.message, 'error')
    });
  }

  cargarClientes() {
    this.apiService.getClientes().subscribe({
      next: (data) => this.clientes = data.filter(c => c.estado),
      error: (error) => this.mostrarMensaje('Error al cargar clientes', 'error')
    });
  }

  get cuentasFiltradas() {
    if (!this.busqueda) return this.cuentas;
    const termino = this.busqueda.toLowerCase();
    return this.cuentas.filter(c => 
      c.numeroCuenta.includes(termino) ||
      c.clienteNombre?.toLowerCase().includes(termino) || 
      c.tipoCuenta.toLowerCase().includes(termino)
    );
  }

  seleccionarCuenta(cuenta: Cuenta) {
    this.cuentaSeleccionada = { ...cuenta };
    this.modoEdicion = true;
    this.nuevaCuenta = { ...cuenta };
  }

  guardarCuenta() {
    if (!this.nuevaCuenta.numeroCuenta || !this.nuevaCuenta.tipoCuenta || !this.nuevaCuenta.clienteId) {
      this.mostrarMensaje('Complete todos los campos obligatorios', 'error');
      return;
    }

    if (this.modoEdicion && this.cuentaSeleccionada?.id) {
      this.apiService.updateCuenta(this.cuentaSeleccionada.id, this.nuevaCuenta).subscribe({
        next: () => {
          this.mostrarMensaje('Cuenta actualizada exitosamente', 'success');
          this.cargarCuentas();
          this.cancelar();
        },
        error: (error) => this.mostrarMensaje('Error: ' + error.error.message, 'error')
      });
    } else {
      this.apiService.createCuenta(this.nuevaCuenta).subscribe({
        next: () => {
          this.mostrarMensaje('Cuenta creada exitosamente', 'success');
          this.cargarCuentas();
          this.cancelar();
        },
        error: (error) => this.mostrarMensaje('Error: ' + error.error.message, 'error')
      });
    }
  }

  eliminarCuenta(id: number) {
    if (confirm('¿Eliminar esta cuenta?')) {
      this.apiService.deleteCuenta(id).subscribe({
        next: () => {
          this.mostrarMensaje('Cuenta eliminada exitosamente', 'success');
          this.cargarCuentas();
        },
        error: (error) => this.mostrarMensaje('Error: ' + error.error.message, 'error')
      });
    }
  }

  cancelar() {
    this.nuevaCuenta = this.inicializarCuenta();
    this.cuentaSeleccionada = null;
    this.modoEdicion = false;
  }

  mostrarMensaje(texto: string, tipo: 'success' | 'error') {
    this.mensaje = texto;
    this.mensajeTipo = tipo;
    setTimeout(() => this.mensaje = '', 5000);
  }
}
