import { LocationModel } from "./location.model";

export interface PopularDestDto {
    popularDestId:number,
    location:string,
    image: string,
    url: string,
    description?: string
}