import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITopping, Topping } from '../topping.model';
import { ToppingService } from '../service/topping.service';

@Component({
  selector: 'jhi-topping-update',
  templateUrl: './topping-update.component.html',
})
export class ToppingUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.minLength(2)]],
    price: [null, [Validators.required]],
    description: [],
  });

  constructor(protected toppingService: ToppingService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ topping }) => {
      this.updateForm(topping);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const topping = this.createFromForm();
    if (topping.id !== undefined) {
      this.subscribeToSaveResponse(this.toppingService.update(topping));
    } else {
      this.subscribeToSaveResponse(this.toppingService.create(topping));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITopping>>): void {
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

  protected updateForm(topping: ITopping): void {
    this.editForm.patchValue({
      id: topping.id,
      name: topping.name,
      price: topping.price,
      description: topping.description,
    });
  }

  protected createFromForm(): ITopping {
    return {
      ...new Topping(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      price: this.editForm.get(['price'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }
}
