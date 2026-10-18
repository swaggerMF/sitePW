import { Injectable, inject, signal, computed } from "@angular/core";
import { Trip } from "../core/models";
import { AuthService } from "../core/auth.service";
@Injectable()
export class TripContext {
  trip = signal<Trip | null>(null);
  auth = inject(AuthService);
  owner = computed(() => this.trip()?.ownerId === this.auth.user()?.id);
  get id() {
    return this.trip()!.id;
  }
  get currency() {
    return this.trip()!.currency;
  }
  canEdit(authorId: number) {
    return this.owner() || authorId === this.auth.user()?.id;
  }
}
