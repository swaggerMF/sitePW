import {
  Component,
  inject,
  input,
  output,
  signal,
  computed,
  OnChanges,
} from "@angular/core";
import { CurrencyPipe, DatePipe } from "@angular/common";
import { forkJoin } from "rxjs";
import { BalanceSummary, Member, Settlement, Transfer } from "../core/models";
import { ApiService } from "../core/api.service";
import { TripContext } from "../trips/trip-context";
import { PageState } from "../shared/page-state";
import { ModalComponent } from "../shared/modal.component";
import { ConfirmComponent } from "../shared/confirm.component";
import { SettlementFormComponent } from "./settlement-form.component";
@Component({
  selector: "app-finance",
  imports: [
    CurrencyPipe,
    DatePipe,
    ModalComponent,
    ConfirmComponent,
    SettlementFormComponent,
  ],
  templateUrl: "./finance.component.html",
})
export class FinanceComponent extends PageState implements OnChanges {
  ctx = inject(TripContext);
  api = inject(ApiService);
  members = input.required<Member[]>();
  revision = input(0);
  totalChanged = output<number>();
  summary = signal<BalanceSummary | null>(null);
  settlements = signal<Settlement[]>([]);
  recording = signal(false);
  selected = signal<Transfer | null>(null);
  removing = signal<Settlement | null>(null);
  mine = computed(
    () =>
      this.summary()?.balances.find(
        (b) => b.userId === this.ctx.auth.user()?.id,
      )?.balance || 0,
  );
  ngOnChanges() {
    this.load();
  }
  load() {
    this.loading.set(true);
    forkJoin({
      summary: this.api.get<BalanceSummary>(
        "/trips/" + this.ctx.id + "/balances",
      ),
      settlements: this.api.get<Settlement[]>(
        "/trips/" + this.ctx.id + "/settlements",
      ),
    }).subscribe({
      next: (r) => {
        this.summary.set(r.summary);
        this.totalChanged.emit(r.summary.totalExpenses);
        this.settlements.set(r.settlements);
        this.loading.set(false);
      },
      error: this.fail,
    });
  }
  canRecord(t: Transfer) {
    return (
      this.ctx.owner() ||
      [t.fromUserId, t.toUserId].includes(this.ctx.auth.user()?.id || 0)
    );
  }
  open(t: Transfer | null) {
    this.selected.set(t);
    this.error.set("");
    this.recording.set(true);
  }
  save(body: unknown) {
    this.busy.set(true);
    this.api.post("/trips/" + this.ctx.id + "/settlements", body).subscribe({
      next: () => {
        this.recording.set(false);
        this.busy.set(false);
        this.load();
      },
      error: this.fail,
    });
  }
  remove(s: Settlement) {
    this.busy.set(true);
    this.api.delete("/settlements/" + s.id).subscribe({
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
