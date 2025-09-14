import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LandingRoutingModule } from './landing-routing.module';
import { LoginComponent } from './login/login.component';
import { VerificationComponent } from './verification/verification.component';
import { DialogModule } from '@angular/cdk/dialog';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgOtpInputModule } from 'ng-otp-input';


@NgModule({
  declarations: [
    LoginComponent,
    VerificationComponent
  ],
  imports: [
    CommonModule,
    LandingRoutingModule,
    DialogModule,
    ReactiveFormsModule,
    FormsModule,
    NgOtpInputModule
  ]
})
export class LandingModule { }
