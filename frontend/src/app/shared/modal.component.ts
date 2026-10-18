import {
  AfterViewInit,
  Component,
  ElementRef,
  OnDestroy,
  input,
  output,
  viewChild,
} from "@angular/core";
@Component({
  selector: "app-modal",
  templateUrl: "./modal.component.html",
  styles: [
    `
      dialog {
        border: 1px solid var(--line);
        border-radius: 12px;
        padding: 28px;
        width: min(560px, calc(100% - 32px));
        max-height: 90dvh;
        color: var(--ink);
        background: var(--paper);
      }
      dialog::backdrop {
        background: #18342b66;
        backdrop-filter: blur(3px);
      }
      .modal-heading {
        display: flex;
        align-items: center;
        justify-content: space-between;
        gap: 16px;
      }
      .modal-heading h2 {
        font-size: 24px;
        margin: 0;
      }
    `,
  ],
})
export class ModalComponent implements AfterViewInit, OnDestroy {
  title = input.required<string>();
  closed = output<void>();
  dialog = viewChild.required<ElementRef<HTMLDialogElement>>("dialog");
  ngAfterViewInit() {
    this.dialog().nativeElement.showModal();
  }
  onCancel(e: Event) {
    e.preventDefault();
    this.closed.emit();
  }
  ngOnDestroy() {
    this.dialog().nativeElement.close();
  }
}
