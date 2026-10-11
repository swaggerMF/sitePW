export interface User { id:number; username:string; email:string }
export interface Session { token:string; user:User }
export interface Trip {id:number;name:string;destination:string;description:string;startDate:string;endDate:string;currency:string;ownerId:number;memberCount:number}
export interface Member extends User {role:'OWNER'|'MEMBER'}
