import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HotelBookingComponent } from './hotel-booking/hotel-booking.component';
import { HotelBookingRoutingModule } from './hotel-booking-routing.module';
import { ConfirmationComponent } from './confirmation/confirmation.component';
import { PoliciesComponent } from './policies/policies.component';
import { DialogModule } from '@angular/cdk/dialog';
import { MatDividerModule } from '@angular/material/divider';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { MyTripsComponent } from './my-trips/my-trips.component';
import { OverviewComponent } from './overview/overview.component';
import { FooterComponent } from '../shared/footer/footer.component';
import { NavbarComponent } from '../shared/navbar/navbar.component';

@NgModule({
  declarations: [
    HotelBookingComponent,
    ConfirmationComponent,
    PoliciesComponent,
    MyTripsComponent,
    OverviewComponent
   
  ],
  imports: [
    CommonModule,
    HotelBookingRoutingModule,
    DialogModule,
    MatDividerModule,
    ReactiveFormsModule,
    FormsModule,
    FooterComponent,
    NavbarComponent
  ]
})
export class HotelBookingModule { }
