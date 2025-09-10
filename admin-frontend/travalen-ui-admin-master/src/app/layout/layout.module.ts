import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LayoutComponent } from './layout/layout.component';
import { SideBarComponent } from '../shared/side-bar/side-bar.component';
import { HeaderComponent } from '../shared/header/header.component';
import { LayoutRoutingModule } from './layout-routing.module';



@NgModule({
  declarations: [
    LayoutComponent,
    
  ],
  imports: [
    CommonModule,
    HeaderComponent,
    SideBarComponent,
    LayoutRoutingModule
  ]
})
export class LayoutModule { }
