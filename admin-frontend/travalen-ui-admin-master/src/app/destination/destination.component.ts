import { Dialog } from '@angular/cdk/dialog';
import { Component, OnInit } from '@angular/core';
import { DestinationManagementComponent } from './destination-management/destination-management.component';
import { PopularDestinationService } from '../data-access/services/popular-destination.service';
import { PopularDestModel } from '../data-access/model/popular-dest.madel';

@Component({
  selector: 'app-Destination',
  templateUrl: './destination.component.html',
  styleUrls: ['./destination.component.scss']
})
export class DestinationComponent implements OnInit {
  destinations: PopularDestModel[] =[];

  constructor(private dialog: Dialog,
    private destService:PopularDestinationService) { }

  ngOnInit(): void {
    this.getDestination()
  }

  addDestination() {
    this.dialog.open(DestinationManagementComponent, {}).closed.subscribe(resp=>{
      this.getDestination();
    });
  }

  getDestination(){
    this.destService.getPopularDest().subscribe(resp=>{
      this.destinations = resp;
    })

  }

  
  delete(dest:PopularDestModel){
    this.destService.delete(dest).subscribe(resp=>{
      this.getDestination();
    })
   }
}
