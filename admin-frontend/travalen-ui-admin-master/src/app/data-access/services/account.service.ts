import { Injectable } from '@angular/core';
import { CommonService } from './common.service';

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  constructor(private commonService: CommonService,) { }

  createAccount(req: any) {
      return this.commonService.httpPostWithOutToken("auth/register",req);
  }

  verifyOtp(req: any) {
    return this.commonService.httpPostWithOutToken("auth/verifyOtp",req);
  }

  signIn(req: any) {
    return this.commonService.httpPostWithOutToken("auth/login",req);
  }

  sendOtp(req: any) {
    return this.commonService.httpPostWithOutToken("auth/sendOtp",req);
  }

  forgotPassOtpVerify(req: any) {
    return this.commonService.httpPostWithOutToken("auth/forgotPass/verifyOtp",req);
  }

  chamgePassword(req: any) {
    return this.commonService.httpPostWithOutToken("auth/changePassword",req);
  }

  getRoles(){
    return this.commonService.httpGET("user/roles");
  }

  createUser(req: any) {
    return this.commonService.httpPOST("user/create",req);
  }

  getExternalUser(){
    return this.commonService.httpGET("user/external");
  }

  getInternalUser(){
    return this.commonService.httpGET("user/internal");
  }

  inactivateUser(req: any) {
    return this.commonService.httpPOST("user/inactivate",req);
  }

  activateUser(req: any) {
    return this.commonService.httpPOST("user/activate",req);
  }
  
  updateUser(req: any) {
    return this.commonService.httpPOST("user/update",req);
  }


 

}
