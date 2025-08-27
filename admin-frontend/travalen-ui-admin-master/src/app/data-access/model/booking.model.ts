import { Hotel, RoomModel } from "./hotel.model";
import { LocationModel } from "./location.model";

export interface BookingModel {
    checkIn: string,
    checkOut: string,
    adults: number,
    children: number,
    hotel: Hotel,
    hotelRoom: RoomModel,
    noOfRooms: number
  
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
    bookingDetails: BookingModel[],
    priceSlab?: number,
    checkIn: string,
    checkOut: string,
    amount?: string,
    location?:LocationModel[],
    status?: string,
    bookingDate?: string,
    payment:PaymentModel[]

}

export interface PaymentModel {
    paymentDate: string,
    paymentId: string
    status: string
    transactionType: string
}


