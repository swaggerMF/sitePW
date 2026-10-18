import { Component, inject } from "@angular/core";
import { RouterOutlet, RouterLink } from "@angular/router";
import { AuthService } from "./core/auth.service";
@Component({
  selector: "app-root",
  imports: [RouterOutlet, RouterLink],
  templateUrl: "./app.component.html",
})
export class AppComponent {
  auth = inject(AuthService);
  constructor() {
    this.auth.restore();
  }
}
