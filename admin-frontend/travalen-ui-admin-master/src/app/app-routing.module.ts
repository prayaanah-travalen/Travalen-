import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { AppAuthGuard } from './app.auth.guard';

const routes: Routes = [
  { 
    path: '', component: AppComponent,
    children:[
    { 
      path: '',redirectTo: '/landing', pathMatch: 'full'
    },
    { 
      path: 'landing', loadChildren: () => import('./landing/landing.module').then(m => m.LandingModule) ,
    },
    
    { 
      path: 'travalen', loadChildren: () => import('./layout/layout.module').then(m => m.LayoutModule) 
    }
  ],
  // canActivate:[AppAuthGuard]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
