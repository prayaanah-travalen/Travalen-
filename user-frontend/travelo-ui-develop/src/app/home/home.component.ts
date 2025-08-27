
import { LiveAnnouncer } from '@angular/cdk/a11y';
import { Component, OnInit, inject } from '@angular/core';
import { NonNullableFormBuilder } from '@angular/forms';
import { MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { Router } from '@angular/router';
import { Observable, map, startWith } from 'rxjs';
import { UtilsService } from '../data-access/services/utils.service';
import { LocationModel } from '../data-access/models/location.model';
import { HotelSearchModel } from '../data-access/models/hotel-search.model';
import { Dialog } from '@angular/cdk/dialog';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
}
)
export class HomeComponent implements OnInit {
	removable = true;
	selectable = false;
	tillDateDisable = new Date();
	maxDateEnable = new Date();
	isNoOfGuestOpen: boolean = false;

	searchInput: HotelSearchModel | undefined;

	openLink($event: MouseEvent) {
		throw new Error('Method not implemented.');
	}
	openBottomSheet() {
		throw new Error('Method not implemented.');
	}

	locations: LocationModel[]=[];
	filteredLocations: Observable<LocationModel[]>;
	selectedLocation:LocationModel[] = [];
	announcer = inject(LiveAnnouncer);

	searchForm = this.fb.group({
		location: [""],
		checkIn: [""],
		checkOut: [""],
		children:[0],
		adults:[0]
	});

	constructor(private router: Router,
		private fb: NonNullableFormBuilder,
		private utils: UtilsService,
		private dialog: Dialog
	) {
		this.getLocations();

		this.filteredLocations = this.searchForm.controls['location'].valueChanges.pipe(
			startWith(''),
			map(value => this._filter(value || '')),
		  );

		
	}

	ngOnInit(): void {
		this.tillDateDisable.setDate(this.tillDateDisable.getDate() + 1);
		this.maxDateEnable.setDate(this.maxDateEnable.getDate() + 365);

		let cachedSearchInput = localStorage.getItem("hotelSearch");
		if(cachedSearchInput && cachedSearchInput !== null) this.searchInput = JSON.parse(cachedSearchInput);

		 
		if(this.searchInput) {
			this.selectedLocation = this.searchInput ? this.searchInput.location : [];
			// this.selectedLocationNames = this.searchInput ?this.searchInput.location.map(loc=>loc.location).toString() : '';
			this.searchForm.patchValue({
			  checkIn: this.searchInput.checkIn,
			  checkOut: this.searchInput.checkOut,
			  adults: this.searchInput.adults,
			  children: this.searchInput.children
			});
		  }
		
	}

	filterLocations() {
		
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

	search() {
		if(this.selectedLocation.length === 0) {
			this.searchForm.controls['location'].setErrors({ 'required': null })
		}

		if(this.searchForm.get('checkIn')?.getRawValue() === '') {
			this.searchForm.controls['checkIn'].setErrors({ 'required': null })
		}

		if(this.searchForm.get('checkOut')?.getRawValue() === '') {
			this.searchForm.controls['checkOut'].setErrors({ 'required': null })
		}

		if(this.searchForm.get('adults')?.getRawValue() <= 0 && this.searchForm.get('children')?.getRawValue() <= 0) {
			this.searchForm.controls['adults'].setErrors({ 'required': null })
		}

		if(this.searchForm.valid) {
			let hotelSearch: HotelSearchModel = {
				location: this.selectedLocation,
				checkIn: this.searchForm.get('checkIn')?.getRawValue(),
				checkOut:  this.searchForm.get('checkOut')?.getRawValue(),
				noOfGuests: this.searchForm.get('adults')?.getRawValue() + this.searchForm.get('children')?.getRawValue(),
				adults: this.searchForm.get('adults')?.getRawValue(),
				children: this.searchForm.get('children')?.getRawValue()
			}
			this.router.navigateByUrl("/hotels", { 
				state: {searchInput: hotelSearch}
			  });
		}
	

	}


	dateEmitter(event: any, type:string) {
		if(type === 'checkin') {
			this.searchForm.patchValue({checkIn: event})
		} else {
			this.searchForm.patchValue({checkOut: event})
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
		this.searchForm.patchValue({location:''})
	}

	remove(location: LocationModel): void {
		const index = this.selectedLocation.indexOf(location);

		if (index >= 0) {
			this.selectedLocation.splice(index, 1);

			this.announcer.announce(`Removed ${location}`);
		}
	}


	openNoOfGuest() {
		this.isNoOfGuestOpen = true;
	}


	continue() {
		this.isNoOfGuestOpen = false;
		
	}

	// remove(location: string): void {
	//   const index = this.fruits.indexOf(fruit);

	//   if (index >= 0) {
	//     this.fruits.splice(index, 1);
	//   }
	// }

}
