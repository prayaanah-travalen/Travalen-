import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PriceSlabComponent } from './price-slab.component';

const routes: Routes = [{path:'', component: PriceSlabComponent}];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PriceSlabRoutingModule { }
