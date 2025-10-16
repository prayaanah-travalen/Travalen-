import { NgModule } from '@angular/core';
import { AsyncPipe, CommonModule, NgFor } from '@angular/common';
import { HotelManagmentRoutingModule } from './hotel-managment-routing.module';
import { DialogModule } from '@angular/cdk/dialog';
import { HotelManagmentComponent } from './hotel-managment.component';
import {MatTabsModule} from '@angular/material/tabs';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FileUploadComponent } from '../../shared/file-upload/file-upload.component';
import {MatSelectModule} from '@angular/material/select';
import {MatTableModule} from '@angular/material/table';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatPaginatorModule } from '@angular/material/paginator';
import { DragDropModule } from '@angular/cdk/drag-drop';


@NgModule({
  declarations: [
    HotelManagmentComponent
  ],
  imports: [
    CommonModule,
    HotelManagmentRoutingModule,
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
    MatSnackBarModule,
    MatPaginatorModule,
    DragDropModule
  ]
})
export class HotelManagmentModule { }
