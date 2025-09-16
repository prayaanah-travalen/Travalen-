import { Dialog } from '@angular/cdk/dialog';
import { Component, OnInit, HostListener } from '@angular/core';
import { LoginComponent } from './login/login.component';
import { environment } from 'src/environments/environment.prod';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';

@Component({
  selector: 'app-landing',
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.scss']
})
export class LandingComponent implements OnInit {

  // adminUrl: string = environment.adminUrl;

  // constructor(private dialog: Dialog,
  //   private router: Router) { }

  // ngOnInit(): void {
  // }

  // login() {
  //   if(this.isLoginRequired() === false) {
  //     this.router.navigateByUrl("/home");
  //   } else {
  //     this.dialog.open(LoginComponent,{ 
  //       width: '395px',
        
      
  //     });
  //   }
   
  // }

  // openAdmin() {
  //   window.open(this.adminUrl,"_blank")
  // }

  // isLoginRequired(){
  //   let jwt = localStorage.getItem("auth");
  //   if(jwt !== null) {
  //     let jwtData = jwt.split('.')[1]
  //     let decodedJwtJsonData = window.atob(jwtData)
  //     let decodedJwtData = JSON.parse(decodedJwtJsonData)

  //     if(Date.now() < decodedJwtData.exp * 1000) {
  //       return false;
  //     }
  //   }
  //   return true;
  // }

  adminUrl: string = environment.adminUrl;

  // For navbar scrolling effect
  isScrolled: boolean = false;

  // For mobile menu toggle
  isMobileMenuOpen: boolean = false;

  // To track which section is active
  activeSection: string = '';

  constructor(private dialog: Dialog,
              private router: Router) { }

  ngOnInit(): void {
    // Optional: check the current scroll position initially
    this.onScroll();
  }

  // Detect scroll for navbar styling
  @HostListener('window:scroll', [])
  onScroll() {
    this.isScrolled = window.scrollY > 50; // Adjust threshold as needed
    this.updateActiveSection();
  }

  // Toggle mobile menu
  toggleMobileMenu() {
    this.isMobileMenuOpen = !this.isMobileMenuOpen;
  }

  // Scroll smoothly to a section
  scrollToSection(sectionId: string, event: Event) {
    event.preventDefault();
    const element = document.getElementById(sectionId);
    if (element) {
      element.scrollIntoView({ behavior: 'smooth', block: 'start' });
      this.isMobileMenuOpen = false; // Close mobile menu after click
    }
  }

  // Highlight active section based on scroll
  updateActiveSection() {
    const sections = ['about', 'top-destinations', 'services', 'list-hotel'];
    for (let sec of sections) {
      const el = document.getElementById(sec);
      if (el) {
        const rect = el.getBoundingClientRect();
        if (rect.top <= 80 && rect.bottom >= 80) {
          this.activeSection = sec;
          break;
        }
      }
    }
  }

  login() {
    if(this.isLoginRequired() === false) {
      this.router.navigateByUrl("/home");
    } else {
      this.dialog.open(LoginComponent,{ 
        width: '395px'
      });
    }
  }

  openAdmin() {
    window.open(this.adminUrl,"_blank")
  }

  isLoginRequired(){
    let jwt = localStorage.getItem("auth");
    if(jwt !== null) {
      let jwtData = jwt.split('.')[1]
      let decodedJwtJsonData = window.atob(jwtData)
      let decodedJwtData = JSON.parse(decodedJwtJsonData)

      if(Date.now() < decodedJwtData.exp * 1000) {
        return false;
      }
    }
    return true;
  }

}
