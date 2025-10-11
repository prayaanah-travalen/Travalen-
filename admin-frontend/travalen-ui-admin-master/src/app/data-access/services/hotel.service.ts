import { Injectable } from '@angular/core';
import { HotelReqModel, RoomReqModel } from '../model/hotel-req.model';
import { CommonService } from './common.service';
import { map, Observable } from 'rxjs';
import { Hotel, HotelImage, RoomModel, RoomPriceSlabModel } from '../model/hotel.model';
import { DomSanitizer } from '@angular/platform-browser';

@Injectable({
  providedIn: 'root'
})
export class HotelService {
  http: any;

  constructor(private commonService: CommonService,
    private sanitizer: DomSanitizer) { }

  saveHotelWithFormData(req: HotelReqModel, hotelImages: File[]) {
    let payload=new FormData();
    payload.append("hotel", new Blob(
      [JSON.stringify(req)],
      { type: "application/json" }
    ));
    hotelImages.forEach(img=>  {
      payload.append("hotelImages", img)
      payload.append("contentType", img.type)
    });

    // roomImages.forEach(img=>   payload.append("roomImages", img));
    return this.commonService.httpPOST("hotel/save", payload);
  }

  // getHotels() {
    
  //   return this.commonService.httpGET("hotel/hotels")
  //     .pipe(map(res=>{
  //       let hotelList: Hotel[] = [];
  //       res.map( (htl:any)=>{
  //         let hotel = this.toHotelModel(htl);
  //         if(hotel !== null)   hotelList.push(hotel);
  //       })
  //       return hotelList;
  //     }

  //   ));
  // }

  getHotels(page: number = 0, size: number = 5): Observable<{
  content: Hotel[],
  totalElements: number,
  totalPages: number,
  pageable: { pageNumber: number, pageSize: number }
}> {
  const url = `hotel/hotels?page=${page}&size=${size}`;
  return this.commonService.httpGET(url).pipe(
    map((res: any) => {
      return {
        content: (res.content || res).map((htl: any) => this.toHotelModel(htl)).filter((h: null) => h !== null),
        totalElements: res.totalElements ?? (res.length || 0),
        totalPages: res.totalPages ?? 1,
        pageable: {
          pageNumber: res.pageable?.pageNumber ?? page,
          pageSize: res.pageable?.pageSize ?? size
        }
      };
    })
  );
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
        roomPackage: rm.roomPackage,
        extraBedCostAdult: rm.extraBedCostAdult,
        extraBedCostChild: rm.extraBedCostChild
      }
      return room;
    })
  }

  toRoomPriceSlab(priceSlab: any[]): RoomPriceSlabModel[] {
    return priceSlab.map(pr=>{
      return {
        id:pr.id,
        priceSlabId: {
          id: pr.priceSlabId.id,
          priceSlab: pr.priceSlabId.priceSlab,
          maxAllowedGuest: pr.priceSlabId.maxAllowedGuest,
          maxAllowedRoom: pr.priceSlabId.maxAllowedRoom,
          noOfNights:  pr.priceSlabId.noOfNights
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

  getAllAmenities() {
  const endPoint = `hotel/amenities`;
  return this.commonService.httpGET(endPoint)
    .pipe(
      map((amenities: any[]) => amenities.map(a => this.toAmenityModel(a)))
    );
  }

  addAmenity(amenity: { amenity: string }) {
    const endPoint = `hotel/add-amenities`;
    return this.commonService.httpPOST(endPoint, amenity)
      .pipe(map(a => this.toAmenityModel(a)));
  }

  // Model transformer (optional helper)
  private toAmenityModel(a: any) {
    return {
      id: a.id,
      description: a.description
    };
  }


  toHotelImage(images:any) {
    return images.map((img: any)=> {
      let objectURL = 'data:image/png;base64,' + img.image;
      let images: HotelImage = {
        imageId: img.imageId,
        // image: img.image
        image: this.sanitizer.bypassSecurityTrustUrl(objectURL),
        imageName: img.imageName,
        imageByte: img.image
      }
      return images
    })

  }

  toHotelModel(htl: any) {
    if(htl && htl !== null) {
      let hotel: Hotel = {
        hotelCode: htl.hotelCode,
        hotelName: htl.hotelName,
        city: htl.city,
        state: htl.state,
        address: htl.address,
        starRating: htl.starRating,
        images: this.toHotelImage(htl.hotelImages),
        location:htl.location,
        rooms: this.toRoomModel(htl.hotelRooms),
        amenities: htl.amenities,
        about: htl.about,
        websiteLink: htl.websiteLink,
        propertyRule: htl.propertyRule,
        email:htl.email,
        latitude: htl.latitude,
        longitude: htl.longitude,
        conatctDetails: htl.contactDetails
      }
      return hotel;
    }
    return null
  }

  saveRoom(req: any, images: File[]) {
    let payload=new FormData();
    payload.append("room", new Blob(
      [JSON.stringify(req)],
      { type: "application/json" }
    ));
    images.forEach(img=>  {
      payload.append("roomImages", img)
      payload.append("contentType", img.type)
    });

    // roomImages.forEach(img=>   payload.append("roomImages", img));
    return this.commonService.httpPOST("hotel/room/save", payload);
  }


  deleteHotel(req:HotelReqModel) {
     return this.commonService.httpPOST("hotel/delete", req);
  }

  deleteHotelRoom(req:any) {
    return this.commonService.httpPOST("hotel/room/delete", req);
 }


 // Add this method to HotelService.ts
saveContactPerson(contactData: any) {
  return this.commonService.httpPOST("hotel/contact/save", contactData);
}
}
