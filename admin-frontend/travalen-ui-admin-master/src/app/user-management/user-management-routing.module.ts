import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserManagementComponent } from './user-management.component';
import { InternalUserComponent } from './internal-user/internal-user.component';
import { ExternalUsersComponent } from './external-users/external-users.component';

const routes: Routes = [{path:'internalUser', component:InternalUserComponent},
{path:'externalUser', component:ExternalUsersComponent}];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserManagementRoutingModule { }
