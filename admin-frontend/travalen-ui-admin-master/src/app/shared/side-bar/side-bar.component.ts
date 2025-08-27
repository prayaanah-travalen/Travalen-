import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-side-bar',
  templateUrl: './side-bar.component.html',
  styleUrls: ['./side-bar.component.scss'],
  standalone: true,
  imports:[RouterModule, CommonModule]
})
export class SideBarComponent implements OnInit {
  roles: any[]  = [];
  isSuperAdmin: boolean = false;
  isHotelAdmin: boolean = false;
  isAdmin: boolean = false;
  isBookingAdmin: boolean = false;

  constructor() { }

  ngOnInit(): void {

    let jwt = localStorage.getItem("auth");
    if(jwt !== null) {
      let jwtData = jwt.split('.')[1]
      let decodedJwtJsonData = window.atob(jwtData)
      let decodedJwtData = JSON.parse(decodedJwtJsonData)
      this.roles = decodedJwtData.roles;
      this.roles.forEach(rl=>{
        if(rl.authority ==='Super Admin') {
          this.isSuperAdmin = true;
        }

        if(rl.authority ==='Hotel Admin') {
          this.isHotelAdmin = true;
        }

        if(rl.authority ==='Admin') {
          this.isAdmin = true;
        }

        if(rl.authority ==='Booking Admin') {
          this.isBookingAdmin = true;
        }

      })
      // if(this.roles.includes('Super Admin')) {
      //     this.isSuperAdmin = true;
      // }

      // if(this.roles.includes('Hotel Admin')) {
      //   this.isHotelAdmin = true;
      // }

      // if(this.roles.includes('Admin')) {
      //   this.isAdmin = true;
      // }

      // if(this.roles.includes('Booking Admin')) {
      //   this.isBookingAdmin = true;
      // }

    }
    
  }



}
