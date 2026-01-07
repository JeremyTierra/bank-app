import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  features = [
    {
      icon: 'group',
      title: 'Clientes',
      description: 'Gestiona la información de tus clientes de manera eficiente',
      link: '/clientes'
    },
    {
      icon: 'account_balance',
      title: 'Cuentas',
      description: 'Administra cuentas de ahorro y corrientes',
      link: '/cuentas'
    },
    {
      icon: 'swap_horiz',
      title: 'Movimientos',
      description: 'Realiza depósitos y retiros con control de límites',
      link: '/movimientos'
    },
    {
      icon: 'assessment',
      title: 'Reportes',
      description: 'Genera reportes detallados y exporta a PDF',
      link: '/reportes'
    }
  ];
}
