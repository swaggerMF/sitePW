import { Component, input, output, OnInit, inject } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { Expense, Member } from "../core/models";
import { TripContext } from "../trips/trip-context";
@Component({
  selector: "app-expense-form",
  imports: [FormsModule],
  templateUrl: "./expense-form.component.html",
})
export class ExpenseFormComponent implements OnInit {
  ctx = inject(TripContext);
  initial = input<Expense | null>(null);
  members = input.required<Member[]>();
  busy = input(false);
  error = input("");
  saved = output<any>();
  cancelled = output<void>();
  model = {
    description: "",
    amount: 0,
    date: new Date().toLocaleDateString("en-CA"),
    paidById: this.ctx.auth.user()?.id || 0,
    participantIds: [] as number[],
  };
  ngOnInit() {
    const e = this.initial();
    if (e) this.model = { ...e, participantIds: e.shares.map((s) => s.userId) };
    else this.model.participantIds = this.members().map((m) => m.id);
  }
  toggle(id: number) {
    this.model.participantIds = this.model.participantIds.includes(id)
      ? this.model.participantIds.filter((i) => i !== id)
      : [...this.model.participantIds, id];
  }
}
