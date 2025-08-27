import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BookingRoutingModule } from './booking-routing.module';
import { BookingComponent } from './booking/booking.component';
import { MatTableModule } from '@angular/material/table';
import { BookingDetailsComponent } from './booking-details/booking-details.component';
import { DialogModule } from '@angular/cdk/dialog';
import { MatDividerModule } from '@angular/material/divider';
import { MatMenuModule } from '@angular/material/menu';

@NgModule({
  declarations: [
    BookingComponent,
    BookingDetailsComponent
  ],
  imports: [
    CommonModule,
    BookingRoutingModule,
    MatTableModule,
    DialogModule,
    MatDividerModule,
    MatMenuModule
  ]
})
export class BookingModule { }
