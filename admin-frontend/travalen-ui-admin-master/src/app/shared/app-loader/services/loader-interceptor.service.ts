import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { LoaderService } from './loader.service';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class LoaderInterceptorService implements HttpInterceptor {

  private loaderCounter: number = 0;
  private requests: HttpRequest<any>[] = [];
  
  constructor(private loaderService: LoaderService) { }

  removeRequest(req: HttpRequest<any>) {
    const i = this.requests.indexOf(req);
    if (i >= 0) {
      this.requests.splice(i, 1);
    }
    this.hideLoader();
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Check if loader should be shown
    const disableLoader = req.headers.get("disableLoader");
    const shouldShowLoader = disableLoader === undefined || disableLoader === null || disableLoader === 'false';
    
    if (shouldShowLoader) {
      this.showLoader();
    }
    
    // Create a new request without the disableLoader header if it exists
    let newReq = req;
    if (disableLoader !== undefined && disableLoader !== null) {
      newReq = req.clone({ headers: req.headers.delete('disableLoader') });
    }
    
    this.requests.push(newReq);

    return next.handle(newReq).pipe(
      catchError(err => {
        this.removeRequest(newReq);
        
        // Handle authentication errors
        if (err.status === 401 || err.status === 403) {
          localStorage.clear();
          window.location.reload();
        }
        
        // Pass the full error object, not just the status
        return throwError(err);
      })
    );
  }

  protected showLoader() {
    this.loaderCounter++;
    if (!this.loaderService.isLoading.value) {
      this.loaderService.isLoading.next(true);
    }
  }

  protected hideLoader() {
    if (this.loaderCounter === 0) return;
    this.loaderCounter--;
    if (this.loaderCounter === 0 && this.loaderService.isLoading.value) {
      this.loaderService.isLoading.next(false);
    }
  }
}