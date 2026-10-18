import { Component, computed, inject, signal, OnInit } from "@angular/core";
import { DatePipe, CurrencyPipe } from "@angular/common";
import { ApiService } from "../core/api.service";
import { Activity } from "../core/models";
import { TripContext } from "../trips/trip-context";
import { PageState } from "../shared/page-state";
import { ModalComponent } from "../shared/modal.component";
import { ConfirmComponent } from "../shared/confirm.component";
import { ActivityFormComponent } from "./activity-form.component";
@Component({
  imports: [
    DatePipe,
    CurrencyPipe,
    ModalComponent,
    ConfirmComponent,
    ActivityFormComponent,
  ],
  templateUrl: "./itinerary.component.html",
})
export class ItineraryComponent extends PageState implements OnInit {
  ctx = inject(TripContext);
  api = inject(ApiService);
  items = signal<Activity[]>([]);
  editing = signal(false);
  selected = signal<Activity | null>(null);
  viewing = signal<Activity | null>(null);
  removing = signal<Activity | null>(null);
  groups = computed(() =>
    Array.from(
      this.items().reduce((map, a) => {
        map.set(a.date, [...(map.get(a.date) || []), a]);
        return map;
      }, new Map<string, Activity[]>()),
      ([date, items]) => ({ date, items }),
    ),
  );
  ngOnInit() {
    this.load();
  }
  day(date: string) {
    return (
      Math.round(
        (Date.parse(date) - Date.parse(this.ctx.trip()!.startDate)) / 86400000,
      ) + 1
    );
  }
  load() {
    this.loading.set(true);
    this.api
      .get<Activity[]>("/trips/" + this.ctx.id + "/activities")
      .subscribe({
        next: (a) => {
          this.items.set(a);
          this.loading.set(false);
        },
        error: this.fail,
      });
  }
  open(a: Activity | null) {
    this.selected.set(a);
    this.error.set("");
    this.editing.set(true);
  }
  save(body: unknown) {
    this.busy.set(true);
    this.error.set("");
    const a = this.selected();
    (a
      ? this.api.put("/activities/" + a.id, body)
      : this.api.post("/trips/" + this.ctx.id + "/activities", body)
    ).subscribe({
      next: () => {
        this.editing.set(false);
        this.busy.set(false);
        this.load();
      },
      error: this.fail,
    });
  }
  remove(a: Activity) {
    this.busy.set(true);
    this.api.delete("/activities/" + a.id).subscribe({
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
