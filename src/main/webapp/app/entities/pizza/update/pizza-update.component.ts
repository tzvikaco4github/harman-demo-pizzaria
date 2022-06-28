import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPizza, Pizza } from '../pizza.model';
import { PizzaService } from '../service/pizza.service';
import { PizzaSize } from 'app/entities/enumerations/pizza-size.model';

@Component({
  selector: 'jhi-pizza-update',
  templateUrl: './pizza-update.component.html',
})
export class PizzaUpdateComponent implements OnInit {
  isSaving = false;
  pizzaSizeValues = Object.keys(PizzaSize);

  editForm = this.fb.group({
    id: [],
    pizzaSize: [null, [Validators.required]],
    price: [null, [Validators.required, Validators.min(1)]],
  });

  constructor(protected pizzaService: PizzaService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pizza }) => {
      this.updateForm(pizza);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pizza = this.createFromForm();
    if (pizza.id !== undefined) {
      this.subscribeToSaveResponse(this.pizzaService.update(pizza));
    } else {
      this.subscribeToSaveResponse(this.pizzaService.create(pizza));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPizza>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(pizza: IPizza): void {
    this.editForm.patchValue({
      id: pizza.id,
      pizzaSize: pizza.pizzaSize,
      price: pizza.price,
    });
  }

  protected createFromForm(): IPizza {
    return {
      ...new Pizza(),
      id: this.editForm.get(['id'])!.value,
      pizzaSize: this.editForm.get(['pizzaSize'])!.value,
      price: this.editForm.get(['price'])!.value,
    };
  }
}
