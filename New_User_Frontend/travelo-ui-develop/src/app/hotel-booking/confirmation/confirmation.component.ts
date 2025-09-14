import { DIALOG_DATA, DialogRef } from '@angular/cdk/dialog';
import { Component, Inject, NgZone, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Booking } from 'src/app/data-access/models/booking.model';
import { RoomModel } from 'src/app/data-access/models/hotel.model';
import { BookingService } from 'src/app/data-access/services/booking.service';
import { environment } from 'src/environments/environment';

declare var Razorpay: any;

@Component({
  selector: 'app-confirmation',
  templateUrl: './confirmation.component.html',
  styleUrls: ['./confirmation.component.scss']
})
export class ConfirmationComponent implements OnInit {
  bookingRequest! : Booking;
  paymentId: string = '';
  cgst: number = 0;
  sgst: number = 0;

  constructor(   @Inject(DIALOG_DATA) public data: { bookingRequest: Booking},
    private dialog: DialogRef,
    private bookingService: BookingService,
    private toastr: ToastrService,private router: Router,
	private zone: NgZone) { }

  ngOnInit(): void {
      this.bookingRequest = this.data.bookingRequest;
      this.cgst = this.bookingRequest.priceSlab ? (this.bookingRequest.priceSlab * 8)/100 : 0;
      this.sgst = this.bookingRequest.priceSlab ? (this.bookingRequest.priceSlab * 8)/100 : 0;
  }

  close() {
    this.dialog.close();
  }

	payNow() {

		let error:any = '';
		this.bookingService.bookHotel(this.bookingRequest).subscribe(resp=>{

		if(resp.orderId !== null) {
			this.bookingRequest['bookingId'] = resp.bookingId;
			localStorage.setItem('booking', JSON.stringify(this.bookingRequest));

			var options = {
				key: environment.rkey, // Enter the Key ID generated from the Dashboard
				amount: this.bookingRequest.amount ? parseFloat(this.bookingRequest.amount)*100 : 0, // Amount is in currency subunits. Default currency is INR. Hence, 50000 refers to 50000 paise
				currency: "INR",
				name: this.bookingRequest.guestDetails?.firstName,
				description: "Travalen Transaction",
				// image: "https://example.com/your_logo",
				order_id: resp.orderId, //This is a sample Order ID. Pass the `id` obtained in the response of Step 1
				// callback_url: "http://localhost:8080/api/payment/callback",
				// redirect: false,
				prefill: {
					name: this.bookingRequest.guestDetails?.firstName,
					email: this.bookingRequest.guestDetails?.email,
					contact: this.bookingRequest.guestDetails?.phone
				},
				notes: {
					address: "Travalen"
				},
				theme: {
					color: "#3399cc"
				},
				handler: function (response:any,error:any){
					
				},
			};
			options.handler = ((response, error) => {
			
  				this.bookingService.updatePaymentStatus(response).subscribe(resp=>{
					localStorage.removeItem("booking");
					this.zone.run(() => {
						this.router.navigateByUrl("/booking/my-trips");
					});
				});
				
			  });
		  
			this.close();

			var rzp1 = new Razorpay(options);
			rzp1.open();
						
			rzp1.on('payment.failed', function (response: any){    
				error = response.error.reason;
				console.log(error)
			}
			);

			

		} else {
			this.toastr.error("Something went wrong. Please try after sometime");
			this.close();
		}

		}
		);
	}



}
