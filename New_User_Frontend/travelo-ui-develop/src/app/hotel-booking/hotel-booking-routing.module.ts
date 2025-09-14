import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HotelBookingComponent } from './hotel-booking/hotel-booking.component';
import { ConfirmationComponent } from './confirmation/confirmation.component';
import { PoliciesComponent } from './policies/policies.component';
import { MyTripsComponent } from './my-trips/my-trips.component';
import { OverviewComponent } from './overview/overview.component';



const routes: Routes = [
 
  {
    path:'', component : HotelBookingComponent
  },
  {
    path:'my-trips', component : MyTripsComponent
  },
  {
    path:'overview', component : OverviewComponent
  },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class HotelBookingRoutingModule { }
