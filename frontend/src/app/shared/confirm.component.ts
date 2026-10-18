import { Component, input, output } from "@angular/core";
import { ModalComponent } from "./modal.component";
@Component({
  selector: "app-confirm",
  imports: [ModalComponent],
  templateUrl: "./confirm.component.html",
})
export class ConfirmComponent {
  confirmLabel = input("Delete");
  title = input("Delete this item?");
  message = input("This action cannot be undone.");
  busy = input(false);
  confirmed = output<void>();
  cancelled = output<void>();
}
