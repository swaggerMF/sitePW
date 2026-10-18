import { Component, inject, signal, OnInit } from "@angular/core";
import { DatePipe } from "@angular/common";
import { RouterLink } from "@angular/router";
import { ApiService } from "../core/api.service";
import { AuthService } from "../core/auth.service";
import { Trip } from "../core/models";
import { ModalComponent } from "../shared/modal.component";
import { TripFormComponent } from "../shared/trip-form.component";
import { PageState } from "../shared/page-state";
@Component({
  selector: "app-trips",
  imports: [DatePipe, RouterLink, ModalComponent, TripFormComponent],
  templateUrl: "./trips.component.html",
})
export class TripsComponent extends PageState implements OnInit {
  api = inject(ApiService);
  auth = inject(AuthService);
  trips = signal<Trip[]>([]);
  creating = signal(false);
  ngOnInit() {
    this.load();
  }
  load() {
    this.loading.set(true);
    this.api.get<Trip[]>("/trips").subscribe({
      next: (t) => {
        this.trips.set(t);
        this.loading.set(false);
      },
      error: this.fail,
    });
  }
  create(body: unknown) {
    this.busy.set(true);
    this.error.set("");
    this.api.post<Trip>("/trips", body).subscribe({
      next: () => {
        this.creating.set(false);
        this.busy.set(false);
        this.load();
      },
      error: this.fail,
    });
  }
}
