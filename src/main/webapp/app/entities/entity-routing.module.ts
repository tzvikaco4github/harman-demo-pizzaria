import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'pizza',
        data: { pageTitle: 'Pizzas' },
        loadChildren: () => import('./pizza/pizza.module').then(m => m.PizzaModule),
      },
      {
        path: 'topping',
        data: { pageTitle: 'Toppings' },
        loadChildren: () => import('./topping/topping.module').then(m => m.ToppingModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
