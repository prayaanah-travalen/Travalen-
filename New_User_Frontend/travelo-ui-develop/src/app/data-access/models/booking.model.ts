import { Hotel } from "./hotel.model";
import { LocationModel } from "./location.model";

export interface BookingDetailModel {
    checkIn: string,
    checkOut: string,
    adults: number,
    children: number,
    hotel: Hotel,
  
}

export interface GuestDetails {
    firstName: string,
    lastName: string,
    phone: string,
    email: string

}


export interface Booking {
    bookingId?:number,
    adults: number,
    children: number,
    guestDetails?: GuestDetails,
    bookingFor?: string,
    bookingDetails: BookingDetailModel[],
    priceSlab?: number,
    checkIn: string,
    checkOut: string,
    amount?: string,
    location?:LocationModel[],
    status?: string

}


