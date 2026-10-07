import {inject,Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
@Injectable({providedIn:'root'})
export class ApiService {
 private http=inject(HttpClient);
 get<T>(path:string){return this.http.get<T>('/api'+path);}
 post<T>(path:string,body:unknown){return this.http.post<T>('/api'+path,body);}
 put<T>(path:string,body:unknown){return this.http.put<T>('/api'+path,body);}
 delete(path:string){return this.http.delete<void>('/api'+path);}
}
