import { LiveAnnouncer } from '@angular/cdk/a11y';
import { DIALOG_DATA, DialogRef } from '@angular/cdk/dialog';
import { Component, Inject, OnInit, inject } from '@angular/core';
import { NonNullableFormBuilder, Validators } from '@angular/forms';
import { CommonParamModel } from 'src/app/data-access/model/common-param.model';
import { RoomReqModel } from 'src/app/data-access/model/hotel-req.model';
import { Hotel, HotelImage, RoomModel } from 'src/app/data-access/model/hotel.model';
import { PriceSlabModel } from 'src/app/data-access/model/price-slab.model';
import { HotelService } from 'src/app/data-access/services/hotel.service';
import { UtilService } from 'src/app/data-access/services/utils.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-add-room',
  templateUrl: './add-room.component.html',
  styleUrls: ['./add-room.component.scss']
})
export class AddRoomComponent implements OnInit {
  amenityList: string[] = [];
  announcer = inject(LiveAnnouncer);
  removable = true;
	selectable = false;
  roomTagList: string[] = [];
  priceSlabList: PriceSlabModel[] = [];
  deletedImages: number[] = [];

  hotel!: Hotel;

  savedImages: HotelImage[] = [];
  // roomImages: File[] = [];
  roomImages: File[] = [];

  roomPackage: CommonParamModel[] = [];

  roomForm = this.fb.group({
        roomCode: [0],
        occupancy: [0],
        roomDescription: [''],
        price: [''],
        bedType: [''],
        priceSlab: [0],
        roomName: [''],
        amenity:[''],
        roomTag: [''],
        noOfRooms:[0],
        package:['', [Validators.required]],
        extraBedCostAdult: [''],
        extraBedCostChild: ['']
	});

  submitted: boolean = false;

  amenities: CommonParamModel[] =[];

  constructor( private fb: NonNullableFormBuilder,
    @Inject(DIALOG_DATA) public data: { hotelCode: number, room: RoomModel, action:string},
    private utilService: UtilService,
    private hotelService: HotelService,
    private dialog: DialogRef,
    private snackBar: MatSnackBar 
    ) { }

  ngOnInit(): void {
    this.getPriceSlab();
    if(this.data.action === "MODIFY") {
        this.setFormField(this.data.room);
    }

    this.utilService.getRoomPackage().subscribe(resp=>{
      this.roomPackage = resp;
    })

    this.utilService.getRoomAmenities().subscribe(resp=>{
      this.amenities = resp;
    })
  }

  setFormField(room: RoomModel) {
    if(room) {
      this.roomForm.patchValue({
        roomCode: room.hotelRoomId,
          occupancy: room.occupancy,
          roomDescription: room.roomDescription,
          price: room.price,
          bedType: room.bedType,
          priceSlab: room.priceSlab[0].priceSlabId.id,
          roomName: room.roomName,
          noOfRooms: room.noOfRooms,
          package: room.roomPackage,
          extraBedCostAdult: room.extraBedCostAdult,
          extraBedCostChild: room.extraBedCostChild
        
      })
      this.amenityList = room.amenities;
      this.roomTagList = room.roomTags;
      this.savedImages= room.roomImages;
    }


  }

  getPriceSlab() {
    this.utilService.getPriceSlab().subscribe(resp=>{
      this.priceSlabList = resp;
    })
  }

  addAminity(amenity: any) {
    // this.amenityList.push(this.roomForm.get('amenity')?.getRawValue());
    this.amenityList.push(amenity.description)
    this.roomForm.patchValue({amenity:''});
  }

  removeAmenity(amenity: string) {
    const index = this.amenityList.indexOf(amenity);

		if (index >= 0) {
			this.amenityList.splice(index, 1);

			this.announcer.announce(`Removed ${amenity}`);
		}
  }

  addRoomTag() {
    this.roomTagList.push(this.roomForm.get('roomTag')?.getRawValue());
    this.roomForm.patchValue({roomTag:''});
  }

  removeRoomTag(roomTag: string) {
    const index = this.roomTagList.indexOf(roomTag);

		if (index >= 0) {
			this.roomTagList.splice(index, 1);

			this.announcer.announce(`Removed ${roomTag}`);
		}
  }

  saveRoom() {
    this.validate();
    if(!this.roomForm.valid){
      this.submitted = true;

    //  toaster message for form validation failure
    this.snackBar.open('Please fill in all required fields.', 'Dismiss', {
      duration: 3000,
      panelClass: ['error-snackbar']
    });


      return false;
    }
   

    let priceSlabs: number[] = [];
    priceSlabs.push(this.roomForm.get('priceSlab')?.getRawValue());
 
    let room: RoomReqModel = {
      hotelRoomId: this.data.action === "MODIFY" ?  this.roomForm.get('roomCode')?.getRawValue() : null,
      hotelCode: this.data.hotelCode,
      roomName: this.roomForm.get('roomName')?.getRawValue(),
      roomDescription: this.roomForm.get('roomDescription')?.getRawValue(),
      price:  this.roomForm.get('price')?.getRawValue(),
      bedType:  this.roomForm.get('bedType')?.getRawValue(),
      priceSlab: priceSlabs,
      amenities: this.amenityList,
      roomTags: this.roomTagList,
      deletedImages: this.deletedImages,
      noOfRooms: this.roomForm.get('noOfRooms')?.getRawValue(),
      roomPackage: this.roomForm.get('package')?.getRawValue(),
      extraBedCostAdult: this.roomForm.get('extraBedCostAdult')?.getRawValue(),
      extraBedCostChild: this.roomForm.get('extraBedCostChild')?.getRawValue(),
    };

   
    let req= {
      hotelCode: this.data.hotelCode,
      roomDetails: room
    }


  //   this.hotelService.saveRoom(req, this.roomImages).subscribe(resp=>{
  //     if(resp.response !== null && resp.success === "SUCCESS") {
  //       this.dialog.close();
  //    }
  //   })
  //   this.submitted = false;
  //   return true;
  // }

  
  this.hotelService.saveRoom(req, this.roomImages).subscribe({
    next: (resp) => {
      if (resp.response !== null && resp.success === "SUCCESS") {
        // Success Toaster Message
        this.snackBar.open('Room saved successfully!', 'Dismiss', {
          duration: 3000,
          panelClass: ['success-snackbar']
        });
        this.dialog.close();
      } else {
        // Failure Toaster Message based on API response
        this.snackBar.open('Failed to save room. Please try again.', 'Dismiss', {
          duration: 3000,
          panelClass: ['error-snackbar']
        });
      }
    },
    error: (err) => {
      // Error Toaster Message for network or server errors
      console.error('API Error:', err);
      this.snackBar.open('An unexpected error occurred. Please try again later.', 'Dismiss', {
        duration: 3000,
        panelClass: ['error-snackbar']
      });
    }
  });

  this.submitted = false;
  return true;
}




  validate() {
    if(this.roomForm.get('priceSlab')?.getRawValue() <= 0) {
      this.roomForm.controls['priceSlab'].setErrors({ 'required': null });
      return false;
    }

    if(this.roomForm.get('noOfRooms')?.getRawValue() <= 0) {
      this.roomForm.controls['noOfRooms'].setErrors({ 'required': null });
      return false;
    }


    return true;
  }





  
  deletedImage(event:number) {
    this.deletedImages.push(event);
  }

  
  imageEmitter(event: File[], ) {
    this.roomImages = event;
   }

  close(){
      this.dialog.close();
  }

}
