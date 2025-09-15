import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MatDialogModule } from '@angular/material/dialog';
import { SearchPageModule } from './search-page/search-page.module';
import { HotelDetailsModule } from './hotel-details/hotel-details.module';
import { DemoComponent } from './demo/demo.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { LoaderInterceptorService } from './shared/app-loader/services/loader-interceptor.service';
import { LoaderService } from './shared/app-loader/services/loader.service';
import { AppLoaderModule } from './shared/app-loader/app-loader.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { ToastrModule } from 'ngx-toastr';
// import { AgmCoreModule } from '@agm/core';
import { GoogleMapComponent } from './shared/google-map/google-map.component';
import { AlertComponent } from './shared/alert/alert.component';

import { GoogleMapsModule } from '@angular/google-maps';




@NgModule({

  declarations: [
    AppComponent,
    DemoComponent,

    // GoogleMapComponent,
    
  
    
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    MatDialogModule,
    SearchPageModule,
    HotelDetailsModule,
    HttpClientModule,
    AppLoaderModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot(),
    GoogleMapsModule,
    // AgmCoreModule.forRoot({
      // please get your own API key here:
      // https://developers.google.com/maps/documentation/javascript/get-api-key?hl=en
      // apiKey: 'AIzaSyAvcDy5ZYc2ujCS6TTtI3RYX5QmuoV8Ffw',
    // }),
  ],
  providers: [ LoaderService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: LoaderInterceptorService,
      multi: true,
    },
  ],
  bootstrap: [AppComponent],
  exports:[]
})
export class AppModule { }
