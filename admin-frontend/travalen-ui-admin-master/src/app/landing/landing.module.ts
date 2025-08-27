import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LandingComponent } from './landing/landing.component';
import { LandingRoutingModule } from './landing-routing.module';
import { CreateAccountComponent } from './create-account/create-account.component';
import { DialogModule } from '@angular/cdk/dialog';
import { SignInComponent } from './sign-in/sign-in.component';
import { ForgetPasswordComponent } from './forget-password/forget-password.component';
import { NgOtpInputModule } from 'ng-otp-input';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';


@NgModule({
  declarations: [
    LandingComponent,
    CreateAccountComponent,
    SignInComponent,
    ForgetPasswordComponent
  ],
  imports: [
    CommonModule,
    LandingRoutingModule,
    DialogModule,
    NgOtpInputModule,
    ReactiveFormsModule,
    FormsModule,
  ]
})
export class LandingModule { }
