import {Component,inject} from '@angular/core';
import {RouterOutlet,RouterLink} from '@angular/router';
import {AuthService} from './core/auth.service';
@Component({selector:'app-root',imports:[RouterOutlet,RouterLink],template:`<header class="site-header"><a class="brand" routerLink="/trips"><span class="brand-mark">➤</span> TripBoard<span class="brand-dot">.</span></a><div class="header-right">@if(auth.token()){<a routerLink="/trips">My trips</a><span class="avatar">{{auth.user()?.username?.slice(0,1)?.toUpperCase()}}</span><span class="user-name">{{auth.user()?.username}}</span><button class="text-button" (click)="auth.logout()">Sign out</button>}@else{<span class="muted">Go together.</span>}</div></header><main><router-outlet /></main><footer class="site-footer">TripBoard <span>Made for the journey, and the people along the way.</span></footer>`})
export class AppComponent {auth=inject(AuthService);constructor(){this.auth.restore();}}
