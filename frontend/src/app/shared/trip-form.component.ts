import { Component, input, output, OnInit } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { Trip } from "../core/models";
@Component({
  selector: "app-trip-form",
  imports: [FormsModule],
  templateUrl: "./trip-form.component.html",
})
export class TripFormComponent implements OnInit {
  initial = input<Trip | null>(null);
  busy = input(false);
  error = input("");
  saved = output<any>();
  cancelled = output<void>();
  model = {
    name: "",
    destination: "",
    description: "",
    startDate: "",
    endDate: "",
    currency: "EUR",
  };
  ngOnInit() {
    if (this.initial()) this.model = { ...this.initial()! };
  }
}
