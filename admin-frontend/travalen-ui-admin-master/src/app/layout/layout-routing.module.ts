import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LayoutComponent } from './layout/layout.component';
import { AppAuthGuard } from '../app.auth.guard';

const routes: Routes = [

  {
    path: '', component: LayoutComponent,
    children: [
      // { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
      { 
        path: 'hotel-profiles', loadChildren: () => import('../hotel/hotel.module').then(m => m.HotelEditModule) 
      },
      { 
        path: 'dashboard', loadChildren: () => import('../dashboard/dashboard.module').then(m => m.DashboardModule) 
      },
      { 
        path: 'booking', loadChildren: () => import('../booking/booking.module').then(m => m.BookingModule) 
      },
      { 
        path: 'calendar', loadChildren: () => import('../calendar/calendar.module').then(m => m.CalendarModule) 
      },
      { 
        path: 'Destination', loadChildren: () => import('../destination/destination.module').then(m => m.DestinationModule) 
      },
      { 
        path: 'priceSlab', loadChildren: () => import('../price-slab/price-slab.module').then(m => m.PriceSlabModule) 
      },
      { 
        path: 'userManagement', loadChildren: () => import('../user-management/user-management.module').then(m => m.UserManagementModule) 
      }
    ],
    canActivate:[AppAuthGuard]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class LayoutRoutingModule { }
