import { Component, OnInit } from '@angular/core';
import { HotelService } from '../data-access/services/hotel.service';
import { Hotel, RoomModel } from '../data-access/models/hotel.model';
import { HotelSearchModel } from '../data-access/models/hotel-search.model';
import { NonNullableFormBuilder } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { BookingDetailModel, Booking } from '../data-access/models/booking.model';
import { Router } from '@angular/router';
import { PriceSlabModel } from '../data-access/models/price-slab.model';
import { UtilsService } from '../data-access/services/utils.service';
import { max } from 'rxjs';
import { formatDate } from '@angular/common';
import { PopularDestDto } from '../data-access/models/dsetination.model';
import { BookingService } from '../data-access/services/booking.service';

@Component({
  selector: 'app-hotel-details',
  templateUrl: './hotel-details.component.html',
  styleUrls: ['./hotel-details.component.scss']
})
export class HotelDetailsComponent implements OnInit {
  hotelCode: number = 0;
  hotel!: Hotel;
  searchInput: HotelSearchModel | undefined;
  selectedLocation: string | undefined;
  removable = false;
	selectable = false;
  selectedHotelImg: any;
  tillDateDisable = new Date();
	maxDateEnable = new Date();
  selectedRooms: RoomModel[] = [];
  selectedPriceSlabIds: number[] = [];
  selectedPriceSlab: PriceSlabModel[] = [];
  priceSlabs:PriceSlabModel[] = [];

  bookingForm = this.fb.group({
		checkIn: [""],
		checkOut: [""],
		roomId: [0],

	});
  bookingReq!: Booking; 

  popularDest:PopularDestDto[] = [];

  bookingId: number | undefined;

  constructor(private router: Router,
    private hotelSrvice: HotelService,
    private fb: NonNullableFormBuilder,
    private toastr: ToastrService,
    private utils: UtilsService,
    private bookingService: BookingService
    ) { }


  ngOnInit(): void {
    this.getPopularDestination();

    this.getPriceSlabs();

    if(window.history.state.hotelCode){
      this.hotelCode =  window.history.state.hotelCode;
      localStorage.setItem("hotelCode", JSON.stringify(this.hotelCode));
    } else {
      let cachedSearchInput = localStorage.getItem("hotelCode");
      if(cachedSearchInput && cachedSearchInput !== null) this.hotelCode = JSON.parse(cachedSearchInput);
    }

    let cachedSearchInput = localStorage.getItem("hotelSearch");
    if(cachedSearchInput && cachedSearchInput !== null) this.searchInput = JSON.parse(cachedSearchInput);

    if(this.searchInput) {

      var checkin = this.searchInput.checkIn.split("-");
      var checkOut = this.searchInput.checkOut.split("-");

      this.tillDateDisable.setDate(new Date(checkin[2]+ "-"+checkin[1] + "-" +checkin[0]).getDate());
      this.maxDateEnable.setDate(new Date(checkOut[2]+ "-"+checkOut[1] + "-" +checkOut[0]).getDate());

      this.selectedPriceSlab = this.searchInput.priceSlab ? this.searchInput.priceSlab : [];
      this.selectedPriceSlabIds = this.searchInput.priceSlab ? this.searchInput.priceSlab?.map(ps=>ps.id) : [];

    } 

    this.selectedLocation = this.searchInput?.location.map(loc=>loc.location).toString();

    this.getHotel();

    this.fetchBookingRequest();

  }

  
  getPriceSlabs() {
    this.utils.getPriceSlab().subscribe(resp=>{
      this.priceSlabs = resp
    });
  }

  getHotel() {
    if(this.hotelCode > 0) {
      this.hotelSrvice.getHotelById(this.hotelCode)
      .subscribe(resp=>{
        this.hotel = resp;
        this.selectedHotelImg = this.hotel.images[0].image;
      })
    }
  
  }

  getIconClass(event: string) {
    let type = event.toUpperCase();
    switch(type) {
      case 'RESTAURANT' : return 'fa-utensils'
      case 'PARKING' : return 'fa-parking'
      case 'WIFI': return 'fa-wifi'
      case 'LAUNDRY' : return 'fa-washer'
      case 'DRY CLEANING' : return 'fa-dryer-alt'
      default : return ''
    }

  }

  addToList() {
  
    if(this.bookingForm.get('checkIn')?.getRawValue() === '' || this.bookingForm.get('checkOut')?.getRawValue() === '') {
      this.toastr.error("Select date");
      return false;
    }

    if(this.selectedRooms.length <= 0) {
      this.toastr.error("Select room");
      return false;
    }

    let maxRoom = 0;
    let totalRooms = 0;

    let totalPrice = 0;
    let totalPriceForSelectedRooms = 0;
    
    this.selectedPriceSlab.map(pr=>
      {
        maxRoom = maxRoom + pr.maxAllowedRoom;
        totalPrice = totalPrice + pr.priceSlab;
      });

    this.selectedRooms.map(rm=>{
      if(rm.noOfSelectedRooms) totalRooms = rm.noOfSelectedRooms;
      totalPriceForSelectedRooms = totalPriceForSelectedRooms + parseFloat(rm.price);

    })

    if(totalPriceForSelectedRooms > totalPrice){
      this.toastr.error("Reached the maximum limit of the selected budget" + totalPrice);
      return false;
    }

    if(totalRooms > maxRoom) {
      this.toastr.error("Maximum allowed rooms under select budget is " + maxRoom);
      return false;
    }

    this.setBookingRequest();

    return true;
  }

  selectRoom(room:RoomModel) {
    room['noOfSelectedRooms'] = room.noOfSelectedRooms ? room.noOfSelectedRooms + 1 : 1;
    this.selectedRooms.push(room);
  }

  increment(room:RoomModel) {
    room['noOfSelectedRooms'] = room.noOfSelectedRooms ? room.noOfSelectedRooms + 1 : 1;
    
    this.selectedRooms = this.selectedRooms.map(rm=>{
      if(rm.hotelRoomId === room.hotelRoomId){
        rm.noOfSelectedRooms =  room['noOfSelectedRooms']
      }
      return rm;
    })

  }

  decrement(room:RoomModel) {
    room['noOfSelectedRooms'] = room.noOfSelectedRooms ? room.noOfSelectedRooms - 1 : 0;
    this.selectedRooms = this.selectedRooms.map(rm=>{
      if(rm.hotelRoomId === room.hotelRoomId){
        rm.noOfSelectedRooms =  room['noOfSelectedRooms']
      }
      return rm;
    }).filter(rm=> rm.noOfSelectedRooms && rm.noOfSelectedRooms > 0)
    
  }

  fetchBookingRequest() {
    let cachedBooking = localStorage.getItem("booking");
    if(cachedBooking && cachedBooking !== null) this.bookingReq = JSON.parse(cachedBooking);
  }

  setBookingRequest() {
     // let htl: Hotel = this.hotel;
     let htl: Hotel = {...this.hotel};
   
     htl['rooms'] = this.selectedRooms;
 
     let booking: BookingDetailModel = {
         checkIn: this.dateFormat(this.bookingForm.get('checkIn')?.getRawValue()),
         checkOut: this.dateFormat(this.bookingForm.get('checkOut')?.getRawValue()),
         adults: this.searchInput?.adults ? this.searchInput.adults : 0,
         children: this.searchInput?.children ? this.searchInput?.children : 0,
         hotel: htl
 
     }
     let bookingDetails: BookingDetailModel[] = [];
     bookingDetails.push(booking);
    
     this.fetchBookingRequest();
 
     let priceSlab = 0;
     this.searchInput?.priceSlab?.map(ps=>{
       priceSlab = priceSlab + ps.priceSlab;
     })
    //  this.bookingReq = {
    //   bookingDetails: bookingDetails,
    //   adults: this.searchInput?.adults ? this.searchInput.adults : 0,
    //   children: this.searchInput?.children ? this.searchInput?.children : 0, 
    //   priceSlab: priceSlab,
    //   checkIn: this.searchInput?.checkIn ? this.searchInput?.checkIn : '',
    //   checkOut: this.searchInput?.checkOut ? this.searchInput?.checkOut : '',
    // }
    
     if(this.bookingReq) {
      //  bookingDetails = this.bookingReq.bookingDetails ? this.bookingReq.bookingDetails : [];
       this.bookingReq['bookingDetails'] = this.bookingReq.bookingDetails.concat(bookingDetails);
       this.bookingReq['adults'] = this.searchInput?.adults ? this.searchInput.adults : 0;
       this.bookingReq['children'] = this.searchInput?.children ? this.searchInput?.children : 0;
   
     } else {
       this.bookingReq = {
         bookingDetails: bookingDetails,
         adults: this.searchInput?.adults ? this.searchInput.adults : 0,
         children: this.searchInput?.children ? this.searchInput?.children : 0, 
         priceSlab: priceSlab,
         checkIn: this.searchInput?.checkIn ? this.searchInput?.checkIn : '',
         checkOut: this.searchInput?.checkOut ? this.searchInput?.checkOut : '',
       }
     }
     
     localStorage.setItem('booking', JSON.stringify(this.bookingReq));

     this.bookingService.addHotelsForBooking(this.bookingReq).subscribe(resp=>{
      if(resp.response && resp.response.bookingId) {
        this.bookingId = resp.response.bookingId;
        localStorage.setItem("bookingId",resp.response.bookingId);

      }
    })
  }


  dateFormat(dte: any) {
    return formatDate(dte, 'dd-MM-YYYY', 'en');
  }

  getPopularDestination(){
    this.utils.getPopularDestination().subscribe(resp=>{
      this.popularDest = resp;
     })
  }
}
