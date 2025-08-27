import { NgModule } from '@angular/core';
import { AsyncPipe, CommonModule, NgFor } from '@angular/common';

import { HotelDetailsRoutingModule } from './hotel-details-routing.module';
import { HotelDetailsComponent } from './hotel-details.component';
import { NavbarComponent } from '../shared/navbar/navbar.component';
import { PopularDestinationComponent } from '../shared/popular-destination/popular-destination.component';
import { FooterComponent } from '../shared/footer/footer.component';
import { GetInTouchComponent } from '../shared/get-in-touch/get-in-touch.component';
import { MatChipsModule } from '@angular/material/chips';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatInputModule} from '@angular/material/input';
import {MatNativeDateModule} from '@angular/material/core';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { ReservationComponent } from '../shared/reservation/reservation.component';
import { GoogleMapComponent } from '../shared/google-map/google-map.component';


@NgModule({
  declarations: [
    HotelDetailsComponent,
  ],
  imports: [
    CommonModule,
    NavbarComponent,
    PopularDestinationComponent,
    FooterComponent,
    GetInTouchComponent,
    HotelDetailsRoutingModule,
    MatChipsModule,
    MatIconModule,
    AsyncPipe,
    MatFormFieldModule,
    NgFor,
    MatFormFieldModule,
    MatInputModule,
    MatNativeDateModule,
    MatDatepickerModule,
    ReactiveFormsModule,
    FormsModule,
    ReservationComponent,
    GoogleMapComponent
  ]
})
export class HotelDetailsModule { }
