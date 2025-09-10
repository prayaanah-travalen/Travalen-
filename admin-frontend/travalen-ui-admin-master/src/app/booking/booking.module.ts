import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BookingRoutingModule } from './booking-routing.module';
import { BookingComponent } from './booking/booking.component';
import { MatTableModule } from '@angular/material/table';
import { BookingDetailsComponent } from './booking-details/booking-details.component';
import { DialogModule } from '@angular/cdk/dialog';
import { MatDividerModule } from '@angular/material/divider';
import { MatMenuModule } from '@angular/material/menu';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatIconModule } from '@angular/material/icon';
import { MatNativeDateModule } from '@angular/material/core';
import { MatInputModule } from '@angular/material/input';

@NgModule({
  declarations: [
    BookingComponent,
    BookingDetailsComponent
  ],
  imports: [
    FormsModule,  
    CommonModule,
    BookingRoutingModule,
    MatTableModule,
    DialogModule,
    MatDividerModule,
    MatMenuModule,
    MatFormFieldModule,
    MatDatepickerModule,
    MatInputModule,
    MatNativeDateModule,
    MatIconModule
  ]
})
export class BookingModule { }
