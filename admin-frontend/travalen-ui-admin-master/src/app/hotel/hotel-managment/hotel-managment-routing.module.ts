import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HotelManagmentComponent } from './hotel-managment.component';


const routes: Routes = [
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
export class HotelManagmentRoutingModule { }
