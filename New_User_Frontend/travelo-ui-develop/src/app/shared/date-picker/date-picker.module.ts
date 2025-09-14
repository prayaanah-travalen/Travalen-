import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DatePickerComponent } from './date-picker.component';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatIconModule} from '@angular/material/icon';
import { MatNativeDateModule } from '@angular/material/core';
import { FormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    DatePickerComponent
  ],
  imports: [
    CommonModule,
    MatDatepickerModule,
    MatIconModule,
    MatNativeDateModule, 
    FormsModule
  ],
  exports: [DatePickerComponent]
})
export class DatePickerModule { }
