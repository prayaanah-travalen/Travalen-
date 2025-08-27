import { Component, OnInit } from '@angular/core';
import { PrivacyPolicyComponent } from '../privacy-policy/privacy-policy.component';
import { Dialog } from '@angular/cdk/dialog';
import { MatDialog } from '@angular/material/dialog';
import { TermsandconditionsComponent } from '../termsandconditions/termsandconditions.component';
import { RouterModule } from '@angular/router';
import { RefundPolicyComponent } from '../refund-policy/refund-policy.component';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss'],
  standalone:true,
  imports:[PrivacyPolicyComponent, TermsandconditionsComponent, RouterModule, RefundPolicyComponent]
})
export class FooterComponent implements OnInit {

  constructor(private dialog: Dialog) { }

  ngOnInit(): void {
  }

  privacy() {
    this.dialog.open(PrivacyPolicyComponent,{
      
    })
  }

  terms() {
    this.dialog.open(TermsandconditionsComponent,{
      
    })
  }

  refund(){
    this.dialog.open(RefundPolicyComponent,{
      
    })
  }

}
