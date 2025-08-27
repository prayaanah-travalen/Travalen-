import { NgModule } from '@angular/core';
import { CommonModule, NgFor } from '@angular/common';

import { UserManagementRoutingModule } from './user-management-routing.module';
import { UserManagementComponent } from './user-management.component';
import { ExternalUsersComponent } from './external-users/external-users.component';
import { InternalUserComponent } from './internal-user/internal-user.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatSelectModule } from '@angular/material/select';
import { DialogModule } from '@angular/cdk/dialog';
import { MatTableModule } from '@angular/material/table';


@NgModule({
  declarations: [UserManagementComponent, ExternalUsersComponent, InternalUserComponent],
  imports: [
    CommonModule,
    UserManagementRoutingModule,
    MatSelectModule, 
    FormsModule, 
    ReactiveFormsModule, 
    NgFor,
    DialogModule,
    MatTableModule
  ]
})
export class UserManagementModule { }
