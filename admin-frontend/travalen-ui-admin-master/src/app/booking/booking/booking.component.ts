import { Dialog } from '@angular/cdk/dialog';
import { Component, OnInit, ViewChild } from '@angular/core';
import { Booking, BookingModel } from 'src/app/data-access/model/booking.model';
import { BookingService } from 'src/app/data-access/services/booking.service';
import { BookingDetailsComponent } from '../booking-details/booking-details.component';
import { MatMenuTrigger } from '@angular/material/menu';


@Component({
  selector: 'app-booking',
  templateUrl: './booking.component.html',
  styleUrls: ['./booking.component.scss'],

})
export class BookingComponent implements OnInit {
  firstDay!: Date | null;
  lastDay!: Date | null;

 

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
      this.allBookings = resp || [];
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

  allBookings: any[] = []; 

  dateChange() {
    this.filterBookings();
    if (this.firstDay && this.lastDay) {
      const start = new Date(this.firstDay).setHours(0, 0, 0, 0);
      const end = new Date(this.lastDay).setHours(23, 59, 59, 999);
  
      this.bookings = this.allBookings.filter(b => {
        const bookingDate = new Date(b.bookingDate).getTime(); // use bookingDate field
        return bookingDate >= start && bookingDate <= end;
      });
    } else {
      this.bookings = [...this.allBookings]; // reset when cleared
    }
  }


  searchText: string = '';

filterBookings() {
  this.bookings = this.allBookings.filter(b => {
    const bookingDate = new Date(b.bookingDate.replace(/-/g,'/')).getTime();
    const start = this.firstDay ? new Date(this.firstDay).setHours(0,0,0,0) : null;
    const end = this.lastDay ? new Date(this.lastDay).setHours(23,59,59,999) : null;

    // Filter by date range
    const dateMatch = (!start || !end) || (bookingDate >= start && bookingDate <= end);

    // Filter by search text
    const searchMatch = this.searchText
      ? b.guestDetails.firstName.toLowerCase().includes(this.searchText.toLowerCase()) ||
        b.guestDetails.lastName.toLowerCase().includes(this.searchText.toLowerCase())
      : true;

    return dateMatch && searchMatch;
  });
}

  
}
