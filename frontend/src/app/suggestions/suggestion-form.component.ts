import { Component, input, output, OnInit, inject } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { Suggestion } from "../core/models";
import { TripContext } from "../trips/trip-context";
@Component({
  selector: "app-suggestion-form",
  imports: [FormsModule],
  templateUrl: "./suggestion-form.component.html",
})
export class SuggestionFormComponent implements OnInit {
  ctx = inject(TripContext);
  initial = input<Suggestion | null>(null);
  busy = input(false);
  error = input("");
  saved = output<any>();
  cancelled = output<void>();
  model = { title: "", location: "", description: "", estimatedCost: 0 };
  ngOnInit() {
    if (this.initial()) this.model = { ...this.initial()! };
  }
}
