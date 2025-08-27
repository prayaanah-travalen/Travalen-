import { DialogRef } from '@angular/cdk/dialog';
import { Component, OnInit } from '@angular/core';
import { NonNullableFormBuilder } from '@angular/forms';
import { PopularDestModel } from 'src/app/data-access/model/popular-dest.madel';
import { PopularDestinationService } from 'src/app/data-access/services/popular-destination.service';

@Component({
  selector: 'app-destination-management',
  templateUrl: './destination-management.component.html',
  styleUrls: ['./destination-management.component.scss']
})
export class DestinationManagementComponent implements OnInit {



  savedImages: any[] = [];
  destImages: File[] = [];

  destForm = this.fb.group({
    destinationName: [""],
    location: [""],
    description: [''],
  });

constructor(private fb: NonNullableFormBuilder,
  private popularDestService: PopularDestinationService,
  private dialogRef: DialogRef) { }

  ngOnInit(): void {
  }
   
  
  imageEmitter(event: File[]) {
    this.destImages = event;
   }

   addDest() {
      let req: any={
        location: this.destForm.get('location')?.getRawValue(),
        name: this.destForm.get('destinationName')?.getRawValue(),
        description: this.destForm.get('description')?.getRawValue()
      }

      this.popularDestService.saveDestination(req, this.destImages).subscribe(resp=>{
        this.dialogRef.close();
      });
   }

   close(){
    this.dialogRef.close();
   }

}
