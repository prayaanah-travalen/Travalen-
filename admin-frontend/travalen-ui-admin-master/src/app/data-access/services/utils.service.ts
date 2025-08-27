import { Injectable } from '@angular/core';
import { CommonService } from './common.service';
import { map } from 'rxjs';
import { LocationModel } from '../model/location.model';
import { PriceSlabModel } from '../model/price-slab.model';

@Injectable({
  providedIn: 'root'
})
export class UtilService {

  constructor(private commonService: CommonService) { }

	getLocations() {

		return this.commonService.httpGET("location")
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
		return this.commonService.httpGET("priceSlab")
			.pipe(map(res=>{
				let priceSlabs: PriceSlabModel[] = [];
				res.map( (price:any)=>{
					priceSlabs.push({id:price.id, priceSlab:price.priceSlab, maxAllowedGuest: price.maxAllowedGuest, maxAllowedRoom: price.maxAllowedRoom, noOfNights: price.noOfNights })
				})
				return priceSlabs;
				}
			));
	}

	savePriceSlab(req: any) {
		return this.commonService.httpPOST("priceSlab/save", req);
	
	}

	deletePriceSlab(req: any) {
		return this.commonService.httpPOST("priceSlab/delete", req);
	}

	getRoomPackage(){
		return this.commonService.httpGET("room-package");
	}

	getAmenities(){
		return this.commonService.httpGET("amenities");
	}

	getRoomAmenities() {
		return this.commonService.httpGET("room-amenities");
	}
}
