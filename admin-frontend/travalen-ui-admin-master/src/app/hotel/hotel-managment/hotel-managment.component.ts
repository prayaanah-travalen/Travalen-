import { Dialog } from '@angular/cdk/dialog';
import { Component, OnInit, inject } from '@angular/core';
import { AddRoomComponent } from '../add-room/add-room.component';
import { NonNullableFormBuilder, Validators } from '@angular/forms';
import { LiveAnnouncer } from '@angular/cdk/a11y';
import { FileModel } from 'src/app/data-access/model/file.model';
import { HotelReqModel, ImageModel, RoomReqModel } from 'src/app/data-access/model/hotel-req.model';
import { HotelService } from 'src/app/data-access/services/hotel.service';
import { Hotel, HotelImage, RoomModel } from 'src/app/data-access/model/hotel.model';
import { ActivatedRoute } from '@angular/router';
import { FinanceService } from 'src/app/data-access/services/finance.service';
import { FinanceModel } from 'src/app/data-access/model/finance.model';
import { CommonParamModel } from 'src/app/data-access/model/common-param.model';
import { UtilService } from 'src/app/data-access/services/utils.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UserStateService } from 'src/app/data-access/services/user-state.service';
import { CdkDragDrop, moveItemInArray } from '@angular/cdk/drag-drop';

@Component({
  selector: 'app-hotel-managment',
  templateUrl: './hotel-managment.component.html',
  styleUrls: ['./hotel-managment.component.scss']
})
export class HotelManagmentComponent implements OnInit {

  isSaving: boolean = false;

  action: string = "ADD";
  hotelAction: string = "ADD";
  removable = true;
  selectable = false;
  announcer = inject(LiveAnnouncer);
  deletedImages: number[] = [];

  hotel!: Hotel;

  savedImages: HotelImage[] = [];
  hotelImages: File[] = [];
  request: HotelReqModel | undefined;

  hotelForm = this.fb.group({
    hotelName: ['', [Validators.required]],
    location: ['', [Validators.required]],
    address: ['', [Validators.required]],
    postalCode: [''],
    city: [''],
    state: [''],
    country: [''],
    starRating: [''],
    hotelRooms: [''],
    amenity: [''],
    about: [''],
    email: ['', [Validators.required, Validators.email]],
    websiteLink: [''], 
    propertyRule: [''],
    latitude: [""],
    longitude: [""]
  });

  financeForm = this.fb.group({
    id: [0],
    country: [""],
    bankName: [""],
    swiftCode: [""],
    ifsc: [""],
    accountNumber: [""],
    accountHolderName: [""],
    registeredForGst: [""],
    tradeName: [""],
    gstIn: [""],
    hotelCode: [""],
    pan: [""]
  });

  contactForm = this.fb.group({
    firstName: [""], // Fixed: was [0] instead of [""]
    lastName: [""],
    phone: [""],
    whatsapp: [""],
    designation: [""],
    email: [""]
  });

  hotelCode: number = 0;
  amenityList: any[] = [];
  roomPackage: CommonParamModel[] = [];
  amenities: any[] = [];
  showOtherInput: boolean = false;
  otherAmenity: string = '';


  isLoadingHotel = false;
  // Getter to check if hotel is loaded and has a hotel code
  get isHotelLoaded(): boolean {
    return !!(this.hotel?.hotelCode);
  }

  constructor(
    private dialog: Dialog,
    private fb: NonNullableFormBuilder,
    private hotelService: HotelService,
    private activatedRoute: ActivatedRoute,
    private financeService: FinanceService,
    private utilService: UtilService,
    private snackBar: MatSnackBar,
    private userStateService: UserStateService
  ) { }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(params => {
      let id = params['id'];
      if (id) {
        this.hotelCode = id;
        localStorage.setItem("hotelId", id);
      }
    });
    
    this.getHotelById();
    this.loadUtilityData();
    this.getFinance();
  }

  private loadUtilityData(): void {
    this.utilService.getRoomPackage().subscribe(resp => {
      this.roomPackage = resp;
    });

    this.utilService.getAmenities().subscribe(resp => {
      this.amenities = resp;
    });
  }

  enableEditMode(): void {
    this.hotelAction = 'EDIT';
    this.hotelForm.enable();
    // Clear any previous validation errors
    Object.keys(this.hotelForm.controls).forEach(key => {
      this.hotelForm.get(key)?.setErrors(null);
    });
  }

  cancelEdit(): void {
    this.hotelAction = 'MODIFY';
    this.setFormFields(this.hotel);
    this.hotelForm.disable();
    this.deletedImages = [];
    this.hotelImages = [];
  }


  
  updateHotel(): void {
    if (!this.validate() || !this.hotelForm.valid) {
      this.markFormGroupTouched(this.hotelForm);
      return;
    }
  
    this.isSaving = true;
    
    let req: HotelReqModel = {
      hotelName: this.hotelForm.get('hotelName')?.getRawValue(),
      location: this.hotelForm.get('location')?.getRawValue(),
      address: this.hotelForm.get('address')?.getRawValue(),
      websiteLink: this.hotelForm.get('websiteLink')?.getRawValue(),
      amenities: this.amenityList,
      about: this.hotelForm.get('about')?.getRawValue(),
      propertyRule: this.hotelForm.get('propertyRule')?.getRawValue(),
      deletedImages: this.deletedImages,
      email: this.hotelForm.get('email')?.getRawValue(),
      latitude: this.hotelForm.get('latitude')?.getRawValue(),
      longitude: this.hotelForm.get('longitude')?.getRawValue(),
      // FIX: Always include hotelCode when updating
      hotelCode: this.hotelCode 
    };
    
    // Rest of the method remains the same...
    this.hotelService.saveHotelWithFormData(req, this.hotelImages).subscribe({
      next: (resp) => {
        this.isSaving = false;
        if (resp.response) {
          this.hotel = resp.response;
          // Update hotelCode from response if needed
          if (resp.response.hotelCode) {
            this.hotelCode = resp.response.hotelCode;
            localStorage.setItem("hotelId", JSON.stringify(this.hotelCode));
          }
          
          this.setFormFields(this.hotel);
          this.userStateService.updateHotelName(this.hotel.hotelName || '');
          
          if (resp.response.images) {
            this.savedImages = [...resp.response.images];
          }
  
          this.hotelImages = [];
          this.deletedImages = [];
  
          this.snackBar.open('Hotel updated successfully!', 'Close', { duration: 3000 });
          
          // Switch to MODIFY mode and disable form
          this.hotelAction = 'MODIFY';
          this.hotelForm.disable();
        }
      },
      error: (err) => {
        this.isSaving = false;
        console.error('Error updating hotel:', err);
        this.snackBar.open('Error updating hotel!', 'Close', { duration: 3000 });
      }
    });
  }
 


  getHotelById() {
    let hotelId = 0;
    let cachedSearchInput = localStorage.getItem("hotelId");
    if (cachedSearchInput && cachedSearchInput !== null) {
      hotelId = JSON.parse(cachedSearchInput);
    }
    
    if (hotelId === 0) {
      // New hotel case
      this.hotelAction = 'ADD';
      this.hotelForm.enable();
      return;
    }
  
    this.isLoadingHotel = true;
    this.hotelService.getHotelById(hotelId).subscribe({
      next: (resp) => {
        if (resp !== null) {
          this.hotel = resp;
          // FIX: Add this line to properly set the hotelCode property
          this.hotelCode = this.hotel.hotelCode; 
          localStorage.setItem("hotelId", JSON.stringify(this.hotel?.hotelCode));

          this.userStateService.updateHotelName(this.hotel.hotelName || '');

          this.setFormFields(this.hotel);
          this.savedImages = this.hotel.images;
          this.hotelAction = 'MODIFY';
          this.disableEnableForm();
        }
        this.isLoadingHotel = false;
      },
      error: (err) => {
        console.error('Error loading hotel details:', err);
        this.isLoadingHotel = false;
        this.snackBar.open('Error loading hotel details', 'Close', { duration: 3000 });
      }
    });
  }
  
  disableEnableForm() {
    if (this.hotelAction === 'MODIFY') {
      this.hotelForm.disable();
    } else {
      this.hotelForm.enable();
    }
  }

  addAminity(amenity: any) {
    if (amenity === 'OTHER') {
    this.showOtherInput = true;
  } else {
    this.showOtherInput = false;
    if (amenity && amenity.description && !this.amenityList.includes(amenity.description)) {
      this.amenityList.push(amenity.description);
      this.hotelForm.patchValue({ amenity: '' });
    } }
  }

  removeAmenity(amenity: string) {
    const index = this.amenityList.indexOf(amenity);
    if (index >= 0) {
      this.amenityList.splice(index, 1);
      this.announcer.announce(`Removed ${amenity}`);
    }
  }

  addOtherAmenity() {
  if (!this.otherAmenity?.trim()) return;
  console.log("Amenity added:", this.otherAmenity);
  const newAmenity = { amenity: this.otherAmenity };

  this.hotelService.addAmenity(newAmenity).subscribe({
    next: (savedAmenity: any) => {
      console.log("Amenity added:", savedAmenity);

      this.amenities.push(savedAmenity);
      this.amenityList.push(savedAmenity.description);

      this.hotelForm.patchValue({ amenity: '' });
      this.otherAmenity = '';
      this.showOtherInput = false;
    },
    error: (err: any) => {
      console.error('Failed to add amenity', err);
    }
  });
}




  imageEmitter(event: File[]) {
    this.hotelImages = event;
  }

  saveHotel() {
    console.log('=== SAVE HOTEL DEBUG INFO ===');
    console.log('Form valid:', this.hotelForm.valid);
    console.log('Form errors:', this.hotelForm.errors);
    console.log('Form values:', this.hotelForm.getRawValue());
    console.log('Amenity list:', this.amenityList);
    console.log('Hotel images count:', this.hotelImages?.length || 0);
    console.log('Hotel code:', this.hotelCode);
  
    if (!this.validate() || !this.hotelForm.valid) {
      console.log('VALIDATION FAILED');
      console.log('Hotel Name valid:', this.hotelForm.get('hotelName')?.valid);
      console.log('Location valid:', this.hotelForm.get('location')?.valid);
      console.log('Address valid:', this.hotelForm.get('address')?.valid);
      console.log('Email valid:', this.hotelForm.get('email')?.valid);
      this.markFormGroupTouched(this.hotelForm);
      return;
    }
  
    console.log('Validation passed, proceeding with save...');
    this.isSaving = true;
  
    let req: HotelReqModel = {
      hotelName: this.hotelForm.get('hotelName')?.getRawValue(),
      location: this.hotelForm.get('location')?.getRawValue(),
      address: this.hotelForm.get('address')?.getRawValue(),
      websiteLink: this.hotelForm.get('websiteLink')?.getRawValue(),
      amenities: this.amenityList,
      about: this.hotelForm.get('about')?.getRawValue(),
      propertyRule: this.hotelForm.get('propertyRule')?.getRawValue(),
      deletedImages: this.deletedImages,
      email: this.hotelForm.get('email')?.getRawValue(),
      latitude: this.hotelForm.get('latitude')?.getRawValue(),
      longitude: this.hotelForm.get('longitude')?.getRawValue()
    };
  
    if (this.hotelCode && this.hotelCode > 0) {
      req.hotelCode = this.hotelCode;
    }
  
    console.log('Saving hotel request:', req);
    console.log('Hotel images count:', this.hotelImages?.length || 0);
  
    this.hotelService.saveHotelWithFormData(req, this.hotelImages).subscribe({
      next: (resp) => {
        this.isSaving = false;
        console.log('Save response:', resp);
  
        if (resp && resp.response) {
          this.hotel = resp.response;
  
          if (!this.hotelCode || this.hotelCode === 0) {
            this.hotelCode = this.hotel.hotelCode;
            localStorage.setItem("hotelId", JSON.stringify(this.hotel?.hotelCode));
          }
          
          this.userStateService.updateHotelName(this.hotel.hotelName || '');

          this.setFormFields(this.hotel);
          this.savedImages = this.hotel.images;
          this.hotelImages = [];
          this.deletedImages = [];
          this.snackBar.open('Hotel saved successfully!', 'Close', { duration: 3000 });
          this.hotelAction = 'MODIFY';
          this.hotelForm.disable();
        } else {
          const errorMessage = resp?.errorMessage || 'Error saving hotel';
          console.error('Save failed:', errorMessage);
          this.snackBar.open(errorMessage, 'Close', { duration: 5000 });
        }
      },
      error: (err) => {
        this.isSaving = false;
        console.error('Error saving hotel:', err);
  
        let errorMessage = 'Error saving hotel!';
        if (err.error && err.error.errorMessage) {
          errorMessage = err.error.errorMessage;
        } else if (err.message) {
          errorMessage = err.message;
        } else if (err.status) {
          errorMessage = `Server returned ${err.status}: ${err.statusText}`;
        }
  
        this.snackBar.open(errorMessage, 'Close', { duration: 5000 });
      }
    });
  }
  
  
  


  validate(): boolean {
    const hotelName = this.hotelForm.get('hotelName')?.getRawValue();
    const location = this.hotelForm.get('location')?.getRawValue();
    const address = this.hotelForm.get('address')?.getRawValue();
    const email = this.hotelForm.get('email')?.getRawValue();

    let isValid = true;

    if (!hotelName || hotelName.trim() === '') {
      this.hotelForm.controls['hotelName'].setErrors({ 'required': true });
      isValid = false;
    }

    if (!location || location.trim() === '') {
      this.hotelForm.controls['location'].setErrors({ 'required': true });
      isValid = false;
    }

    if (!address || address.trim() === '') {
      this.hotelForm.controls['address'].setErrors({ 'required': true });
      isValid = false;
    }

    if (!email || email.trim() === '') {
      this.hotelForm.controls['email'].setErrors({ 'required': true });
      isValid = false;
    }

    return isValid;
  }

  private markFormGroupTouched(formGroup: any) {
    Object.keys(formGroup.controls).forEach(key => {
      const control = formGroup.get(key);
      control?.markAsTouched();
      if (control?.controls) {
        this.markFormGroupTouched(control);
      }
    });
  }

  deletedImage(event: number) {
    this.deletedImages.push(event);
  }

  setFormFields(hotel: Hotel) {
    this.hotelForm.patchValue({
      hotelName: hotel.hotelName,
      location: hotel.location?.location,
      address: hotel.address,
      city: hotel.city,
      state: hotel.state,
      starRating: hotel.starRating,
      about: hotel.about,
      email: hotel.email,
      websiteLink: hotel.websiteLink || '',
      propertyRule: hotel.propertyRule,
      latitude: hotel.latitude, 
      longitude: hotel.longitude 
    });

    if (hotel.conatctDetails && hotel.conatctDetails.length > 0) {
      this.contactForm.patchValue({
        whatsapp: hotel.conatctDetails[0].whatsappNumber,
        phone: hotel.conatctDetails[0].contactNumber,
        designation: hotel.conatctDetails[0].designation,
        firstName: hotel.conatctDetails[0].firstName,
        // lastName: hotel.conatctDetails[0].lastNamestName,
        lastName: hotel.conatctDetails[0].lastName,
        email: hotel.conatctDetails[0].email,
      });
    }

    // Fixed: Properly set amenities
    this.amenityList = hotel.amenities ? [...hotel.amenities] : [];
  }

  addRoom() {
    this.dialog.open(AddRoomComponent, {
      data: {
        hotelCode: this.hotel.hotelCode,
        action: "ADD"
      }
    }).closed.subscribe(resp => {
      this.getHotelById();
    });
  }

  editRoom(room: RoomModel) {
    this.dialog.open(AddRoomComponent, {
      data: {
        hotelCode: this.hotel.hotelCode,
        room: room,
        action: "MODIFY"
      }
    }).closed.subscribe(resp => {
      this.getHotelById();
    });
  }

  deleteRoom(roomId: number) {
    let room: RoomReqModel = {
      hotelRoomId: roomId,
    };

    let req = {
      roomDetails: room
    };

    this.hotelService.deleteHotelRoom(req).subscribe(resp => {
      if (resp.success === "SUCCESS") {
        this.getHotelById();
      }
    });
  }

  saveFinance() {
    if (!this.hotel?.hotelCode) return;
    
    let req: FinanceModel = {
      country: this.financeForm.get('country')?.getRawValue(),
      bankName: this.financeForm.get('bankName')?.getRawValue(),
      swiftCode: this.financeForm.get('swiftCode')?.getRawValue(),
      ifsc: this.financeForm.get('ifsc')?.getRawValue(),
      accountNumber: this.financeForm.get('accountNumber')?.getRawValue(),
      accountHolderName: this.financeForm.get('accountHolderName')?.getRawValue(),
      registeredForGst: this.financeForm.get('registeredForGst')?.getRawValue(),
      tradeName: this.financeForm.get('tradeName')?.getRawValue(),
      gstIn: this.financeForm.get('gstIn')?.getRawValue(),
      hotelCode: this.hotel.hotelCode,
      pan: this.financeForm.get('pan')?.getRawValue(),
    };

    if (this.financeForm.get('id')?.getRawValue() > 0) {
      req['id'] = this.financeForm.get('id')?.getRawValue();
    }

    this.financeService.saveFinance(req).subscribe(resp => {
      if (resp.success === "SUCCESS") {
        this.snackBar.open('Finance details saved successfully!', 'Close', { duration: 3000 });
      }
    });
  }

  getFinance() {
    let req = {
      hotelCode: this.hotelCode
    };

    this.financeService.getFinance(req).subscribe(resp => {
      if (resp.success === "SUCCESS") {
        this.financeForm.patchValue({
          id: resp.response.id,
          country: resp.response.country,
          bankName: resp.response.bankName,
          swiftCode: resp.response.swiftCode,
          ifsc: resp.response.ifsc,
          accountNumber: resp.response.accountNumber,
          accountHolderName: resp.response.accountHolderName,
          registeredForGst: resp.response.registeredForGst,
          tradeName: resp.response.tradeName,
          gstIn: resp.response.gstIn,
          hotelCode: resp.response.hotelCode,
          pan: resp.response.pan
        });
      }
    });
  }


  // Add this method to HotelManagmentComponent.ts
saveContact(): void {
  if (!this.hotel?.hotelCode) {
    this.snackBar.open('Please save hotel details first', 'Close', { duration: 3000 });
    return;
  }

  if (this.contactForm.invalid) {
    this.markFormGroupTouched(this.contactForm);
    this.snackBar.open('Please fill all required fields', 'Close', { duration: 3000 });
    return;
  }

  this.isSaving = true;

  const contactData = {
    hotelCode: this.hotel.hotelCode,
    firstName: this.contactForm.get('firstName')?.getRawValue(),
    lastName: this.contactForm.get('lastName')?.getRawValue(),
    phone: this.contactForm.get('phone')?.getRawValue(),
    whatsapp: this.contactForm.get('whatsapp')?.getRawValue(),
    email: this.contactForm.get('email')?.getRawValue(),
    designation: this.contactForm.get('designation')?.getRawValue()
  };

  this.hotelService.saveContactPerson(contactData).subscribe({
    next: (resp) => {
      this.isSaving = false;
      if (resp.success === "SUCCESS") {
        this.snackBar.open('Contact person saved successfully!', 'Close', { duration: 3000 });
        // Update the hotel object with the new contact details
        if (resp.response) {
          if (!this.hotel.conatctDetails) {
            this.hotel.conatctDetails = [];
          }
          
          // Check if contact already exists
          const existingIndex = this.hotel.conatctDetails.findIndex(
            contact => contact.email === resp.response.email
          );
          
          if (existingIndex >= 0) {
            this.hotel.conatctDetails[existingIndex] = resp.response;
          } else {
            this.hotel.conatctDetails.push(resp.response);
          }
        }
      } else {
        this.snackBar.open(resp.errorMessage || 'Error saving contact person', 'Close', { duration: 3000 });
      }
    },
    error: (err) => {
      this.isSaving = false;
      console.error('Error saving contact person:', err);
      this.snackBar.open('Error saving contact person', 'Close', { duration: 3000 });
    }
  });
}
}

