import { CommonModule } from '@angular/common';
import { Component, OnInit , HostListener} from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-side-bar',
  templateUrl: './side-bar.component.html',
  styleUrls: ['./side-bar.component.scss'],
  standalone: true,
  imports:[RouterModule, CommonModule]
})
export class SideBarComponent implements OnInit {
 
  // roles: any[] = [];
  // isSuperAdmin: boolean = false;
  // isHotelAdmin: boolean = false;
  // isAdmin: boolean = false;
  // isBookingAdmin: boolean = false;

  
  // isSidebarOpen: boolean = false;

  // constructor() {}

  // ngOnInit(): void {
  //   let jwt = localStorage.getItem("auth");
  //   if (jwt !== null) {
  //     let jwtData = jwt.split('.')[1];
  //     let decodedJwtJsonData = window.atob(jwtData);
  //     let decodedJwtData = JSON.parse(decodedJwtJsonData);
  //     this.roles = decodedJwtData.roles || [];

  //     this.roles.forEach((rl: any) => {
  //       if (rl.authority === 'Super Admin') {
  //         this.isSuperAdmin = true;
  //       }
  //       if (rl.authority === 'Hotel Admin') {
  //         this.isHotelAdmin = true;
  //       }
  //       if (rl.authority === 'Admin') {
  //         this.isAdmin = true;
  //       }
  //       if (rl.authority === 'Booking Admin') {
  //         this.isBookingAdmin = true;
  //       }
  //     });
  //   }
  // }

  
  // toggleSidebar(): void {
  //   this.isSidebarOpen = !this.isSidebarOpen;
  //   document.body.style.overflow = this.isSidebarOpen ? 'hidden' : 'auto';
  // }

  // closeSidebar(): void {
  //   this.isSidebarOpen = false;
  //   document.body.style.overflow = 'auto';
  // }

  // onMobileNavClick(): void {
  //   if (this.isMobileView()) {
  //     this.closeSidebar();
  //   }
  // }

  // private isMobileView(): boolean {
  //   return window.innerWidth <= 768;
  // }

  // @HostListener('window:resize', ['$event'])
  // onResize(event: any): void {
  //   if (event.target.innerWidth > 768 && this.isSidebarOpen) {
  //     this.closeSidebar();
  //   }
  // }

  // @HostListener('document:click', ['$event'])
  // onDocumentClick(event: MouseEvent): void {
  //   if (!this.isMobileView() || !this.isSidebarOpen) {
  //     return;
  //   }

  //   const target = event.target as HTMLElement;
  //   const sidebar = document.querySelector('.sidebar-container');
  //   const toggle = document.querySelector('.mobile-toggle');

  //   if (sidebar &&
  //       !sidebar.contains(target) &&
  //       toggle &&
  //       !toggle.contains(target)) {
  //     this.closeSidebar();
  //   }
  // }

  // @HostListener('document:keydown.escape', ['$event'])
  // onEscapePress(): void {
  //   if (this.isSidebarOpen) {
  //     this.closeSidebar();
  //   }
  // }

  // ngOnDestroy(): void {
  //   document.body.style.overflow = 'auto';
  // }


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

  sidebarOpen = false;

  toggleSidebar() {
    this.sidebarOpen = !this.sidebarOpen;
  }
  
}
