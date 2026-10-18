import { signal } from "@angular/core";
export class PageState {
  loading = signal(false);
  busy = signal(false);
  error = signal("");
  fail = (e: any) => {
    this.error.set(e.error?.message || "Unable to connect. Please try again.");
    this.loading.set(false);
    this.busy.set(false);
  };
}
