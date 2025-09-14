import { Injectable } from '@angular/core';
import { CommonService } from './common.service';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private commonService: CommonService) { }

  sendOtp(phoneNo: string, fname: string, lname:String) {
    let payload = {
      phoneNo: phoneNo,
      firstName: fname,
      lastName: lname
    }
    return this.commonService.httpPostWithOutToken("auth/sendOtp", payload);
  }

  verifyOtp(req: any) {
   
    return this.commonService.httpPostWithOutToken("auth/verifyOtp", req);
  }
}
