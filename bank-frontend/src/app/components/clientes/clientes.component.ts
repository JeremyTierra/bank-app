import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';
import { Cliente } from '../../models/models';

@Component({
  selector: 'app-clientes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './clientes.component.html',
  styleUrl: './clientes.component.css'
})
export class ClientesComponent implements OnInit {
  clientes: Cliente[] = [];
  clienteSeleccionado: Cliente | null = null;
  modoEdicion = false;
  busqueda = '';
  mensaje = '';
  mensajeTipo: 'success' | 'error' = 'success';

  nuevoCliente: Cliente = this.inicializarCliente();

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    this.cargarClientes();
  }

  inicializarCliente(): Cliente {
    return {
      nombre: '',
      genero: '',
      edad: 0,
      identificacion: '',
      direccion: '',
      telefono: '',
      contrasena: '',
      estado: true
    };
  }

  cargarClientes() {
    this.apiService.getClientes().subscribe({
      next: (data) => {
        this.clientes = data;
      },
      error: (error) => {
        this.mostrarMensaje('Error al cargar clientes: ' + error.message, 'error');
      }
    });
  }

  get clientesFiltrados() {
    if (!this.busqueda) return this.clientes;
    
    const termino = this.busqueda.toLowerCase();
    return this.clientes.filter(c => 
      c.nombre.toLowerCase().includes(termino) ||
      c.identificacion.includes(termino) ||
      c.telefono.includes(termino)
    );
  }

  seleccionarCliente(cliente: Cliente) {
    this.clienteSeleccionado = { ...cliente };
    this.modoEdicion = true;
    this.nuevoCliente = { ...cliente };
    // Limpiar la contraseña al editar para que el usuario sepa que debe ingresarla nuevamente si quiere cambiarla
    this.nuevoCliente.contrasena = '';
  }

  guardarCliente() {
    if (!this.validarCliente()) return;

    if (this.modoEdicion && this.clienteSeleccionado?.clienteId) {
      // Al actualizar, si la contraseña está vacía, no enviarla
      const clienteActualizado = { ...this.nuevoCliente };
      
      // Si la contraseña está vacía, eliminarla completamente del objeto
      if (!clienteActualizado.contrasena || clienteActualizado.contrasena.trim() === '') {
        delete (clienteActualizado as any).contrasena;
      }
      
      this.apiService.updateCliente(this.clienteSeleccionado.clienteId, clienteActualizado).subscribe({
        next: () => {
          this.mostrarMensaje('Cliente actualizado exitosamente', 'success');
          this.cargarClientes();
          this.cancelar();
        },
        error: (error) => {
          this.mostrarMensaje('Error al actualizar: ' + error.error.message, 'error');
        }
      });
    } else {
      this.apiService.createCliente(this.nuevoCliente).subscribe({
        next: () => {
          this.mostrarMensaje('Cliente creado exitosamente', 'success');
          this.cargarClientes();
          this.cancelar();
        },
        error: (error) => {
          this.mostrarMensaje('Error al crear: ' + error.error.message, 'error');
        }
      });
    }
  }

  eliminarCliente(id: number) {
    if (confirm('¿Está seguro de eliminar este cliente?')) {
      this.apiService.deleteCliente(id).subscribe({
        next: () => {
          this.mostrarMensaje('Cliente eliminado exitosamente', 'success');
          this.cargarClientes();
        },
        error: (error) => {
          this.mostrarMensaje('Error al eliminar: ' + error.error.message, 'error');
        }
      });
    }
  }

  validarCliente(): boolean {
    if (!this.nuevoCliente.nombre || !this.nuevoCliente.identificacion || 
        !this.nuevoCliente.telefono) {
      this.mostrarMensaje('Por favor complete todos los campos obligatorios', 'error');
      return false;
    }
    
    // Contraseña solo obligatoria al crear nuevo cliente
    if (!this.modoEdicion && !this.nuevoCliente.contrasena) {
      this.mostrarMensaje('La contraseña es obligatoria para crear un cliente', 'error');
      return false;
    }
    
    return true;
  }

  cancelar() {
    this.nuevoCliente = this.inicializarCliente();
    this.clienteSeleccionado = null;
    this.modoEdicion = false;
  }

  mostrarMensaje(texto: string, tipo: 'success' | 'error') {
    this.mensaje = texto;
    this.mensajeTipo = tipo;
    setTimeout(() => this.mensaje = '', 5000);
  }
}
