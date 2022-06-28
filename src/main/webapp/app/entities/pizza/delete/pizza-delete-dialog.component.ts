import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPizza } from '../pizza.model';
import { PizzaService } from '../service/pizza.service';

@Component({
  templateUrl: './pizza-delete-dialog.component.html',
})
export class PizzaDeleteDialogComponent {
  pizza?: IPizza;

  constructor(protected pizzaService: PizzaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pizzaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
