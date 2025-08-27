import { Dialog } from '@angular/cdk/dialog';
import { formatDate } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { BookingDetailsComponent } from 'src/app/booking/booking-details/booking-details.component';
import { Booking } from 'src/app/data-access/model/booking.model';



@Component({
  selector: 'app-user-dashboard',
  templateUrl: './user-dashboard.component.html',
  styleUrls: ['./user-dashboard.component.scss']
})
export class UserDashboardComponent implements OnInit {
  todaysCheckIn: number = 0;

  todaysCheckOut: number = 0;

  @Input() _bookings: Booking[] = [];

  @Input() set bookings(val: Booking[] | []) {
    this._bookings = val
    if (val) {
      let date = formatDate(new Date(), 'YYYY-MM-dd', 'en');
      this.todaysCheckIn = this._bookings.filter(bk=>{
          let len  = bk.bookingDetails.filter(htl=>this.dateformat(htl.checkIn) === date).length
          if(len > 0) return true;
          return false
      }).length;
      
      this.todaysCheckOut = this._bookings.filter(bk=>{
          let len  = bk.bookingDetails.filter(htl=>this.dateformat(htl.checkOut) === date).length
          if(len > 0) return true;
          return false
      }).length;
    }
  }

  displayedColumns: string[] = ['name','location', 'bookingDate', 'phone', 'noOfGuests','roomType'];


  constructor(private dialog: Dialog) { }

  ngOnInit(): void {
  }
   

  dateformat(dte: any) {
    // let date = new Date();
      // const day = date.toLocaleString('default', { day: '2-digit' });
      // const month = date.toLocaleString('default', { month: 'short' });
      // const year = date.toLocaleString('default', { year: 'numeric' });
      // return day + '-' + month + '-' + year;
      if(dte !== null) {
        let date = new Date(dte);
        return formatDate(date, 'YYYY-MM-dd', 'en');
      }
      return '';
  }

  bookingDetails(booking: Booking) {
    this.dialog.open(BookingDetailsComponent,{
      width:'550px',
      data:{booking: booking}
  })
  }
}
