import { Component, inject, signal, OnInit } from "@angular/core";
import { CurrencyPipe } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { RouterLink } from "@angular/router";
import { ApiService } from "../core/api.service";
import { Suggestion } from "../core/models";
import { TripContext } from "../trips/trip-context";
import { PageState } from "../shared/page-state";
import { ModalComponent } from "../shared/modal.component";
import { ConfirmComponent } from "../shared/confirm.component";
import { SuggestionFormComponent } from "./suggestion-form.component";
@Component({
  imports: [
    CurrencyPipe,
    FormsModule,
    RouterLink,
    ModalComponent,
    ConfirmComponent,
    SuggestionFormComponent,
  ],
  templateUrl: "./suggestions.component.html",
})
export class SuggestionsComponent extends PageState implements OnInit {
  ctx = inject(TripContext);
  api = inject(ApiService);
  items = signal<Suggestion[]>([]);
  editing = signal(false);
  selected = signal<Suggestion | null>(null);
  viewing = signal<Suggestion | null>(null);
  removing = signal<Suggestion | null>(null);
  converting = signal<Suggestion | null>(null);
  decision = signal<{ idea: Suggestion; accept: boolean } | null>(null);
  conversion = { date: this.ctx.trip()!.startDate, startTime: "09:00" };
  ngOnInit() {
    this.load();
  }
  load() {
    this.loading.set(true);
    this.api
      .get<Suggestion[]>("/trips/" + this.ctx.id + "/suggestions")
      .subscribe({
        next: (s) => {
          this.items.set(s);
          this.loading.set(false);
        },
        error: this.fail,
      });
  }
  open(s: Suggestion | null) {
    this.selected.set(s);
    this.error.set("");
    this.editing.set(true);
  }
  save(body: unknown) {
    this.busy.set(true);
    this.error.set("");
    const s = this.selected();
    (s
      ? this.api.put("/suggestions/" + s.id, body)
      : this.api.post("/trips/" + this.ctx.id + "/suggestions", body)
    ).subscribe({
      next: () => {
        this.editing.set(false);
        this.busy.set(false);
        this.load();
      },
      error: this.fail,
    });
  }
  vote(s: Suggestion, value: string) {
    this.busy.set(true);
    this.api
      .post<Suggestion>("/suggestions/" + s.id + "/vote", { value })
      .subscribe({
        next: (r) => {
          this.items.update((all) => all.map((i) => (i.id === r.id ? r : i)));
          this.busy.set(false);
        },
        error: this.fail,
      });
  }
  decide(s: Suggestion, accept: boolean) {
    this.busy.set(true);
    this.api
      .post("/suggestions/" + s.id + (accept ? "/accept" : "/reject"), {})
      .subscribe({
        next: () => {
          this.decision.set(null);
          this.busy.set(false);
          this.load();
        },
        error: (e) => {
          this.decision.set(null);
          this.fail(e);
        },
      });
  }
  convert(s: Suggestion) {
    this.busy.set(true);
    this.api
      .post("/suggestions/" + s.id + "/convert", this.conversion)
      .subscribe({
        next: () => {
          this.converting.set(null);
          this.busy.set(false);
          this.load();
        },
        error: this.fail,
      });
  }
  remove(s: Suggestion) {
    this.busy.set(true);
    this.api.delete("/suggestions/" + s.id).subscribe({
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
