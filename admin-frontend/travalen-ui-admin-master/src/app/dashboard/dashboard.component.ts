import { Component, OnInit } from '@angular/core';
import { BookingService } from '../data-access/services/booking.service';
import { Booking } from '../data-access/model/booking.model';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  user: string = 'normalUser';

  bookings: Booking[] = [];
  displayedColumns: string[] = ['name','location', 'bookingDate', 'phone', 'noOfGuests','roomType'];


  constructor(private bookingService: BookingService) { }

  ngOnInit(): void {
    this.getBookings();
  }

  getBookings() {
    this.bookingService.getBookings().subscribe(resp=>{
      this.bookings = resp;
    })
  }
}
