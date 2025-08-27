import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HotelManagmentComponent } from './hotel-managment/hotel-managment.component';
import { HotelListComponent } from './hotel-list/hotel-list.component';


const routes: Routes = [
  {
    path:'', component : HotelListComponent
  },
  {
    path:'hotel/:id', component : HotelManagmentComponent
  },
  {
    path:'hotel', component : HotelManagmentComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class HotelEditRoutingModule { }
