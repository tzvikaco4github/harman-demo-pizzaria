import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITopping } from '../topping.model';

@Component({
  selector: 'jhi-topping-detail',
  templateUrl: './topping-detail.component.html',
})
export class ToppingDetailComponent implements OnInit {
  topping: ITopping | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ topping }) => {
      this.topping = topping;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
