import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { Booking } from 'src/app/data-access/models/booking.model';
import { BookingService } from 'src/app/data-access/services/booking.service';

@Component({
  selector: 'app-my-trips',
  templateUrl: './my-trips.component.html',
  styleUrls: ['./my-trips.component.scss']
})
export class MyTripsComponent implements OnInit {

  bookings: Booking[] = [];

  constructor(private bookingService: BookingService,
    private toastr: ToastrService,) { }

  ngOnInit(): void {
     this.getBookings();
  }

  getBookings() {
    this.bookingService.getBookings().subscribe(resp=>{
      this.bookings = resp;
    })
  }

  cancel(bookingId:number) {
    let req = {
      bookingId: bookingId
    }

    this.bookingService.cancel(req).subscribe(resp=>{
      if(resp.success === 'SUCCESS'){
        this.getBookings();
      } else {
        this.toastr.error("Cancellation failed. Please contact customer support")
      }
        
    })
  }

  postpone() {
    
  }

}
