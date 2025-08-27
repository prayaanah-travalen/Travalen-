import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CommonService } from './common.service';
import { map } from 'rxjs';
import { Hotel, HotelImage, RoomModel, RoomPriceSlabModel } from '../models/hotel.model';
import { DomSanitizer } from '@angular/platform-browser';
import { HotelSearchModel } from '../models/hotel-search.model';

@Injectable({
  providedIn: 'root'
})
export class HotelService {

  constructor(private commonService: CommonService,
    private sanitizer: DomSanitizer) { }

  getHotels(req: HotelSearchModel) {
    let locations: number[] = [];
    if(req.location) locations = req.location.map(loc=>loc.locationId);
    let payload = {
       locationId: locations,
       checkIn: req.checkIn,
        checkOut: req.checkOut,
        priceSlab: req.priceSlab ? req.priceSlab : []
    }

    return this.commonService.httpPOST("hotels",payload)
      .pipe(map(res=>{
        let hotelList: Hotel[] = [];
        res.map( (htl:any)=>{
          hotelList.push(this.toHotelModel(htl));
        })
        return hotelList;
      }

    ));
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

  getHotelById(id: number) {
    let locations: number[] = [];
    const endPoint = `hotel?id=${id}`
    return this.commonService.httpGET(endPoint)
      .pipe(map(htl=>{
          
        return this.toHotelModel(htl);
      }

    ));
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
}
