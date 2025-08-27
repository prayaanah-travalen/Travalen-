import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DestinationRoutingModule } from './destination-routing.module';
import { DestinationComponent } from './destination.component';
import { DestinationManagementComponent } from './destination-management/destination-management.component';
import { DialogModule } from '@angular/cdk/dialog';
import { FileUploadComponent } from '../shared/file-upload/file-upload.component';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';



@NgModule({
  declarations: [
    DestinationComponent,
    DestinationManagementComponent
  
  ],
  imports: [
    CommonModule,
    DestinationRoutingModule,
    DialogModule,
    FileUploadComponent,
    ReactiveFormsModule,
    FormsModule,
  ]
})
export class DestinationModule { }
