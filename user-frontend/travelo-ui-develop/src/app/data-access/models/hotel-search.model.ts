import { LocationModel } from "./location.model";
import { PriceSlabModel } from "./price-slab.model";

export interface HotelSearchModel {
    location: LocationModel[],
    checkIn: string,
    checkOut: string,
    noOfGuests: number,
    priceSlab?: PriceSlabModel[],
    adults: number,
    children:number,
    totalPrice?: number
}
