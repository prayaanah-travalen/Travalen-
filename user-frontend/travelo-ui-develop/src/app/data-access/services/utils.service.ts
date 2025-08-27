import { Injectable } from '@angular/core';
import { CommonService } from './common.service';
import { LocationModel } from '../models/location.model';
import { map } from 'rxjs';
import { PriceSlabModel } from '../models/price-slab.model';

@Injectable({
  providedIn: 'root'
})
export class UtilsService {

  constructor(private commonService: CommonService) { }

	getLocations() {

		return this.commonService.httpGET("util/location")
			.pipe(map(res=>{
				let locations: LocationModel[] = [];
				res.map( (loc:any)=>{
					locations.push({locationId:loc.locationId, location:loc.location })
				})
				return locations;
				}
			));
	}

	getPriceSlab() {
		return this.commonService.httpGET("util/priceSlab")
			.pipe(map(res=>{
				let priceSlabs: PriceSlabModel[] = [];
				res.map( (price:any)=>{
					priceSlabs.push({id:price.id, priceSlab:price.priceSlab, maxAllowedGuest: price.maxAllowedGuest, maxAllowedRoom: price.maxAllowedRoom, noOfNights: price.noOfNights })
				})
				return priceSlabs;
				}
			));
	}

	getPopularDestination() {
		return this.commonService.httpGET("util/popularDest");
	}
}
