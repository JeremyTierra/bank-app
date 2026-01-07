import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';
import { Movimiento, Cuenta } from '../../models/models';

@Component({
  selector: 'app-movimientos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './movimientos.component.html',
  styleUrl: './movimientos.component.css'
})
export class MovimientosComponent implements OnInit {
  movimientos: Movimiento[] = [];
  cuentas: Cuenta[] = [];
  busqueda = '';
  mensaje = '';
  mensajeTipo: 'success' | 'error' = 'success';

  nuevoMovimiento: Movimiento = {
    tipoMovimiento: '',
    valor: 0,
    numeroCuenta: ''
  };

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    this.cargarMovimientos();
    this.cargarCuentas();
  }

  cargarMovimientos() {
    this.apiService.getMovimientos().subscribe({
      next: (data) => this.movimientos = data,
      error: (error) => this.mostrarMensaje('Error al cargar movimientos', 'error')
    });
  }

  cargarCuentas() {
    this.apiService.getCuentas().subscribe({
      next: (data) => this.cuentas = data.filter(c => c.estado),
      error: (error) => this.mostrarMensaje('Error al cargar cuentas', 'error')
    });
  }

  get movimientosFiltrados() {
    if (!this.busqueda) return this.movimientos;
    const termino = this.busqueda.toLowerCase();
    return this.movimientos.filter(m => 
      m.numeroCuenta.includes(termino) ||
      m.tipoMovimiento.toLowerCase().includes(termino)
    );
  }

  realizarMovimiento() {
    if (!this.nuevoMovimiento.numeroCuenta || !this.nuevoMovimiento.tipoMovimiento) {
      this.mostrarMensaje('Complete todos los campos', 'error');
      return;
    }

    if (this.nuevoMovimiento.valor === 0) {
      this.mostrarMensaje('El valor no puede ser cero', 'error');
      return;
    }

    this.apiService.createMovimiento(this.nuevoMovimiento).subscribe({
      next: () => {
        this.mostrarMensaje('Movimiento realizado exitosamente', 'success');
        this.cargarMovimientos();
        this.limpiarFormulario();
      },
      error: (error) => {
        this.mostrarMensaje('Error: ' + error.error.message, 'error');
      }
    });
  }

  onTipoMovimientoChange() {
    if (this.nuevoMovimiento.tipoMovimiento === 'Retiro' || 
        this.nuevoMovimiento.tipoMovimiento === 'Debito') {
      this.nuevoMovimiento.valor = Math.abs(this.nuevoMovimiento.valor) * -1;
    } else if (this.nuevoMovimiento.tipoMovimiento === 'Deposito' || 
               this.nuevoMovimiento.tipoMovimiento === 'Credito') {
      this.nuevoMovimiento.valor = Math.abs(this.nuevoMovimiento.valor);
    }
  }

  eliminarMovimiento(id: number) {
    if (confirm('¿Eliminar este movimiento?')) {
      this.apiService.deleteMovimiento(id).subscribe({
        next: () => {
          this.mostrarMensaje('Movimiento eliminado', 'success');
          this.cargarMovimientos();
        },
        error: (error) => this.mostrarMensaje('Error: ' + error.error.message, 'error')
      });
    }
  }

  limpiarFormulario() {
    this.nuevoMovimiento = {
      tipoMovimiento: '',
      valor: 0,
      numeroCuenta: ''
    };
  }

  mostrarMensaje(texto: string, tipo: 'success' | 'error') {
    this.mensaje = texto;
    this.mensajeTipo = tipo;
    setTimeout(() => this.mensaje = '', 5000);
  }
}
