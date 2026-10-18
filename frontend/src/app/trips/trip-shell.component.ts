import { Component, inject, OnInit, signal } from "@angular/core";
import { DatePipe } from "@angular/common";
import {
  ActivatedRoute,
  Router,
  RouterLink,
  RouterLinkActive,
  RouterOutlet,
} from "@angular/router";
import { ApiService } from "../core/api.service";
import { Trip } from "../core/models";
import { TripContext } from "./trip-context";
import { PageState } from "../shared/page-state";
import { ModalComponent } from "../shared/modal.component";
import { ConfirmComponent } from "../shared/confirm.component";
import { TripFormComponent } from "../shared/trip-form.component";
@Component({
  selector: "app-trip-shell",
  providers: [TripContext],
  imports: [
    DatePipe,
    RouterLink,
    RouterLinkActive,
    RouterOutlet,
    ModalComponent,
    ConfirmComponent,
    TripFormComponent,
  ],
  templateUrl: "./trip-shell.component.html",
})
export class TripShellComponent extends PageState implements OnInit {
  ctx = inject(TripContext);
  api = inject(ApiService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  editing = signal(false);
  deleting = signal(false);
  links = [
    { path: "", label: "Overview" },
    { path: "itinerary", label: "Itinerary" },
    { path: "expenses", label: "Expenses" },
    { path: "suggestions", label: "Ideas" },
    { path: "members", label: "Members" },
  ];
  ngOnInit() {
    this.route.paramMap.subscribe((p) => {
      this.loading.set(true);
      this.api.get<Trip>("/trips/" + p.get("id")).subscribe({
        next: (t) => {
          this.ctx.trip.set(t);
          this.loading.set(false);
        },
        error: this.fail,
      });
    });
  }
  save(body: unknown) {
    this.busy.set(true);
    this.api.put<Trip>("/trips/" + this.ctx.id, body).subscribe({
      next: (t) => {
        this.ctx.trip.set(t);
        this.editing.set(false);
        this.busy.set(false);
      },
      error: this.fail,
    });
  }
  remove() {
    this.busy.set(true);
    this.api.delete("/trips/" + this.ctx.id).subscribe({
      next: () => void this.router.navigate(["/trips"]),
      error: (e) => {
        this.deleting.set(false);
        this.fail(e);
      },
    });
  }
}
