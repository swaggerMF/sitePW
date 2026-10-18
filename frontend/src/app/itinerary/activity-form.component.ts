import { Component, input, output, OnInit, inject } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { Activity } from "../core/models";
import { TripContext } from "../trips/trip-context";
@Component({
  selector: "app-activity-form",
  imports: [FormsModule],
  templateUrl: "./activity-form.component.html",
})
export class ActivityFormComponent implements OnInit {
  ctx = inject(TripContext);
  initial = input<Activity | null>(null);
  busy = input(false);
  error = input("");
  saved = output<any>();
  cancelled = output<void>();
  model = {
    title: "",
    location: "",
    date: this.ctx.trip()!.startDate,
    startTime: "09:00",
    estimatedCost: 0,
    description: "",
  };
  ngOnInit() {
    if (this.initial()) this.model = { ...this.initial()! };
  }
}
