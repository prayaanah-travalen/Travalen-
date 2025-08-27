import { Dialog } from '@angular/cdk/dialog';
import { Component, OnInit, ViewChild } from '@angular/core';
import { Booking, BookingModel } from 'src/app/data-access/model/booking.model';
import { BookingService } from 'src/app/data-access/services/booking.service';
import { BookingDetailsComponent } from '../booking-details/booking-details.component';
import { MatMenuTrigger } from '@angular/material/menu';

@Component({
  selector: 'app-booking',
  templateUrl: './booking.component.html',
  styleUrls: ['./booking.component.scss']
})
export class BookingComponent implements OnInit {

  bookings: Booking[] = [];
  displayedColumns: string[] = ['name','location', 'bookingDate', 'phone', 'noOfGuests','roomType','status', 'payment'];

  actionCtxMenuList!: any[];
  obContextMenuPosition = { x: '0px', y: '0px' };
  @ViewChild(MatMenuTrigger) contextMenu!: MatMenuTrigger;
  @ViewChild('trigger') trigger!: MatMenuTrigger;

  constructor(private bookingService: BookingService, private dialog: Dialog) { }

  ngOnInit(): void {

    this.getBookings();
  }

  getBookings() {
    this.bookingService.getBookings().subscribe(resp=>{
      this.bookings = resp;
    })
  }

  bookingDetails(booking: Booking) {
    this.dialog.open(BookingDetailsComponent,{
      width:'550px',
      data:{booking: booking}
   })
  }

  setContextMenus(): void {
    this.actionCtxMenuList =
      [
        { id: 'close', title: 'Close Booking', icon: 'buy icon' },
        { id: 'cancel', title: 'Cancel Booking', icon: 'buy icon' }
      ];
  }

  onContextMenuItemClick(menu: any) {
    const rowItem: Booking = this.contextMenu.menuData['row'];
    switch (menu.id) {
      case 'close':
        this.closeBooking(rowItem);
        break;
      case 'cancel':
        this.cancelBooking(rowItem);
        break;
      default:
        break;
    }
  }

  onContextMenu(event: MouseEvent, row: Booking) {
    event.preventDefault();
    this.setContextMenus();
    this.obContextMenuPosition.x = event.clientX + 'px';
    this.obContextMenuPosition.y = event.clientY + 'px';
    this.contextMenu.menuData = { row: row };
    this.trigger.openMenu();
  }

  closeBooking(booking: Booking) {
    let req = {
      bookingId: booking.bookingId,
    }
    this.bookingService.closeBooking(req).subscribe(resp=>{
      if(resp.success === 'SUCCESS') {
        this.getBookings();
      }
    })
  }

  cancelBooking(booking: Booking) {
    let req = {
      bookingId: booking.bookingId,
    }
    this.bookingService.cancelBooking(req).subscribe(resp=>{
      if(resp.success === 'SUCCESS') {
        this.getBookings();
      }
    })
  }


}
