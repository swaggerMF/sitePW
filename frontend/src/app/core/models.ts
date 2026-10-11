export interface User { id:number; username:string; email:string }
export interface Session { token:string; user:User }
export interface Trip {id:number;name:string;destination:string;description:string;startDate:string;endDate:string;currency:string;ownerId:number;memberCount:number}
export interface Member extends User {role:'OWNER'|'MEMBER'}
export interface Activity {id:number;title:string;description:string;location:string;date:string;startTime:string;estimatedCost:number;createdById:number;createdBy:string}
export interface Expense {id:number;description:string;amount:number;paidById:number;paidBy:string;date:string;createdById:number;shares:{userId:number;username:string;amount:number}[]}
export interface Balance {userId:number;username:string;totalPaid:number;totalShare:number;settlementsSent:number;settlementsReceived:number;balance:number}
export interface Transfer {fromUserId:number;fromUser:string;toUserId:number;toUser:string;amount:number}
export interface BalanceSummary {totalExpenses:number;balances:Balance[];transfers:Transfer[]}
export interface Settlement extends Transfer {id:number;date:string;createdById:number}
export interface Suggestion {id:number;title:string;description:string;location:string;estimatedCost:number;status:'OPEN'|'ACCEPTED'|'REJECTED';createdById:number;createdBy:string;upVotes:number;downVotes:number;myVote:'UP'|'DOWN'|null;converted:boolean;activityId:number|null}
