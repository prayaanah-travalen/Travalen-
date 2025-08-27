import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PriceSlabRoutingModule } from './price-slab-routing.module';
import { PriceSlabComponent } from './price-slab.component';
import { MatTableModule } from '@angular/material/table';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { PriceSlabManagementComponent } from './price-slab-management/price-slab-management.component';
import { DialogModule } from '@angular/cdk/dialog';


@NgModule({
  declarations: [PriceSlabComponent, PriceSlabManagementComponent],
  imports: [
    CommonModule,
    PriceSlabRoutingModule,
    MatTableModule,
    ReactiveFormsModule,
    FormsModule,
    DialogModule
  ]
})
export class PriceSlabModule { }
