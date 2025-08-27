import { Component, OnInit, inject } from '@angular/core'
import { Router } from '@angular/router';
import { HotelService } from '../data-access/services/hotel.service';
import { Hotel } from '../data-access/models/hotel.model';
import { HotelSearchModel } from '../data-access/models/hotel-search.model';
import { UtilsService } from '../data-access/services/utils.service';
import { PriceSlabModel } from '../data-access/models/price-slab.model';
import { MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { startWith, map, Observable } from 'rxjs';
import { LocationModel } from '../data-access/models/location.model';
import { LiveAnnouncer } from '@angular/cdk/a11y';
import { NonNullableFormBuilder } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { Booking } from '../data-access/models/booking.model';

@Component({
  selector: 'app-search-page',
  templateUrl: './search-page.component.html',
  styleUrls: ['./search-page.component.scss']
})
export class SearchPageComponent implements OnInit {

  hotelList: Hotel[] = [];
  searchInput: HotelSearchModel | undefined;
  selectedLocationNames: string = '';
  priceSlabs:PriceSlabModel[] = [];
  selectedPriceSlab: PriceSlabModel[] = [];
  selectedPriceSlabIds: number[] = [];

  seletedHotels: Hotel[] = [];

  locations: LocationModel[]=[];
	filteredLocations: Observable<LocationModel[]>;
	selectedLocation:LocationModel[] = [];
	announcer = inject(LiveAnnouncer);
  searchForm = this.fb.group({
		location: [""],
		checkIn: [""],
		checkOut: [""],
    adults:[0],
    children:[0]
	});

  removable = true;
	selectable = false;
	tillDateDisable = new Date();
	maxDateEnable = new Date();

  bookingReq!: Booking; 
  showReservation: boolean = false;

  isNoOfGuestOpen:boolean = false;
  isCustomPriceOpen: boolean = false;

  priceSlab1: number = 0;
  priceSlab2: number = 0;
  checkinDateError: string = '';

  constructor(private router: Router, 
    private hotelSrvice: HotelService,
    private utils: UtilsService,
    private fb: NonNullableFormBuilder,
    private toastr: ToastrService) { 

    this.getLocations();

		this.filteredLocations = this.searchForm.controls['location'].valueChanges.pipe(
			startWith(''),
			map(value => this._filter(value || '')),
		  );

    }

  ngOnInit(): void {
    this.tillDateDisable.setDate(this.tillDateDisable.getDate() + 1);
		this.maxDateEnable.setDate(this.maxDateEnable.getDate() + 365);

  	
    this.getPriceSlabs();
   
    let cachedSearchInput = localStorage.getItem("hotelSearch");
    if(cachedSearchInput && cachedSearchInput !== null) this.searchInput = JSON.parse(cachedSearchInput);

    if(window.history.state.searchInput){
      // this.searchInput =  window.history.state.searchInput;
      // localStorage.setItem("hotelSearch", JSON.stringify(this.searchInput));

      let incomingSearch: HotelSearchModel =  window.history.state.searchInput;
      if(this.searchInput) {
        this.searchInput['adults'] = incomingSearch.adults;
        this.searchInput['children'] = incomingSearch.children;
        this.searchInput['checkIn'] = incomingSearch.checkIn;
        this.searchInput['checkOut'] = incomingSearch.checkOut;
        this.searchInput['location'] = incomingSearch.location;

      } else {
        this.searchInput = incomingSearch;
      }

      localStorage.setItem("hotelSearch", JSON.stringify(this.searchInput));
    } 
    // else {
    //   let cachedSearchInput = localStorage.getItem("hotelSearch");
    //   if(cachedSearchInput && cachedSearchInput !== null) this.searchInput = JSON.parse(cachedSearchInput);
    // }
    
    if(this.searchInput) {
      this.selectedLocation = this.searchInput ? this.searchInput.location : [];
      this.selectedLocationNames = this.searchInput ?this.searchInput.location.map(loc=>loc.location).toString() : '';
      this.selectedPriceSlab = this.searchInput.priceSlab ? this.searchInput.priceSlab : [];
      this.selectedPriceSlabIds = this.searchInput.priceSlab ? this.searchInput.priceSlab?.map(ps=>ps.id) : [];
      this.searchForm.patchValue({
        checkIn: this.searchInput.checkIn,
        checkOut: this.searchInput.checkOut,
        adults: this.searchInput.adults,
        children: this.searchInput.children
      });
    }
   
    this.fetchBookingRequest();
    this.getHotels();
  }

  getPriceSlabs() {
      this.utils.getPriceSlab().subscribe(resp=>{
        this.priceSlabs = resp
      });
  }

  showHotelDetails(hotelCode: number) {
    if(this.selectedPriceSlab.length) {
      this.router.navigateByUrl("/hotel-details", { 
        state: {hotelCode: hotelCode}
        });
    } else {
        this.toastr.error("Please select budget");
    }
   
  }

  selectPrice(event:PriceSlabModel) {
    const index = this.selectedPriceSlabIds.indexOf(event.id);
    
    if(index < 0) {
      this.selectedPriceSlab.push(event);
      this.selectedPriceSlabIds.push(event.id);
    } else {
      this.selectedPriceSlab.splice(index, 1);
      this.selectedPriceSlabIds.splice(index, 1);
    }
    if(this.searchInput) {
      this.searchInput['priceSlab'] = this.selectedPriceSlab;
      localStorage.setItem("hotelSearch",JSON.stringify(this.searchInput));
      this.getHotels();
    }
 
  
  }


  getHotels() {
    let  locationsToSearch: LocationModel[] = [...this.selectedLocation];


    if(this.bookingReq) {
      console.log(this.bookingReq)
      locationsToSearch = locationsToSearch.filter((element, index) => {
        let ind = this.bookingReq && this.bookingReq.bookingDetails.findIndex(e=>          e.hotel.location.locationId !== element.locationId)
        console.log(ind)
        console.log(element)
        return ind >= 0

        });
      }

      locationsToSearch.sort((a, b) => a.location.localeCompare(b.location));
      locationsToSearch = locationsToSearch.filter((element, index) => {
        return index == 0
          
      });
  
  
    let req = {
      location: locationsToSearch.length ? locationsToSearch : this.selectedLocation,
      checkIn: this.searchForm.get('checkIn')?.getRawValue(),
      checkOut: this.searchForm.get('checkOut')?.getRawValue(),
      noOfGuests: this.searchForm.get('adults')?.getRawValue() + this.searchForm.get('children')?.getRawValue() ,
      priceSlab: this.selectedPriceSlab,
      adults: this.searchForm.get('adults')?.getRawValue(),
      children: this.searchForm.get('children')?.getRawValue()
    }

    this.hotelSrvice.getHotels(req)
      .subscribe(resp=>{
        this.hotelList = resp;
    })

  
  }

  getLocations() {
		this.utils.getLocations().subscribe(resp=>{
			this.locations = resp;
		});
		if(this.locations.length){
			this.filteredLocations = this.searchForm.controls['location'].valueChanges.pipe(
				startWith(''),
				map(value => this._filter(value || '')),
			  );
		}
	}

  private _filter(value: string): LocationModel[] {
		const filterValue = this._normalizeValue(value);
		return this.locations.filter(loc => this._normalizeValue(loc.location).includes(filterValue));
	}

	private _normalizeValue(value: string): string {
		return value.toString().toLowerCase();
	}

	onSelectionChange(event:MatAutocompleteSelectedEvent) {
		const index = this.selectedLocation.indexOf(event.option.value);

		if (index < 0) {
			this.selectedLocation.push(event.option.value);
		}
		this.searchForm.patchValue({location:''});
    this.search();
	}

	remove(location: LocationModel): void {
		const index = this.selectedLocation.indexOf(location);

		if (index >= 0) {
			this.selectedLocation.splice(index, 1);

			this.announcer.announce(`Removed ${location}`);
		}
    this.search();
	}

  removeHotels(hotel: Hotel): void {
		// const index = this.seletedHotels.indexOf(hotel);

		// if (index >= 0) {
		// 	this.seletedHotels.splice(index, 1);
    //   this.bookingReq['bookingDetails'] = this.bookingReq.bookingDetails.filter(bk=>bk.hotel.hotelCode !== hotel.hotelCode);

		// 	this.announcer.announce(`Removed ${hotel}`);
		// }
    this.seletedHotels = this.seletedHotels.filter(htl=>htl.hotelCode !== hotel.hotelCode);
    this.bookingReq['bookingDetails'] = this.bookingReq.bookingDetails.filter(bk=>bk.hotel.hotelCode !== hotel.hotelCode);
    localStorage.setItem("booking",JSON.stringify(this.bookingReq))
    if(this.bookingReq.bookingDetails.length === 0) {
      localStorage.removeItem("booking");
      this.fetchBookingRequest();
    }
    this.getHotels()
	}

  
	dateEmitter(event: any, type:string) {
		if(type === 'checkin') {
			this.searchForm.patchValue({checkIn: event})
		} else {
			this.searchForm.patchValue({checkOut: event})
		}
			
    this.search();
	}

  
  fetchBookingRequest() {
    let cachedBooking = localStorage.getItem("booking");
    if(cachedBooking && cachedBooking !== null) {
      this.bookingReq = JSON.parse(cachedBooking);
      this.seletedHotels = this.bookingReq.bookingDetails.map(bk=>bk.hotel);
      this.showReservation = true;
    } else {
      this.showReservation = false;
    }
  }

  continue() {
		this.isNoOfGuestOpen = false;
		
	}

  search() {
    this.checkinDateError = '';
    if(this.selectedLocation.length === 0) {
			this.searchForm.controls['location'].setErrors({ 'required': null })
		}

		if(this.searchForm.get('checkIn')?.getRawValue() === '') {
			this.searchForm.controls['checkIn'].setErrors({ 'required': null });
      this.checkinDateError = "The check-in date is required."
		}

		if(this.searchForm.get('checkOut')?.getRawValue() === '') {
			this.searchForm.controls['checkOut'].setErrors({ 'required': null })
		}

		if(this.searchForm.get('adults')?.getRawValue() <= 0 && this.searchForm.get('children')?.getRawValue() <= 0) {
			this.searchForm.controls['adults'].setErrors({ 'required': null })
		}

    var checkout = new Date(this.searchForm.get('checkOut')?.getRawValue().replace( /(\d{2})-(\d{2})-(\d{4})/, "$2/$1/$3"))
    var checkin = new Date(this.searchForm.get('checkIn')?.getRawValue().replace( /(\d{2})-(\d{2})-(\d{4})/, "$2/$1/$3"))

    if (checkout <= checkin) {
      this.searchForm.controls['checkIn'].setErrors({ 'required': null })
      this.checkinDateError = "The check-in date should be before the checkout date."
    }

    if(this.searchForm.valid) {
      this.getHotels();
    } 
  
  }

  openNoOfGuest() {
		this.isNoOfGuestOpen = true;
	}

  
	triggerCustomPrice() {
		this.isCustomPriceOpen = !this.isCustomPriceOpen ;
	}

  continueCustomPrice(){
   let priceSlab = this.priceSlabs.filter(price=>price.priceSlab === this.priceSlab1 || price.priceSlab === this.priceSlab2);

   if(priceSlab.length) {
    this.selectedPriceSlab = priceSlab;
    if(this.searchInput) {
      this.searchInput['priceSlab'] = this.selectedPriceSlab;
      localStorage.setItem("hotelSearch",JSON.stringify(this.searchInput));
      this.getHotels();
    }
   }
   
    this.isCustomPriceOpen = !this.isCustomPriceOpen ;
  }


}
