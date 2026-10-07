import {inject} from '@angular/core';
import {HttpInterceptorFn} from '@angular/common/http';
import {catchError,throwError} from 'rxjs';
import {AuthService} from './auth.service';
export const authInterceptor:HttpInterceptorFn=(req,next)=>{
 const auth=inject(AuthService);const token=auth.token();
 return next(token&&req.url.startsWith('/api/')?req.clone({setHeaders:{Authorization:'Bearer '+token}}):req).pipe(catchError(err=>{if(err.status===401&&!req.url.includes('/auth/login')&&!req.url.includes('/auth/register'))auth.logout();return throwError(()=>err);}));
};
