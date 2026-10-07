import {inject,Injectable,signal} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {tap} from 'rxjs';
import {Session,User} from './models';
@Injectable({providedIn:'root'})
export class AuthService {
 private http=inject(HttpClient); private router=inject(Router);
 token=signal(sessionStorage.getItem('tripboard-token'));
 user=signal<User|null>(null);
 authenticate(mode:string,body:unknown){return this.http.post<Session>('/api/auth/'+mode,body).pipe(tap(s=>{sessionStorage.setItem('tripboard-token',s.token);this.token.set(s.token);this.user.set(s.user);}));}
 restore(){if(this.token())this.http.get<User>('/api/auth/me').subscribe({next:u=>this.user.set(u),error:()=>this.logout()});}
 logout(){sessionStorage.removeItem('tripboard-token');this.token.set(null);this.user.set(null);void this.router.navigate(['/login']);}
}
