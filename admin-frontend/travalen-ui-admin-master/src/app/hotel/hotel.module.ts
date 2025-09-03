import { NgModule } from '@angular/core';
import { AsyncPipe, CommonModule, NgFor } from '@angular/common';
import { HotelEditRoutingModule } from './hotel-routing.module';
import { AddRoomComponent } from './add-room/add-room.component';
import { DialogModule } from '@angular/cdk/dialog';
import { HotelManagmentComponent } from './hotel-managment/hotel-managment.component';
import {MatTabsModule} from '@angular/material/tabs';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FileUploadComponent } from '../shared/file-upload/file-upload.component';
import {MatSelectModule} from '@angular/material/select';
import { HotelListComponent } from './hotel-list/hotel-list.component';
import {MatTableModule} from '@angular/material/table';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBarModule } from '@angular/material/snack-bar';


@NgModule({
  declarations: [
    AddRoomComponent,
    HotelManagmentComponent,
    HotelListComponent
  ],
  imports: [
    CommonModule,
    HotelEditRoutingModule,
    DialogModule,
    MatTabsModule,
    ReactiveFormsModule,
    FormsModule,
    MatChipsModule,
    MatIconModule,
    AsyncPipe,
    MatFormFieldModule,
    NgFor,
    FileUploadComponent,
    MatSelectModule,
    MatTableModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ]
})
export class HotelEditModule { }
