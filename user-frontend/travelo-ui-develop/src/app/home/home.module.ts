import { NgModule } from '@angular/core';
import { AsyncPipe, CommonModule, NgFor } from '@angular/common';


import { HomeRoutingModule } from './home-routing.module';
import { SearchPageModule } from '../search-page/search-page.module';
import { NavbarComponent } from '../shared/navbar/navbar.component';
import { HomeComponent } from './home.component';
import { PopularDestinationComponent } from '../shared/popular-destination/popular-destination.component';
import { FooterComponent } from '../shared/footer/footer.component';
import { GetInTouchComponent } from '../shared/get-in-touch/get-in-touch.component';
import { DatePickerModule } from '../shared/date-picker/date-picker.module';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatFormFieldModule } from '@angular/material/form-field';
import { OverlayModule } from '@angular/cdk/overlay';


@NgModule({
  declarations: [
    HomeComponent,
  ],
  imports: [
    CommonModule,
    HomeRoutingModule,
    SearchPageModule,
    NavbarComponent,
    PopularDestinationComponent,
    FooterComponent,
    GetInTouchComponent,
    DatePickerModule,
    MatAutocompleteModule,
    // FormControl,
    ReactiveFormsModule,
    FormsModule,
    MatChipsModule,
    MatIconModule,
    MatChipsModule,
    MatIconModule,
    AsyncPipe,
    MatFormFieldModule,
    NgFor,
    OverlayModule
  ]
})
export class HomeModule { }
