import { Injectable } from '@angular/core';
import { CommonService } from './common.service';

@Injectable({
  providedIn: 'root'
})
export class BookingService {

  constructor(private commonService: CommonService,) { }

  getBookings() {
    return this.commonService.httpGET("booking")
  }

  getBookingsForCalendar(req: any) {
    return this.commonService.httpPOST("booking",req);
  }

  closeBooking(req: any) {
    return this.commonService.httpPOST("booking/close",req);
  }

  cancelBooking(req: any) {
    return this.commonService.httpPOST("booking/cancel",req);
  }
}
