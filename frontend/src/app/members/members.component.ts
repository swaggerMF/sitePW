import { Component, inject, signal, OnInit } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { ApiService } from "../core/api.service";
import { Member } from "../core/models";
import { TripContext } from "../trips/trip-context";
import { PageState } from "../shared/page-state";
import { ConfirmComponent } from "../shared/confirm.component";
@Component({
  imports: [FormsModule, ConfirmComponent],
  templateUrl: "./members.component.html",
})
export class MembersComponent extends PageState implements OnInit {
  ctx = inject(TripContext);
  api = inject(ApiService);
  members = signal<Member[]>([]);
  query = "";
  removing = signal<Member | null>(null);
  ngOnInit() {
    this.load();
  }
  load() {
    this.loading.set(true);
    this.api.get<Member[]>("/trips/" + this.ctx.id + "/members").subscribe({
      next: (m) => {
        this.members.set(m);
        this.ctx.trip.update((t) => (t ? { ...t, memberCount: m.length } : t));
        this.loading.set(false);
      },
      error: this.fail,
    });
  }
  add() {
    this.busy.set(true);
    this.error.set("");
    this.api
      .post("/trips/" + this.ctx.id + "/members", { query: this.query })
      .subscribe({
        next: () => {
          this.query = "";
          this.busy.set(false);
          this.load();
        },
        error: this.fail,
      });
  }
  remove(m: Member) {
    this.busy.set(true);
    this.api.delete("/trips/" + this.ctx.id + "/members/" + m.id).subscribe({
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
