import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CommonService {

  baseUrl = '';

 
  constructor(private http: HttpClient) {
    this.baseUrl=environment.baseURl;
  }

  /**
   * Shared HTTP GET request
   * @param endpoint end point for each get request
   * @returns data returned by API
   */
  public httpGET(endpoint: string) {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem("auth")}`
    });
  
    const requestOptions = { headers: headers };
    return this.http.get<any>(this.baseUrl + endpoint, requestOptions);
  }

  /**
   * Shared HTTP POST request
   * @param endpoint end point for each get request
   * @param payload data passed through API
   * @returns data returned by API
   */
  public httpPOST(endpoint: string, payload: any, options?:any) {
    let reqOption = { withCredentials: true };
    /*if(options !== undefined) {
      reqOption = options
    }*/

    const headers = new HttpHeaders({
      // 'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem("auth")}`
    });
  
    const requestOptions = { headers: headers };
    return this.http.post<any>(this.baseUrl + endpoint, payload, requestOptions);
  }

    /**
   * Shared HTTP POST request
   * @param endpoint end point for each get request
   * @param payload data passed through API
   * @returns data returned by API
   */
    public httpPostWithOutToken(endpoint: string, payload: any, options?:any) {
      let reqOption = { withCredentials: true };
   
      return this.http.post<any>(this.baseUrl + endpoint, payload);
    }


    
  }