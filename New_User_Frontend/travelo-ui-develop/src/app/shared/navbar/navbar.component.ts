import { Dialog } from '@angular/cdk/dialog';
import { Component, OnInit } from '@angular/core';
import {MatMenuModule} from '@angular/material/menu';
import { RouterModule } from '@angular/router';
import { PrivacyPolicyComponent } from '../privacy-policy/privacy-policy.component';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
  standalone:true,
  imports:[MatMenuModule,RouterModule],
  
})
export class NavbarComponent implements OnInit {
  collapsed = true;
 
  constructor(private dialog: Dialog) { }

  ngOnInit(): void {
  }

  toggleCollapsed(): void {
    this.collapsed = !this.collapsed;
  }

  logout() {
    localStorage.clear();
    window.location.reload();

  }

  privacy() {
    this.dialog.open(PrivacyPolicyComponent,{
      
    })
  }

}
