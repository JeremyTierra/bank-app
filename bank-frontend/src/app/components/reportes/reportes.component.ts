import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';
import { Cliente, ReporteMovimiento } from '../../models/models';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';

@Component({
  selector: 'app-reportes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reportes.component.html',
  styleUrl: './reportes.component.css'
})
export class ReportesComponent implements OnInit {
  clientes: Cliente[] = [];
  reporteData: ReporteMovimiento[] = [];
  clienteSeleccionado: number | null = null;
  fechaInicio: string = '';
  fechaFin: string = '';
  mensaje = '';
  mensajeTipo: 'success' | 'error' = 'success';
  cargando = false;

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    this.cargarClientes();
    this.setFechasPorDefecto();
  }

  setFechasPorDefecto() {
    const hoy = new Date();
    const hace30Dias = new Date();
    hace30Dias.setDate(hoy.getDate() - 30);
    
    this.fechaFin = this.formatearFecha(hoy);
    this.fechaInicio = this.formatearFecha(hace30Dias);
  }

  formatearFecha(fecha: Date): string {
    return fecha.toISOString().split('T')[0];
  }

  cargarClientes() {
    this.apiService.getClientes().subscribe({
      next: (data) => this.clientes = data.filter(c => c.estado),
      error: (error) => this.mostrarMensaje('Error al cargar clientes', 'error')
    });
  }

  generarReporte() {
    if (!this.clienteSeleccionado || !this.fechaInicio || !this.fechaFin) {
      this.mostrarMensaje('Por favor complete todos los campos', 'error');
      return;
    }

    // Validar que la fecha de inicio no sea posterior a la fecha de fin
    if (new Date(this.fechaInicio) > new Date(this.fechaFin)) {
      this.mostrarMensaje('La fecha de inicio no puede ser posterior a la fecha de fin', 'error');
      return;
    }

    // Validar que las fechas no sean futuras
    const hoy = new Date();
    hoy.setHours(0, 0, 0, 0);
    if (new Date(this.fechaInicio) > hoy || new Date(this.fechaFin) > hoy) {
      this.mostrarMensaje('Las fechas no pueden ser futuras', 'error');
      return;
    }

    this.cargando = true;
    const fechaInicioISO = new Date(this.fechaInicio + 'T00:00:00').toISOString();
    const fechaFinISO = new Date(this.fechaFin + 'T23:59:59').toISOString();

    this.apiService.getReporte(this.clienteSeleccionado, fechaInicioISO, fechaFinISO).subscribe({
      next: (data) => {
        this.reporteData = data;
        this.cargando = false;
        if (data.length === 0) {
          this.mostrarMensaje('No se encontraron movimientos en el rango de fechas especificado', 'error');
        } else {
          this.mostrarMensaje('Reporte generado exitosamente', 'success');
        }
      },
      error: (error) => {
        this.cargando = false;
        this.mostrarMensaje('Error al generar reporte: ' + error.error.message, 'error');
      }
    });
  }

  get totalCreditos(): number {
    return this.reporteData
      .filter(r => r.movimiento > 0)
      .reduce((sum, r) => sum + r.movimiento, 0);
  }

  get totalDebitos(): number {
    return this.reporteData
      .filter(r => r.movimiento < 0)
      .reduce((sum, r) => sum + Math.abs(r.movimiento), 0);
  }

  exportarPDF() {
    if (this.reporteData.length === 0) {
      this.mostrarMensaje('No hay datos para exportar', 'error');
      return;
    }

    const doc = new jsPDF();
    const cliente = this.clientes.find(c => c.clienteId === Number(this.clienteSeleccionado));
    
    // Título
    doc.setFontSize(18);
    doc.text('Reporte de Estado de Cuenta', 14, 20);
    
    // Información del cliente
    doc.setFontSize(12);
    doc.text(`Cliente: ${cliente?.nombre || 'N/A'}`, 14, 30);
    doc.text(`Período: ${this.fechaInicio} al ${this.fechaFin}`, 14, 37);
    doc.text(`Fecha de generación: ${new Date().toLocaleString()}`, 14, 44);

    // Tabla de movimientos
    const tableData = this.reporteData.map(r => [
      new Date(r.fecha).toLocaleDateString(),
      r.numeroCuenta,
      r.tipo,
      `$${r.saldoInicial.toFixed(2)}`,
      r.estado ? 'Activo' : 'Inactivo',
      `$${r.movimiento.toFixed(2)}`,
      `$${r.saldoDisponible.toFixed(2)}`
    ]);

    autoTable(doc, {
      startY: 52,
      head: [['Fecha', 'Cuenta', 'Tipo', 'Saldo Inicial', 'Estado', 'Movimiento', 'Saldo Disponible']],
      body: tableData,
      theme: 'grid',
      headStyles: { fillColor: [102, 126, 234] },
      styles: { fontSize: 8 }
    });

    // Resumen
    const finalY = (doc as any).lastAutoTable.finalY + 10;
    doc.setFontSize(12);
    doc.text(`Total Créditos: $${this.totalCreditos.toFixed(2)}`, 14, finalY);
    doc.text(`Total Débitos: $${this.totalDebitos.toFixed(2)}`, 14, finalY + 7);
    doc.text(`Balance: $${(this.totalCreditos - this.totalDebitos).toFixed(2)}`, 14, finalY + 14);

    doc.save(`reporte-${cliente?.nombre}-${new Date().getTime()}.pdf`);
    this.mostrarMensaje('PDF descargado exitosamente', 'success');
  }

  exportarJSON() {
    if (this.reporteData.length === 0) {
      this.mostrarMensaje('No hay datos para exportar', 'error');
      return;
    }

    const dataStr = JSON.stringify(this.reporteData, null, 2);
    const dataBlob = new Blob([dataStr], { type: 'application/json' });
    const url = window.URL.createObjectURL(dataBlob);
    const link = document.createElement('a');
    link.href = url;
    link.download = `reporte-${new Date().getTime()}.json`;
    link.click();
    window.URL.revokeObjectURL(url);
    this.mostrarMensaje('JSON descargado exitosamente', 'success');
  }

  mostrarMensaje(texto: string, tipo: 'success' | 'error') {
    this.mensaje = texto;
    this.mensajeTipo = tipo;
    setTimeout(() => this.mensaje = '', 5000);
  }
}
