import { DIALOG_DATA } from '@angular/cdk/dialog';
import { Component, Inject, OnInit } from '@angular/core';
import { Booking } from 'src/app/data-access/model/booking.model';

@Component({
  selector: 'app-booking-details',
  templateUrl: './booking-details.component.html',
  styleUrls: ['./booking-details.component.scss']
})
export class BookingDetailsComponent implements OnInit {
  booking!: Booking;

  constructor( @Inject(DIALOG_DATA) public data: { booking:Booking}) { }

  ngOnInit(): void {
    this.booking = this.data.booking;
  }

}
