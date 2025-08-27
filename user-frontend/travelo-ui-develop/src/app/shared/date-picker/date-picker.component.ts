
import { formatDate } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { DateAdapter } from '@angular/material/core';
import { MatDatepickerInputEvent } from '@angular/material/datepicker';


@Component({
  selector: 'app-date-picker',
  templateUrl: './date-picker.component.html',
  styleUrls: ['./date-picker.component.scss'],
})
export class DatePickerComponent {
  @Input() displayLabel: string = 'Date' ;

  _dateInput: string | undefined = '';
  @Input() set dateInput(val: string | undefined) {
    this._dateInput = val
    if (val) {
      const [day, month, year] = val.split('/');
      this.date = new Date(+year, +month - 1, +day);
    }
  }

  @Output() dateEmitter = new EventEmitter<string>();
  @Input() tillDateDisable?: any;
  @Input() maxDateEnable?:any;
  
  date: Date = new Date()

  constructor(
    //public datepipe: DatePipe,
    private dateAdapter: DateAdapter<Date>) {
    this.dateAdapter.setLocale('en-GB');
  }

  dateChange(event: MatDatepickerInputEvent<any>) {
    const date = formatDate(event.value, 'dd-MM-YYYY', 'en');
    if (date !== undefined && date !== null) {
      this.dateEmitter.emit(date);
    }
  }

}

