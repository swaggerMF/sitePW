import {Routes} from '@angular/router';
import {AuthComponent} from './auth/auth.component';
import {authGuard} from './core/auth.guard';
import {Component} from '@angular/core';
@Component({template:'<section class="page"><h1>My trips</h1><p>Your adventures will appear here.</p></section>'}) class TripsPlaceholder {}
export const routes:Routes=[{path:'login',component:AuthComponent},{path:'register',component:AuthComponent},{path:'trips',component:TripsPlaceholder,canActivate:[authGuard]},{path:'',redirectTo:'trips',pathMatch:'full'},{path:'**',redirectTo:'trips'}];
