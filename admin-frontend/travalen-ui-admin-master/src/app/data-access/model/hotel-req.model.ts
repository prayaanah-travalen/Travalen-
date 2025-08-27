import { FileModel } from "./file.model";

export interface HotelReqModel {
    hotelCode?: number,
    hotelName?: string,
    location?: number,
    address?: string,
    postalCode?: string,
    city?: string,
    state?: string,
    country?: string,
    starRating?: string,
    hotelRooms?: RoomReqModel[],
    amenities?: string[],
    about?: string,
    websiteLink?: string,
    propertyRule?: string,
    images?: ImageModel[],
    deletedImages?: number[],
    email?: string,
    latitude?: string,
    longitude?: string
}


export interface RoomReqModel {
    hotelRoomId?: number,
    hotelCode?: number,
    roomName?: string,
    occupancy?: number,
    roomDescription?: string,
    price?: string,
    bedType?: string,
    priceSlab?: number[],
    amenities?: string[],
    roomTags?: string[],
    deletedImages?: number[],
    noOfRooms?: number,
    roomPackage?: string,
    extraBedCostAdult?: string,
    extraBedCostChild?: string
}

export interface ImageModel {
    imageId?: number,
    image?: any,
    imageName?: string
}