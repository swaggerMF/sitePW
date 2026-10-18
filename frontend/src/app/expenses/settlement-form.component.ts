import { Component, input, output, inject, OnInit } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { Member, Transfer } from "../core/models";
import { TripContext } from "../trips/trip-context";
@Component({
  selector: "app-settlement-form",
  imports: [FormsModule],
  templateUrl: "./settlement-form.component.html",
})
export class SettlementFormComponent implements OnInit {
  ctx = inject(TripContext);
  members = input.required<Member[]>();
  initial = input<Transfer | null>(null);
  busy = input(false);
  error = input("");
  saved = output<any>();
  cancelled = output<void>();
  model = {
    fromUserId: 0,
    toUserId: 0,
    amount: 0,
    date: new Date().toLocaleDateString("en-CA"),
  };
  ngOnInit() {
    const t = this.initial();
    if (t) this.model = { ...this.model, ...t };
  }
}
