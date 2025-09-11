
import { Dialog } from '@angular/cdk/dialog';
import { Component, OnInit, ViewChild } from '@angular/core';
import { Booking, BookingModel } from 'src/app/data-access/model/booking.model';
import { BookingService } from 'src/app/data-access/services/booking.service';
import { BookingDetailsComponent } from '../booking-details/booking-details.component';
import { MatMenuTrigger } from '@angular/material/menu';
import * as XLSX from 'xlsx';


@Component({
  selector: 'app-booking',
  templateUrl: './booking.component.html',
  styleUrls: ['./booking.component.scss'],

})
export class BookingComponent implements OnInit {
  // Date range properties
  // startDate: string = '';
  // endDate: string = '';



  firstDay!: Date | null;
  lastDay!: Date | null;

  // Search and filter properties
  searchText: string = '';
  allBookings: Booking[] = [];
  bookings: Booking[] = [];
  
  // Table configuration
  displayedColumns: string[] = ['name','location', 'bookingDate', 'phone', 'noOfGuests','roomType','status', 'payment'];

  // Context menu properties
  actionCtxMenuList!: any[];
  obContextMenuPosition = { x: '0px', y: '0px' };
  @ViewChild(MatMenuTrigger) contextMenu!: MatMenuTrigger;
  @ViewChild('trigger') trigger!: MatMenuTrigger;

  constructor(private bookingService: BookingService, private dialog: Dialog) { }

  ngOnInit(): void {
    this.getBookings();
  }

  /**
   * Fetch all bookings from API
   */
  getBookings() {
    this.bookingService.getBookings().subscribe((resp) => {
      this.allBookings = resp || [];
      this.bookings = [...this.allBookings];
      console.log(this.bookings); 
    });
  }

  /**
   * Open booking details dialog
   */
  bookingDetails(booking: Booking) {
    this.dialog.open(BookingDetailsComponent, {
      width: '550px',
      data: { booking },
    });
  }

  /**
   * Context menu handling
   */
  setContextMenus(): void {
    this.actionCtxMenuList = [
      { id: 'close', title: 'Close Booking', icon: 'buy icon' },
      { id: 'cancel', title: 'Cancel Booking', icon: 'buy icon' },
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
    }
  }

  onContextMenu(event: MouseEvent, row: Booking) {
    event.preventDefault();
    this.setContextMenus();
    this.obContextMenuPosition.x = event.clientX + 'px';
    this.obContextMenuPosition.y = event.clientY + 'px';
    this.contextMenu.menuData = { row };
    this.trigger.openMenu();
  }

  /**
   * Close booking
   */
  closeBooking(booking: Booking) {
    if (!booking.bookingId) return;
    const req = { bookingId: booking.bookingId };
    this.bookingService.closeBooking(req).subscribe((resp) => {
      if (resp.success === 'SUCCESS') {
        this.getBookings();
      }
    });
  }

  /**
   * Cancel booking
   */
  cancelBooking(booking: Booking) {
    if (!booking.bookingId) return;
    const req = { bookingId: booking.bookingId };
    this.bookingService.cancelBooking(req).subscribe((resp) => {
      if (resp.success === 'SUCCESS') {
        this.getBookings();
      }
    });
  }

  /**
   * Date range change handler
   */
  dateChange() {
    this.filterBookings();
    if (this.firstDay && this.lastDay) {
      const start = new Date(this.firstDay).setHours(0, 0, 0, 0);
      const end = new Date(this.lastDay).setHours(23, 59, 59, 999);

      this.bookings = this.allBookings.filter((b) => {
        if (!b.bookingDate) return false;
        const bookingDate = new Date(b.bookingDate).getTime();
        return bookingDate >= start && bookingDate <= end;
      });
    } else {
      this.bookings = [...this.allBookings];
    }
  }

  /**
   * Filter bookings by date range + search text
   */
  filterBookings() {
    this.bookings = this.allBookings.filter((b) => {
      // ✅ Safe date parsing
      const bookingDate = b.bookingDate
        ? new Date(b.bookingDate.replace(/-/g, '/')).getTime()
        : 0;

      const start = this.firstDay
        ? new Date(this.firstDay).setHours(0, 0, 0, 0)
        : null;
      const end = this.lastDay
        ? new Date(this.lastDay).setHours(23, 59, 59, 999)
        : null;

      const dateMatch =
        !start || !end ? true : bookingDate >= start && bookingDate <= end;

      // ✅ Safe guestDetails access
      const searchMatch = this.searchText
        ? b.guestDetails?.firstName?.toLowerCase().includes(this.searchText.toLowerCase()) ||
          b.guestDetails?.lastName?.toLowerCase().includes(this.searchText.toLowerCase()) ||
          b.guestDetails?.phone?.toLowerCase().includes(this.searchText.toLowerCase())
        : true;

      return dateMatch && searchMatch;
    });
  }



  onDateRangeChange() {
    this.firstDay = this.startDate ? new Date(this.startDate) : null;
    this.lastDay = this.endDate ? new Date(this.endDate) : null;
    this.dateChange();
  }

  clearDateRange() {
    this.startDate = null;
    this.endDate = null;
    this.firstDay = null;
    this.lastDay = null;
    this.bookings = [...this.allBookings];
  }

  // downloadExcel() {
  //   const worksheet = XLSX.utils.json_to_sheet(this.bookings);
  //   const workbook = XLSX.utils.book_new();
  //   XLSX.utils.book_append_sheet(workbook, worksheet, "Bookings");
  //   XLSX.writeFile(workbook, "bookings.xlsx");
  // }

  downloadExcel() {
    const flattenedData = this.bookings.map(b => {
      return {
        Name: `${b.guestDetails?.firstName ?? ''} ${b.guestDetails?.lastName ?? ''}`.trim(),
        Location: b.location?.map(l => l.location).join(' | ') ?? '',
        BookingDate: this.formatDate(b.bookingDate),
        Phone: b.guestDetails?.phone ?? '',
        NoOfGuests: `${b.adults ?? 0} Adult(s), ${b.children ?? 0} Child(ren)`,
        NoOfHotels: b.bookingDetails?.length ?? 0,
        Status: b.status ?? '',
        PaymentStatus: (b.payment ?? [])
          .map(p => {
            if (b.status === 'CANCELLED' && p.transactionType === 'REFUND') {
              return `${p.transactionType} ${p.status}`;
            }
            return p.status;
          })
          .join(', ')
      };
    });
  
    const worksheet = XLSX.utils.json_to_sheet(flattenedData);
    const workbook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet, 'Bookings');
    XLSX.writeFile(workbook, 'bookings.xlsx');
  }
  





  formatDate(dateString?: string): string {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: '2-digit'
    });
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'CONFIRMED': return 'status-confirmed';
      case 'CANCELLED': return 'status-cancelled';
      case 'PENDING': return 'status-pending';
      default: return 'status-default';
    }
  }

  getPaymentStatusClass(payment: any, bookingStatus: string): string {
    if (bookingStatus === 'CANCELLED' && payment.transactionType === 'REFUND') {
      return 'payment-refund';
    }
    switch (payment.status) {
      case 'PAID': return 'payment-success';
      case 'FAILED': return 'payment-failed';
      case 'PENDING': return 'payment-pending';
      default: return 'payment-default';
    }
  }

  startDate: string | null = null;
  endDate: string | null = null;

}