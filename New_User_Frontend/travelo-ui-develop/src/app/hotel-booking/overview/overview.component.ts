import { DIALOG_DATA, DialogRef } from '@angular/cdk/dialog';
import { Component, Inject, OnInit } from '@angular/core';
import { Booking } from 'src/app/data-access/models/booking.model';
import { PaymentModel } from 'src/app/data-access/models/payment.model';
import { BookingService } from 'src/app/data-access/services/booking.service';

@Component({
  selector: 'app-overview',
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.scss']
})
export class OverviewComponent implements OnInit {

  bookingRequest! : Booking;
  paymentId: string = '';
  cgst: number = 0;
  sgst: number = 0;
  payment!:PaymentModel;
  message: string = '';
  error: boolean = false;

  constructor(
    private bookingService: BookingService) { }

  ngOnInit(): void {
    let cachedBooking = localStorage.getItem("booking");
    if(cachedBooking && cachedBooking !== null) this.bookingRequest = JSON.parse(cachedBooking);

    this.cgst = this.bookingRequest.priceSlab ? (this.bookingRequest.priceSlab * 8)/100 : 0;
    this.sgst = this.bookingRequest.priceSlab ? (this.bookingRequest.priceSlab * 8)/100 : 0;

    this.getStatus();
    localStorage.removeItem("booking");
  }

  getStatus() {
    if(this.bookingRequest.bookingId) {
      this.bookingService.getStatus(this.bookingRequest.bookingId).subscribe(resp=>{
        this.payment = resp;
        if(this.payment.status && this.payment.status === "SUCCESS") {
          this.error = false;
          this.message = "Booking confirmed";
        } else {
          this.error =  true;
          this.message = `Booking failed. Please try again`;

        }
      });
    }
   
  }
}
