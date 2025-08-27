import { Dialog } from '@angular/cdk/dialog';
import { Component, OnInit } from '@angular/core';
import { ConfirmationComponent } from '../confirmation/confirmation.component';
import { PoliciesComponent } from '../policies/policies.component';
import { BookingDetailModel, Booking } from 'src/app/data-access/models/booking.model';
import { NonNullableFormBuilder } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { BookingService } from 'src/app/data-access/services/booking.service';
import { Hotel } from 'src/app/data-access/models/hotel.model';
import { Router } from '@angular/router';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-hotel-booking',
  templateUrl: './hotel-booking.component.html',
  styleUrls: ['./hotel-booking.component.scss'],
  providers:[DatePipe]
})
export class HotelBookingComponent implements OnInit {

  constructor(private dialog: Dialog,
    private fb: NonNullableFormBuilder,
    private toastr: ToastrService,
    private bookingService: BookingService,
    private router:Router, public datePipe: DatePipe) { }

    bookingRequest!: Booking;
    policyAccepted: boolean = false;
    policyError: boolean =  false;

    seletedHotels: Hotel[] = [];

  guestForm = this.fb.group({
		firstName: [""],
		lastName: [""],
		email: [""],
    mobile:[""],
    bookingFor:['myself']

	});

  ngOnInit(): void {

    let cachedBooking = localStorage.getItem("booking");
    if(cachedBooking && cachedBooking !== null) {
      this.bookingRequest = JSON.parse(cachedBooking);
      this.seletedHotels = this.bookingRequest.bookingDetails.map(bk=>bk.hotel);
      this.guestForm.patchValue({
        firstName: this.bookingRequest.guestDetails?.firstName,
        lastName: this.bookingRequest.guestDetails?.lastName,
        mobile: this.bookingRequest.guestDetails?.phone,
        email: this.bookingRequest.guestDetails?.email
      })
    } else {
      this.router.navigateByUrl("/hotels")
    }

    let cachedBookingId = localStorage.getItem("bookingId");
    let bookingId = 0;

    if(cachedBookingId && cachedBookingId !== null) bookingId = JSON.parse(cachedBookingId);

    this.bookingService.getBookingDetails(bookingId).subscribe(resp=>{
      // this.bookingRequest = resp;
      /*this.guestForm.patchValue({
        firstName: this.bookingRequest.guestDetails?.firstName,
        lastName: this.bookingRequest.guestDetails?.lastName,
        mobile: this.bookingRequest.guestDetails?.phone,
        email: this.bookingRequest.guestDetails?.email
      })*/
    })

  

  }
  bookNow() {
    if(!this.policyAccepted) {
      this.policyError=true;
      return false;
    }

    if(this.guestForm.get("firstName")?.getRawValue() === '') {
      this.guestForm.controls['firstName'].setErrors({ 'required': null });
    }
    if(this.guestForm.get("lastName")?.getRawValue() === '') {
      this.guestForm.controls['lastName'].setErrors({ 'required': null });
    }
    if(this.guestForm.get("mobile")?.getRawValue() === '') {
      this.guestForm.controls['mobile'].setErrors({ 'required': null });
    }
    if(this.guestForm.get("email")?.getRawValue() === '') {
      this.guestForm.controls['email'].setErrors({ 'required': null });
    }

    if(!this.guestForm.valid) {
      this.toastr.error("Enter guest details");
      return false;
    }

    this.bookingRequest['guestDetails']= {
      firstName: this.guestForm.get("firstName")?.getRawValue(),
      lastName: this.guestForm.get("lastName")?.getRawValue(),
      phone: this.guestForm.get("mobile")?.getRawValue(),
      email: this.guestForm.get("email")?.getRawValue(),
  };
  
  this.bookingRequest['bookingFor'] = this.guestForm.get("bookingFor")?.getRawValue();
  localStorage.setItem("booking", JSON.stringify(this.bookingRequest))

  this.doTemporaryBooking();
    
    return true;
  }


  doTemporaryBooking() {
    this.bookingService.temporaryBooking(this.bookingRequest).subscribe(resp=>{
      if(resp.success === 'SUCCESS') {
        this.bookingRequest['bookingId'] = resp.response.bookingId;
        this.dialog.open(ConfirmationComponent,{
          data: {
            bookingRequest: this.bookingRequest
          }
        });
    
      } else {
        this.toastr.error(resp.errorMessage);
      }
    });
    
  }
  
  Policies()
  {
    this.dialog.open(PoliciesComponent,{})
  }

  submitGuest() {
   this.bookingRequest['guestDetails']= {
        firstName: this.guestForm.get("firstName")?.getRawValue(),
        lastName: this.guestForm.get("lastName")?.getRawValue(),
        phone: this.guestForm.get("mobile")?.getRawValue(),
        email: this.guestForm.get("email")?.getRawValue(),
    };
    
    this.bookingRequest['bookingFor'] = this.guestForm.get("bookingFor")?.getRawValue();
  }

  removeHotels(hotel: Hotel): void {
		// const index = this.seletedHotels.indexOf(hotel);

		// if (index >= 0) {
		// 	this.seletedHotels.splice(index, 1);
    //   this.bookingReq['bookingDetails'] = this.bookingReq.bookingDetails.filter(bk=>bk.hotel.hotelCode !== hotel.hotelCode);

		// 	this.announcer.announce(`Removed ${hotel}`);
		// }
    this.seletedHotels = this.seletedHotels.filter(htl=>htl.hotelCode !== hotel.hotelCode);
    this.bookingRequest['bookingDetails'] = this.bookingRequest.bookingDetails.filter(bk=>bk.hotel.hotelCode !== hotel.hotelCode);
    if(this.bookingRequest.bookingDetails.length === 0) {
      localStorage.removeItem("booking");
      this.fetchBookingRequest();

    }
	}

  fetchBookingRequest() {
    let cachedBooking = localStorage.getItem("booking");
    if(cachedBooking && cachedBooking !== null) {
      this.bookingRequest = JSON.parse(cachedBooking);
      this.seletedHotels = this.bookingRequest.bookingDetails.map(bk=>bk.hotel);
      
    } else {

      // this.router.navigateByUrl("/hotels")
    }
  }
}
