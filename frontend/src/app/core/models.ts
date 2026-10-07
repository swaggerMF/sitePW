export interface User { id:number; username:string; email:string }
export interface Session { token:string; user:User }
