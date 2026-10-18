import { Component, inject, signal, OnInit } from "@angular/core";
import { CurrencyPipe, DatePipe } from "@angular/common";
import { RouterLink } from "@angular/router";
import { TripContext } from "./trip-context";
import { ApiService } from "../core/api.service";
import { Dashboard } from "../core/models";
import { PageState } from "../shared/page-state";
@Component({
  imports: [RouterLink, CurrencyPipe, DatePipe],
  templateUrl: "./overview.component.html",
})
export class OverviewComponent extends PageState implements OnInit {
  ctx = inject(TripContext);
  api = inject(ApiService);
  data = signal<Dashboard | null>(null);
  ngOnInit() {
    this.loading.set(true);
    this.api.get<Dashboard>("/trips/" + this.ctx.id + "/dashboard").subscribe({
      next: (d) => {
        this.data.set(d);
        this.loading.set(false);
      },
      error: this.fail,
    });
  }
}
