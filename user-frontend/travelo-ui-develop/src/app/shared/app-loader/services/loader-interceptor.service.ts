import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LoaderService } from './loader.service';

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
    if (req.headers.get("disableLoader") === undefined || req.headers.get("disableLoader") === null || req.headers.get("disableLoader") === 'false') {
      this.showLoader()
    }
    if (req.headers.get("disableLoader") !== undefined && req.headers.get("disableLoader") !== null) {
      req.headers.delete('disableLoader')
    }
    this.requests.push(req);

    return Observable.create((observer: { next: (arg0: HttpResponse<any>) => void; error: (arg0: any) => void; complete: () => void; }) => {
      const subscription = next.handle(req)
        .subscribe(
          event => {
            if (event instanceof HttpResponse) {
              
              this.removeRequest(req);
              observer.next(event);
            }
          },
          err => {
            this.removeRequest(req);
            observer.error(err.status);
            if (err.status === 401 || err.status === 403) {
             localStorage.clear()
              window.location.reload();
            }
          },
          () => {
            this.removeRequest(req);
            observer.complete();
          });
      return () => {
        this.removeRequest(req);
        subscription.unsubscribe();
      };
    });
  }

  protected showLoader() {
    this.loaderCounter++;
    //if not loading show
    if (!this.loaderService.isLoading.value) {
      this.loaderService.isLoading.next(true);
    }
  }

  protected hideLoader() {
    if (this.loaderCounter === 0) return;
    this.loaderCounter--;
    //hide loader if counter is 0 and showing
    if (this.loaderCounter === 0 && this.loaderService.isLoading.value) {
      this.loaderService.isLoading.next(false);
    }
  }
}
