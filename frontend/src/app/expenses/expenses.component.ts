import { FinanceComponent } from "./finance.component";
import { Component, inject, signal, OnInit } from "@angular/core";
import { DatePipe, CurrencyPipe } from "@angular/common";
import { forkJoin } from "rxjs";
import { ApiService } from "../core/api.service";
import { Expense, Member } from "../core/models";
import { TripContext } from "../trips/trip-context";
import { PageState } from "../shared/page-state";
import { ModalComponent } from "../shared/modal.component";
import { ConfirmComponent } from "../shared/confirm.component";
import { ExpenseFormComponent } from "./expense-form.component";
@Component({
  imports: [
    FinanceComponent,
    DatePipe,
    CurrencyPipe,
    ModalComponent,
    ConfirmComponent,
    ExpenseFormComponent,
  ],
  templateUrl: "./expenses.component.html",
})
export class ExpensesComponent extends PageState implements OnInit {
  ctx = inject(TripContext);
  api = inject(ApiService);
  items = signal<Expense[]>([]);
  members = signal<Member[]>([]);
  editing = signal(false);
  selected = signal<Expense | null>(null);
  viewing = signal<Expense | null>(null);
  removing = signal<Expense | null>(null);
  revision = signal(0);
  total = signal(0);
  ngOnInit() {
    this.load();
  }
  participants(e: Expense) {
    return e.shares.map((s) => s.username).join(", ");
  }
  load() {
    this.loading.set(true);
    forkJoin({
      items: this.api.get<Expense[]>("/trips/" + this.ctx.id + "/expenses"),
      members: this.api.get<Member[]>("/trips/" + this.ctx.id + "/members"),
    }).subscribe({
      next: (r) => {
        this.items.set(r.items);
        this.revision.update((v) => v + 1);
        this.members.set(r.members);
        this.loading.set(false);
      },
      error: this.fail,
    });
  }
  open(e: Expense | null) {
    this.selected.set(e);
    this.error.set("");
    this.editing.set(true);
  }
  save(body: unknown) {
    this.busy.set(true);
    this.error.set("");
    const e = this.selected();
    (e
      ? this.api.put("/expenses/" + e.id, body)
      : this.api.post("/trips/" + this.ctx.id + "/expenses", body)
    ).subscribe({
      next: () => {
        this.editing.set(false);
        this.busy.set(false);
        this.load();
      },
      error: this.fail,
    });
  }
  remove(e: Expense) {
    this.busy.set(true);
    this.api.delete("/expenses/" + e.id).subscribe({
      next: () => {
        this.removing.set(null);
        this.busy.set(false);
        this.load();
      },
      error: (e) => {
        this.removing.set(null);
        this.fail(e);
      },
    });
  }
}
