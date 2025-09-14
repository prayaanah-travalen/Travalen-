import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

// Routing
import { AppRoutingModule } from './app-routing.module';

// Material & UI
import { MatDialogModule } from '@angular/material/dialog';
import { ToastrModule } from 'ngx-toastr';

// App Components
import { AppComponent } from './app.component';
import { DemoComponent } from './demo/demo.component';

// Shared Modules
import { AppLoaderModule } from './shared/app-loader/app-loader.module';
// import { GoogleMapComponent } from './shared/google-map/google-map.component'; // optional: declare in SharedModule if needed
import { AlertComponent } from './shared/alert/alert.component';

// Feature Modules
import { SearchPageModule } from './search-page/search-page.module';
import { HotelDetailsModule } from './hotel-details/hotel-details.module';

// Services
import { LoaderService } from './shared/app-loader/services/loader.service';
import { LoaderInterceptorService } from './shared/app-loader/services/loader-interceptor.service';

// Google Maps
import { AgmCoreModule } from '@agm/core'; // or migrate to @angular/google-maps

@NgModule({
  declarations: [
    AppComponent,
    DemoComponent,
    // AlertComponent, // if used in AppComponent template
    // GoogleMapComponent, // should ideally be in a SharedModule
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    MatDialogModule,
    HttpClientModule,
    AppLoaderModule,
    ToastrModule.forRoot(),

    // Feature Modules
    SearchPageModule,
    HotelDetailsModule,

    // Google Maps Module
    AgmCoreModule.forRoot({
      apiKey:'AIzaSyAvcDy5ZYc2ujCS6TTtI3RYX5QmuoV8Ffw'
    }),
  ],
  providers: [
    LoaderService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: LoaderInterceptorService,
      multi: true,
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule { }
