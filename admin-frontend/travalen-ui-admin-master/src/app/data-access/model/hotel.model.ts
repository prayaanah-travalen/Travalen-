import { SafeResourceUrl } from "@angular/platform-browser";
import { LocationModel } from "./location.model"
import { PriceSlabModel } from "./price-slab.model";

export interface Hotel {
    hotelCode: number,
    hotelName: string,
    city: string,
    state: string,
    address: string,
    starRating: string,
    images: HotelImage[],
    location: LocationModel,
    rooms: RoomModel[],
    amenities: string[],
    about: string,
    websiteLink: string,
    propertyRule: string,
    email: string,
    latitude: string,
    longitude: string,
    conatctDetails: any[]
}

export interface HotelImage {
    imageId: number,
    image: any,
    imageName: string,
    imageByte: any
}

export interface RoomModel {
    hotelRoomId: number,
    hotelCode: number,
    roomName: string,
    occupancy:number,
    roomDescription: string,
    price: string,
    bedType: string,
    roomImages: HotelImage[],
    priceSlab: RoomPriceSlabModel[],
    status: string,
    amenities: string[],
    roomTags: string[],
    noOfRooms: number,
    roomPackage: string,
    extraBedCostAdult?: string,
    extraBedCostChild?: string
}

export interface RoomTypeModel {
    roomCode: number,
    roomName: string,
    bedType: string
}

export interface RoomPriceSlabModel {
    id:number,
    priceSlabId: PriceSlabModel,
    hotelRoomId: number,
    hotelCode:number
}
