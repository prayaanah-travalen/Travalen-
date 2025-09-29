import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { UserModel } from '../model/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserStateService {

  private userSubject = new BehaviorSubject<UserModel | null>(null);
  public user$: Observable<UserModel | null> = this.userSubject.asObservable();

  constructor() {
    this.loadUserFromStorage();
  }

  private loadUserFromStorage(): void {
    const jwt = localStorage.getItem("auth");
    if (jwt) {
      try {
        const jwtData = jwt.split('.')[1];
        const decodedJwtJsonData = window.atob(jwtData);
        const decodedJwtData = JSON.parse(decodedJwtJsonData);
        this.userSubject.next(decodedJwtData.user);
      } catch (error) {
        console.error('Error parsing JWT:', error);
        this.userSubject.next(null);
      }
    }
  }

  updateUser(user: UserModel): void {
    this.userSubject.next(user);
  }

  updateHotelName(hotelName: string): void {
    const currentUser = this.userSubject.value;
    if (currentUser) {
      this.userSubject.next({ ...currentUser, hotelName });
    }
  }

  // Method to clear user data (for logout)
  clearUser(): void {
    this.userSubject.next(null);
  }
}
