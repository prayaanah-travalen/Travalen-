import { Dialog } from '@angular/cdk/dialog';
import { Component, OnInit, inject } from '@angular/core';
import { AddRoomComponent } from '../add-room/add-room.component';
import { NonNullableFormBuilder } from '@angular/forms';
import { LiveAnnouncer } from '@angular/cdk/a11y';
import { FileModel } from 'src/app/data-access/model/file.model';
import {  HotelReqModel, ImageModel, RoomReqModel } from 'src/app/data-access/model/hotel-req.model';
import { HotelService } from 'src/app/data-access/services/hotel.service';
import { Hotel, HotelImage, RoomModel } from 'src/app/data-access/model/hotel.model';
import { ActivatedRoute } from '@angular/router';
import { FinanceService } from 'src/app/data-access/services/finance.service';
import { FinanceModel } from 'src/app/data-access/model/finance.model';
import { CommonParamModel } from 'src/app/data-access/model/common-param.model';
import { UtilService } from 'src/app/data-access/services/utils.service';

@Component({
  selector: 'app-hotel-managment',
  templateUrl: './hotel-managment.component.html',
  styleUrls: ['./hotel-managment.component.scss']
})
export class HotelManagmentComponent implements OnInit {
  action: string = "ADD";
  hotelAction: string = "ADD";
  removable = true;
	selectable = false;
  announcer = inject(LiveAnnouncer);
  deletedImages: number[] = [];

  hotel!: Hotel;

  savedImages: HotelImage[] = [];
  // hotelImages: File[] = [];
  hotelImages: File[] = [];
  request: HotelReqModel | undefined;

  hotelForm = this.fb.group({
  
    hotelName:[''],
    location:[''],
    address:[''],
    postalCode:[''],
    city:[''],
    state:[''],
    country:[''],
    starRating:[''],
    hotelRooms:[''],
    amenity:[''],
    about:[''],
    email: [''],
    websiteLink: [''],
    propertyRule: [''],
    latitude:[""],
    longitude:[""]
	});

  financeForm = this.fb.group({
    id:[0],
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
    pan:[""]
	});

  
  contactForm = this.fb.group({
    firstName:[0],
    lastName: [""],
    phone: [""],
    whatsapp: [""],
    designation: [""],
    email: [""]
    
	});


  hotelCode : number = 0;

  amenityList: any[] = [];

  roomPackage: CommonParamModel[] = [];
  amenities: CommonParamModel[] =[];

  constructor(private dialog: Dialog,
    private fb: NonNullableFormBuilder,
    private hotelService: HotelService,
    private activatedRoute: ActivatedRoute,
    private financeService: FinanceService,
    private utilService: UtilService) { }

  ngOnInit(): void {

    this.activatedRoute.params.subscribe(params => {
      let id = params['id'];
      if(id) {
        this.hotelCode = id;
        localStorage.setItem("hotelId", id);
      }
    });
    this.getHotelById();

    this.utilService.getRoomPackage().subscribe(resp=>{
      this.roomPackage = resp;
    })

    
    this.utilService.getAmenities().subscribe(resp=>{
      this.amenities = resp;
    })

    this.getFinance();

  }

  getHotelById() {
    let hotelId = 0;
    let cachedSearchInput = localStorage.getItem("hotelId");
    if(cachedSearchInput && cachedSearchInput !== null) hotelId = JSON.parse(cachedSearchInput);
    this.hotelService.getHotelById(hotelId).subscribe(resp=>{
      if(resp !== null) {
        this.hotel = resp;
        localStorage.setItem("hotelId", JSON.stringify(this.hotel?.hotelCode));
        this.setFormFields(this.hotel)
        this.savedImages = this.hotel.images;
        this.hotelAction = 'MODIFY';
        this.disableEnableForm();
      } 

    });
  }

  disableEnableForm () {
    if(this.hotelAction === 'MODIFY') {
      this.hotelForm.disable();
    } else {
      this.hotelForm.enable();
    }
  }

  addAminity(amenity: any) {
    // this.amenityList.push(this.hotelForm.get('amenity')?.getRawValue());
    this.amenityList.push(amenity.description)
    this.hotelForm.patchValue({amenity:''});
  }

  removeAmenity(amenity: string) {
    const index = this.amenityList.indexOf(amenity);

		if (index >= 0) {
			this.amenityList.splice(index, 1);

			this.announcer.announce(`Removed ${amenity}`);
		}
  }

  imageEmitter(event: File[], ) {
   this.hotelImages = event;
  }

  saveHotel() {
    this.validate();
    
    if(!this.hotelForm.valid){
      return false;
    }

    let req : HotelReqModel = {
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
    }

    if(this.hotelCode > 0) {
      req['hotelCode'] = this.hotelCode;
    }
    // this.hotelService.saveHotel(req).subscribe(resp=>{

    // })

    this.hotelService.saveHotelWithFormData(req, this.hotelImages).subscribe(resp=>{
      if(resp.response !== null) {
          this.hotel = resp.response;
          localStorage.setItem("hotelId", JSON.stringify(this.hotel?.hotelCode));
          if(this.hotel) {
            this.setFormFields(this.hotel);
            this.savedImages = this.hotel.images;
          }
      }
    });

    return true;
  }

  validate() {
    if(this.hotelForm.get('hotelName')?.getRawValue() === undefined || this.hotelForm.get('hotelName')?.getRawValue() === null || this.hotelForm.get('hotelName')?.getRawValue() === '') {
      this.hotelForm.controls['hotelName'].setErrors({ 'required': true });
      return false;
    }
    if(this.hotelForm.get('location')?.getRawValue() === undefined || this.hotelForm.get('location')?.getRawValue() === null || this.hotelForm.get('location')?.getRawValue() === '') {
      this.hotelForm.controls['location'].setErrors({ 'required': true });
      return false;
    }

    if(this.hotelForm.get('address')?.getRawValue() === undefined || this.hotelForm.get('address')?.getRawValue() === null || this.hotelForm.get('address')?.getRawValue() === '') {
      this.hotelForm.controls['address'].setErrors({ 'required': true });
      return false;
    }

    if(this.hotelForm.get('email')?.getRawValue() === undefined || this.hotelForm.get('email')?.getRawValue() === null || this.hotelForm.get('email')?.getRawValue() === '') {
      this.hotelForm.controls['email'].setErrors({ 'required': true });
      return false;
    }
    return true;
  }


  deletedImage(event:number) {
    this.deletedImages.push(event);
  }


  setFormFields(hotel: Hotel) {
    this.hotelForm.patchValue({
      hotelName:hotel.hotelName,
      location:hotel.location.location,
      address:hotel.address,
      city:hotel.city,
      state:hotel.state,
      starRating:hotel.starRating,
      about:hotel.about,
      email:hotel.email,
      websiteLink:hotel.websiteLink,
      propertyRule:hotel.propertyRule
    });


      this.contactForm.patchValue({
        whatsapp: hotel.conatctDetails[0].whatsappNumber,
        phone: hotel.conatctDetails[0].contactNumber,
        designation: hotel.conatctDetails[0].designation,
        firstName: hotel.conatctDetails[0].firstName,
        lastName: hotel.conatctDetails[0].lastNamestName,
        email: hotel.conatctDetails[0].email,
      })
   

    this.amenityList = hotel.amenities;

  }

  
  addRoom() {
    this.dialog.open(AddRoomComponent,{
      data: { 
        hotelCode: this.hotel.hotelCode,
        action: "ADD"
      }
    }).closed.subscribe(resp=>{
      this.getHotelById();
    })

  }

  editRoom(room: RoomModel) {
    this.dialog.open(AddRoomComponent,{
      data: { 
        hotelCode: this.hotel.hotelCode,
        room: room,
        action: "MODIFY"
      }
    }).closed.subscribe(resp=>{
      this.getHotelById();
    })
  }

  deleteRoom(roomId: number) {
    let room: RoomReqModel = {
      hotelRoomId: roomId,
    }

    let req= {
      roomDetails: room
    }

    this.hotelService.deleteHotelRoom(req).subscribe(resp=>{
      if(resp.success === "SUCCESS") {
        this.getHotelById();
      }
    });
  }

  saveFinance() {
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
    }

    if(this.financeForm.get('id')?.getRawValue() > 0) {
      req['id'] = this.financeForm.get('id')?.getRawValue();
    }

    this.financeService.saveFinance(req).subscribe(resp=>{

    })
  }

  getFinance() {
    let req = {
      hotelCode : this.hotelCode 
    }

    this.financeService.getFinance(req).subscribe(resp=>{
        if(resp.success === "SUCCESS") {
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
    })
  }
}
