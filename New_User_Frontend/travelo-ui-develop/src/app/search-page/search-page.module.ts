import { NgModule } from '@angular/core';
import { AsyncPipe, CommonModule, NgFor } from '@angular/common';

import { SearchPageRoutingModule } from './search-page-routing.module';
import { SearchPageComponent } from './search-page.component';
import { NavbarComponent } from '../shared/navbar/navbar.component';
import { PopularDestinationComponent } from '../shared/popular-destination/popular-destination.component';
import { FooterComponent } from '../shared/footer/footer.component';
import { GetInTouchComponent } from '../shared/get-in-touch/get-in-touch.component';
import {MatCheckboxModule} from '@angular/material/checkbox';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { MatChipsModule } from '@angular/material/chips';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { DatePickerModule } from '../shared/date-picker/date-picker.module';
import {MatDividerModule} from '@angular/material/divider';
import { ReservationComponent } from '../shared/reservation/reservation.component';
import { OverlayModule } from '@angular/cdk/overlay';
import {MatSelectModule} from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';

@NgModule({
  declarations: [
    
    SearchPageComponent,
  ],
  imports: [
    CommonModule,
    SearchPageRoutingModule,
    NavbarComponent,
    PopularDestinationComponent,
    FooterComponent,
    GetInTouchComponent,
    MatCheckboxModule,
    ReactiveFormsModule,
    FormsModule,
    MatChipsModule,
    MatIconModule,
    MatChipsModule,
    MatIconModule,
    AsyncPipe,
    MatFormFieldModule,
    NgFor,
    DatePickerModule,
    MatAutocompleteModule,
    MatDividerModule,
    ReservationComponent,
    OverlayModule,
     MatSelectModule,
     MatDatepickerModule
  ],
  exports:[
    
  ]
})
export class SearchPageModule { }
