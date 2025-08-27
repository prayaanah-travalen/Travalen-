import { Injectable } from '@angular/core';
import {
    CanActivate,
    Router,
} from '@angular/router';
@Injectable({
    providedIn: 'root',
})
export class AppAuthGuard implements CanActivate {
    constructor(
        private router: Router,
    ) { }

    canActivate(): boolean {

        if(localStorage.getItem("auth") === undefined || localStorage.getItem("auth") === null) {
            localStorage.clear();
            this.router.navigateByUrl('landing');
        }

        let jwt = localStorage.getItem("auth");
        if(jwt !== null) {
          let jwtData = jwt.split('.')[1]
          let decodedJwtJsonData = window.atob(jwtData)
          let decodedJwtData = JSON.parse(decodedJwtJsonData)
          if(Date.now() >= decodedJwtData.exp * 1000) {
            localStorage.clear();
            this.router.navigateByUrl('landing');
          }
        }

        return true;
    }
 
  
}