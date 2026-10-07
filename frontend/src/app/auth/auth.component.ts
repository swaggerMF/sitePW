import {Component,inject,signal} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {ActivatedRoute,Router,RouterLink} from '@angular/router';
import {AuthService} from '../core/auth.service';
@Component({selector:'app-auth',imports:[FormsModule,RouterLink],template:`
 <div class="auth-layout"><aside class="auth-story"><span class="eyebrow">GOOD COMPANY. GREAT ADVENTURES.</span><h1>The best trips<br>start together.</h1><p>Make the plan. Find your next favorite place.<br>Keep the money part simple.</p><div class="landscape" aria-hidden="true"><div class="sun"></div><div class="mountain back"></div><div class="mountain front"></div><div class="trail"></div></div><span class="story-footer">A little planning. A lot of possibility.</span></aside>
 <section class="auth-form"><span class="eyebrow">YOUR NEXT CHAPTER</span><h2>{{register?'Make room for adventure.':'Welcome back.'}}</h2><p class="muted">{{register?'Create your account and bring everyone along.':'Your people, your plans, all in one place.'}}</p>
 <form #form="ngForm" (ngSubmit)="submit()">
 @if(register){<label>Username<input name="username" [(ngModel)]="username" required pattern="[a-zA-Z0-9_]{3,40}" autocomplete="username" placeholder="Your name"><small>3–40 letters, numbers or underscores.</small></label>}
 <label>Email<input name="email" type="email" [(ngModel)]="email" required email autocomplete="email" placeholder="you@example.com"></label>
 <label>Password<input name="password" type="password" [(ngModel)]="password" required [minlength]="register?10:1" maxlength="72" [autocomplete]="register?'new-password':'current-password'" placeholder="Your password">@if(register){<small>At least 10 characters.</small>}</label>
 @if(error()){<p class="error" role="alert">{{error()}}</p>}
 <button class="primary full" [disabled]="form.invalid||busy()">{{busy()?'One moment…':register?'Create account':'Sign in'}} <span>↗</span></button>
 </form><p class="auth-switch">{{register?'Already have an account?':'New to TripBoard?'}} <a [routerLink]="register?'/login':'/register'">{{register?'Sign in':'Create an account'}}</a></p></section></div>`})
export class AuthComponent {
 private auth=inject(AuthService);private route=inject(ActivatedRoute);private router=inject(Router);
 register=this.route.snapshot.routeConfig?.path==='register';username='';email='';password='';busy=signal(false);error=signal('');
 submit(){this.busy.set(true);this.error.set('');this.auth.authenticate(this.register?'register':'login',{username:this.username,email:this.email,password:this.password}).subscribe({next:()=>void this.router.navigate(['/trips']),error:e=>{this.error.set(e.error?.message||'Unable to connect. Please try again.');this.busy.set(false);}});}
}
