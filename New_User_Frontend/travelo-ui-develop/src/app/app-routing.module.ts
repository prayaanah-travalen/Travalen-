
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { SearchPageComponent } from './search-page/search-page.component';

import { HotelDetailsComponent } from './hotel-details/hotel-details.component';
import { DemoComponent } from './demo/demo.component';
import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { AppAuthGuard } from './app.auth.guard';


// const routes: Routes = [
//   { 
//       path: '', loadChildren: () => import('./landing/landing.module').then(m => m.LandingModule) 
    
//   },
//   { 
//       path: '', loadChildren: () => import('./home/home.module').then(m => m.HomeModule) 
//   },
//   { 
//       path: '', loadChildren: () => import('./search-page/search-page.module').then(m => m.SearchPageModule) 
//   },
//   { 
//       path: '', loadChildren: () => import('./hotel-details/hotel-details.module').then(m => m.HotelDetailsModule) 
//   },
//   { 
//     path:'demo',component:DemoComponent
//   },

  
//   { 
//     path: '', loadChildren: () => import('./hotel-booking/hotel-booking.module').then(m => m.HotelBookingModule) 
//   },
   
// ];

const routes: Routes = [
	{ 
		path: '', loadChildren: () => import('./landing/landing.module').then(m => m.LandingModule) 
	},
	
  	{ 
      path: '', component: AppComponent,
      children:[
			{ 
				path: '',redirectTo: '/', pathMatch: 'full'
			},
			
			{ 
				path: 'home', loadChildren: () => import('./home/home.module').then(m => m.HomeModule) 
			},
			{ 
				path: 'hotels', loadChildren: () => import('./search-page/search-page.module').then(m => m.SearchPageModule) 
			},
			{ 
				path: 'hotel-details', loadChildren: () => import('./hotel-details/hotel-details.module').then(m => m.HotelDetailsModule) 
			},
			
			{ 
				path: 'booking', loadChildren: () => import('./hotel-booking/hotel-booking.module').then(m => m.HotelBookingModule) 
			}
      ],
	  canActivate:[AppAuthGuard]

    
  },
  { 
    path:'demo',component:DemoComponent
  },
   
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
