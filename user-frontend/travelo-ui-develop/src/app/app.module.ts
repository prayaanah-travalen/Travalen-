import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

// Routing
import { AppRoutingModule } from './app-routing.module';
import { RouterModule } from '@angular/router'; // <-- needed for router-outlet

// Material & UI
import { MatDialogModule } from '@angular/material/dialog';
import { ToastrModule } from 'ngx-toastr';

// App Components
import { AppComponent } from './app.component';
import { DemoComponent } from './demo/demo.component';

// Shared Modules
import { AppLoaderModule } from './shared/app-loader/app-loader.module';
import { AlertComponent } from './shared/alert/alert.component';

// Feature Modules
import { SearchPageModule } from './search-page/search-page.module';
import { HotelDetailsModule } from './hotel-details/hotel-details.module';

// Services
import { LoaderService } from './shared/app-loader/services/loader.service';
import { LoaderInterceptorService } from './shared/app-loader/services/loader-interceptor.service';

// Google Maps
import { AgmCoreModule } from '@agm/core';

@NgModule({
  declarations: [
    AppComponent,
    DemoComponent,
     // declared if used in AppComponent template
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    RouterModule, // <-- needed for <router-outlet>
    MatDialogModule,
    HttpClientModule,
    AppLoaderModule,
    ToastrModule.forRoot(),


    AlertComponent,
    // Feature Modules
    SearchPageModule,
    HotelDetailsModule,

    
 

  AgmCoreModule.forRoot({
    apiKey: 'AIzaSyAvcDy5ZYc2ujCS6TTtI3RYX5QmuoV8Ffw'
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
