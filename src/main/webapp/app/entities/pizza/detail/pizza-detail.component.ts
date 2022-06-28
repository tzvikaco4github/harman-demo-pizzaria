import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPizza } from '../pizza.model';

@Component({
  selector: 'jhi-pizza-detail',
  templateUrl: './pizza-detail.component.html',
})
export class PizzaDetailComponent implements OnInit {
  pizza: IPizza | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pizza }) => {
      this.pizza = pizza;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
