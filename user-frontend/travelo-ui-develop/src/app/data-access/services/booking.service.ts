import { Injectable } from '@angular/core';
import { CommonService } from './common.service';
import { map } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { PaymentModel } from '../models/payment.model';
import { Booking, BookingDetailModel } from '../models/booking.model';
import { Hotel, HotelImage, RoomModel, RoomPriceSlabModel } from '../models/hotel.model';
import { DomSanitizer } from '@angular/platform-browser';

@Injectable({
  providedIn: 'root'
})
export class BookingService {

  constructor(private commonService: CommonService,private http: HttpClient,    private sanitizer: DomSanitizer) { }

  getPaymentId() {
    return this.commonService.httpGET("booking/payment_id").pipe(map(resp=>resp));
  }

  callPhonePe(req: any) {
    let reqPayload = {
      request: req
    }
   return this.httpPOST("https://api-preprod.phonepe.com/apis/pg-sandbox/pg/v1/pay",reqPayload)
  }

  public httpPOST(endpoint: string, payload: any, options?:any) {
    let reqOption = { withCredentials: true };

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      // 'Authorization': `Bearer ${localStorage.getItem("auth")}`
    });
  
    const requestOptions = { headers: headers };
    return this.http.post<any>(endpoint, payload, requestOptions);
  }


  getPaymentLink() {
    return this.commonService.httpGET("booking/payment_link").pipe(map(resp=>{
            let response : PaymentModel = {
              paymentId: resp.paymentId,
              paymentLink: resp.paymentLink,
              bookingId: resp.response.bookingId
            }

            return response;
         }));
    
  }

  temporaryBooking(req: Booking) {
    
    return this.commonService.httpPOST("booking/temporary-booking", this.toBookingApiRequest(req));

  }

  bookHotel(req: Booking) {
    return this.commonService.httpPOST("booking/payment", this.toBookingApiRequest(req)).pipe(map(resp=>{
      let response : PaymentModel = { paymentId: '', paymentLink: '', bookingId: 0} ;
      if(resp.success === "SUCCESS") {
        // let response : PaymentModel = {
        //   paymentId: resp.response.paymentId,
        //   paymentLink: resp.response.paymentLink,
        //   bookingId: resp.response.bookingId
        // }
     
        response.bookingId = resp.response.bookingId;
        response.paymentId = resp.response.paymentId;
        response.orderId= resp.response.orderId;
        response.status = "SUCCESS";

      } else {
        response.status = "ERROR";
      }
   
      return response;
      
   }));

  }

  toBookingApiRequest(bookingRequest: Booking) {
    let bookingDetails:any[] = [];
    bookingRequest.bookingDetails.map(bd=>
       { bd.hotel.rooms.map(rm=>{
            let details = {
              hotelCode:this.toHotelDto(rm, bd.hotel),
              hotelRoomCode: rm.hotelRoomId,
              checkIn: bd.checkIn,
              checkOut: bd.checkOut,
              guestCount: bd.adults + bd.children,
              adult: bd.adults,
              children: bd.children,
              noOfRooms: rm.noOfSelectedRooms,
          }
          bookingDetails.push(details)
        })
      }
    );

    let tax = bookingRequest.priceSlab ? ((bookingRequest.priceSlab) * 16 / 100) : 0;

    let requestInput: any = {
      bookingId: bookingRequest.bookingId,
      bookingDetails:bookingDetails,
      guestDetails: bookingRequest.guestDetails,
      bookedFor: bookingRequest.bookingFor?.toUpperCase(),
      // priceSlab: bookingRequest.priceSlab,
      checkIn: bookingRequest.checkIn,
      checkOut: bookingRequest.checkOut,
      adults: bookingRequest.adults,
      children: bookingRequest.children,
      amount: bookingRequest.priceSlab?  bookingRequest.priceSlab + tax : 0
    }
    return requestInput;
  }

  toHotelDto(room:RoomModel, hotel: Hotel) {
    let rooms = [{hotelRoomId:room.hotelRoomId }]
    return {
      hotelCode: hotel.hotelCode,
      rooms: rooms
    }

  }


  toBookingDetailsModel(bkDetails:any) {

  }

  getBookings() {
    return this.commonService.httpGET("booking");
  }

  cancel(req: any) {
    return this.commonService.httpPOST("booking/cancel",req);
  }

  getStatus(id: number) {
   return this.commonService.httpGET(`booking/status?id=${id}`);
  }

  updatePaymentStatus(req:any) {
    return this.commonService.httpPOST(`payment/status`,req);
  }

  addHotelsForBooking(req: Booking) {
    return this.commonService.httpPOST("booking/details", this.toBookingApiRequest(req));
  }


  getBookingDetails(bookingId: number) {
    return this.commonService.httpGET(`booking/${bookingId}/details`);
  }

  toBookingModel(resp : any) {
    // let booking: Booking = {
    //   bookingId:resp.bookingId,
    //   adults: resp.adults,
    //   children: resp.children,
    //   guestDetails: {firstName: resp.guestDetails.firstName,
    //     lastName: resp.guestDetails.lastName,
    //     phone: resp.guestDetails.phone,
    //     email: resp.guestDetails.email
    //   },
    //   bookingFor: resp.bookingFor,
    //   bookingDetails: resp.bookingDetails,
    //   priceSlab: resp.priceSlab,
    //   checkIn: resp.checkIn,
    //   checkOut: resp.checkOut,
    //   amount: resp.amount,
      
    //   status: resp.status
    // }

    let bookingDetails:BookingDetailModel[] = [];
    resp.bookingDetails.map((bd:any)=>
       { bd.hotel.rooms.map((rm:any)=>{
            let details:BookingDetailModel = {
              hotel: this.toHotelModel(bd.hotelCode),
              // hotelRoomCode: rm.hotelRoomId,
              checkIn: bd.checkIn,
              checkOut: bd.checkOut,
              // guestCount: bd.adults + bd.children,
              adults: bd.adult,
              children: bd.children,
              // noOfSelectedRooms: rm.noOfRooms,
          }
          bookingDetails.push(details)
        })
      }
    );

    // let tax = bookingRequest.priceSlab ? ((bookingRequest.priceSlab) * 16 / 100) : 0;

    // let requestInput: any = {
    //   bookingId: bookingRequest.bookingId,
    //   bookingDetails:bookingDetails,
    //   guestDetails: bookingRequest.guestDetails,
    //   bookedFor: bookingRequest.bookingFor?.toUpperCase(),
    //   // priceSlab: bookingRequest.priceSlab,
    //   checkIn: bookingRequest.checkIn,
    //   checkOut: bookingRequest.checkOut,
    //   adults: bookingRequest.adults,
    //   children: bookingRequest.children,
    //   amount: bookingRequest.priceSlab?  bookingRequest.priceSlab + tax : 0
    // }
  }


  toHotelModel(htl: any) {
    let hotel: Hotel = {
      hotelCode: htl.hotelCode,
      hotelName: htl.hotelName,
      city: htl.city,
      state: htl.state,
      address: htl.address,
      starRating: htl.starRating,
      images: this.toHotelImage(htl.hotelImages),
      location:htl.location,
      rooms: this.toRoomModel(htl.rooms),
      amenities: htl.amenities,
      about: htl.about,
      websiteLink: htl.websiteLink,
      propertyRule: htl.propertyRule,
      checkInTime: htl.checkInTime ? htl.checkInTime : "11:00 AM",
      checkOutTime: htl.checkOutTime ? htl.checkOutTime : "12:00 PM",
      latitude: htl.latitude,
      longitude: htl.longitude,
    }

    return hotel;
  }


  
  toHotelImage(images:any) {
    return images.map((img: any)=> {
      let objectURL = 'data:image/png;base64,' + img.image;
      let images: HotelImage = {
        imageId: img.imageId,
        // image: img.image
        image: this.sanitizer.bypassSecurityTrustUrl(objectURL)
      }
      return images
    })

  }

  toRoomModel(hotelRooms: any[]): RoomModel[] {
    return hotelRooms.map(rm=>{
      let room: RoomModel = {
        hotelRoomId: rm.hotelRoomId,
        hotelCode: rm.hotelCode,
        roomName: rm.roomName,
        occupancy: rm.occupancy,
        roomDescription: rm.roomDescription,
        price: rm.price,
        bedType: rm.bedType,
        roomImages: this.toHotelImage(rm.roomImages),
        priceSlab: this.toRoomPriceSlab(rm.priceSlab),
        status: rm.status,
        amenities: rm.amenities,
        roomTags: rm.roomTags,
        noOfRooms: rm.noOfRooms,
        noOfAvailableRooms: rm.noOfAvailableRooms
      }
      return room;
    })
  }

  toRoomPriceSlab(priceSlab: any[]): RoomPriceSlabModel[] {
    return priceSlab.map(pr=>{
      return {
        id:pr.id,
        priceSlabId: {
          id: pr.id,
          priceSlab: pr.priceSlabId.priceSlab,
          maxAllowedGuest: pr.priceSlabId.maxAllowedGuest,
          maxAllowedRoom: pr.priceSlabId.maxAllowedRoom,
          noOfNights: pr.priceSlabId.noOfNights
        },
        hotelRoomId: pr.hotelRoomId,
        hotelCode: pr.hotelCode
      }
    })
  }
}
