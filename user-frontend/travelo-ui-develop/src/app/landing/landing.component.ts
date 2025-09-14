import { Dialog } from '@angular/cdk/dialog';
import { Component, OnInit , HostListener, OnDestroy} from '@angular/core';
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

  adminUrl: string = environment.adminUrl;

  isScrolled = false;
  isMobileMenuOpen = false;
  activeSection = 'about';

  constructor(private dialog: Dialog,
    private router: Router) { }

  ngOnInit(): void {
    this.updateActiveSection();
  }

  login() {
    if(this.isLoginRequired() === false) {
      this.router.navigateByUrl("/home");
    } else {
      this.dialog.open(LoginComponent,{ 
        width: '395px',
        
      
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



  @HostListener('window:scroll', ['$event'])
  onWindowScroll(): void {
    // Handle navbar scroll effect
    this.isScrolled = window.pageYOffset > 50;
    
    // Update active section based on scroll position
    this.updateActiveSection();
  }

  @HostListener('window:resize', ['$event'])
  onWindowResize(): void {
    // Close mobile menu on resize to desktop
    if (window.innerWidth > 768) {
      this.isMobileMenuOpen = false;
    }
  }

  toggleMobileMenu(): void {
    this.isMobileMenuOpen = !this.isMobileMenuOpen;
  }

  scrollToSection(sectionId: string, event: Event): void {
    event.preventDefault();
    
    // Close mobile menu when clicking on a link
    this.isMobileMenuOpen = false;
    
    // Handle brand logo click to scroll to top
    if (sectionId === 'top') {
      window.scrollTo({
        top: 0,
        behavior: 'smooth'
      });
      return;
    }

    const element = document.getElementById(sectionId);
    if (element) {
      const offsetTop = element.offsetTop - 100; // Account for fixed navbar height
      window.scrollTo({
        top: offsetTop,
        behavior: 'smooth'
      });
    }
  }

  private updateActiveSection(): void {
    const sections = ['about', 'top-destinations', 'services', 'list-hotel'];
    const scrollPosition = window.pageYOffset + 150;

    for (let i = sections.length - 1; i >= 0; i--) {
      const element = document.getElementById(sections[i]);
      if (element && element.offsetTop <= scrollPosition) {
        this.activeSection = sections[i];
        break;
      }
    }

    // Default to first section if at top
    if (window.pageYOffset < 100) {
      this.activeSection = 'about';
    }
  }
}
