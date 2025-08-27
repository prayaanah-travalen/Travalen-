import { formatDate } from '@angular/common';
import { ChangeDetectionStrategy, Component, OnInit, ViewEncapsulation } from '@angular/core';
import * as moment from 'moment';
import { CalendarModel } from 'src/app/data-access/model/calendar.model';
import { BookingService } from 'src/app/data-access/services/booking.service';

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.scss'],
  encapsulation: ViewEncapsulation.None,
  changeDetection: ChangeDetectionStrategy.Default
})
export class CalendarComponent implements OnInit {

  // lastDay = moment().endOf('month');

  // days = Array.from(Array(this.lastDay.daysInMonth()).keys()).map(d => {

  //   return moment()
  //     .day(d + 1)
  //     .format('ddd');
  // });
  days: any[] =[];

  constructor(private bookingService: BookingService) { }

  items: any[] = [];
  currentDate = new Date();
  currentMonth = "";
  stopDate = new Date();
  selectedDate = null;
  currentYear : number = 0;

  tiles: Tile[] = [];

  data: CalendarModel[] = [];
  firstDay: any ;
  lastDay: any;

  ngOnInit() {
    // Creating an array with specified date range
    var date = new Date();
    this.firstDay = new Date(date.getFullYear(), date.getMonth(), 1);
    this.lastDay = new Date(date.getFullYear(), date.getMonth() + 1, 0);
    this.items = this.getDates(
      this.firstDay,
      this.lastDay
    );

    let req = {
      startDate: formatDate(this.firstDay, 'dd-MM-YYYY', 'en'),
      endDate: formatDate(this.lastDay, 'dd-MM-YYYY', 'en')
    }
    this.bookingService.getBookingsForCalendar(req).subscribe(resp=>{
      this.data = resp;

    });
    this.tiles = [  
      {text: 'Room Status', cols: 1, rows: 1, color: 'lightblue'},
      {text: 'Rooms to Sell', cols: 1, rows: 2, color: 'lightblue'},
      {text: 'Net Booked', cols: 1, rows: 3, color: 'lightblue'},
      {text: 'Standard Rate', cols: 1, rows: 4, color: 'lightblue'},
      {text: 'Non Refundable Rate', cols: 1, rows: 5, color: 'lightblue'},
      {text: 'Wekly Rate', cols: 1, rows: 6, color: 'lightgreen'},
      {text: 'Three', cols: 1, rows: 1, color: 'lightpink'},
      {text: 'Four', cols: 1, rows: 1, color: '#DDBDF1'},
    ]
   }

  // Common method to create an array of dates
  getDates(startDate: any, stopDate: any) {
    let dateArray = [];
    let currentDate = moment(startDate);
    stopDate = moment(stopDate);
    while (currentDate <= stopDate) {
      dateArray.push(moment(currentDate).format("YYYY-MM-DD"));
      currentDate = moment(currentDate).add(1, "days");
    }
    return dateArray;
  }

  // Get the selected Date
  select(item: any) {
    this.selectedDate = item;
    let req = {
      startDate: formatDate(item, 'dd-MM-YYYY', 'en'),
      endDate: formatDate(item, 'dd-MM-YYYY', 'en')
    }
    this.bookingService.getBookingsForCalendar(req).subscribe(resp=>{
      this.data = resp;

    });
    
  }

  // Method for changing Month
  changeMonth(e: any) {
    this.currentDate = this.items[e];
    this.currentMonth = new Date(this.currentDate).toLocaleString("default",  {  month: "short" });
    this.currentYear = new Date(this.currentDate).getFullYear();
  }

  // Method to get the current weekday of the date showon
  returnWeekDay(item: any) {
    return new Date(item).toLocaleDateString("default", { weekday: "short" });
  }

  dateChange(){
    this.items = this.getDates(
      this.firstDay,
      this.lastDay
    );

    this.currentMonth = new Date(this.firstDay).toLocaleString("default",  {  month: "short" });
    this.currentYear = new Date(this.firstDay).getFullYear();
  }

  
}

export interface Tile {
  color: string;
  cols: number;
  rows: number;
  text: string;
}
