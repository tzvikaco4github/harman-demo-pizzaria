import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITopping } from '../topping.model';
import { ToppingService } from '../service/topping.service';

@Component({
  templateUrl: './topping-delete-dialog.component.html',
})
export class ToppingDeleteDialogComponent {
  topping?: ITopping;

  constructor(protected toppingService: ToppingService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.toppingService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
