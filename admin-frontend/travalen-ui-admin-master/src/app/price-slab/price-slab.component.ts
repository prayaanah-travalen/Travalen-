import { Component, OnInit } from '@angular/core';
import { NonNullableFormBuilder } from '@angular/forms';
import { UtilService } from '../data-access/services/utils.service';
import { PriceSlabModel } from '../data-access/model/price-slab.model';
import { Dialog, DialogRef } from '@angular/cdk/dialog';
import { ToastrService } from 'ngx-toastr';
import { PriceSlabManagementComponent } from './price-slab-management/price-slab-management.component';

@Component({
  selector: 'app-price-slab',
  templateUrl: './price-slab.component.html',
  styleUrls: ['./price-slab.component.scss']
})
export class PriceSlabComponent implements OnInit {

  priceForm = this.fb.group({
    price:[""],
    guests:[0],
    rooms:[0],
    nights: [0]
	});

  priceSlabList: PriceSlabModel[] = [];

  displayedColumns: string[] = ['price','maxGuests', 'maxRooms', 'nights','action'];


  constructor(private fb: NonNullableFormBuilder,
    private utilService: UtilService,
    private dialog: Dialog){}

  ngOnInit(): void {
    this.getPriceSlab();
  }


  save() {
      if(this.priceForm.get('price')?.getRawValue() === '') {
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
      priceSlab: this.priceForm.get('price')?.getRawValue(),
      maxAllowedGuest: this.priceForm.get('guests')?.getRawValue(),
      maxAllowedRoom: this.priceForm.get('rooms')?.getRawValue(),
      noOfNights: this.priceForm.get('nights')?.getRawValue(),
    }

    this.utilService.savePriceSlab(req).subscribe(resp=>{
      if(resp.success === 'SUCCESS') {
        this.getPriceSlab();
      }
    })
    return true;
  }


  getPriceSlab() {
    this.utilService.getPriceSlab().subscribe(resp=>{
      this.priceSlabList = resp;
    })
  }

  delete(price: PriceSlabModel) {
    this.utilService.deletePriceSlab(price).subscribe(resp=>{
      this.getPriceSlab();
    })
  }

  add() {
    this.dialog.open(PriceSlabManagementComponent,{}).closed.subscribe(resp=>this.getPriceSlab());
  }

  edit(priceSlab: PriceSlabModel) {
    this.dialog.open(PriceSlabManagementComponent,{
      data: {
        priceSlab: priceSlab
      }
    }).closed.subscribe(resp=>this.getPriceSlab());
  }
}

