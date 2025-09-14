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
  amenities: CommonParamModel[] = [];

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
    private snackBar: MatSnackBar
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

  // updateHotel(): void {
  //   if (!this.validate() || !this.hotelForm.valid) {
  //     this.markFormGroupTouched(this.hotelForm);
  //     return;
  //   }

  //   this.isSaving = true;

  //   let req: HotelReqModel = {
  //     hotelName: this.hotelForm.get('hotelName')?.getRawValue(),
  //     location: this.hotelForm.get('location')?.getRawValue(),
  //     address: this.hotelForm.get('address')?.getRawValue(),
  //     websiteLink: this.hotelForm.get('websiteLink')?.getRawValue(), // Fixed: Now properly included
  //     amenities: this.amenityList, // Fixed: Now properly included
  //     about: this.hotelForm.get('about')?.getRawValue(),
  //     propertyRule: this.hotelForm.get('propertyRule')?.getRawValue(),
  //     deletedImages: this.deletedImages,
  //     email: this.hotelForm.get('email')?.getRawValue(),
  //     latitude: this.hotelForm.get('latitude')?.getRawValue(), // Fixed: Now properly included
  //     longitude: this.hotelForm.get('longitude')?.getRawValue(), // Fixed: Now properly included
  //     hotelCode: this.hotelCode
  //   };

  //   this.hotelService.saveHotelWithFormData(req, this.hotelImages).subscribe({
  //     next: (resp) => {
  //       this.isSaving = false;
  //       if (resp.response) {
  //         this.hotel = resp.response;
  //         this.setFormFields(this.hotel); 

  //         this.savedImages = this.hotel.images;
  //         this.hotelImages = [];
  //         this.deletedImages = [];
  //         this.snackBar.open('Hotel updated successfully!', 'Close', { duration: 3000 });
  //         this.hotelAction = 'MODIFY';
  //         this.hotelForm.disable();
  //       }
  //     },
  //     error: (err) => {
  //       this.isSaving = false;
  //       console.error('Error updating hotel:', err);
  //       this.snackBar.open('Error updating hotel!', 'Close', { duration: 3000 });
  //     }
  //   });
  // }

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
      hotelCode: this.hotelCode
    };
  
    
    this.hotelService.saveHotelWithFormData(req, this.hotelImages).subscribe({
      next: (resp) => {
        this.isSaving = false;
        if (resp.response) {
          this.hotel = resp.response;
  
          
          this.setFormFields(this.hotel);
  
          
          if (resp.response.images) {
            this.savedImages = [...resp.response.images];
          }
  
          this.hotelImages = [];
          this.deletedImages = [];
  
          // Notify user
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

    this.hotelService.getHotelById(hotelId).subscribe(resp => {
      if (resp !== null) {
        this.hotel = resp;
        localStorage.setItem("hotelId", JSON.stringify(this.hotel?.hotelCode));
        this.setFormFields(this.hotel);
        this.savedImages = this.hotel.images;
        this.hotelAction = 'MODIFY';
        this.disableEnableForm();
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
    if (amenity && amenity.description && !this.amenityList.includes(amenity.description)) {
      this.amenityList.push(amenity.description);
      this.hotelForm.patchValue({ amenity: '' });
    }
  }

  removeAmenity(amenity: string) {
    const index = this.amenityList.indexOf(amenity);
    if (index >= 0) {
      this.amenityList.splice(index, 1);
      this.announcer.announce(`Removed ${amenity}`);
    }
  }

  imageEmitter(event: File[]) {
    this.hotelImages = event;
  }

  saveHotel() {
    if (!this.validate() || !this.hotelForm.valid) {
      this.markFormGroupTouched(this.hotelForm);
      return;
    }

    this.isSaving = true;

    let req: HotelReqModel = {
      hotelName: this.hotelForm.get('hotelName')?.getRawValue(),
      location: this.hotelForm.get('location')?.getRawValue(),
      address: this.hotelForm.get('address')?.getRawValue(),
      websiteLink: this.hotelForm.get('websiteLink')?.getRawValue(), // Fixed: Now properly included
      amenities: this.amenityList, // Fixed: Now properly included
      about: this.hotelForm.get('about')?.getRawValue(),
      propertyRule: this.hotelForm.get('propertyRule')?.getRawValue(),
      deletedImages: this.deletedImages,
      email: this.hotelForm.get('email')?.getRawValue(),
      latitude: this.hotelForm.get('latitude')?.getRawValue(), // Fixed: Now properly included
      longitude: this.hotelForm.get('longitude')?.getRawValue() // Fixed: Now properly included
    };

    if (this.hotelCode > 0) {
      req['hotelCode'] = this.hotelCode;
    }

    this.hotelService.saveHotelWithFormData(req, this.hotelImages).subscribe({
      next: (resp) => {
        this.isSaving = false;
        if (resp.response) {
          this.hotel = resp.response;
          localStorage.setItem("hotelId", JSON.stringify(this.hotel?.hotelCode));
          this.setFormFields(this.hotel);
          this.savedImages = this.hotel.images;
          this.hotelImages = [];
          this.deletedImages = [];
          this.snackBar.open('Hotel saved successfully!', 'Close', { duration: 3000 });
          this.hotelAction = 'MODIFY';
          this.hotelForm.disable();
        }
      },
      error: (err) => {
        this.isSaving = false;
        console.error('Error saving hotel:', err);
        this.snackBar.open('Error saving hotel!', 'Close', { duration: 3000 });
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
        lastName: hotel.conatctDetails[0].lastNamestName,
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
}

