import { Dialog, DialogRef } from '@angular/cdk/dialog';
import { Component, OnInit } from '@angular/core';
import { HotelReqModel } from 'src/app/data-access/model/hotel-req.model';
import { Hotel } from 'src/app/data-access/model/hotel.model';
import { HotelService } from 'src/app/data-access/services/hotel.service';

@Component({
  selector: 'app-hotel-list',
  templateUrl: './hotel-list.component.html',
  styleUrls: ['./hotel-list.component.scss']
})
export class HotelListComponent implements OnInit {

  displayedColumns: string[] = ['name', 'location', 'status', 'action', 'delete'];

  hotels: Hotel[] = [];

  constructor(private hotelService:HotelService,
    private dialog: Dialog) { }

  ngOnInit(): void {
   this.getHotels();
  }

  getHotels() {
    this.hotelService.getHotels().subscribe(resp=>{
      this.hotels = resp;
    })
    
  }

  delete(hotelcode: number) {

    this.dialog.open(Confirmation).closed.subscribe(resp=>{
      if(resp === true) {
        let req: HotelReqModel = {
          hotelCode: hotelcode
        }
          this.hotelService.deleteHotel(req).subscribe(resp=> this.getHotels());
      }
    })

    
  }

}


@Component({
  selector: 'app-confirmation',
  template: `<div class="content p-5">
        <div>Please confirm to delete</div>
        <div class="text-center mt-2">
          <a class="cancel pr-10" (click)="cancel()">Cancel</a>
          <a class="error-text" (click)="delete()">Delete</a>
        </div>
    </div>`,
  styles: [`
      .content{
        background:#fff;
      }
      a {
        text-decoration: none;
        cursor: pointer;
      }
      .cancel{
        color: #1EDD8D !important;
      }
  `],
  standalone: true
})
export class Confirmation {

  constructor(private dialogRef: DialogRef){}

  cancel() {
    this.dialogRef.close(false);
  }

  delete() {
    this.dialogRef.close(true);
  }
}
