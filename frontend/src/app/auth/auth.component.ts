import { Component, inject, signal } from "@angular/core";
import { FormBuilder, ReactiveFormsModule, Validators } from "@angular/forms";
import { ActivatedRoute, Router, RouterLink } from "@angular/router";
import { AuthService } from "../core/auth.service";

@Component({
  selector: "app-auth",
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: "./auth.component.html",
})
export class AuthComponent {
  private auth = inject(AuthService);
  private router = inject(Router);
  register = inject(ActivatedRoute).snapshot.routeConfig?.path === "register";
  form = inject(FormBuilder).nonNullable.group({
    username: [
      "",
      this.register
        ? [Validators.required, Validators.pattern("[a-zA-Z0-9_]{3,40}")]
        : [],
    ],
    email: [
      "",
      [Validators.required, Validators.email, Validators.maxLength(254)],
    ],
    password: [
      "",
      [
        Validators.required,
        Validators.minLength(this.register ? 10 : 1),
        Validators.maxLength(72),
      ],
    ],
  });
  busy = signal(false);
  error = signal("");

  submit() {
    if (this.form.invalid || this.busy()) return;
    this.busy.set(true);
    this.error.set("");
    this.auth
      .authenticate(
        this.register ? "register" : "login",
        this.form.getRawValue(),
      )
      .subscribe({
        next: () => {
          this.busy.set(false);
          void this.router.navigate(["/trips"]);
        },
        error: (e) => {
          this.error.set(
            e.error?.message || "Unable to connect. Please try again.",
          );
          this.busy.set(false);
        },
      });
  }
}
