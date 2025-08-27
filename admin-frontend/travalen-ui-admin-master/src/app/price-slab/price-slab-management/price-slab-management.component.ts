import { DIALOG_DATA, DialogRef } from '@angular/cdk/dialog';
import { Component, Inject, OnInit } from '@angular/core';
import { NonNullableFormBuilder } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { Booking } from 'src/app/data-access/model/booking.model';
import { PriceSlabModel } from 'src/app/data-access/model/price-slab.model';
import { UtilService } from 'src/app/data-access/services/utils.service';

@Component({
  selector: 'app-price-slab-management',
  templateUrl: './price-slab-management.component.html',
  styleUrls: ['./price-slab-management.component.scss']
})
export class PriceSlabManagementComponent implements OnInit {
  priceForm = this.fb.group({
    id:[0],
    price:[0],
    guests:[0],
    rooms:[0],
    nights: [0]
	});

  priceSlabList: PriceSlabModel[] = [];

  constructor(private fb: NonNullableFormBuilder,
    private utilService: UtilService,
    private dialogRef: DialogRef,
    private toaster: ToastrService,
    @Inject(DIALOG_DATA) public data: { priceSlab:PriceSlabModel}
    ){}
  
  ngOnInit(): void {
    if(this.data && this.data.priceSlab) {
      this.priceForm.patchValue({
        id:this.data.priceSlab.id,
        price:this.data.priceSlab.priceSlab,
        guests:this.data.priceSlab.maxAllowedGuest,
        rooms:this.data.priceSlab.maxAllowedRoom,
        nights: this.data.priceSlab.noOfNights
      })
    }
  }


  save() {
      if(this.priceForm.get('price')?.getRawValue() <= 0) {
          this.priceForm.controls['price'].setErrors({'required': true});
      }
      if(this.priceForm.get('guests')?.getRawValue() <= 0) {
        this.priceForm.controls['guests'].setErrors({'required': true});
      }
      if(this.priceForm.get('rooms')?.getRawValue() <= 0) {
        this.priceForm.controls['rooms'].setErrors({'required': true});
      }

      if(this.priceForm.get('nights')?.getRawValue() <= 0) {
        this.priceForm.controls['nights'].setErrors({'required': true});
      }

      if(!this.priceForm.valid){
        return false;
      }

      let req = {
        id:  this.priceForm.get('id')?.getRawValue() > 0 ? this.priceForm.get('id')?.getRawValue() : null,
        priceSlab: this.priceForm.get('price')?.getRawValue(),
        maxAllowedGuest: this.priceForm.get('guests')?.getRawValue(),
        maxAllowedRoom: this.priceForm.get('rooms')?.getRawValue(),
        noOfNights: this.priceForm.get('nights')?.getRawValue(),
      }

      this.utilService.savePriceSlab(req).subscribe(resp=>{
        if(resp.success === 'SUCCESS') {
          this.dialogRef.close();
        } else {
            this.toaster.error(resp.errorMessage)
        }
      })
      return true;
  }

  close() {
    this.dialogRef.close();
  }
}
